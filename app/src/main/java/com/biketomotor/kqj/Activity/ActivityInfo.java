package com.biketomotor.kqj.Activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.biketomotor.kqj.Adapter.UserItemAdapter;
import com.biketomotor.kqj.Class.HttpsUtil;
import com.biketomotor.kqj.Class.User;
import com.biketomotor.kqj.Class.UserItem;
import com.biketomotor.kqj.Interface.HttpsListener;
import com.biketomotor.kqj.R;
import com.biketomotor.kqj.Service.SignService;
import com.biketomotor.kqj.Task.SignTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActivityInfo
        extends BaseActivity
        implements View.OnClickListener {
    private static final String TAG = "TagActivityInfo";

    private ImageView ivTask;
    private TextView tvName;
    private TextView tvPlace;
    private TextView tvStartTime;
    private TextView tvEndTime;
    private TextView tvInfo;
    private Button btSignin;
    private Button btAddUser;

    private List<UserItem> userItemList;
    private UserItemAdapter userItemAdapter;
    private RecyclerView recyclerView;

    private String id;
    private String name;
    private String place;
    private String startTime;
    private String endTime;
    private String creater;
    private int requestCode;
    private boolean signStarted;

    private WifiManager wifiManager;
    private SignService signService;
    private SignService.SignServiceListener listener = new SignService.SignServiceListener() {
        @Override
        public void onFinish(final String m) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (m != null) {
                        toast(m);
                    }
                    startService(new Intent(ActivityInfo.this, SignService.class));
                }
            });
        }
    };
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            SignService.SignServiceBinder binder = (SignService.SignServiceBinder)iBinder;
            signService = binder.getService();
            signService.setListener(listener);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            signService = null;
            bindSignService();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        onActivityInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (signService != null) {
            signService.setListener(null);
            unbindService(serviceConnection);
        }
        if (signStarted) {
            stopService(new Intent(ActivityInfo.this, SignService.class));
        }
    }

    private void initView() {
        setContentView(R.layout.activity_info);

        findViewById(R.id.title_number).setVisibility(View.INVISIBLE);
        TextView tvTitleName = findViewById(R.id.title_name);
        tvTitleName.setText("活动详情");
        ivTask = findViewById(R.id.title_task);
        ivTask.setOnClickListener(this);

        tvName = findViewById(R.id.tv_name);
        tvPlace = findViewById(R.id.tv_place);
        tvStartTime = findViewById(R.id.tv_start_time);
        tvEndTime = findViewById(R.id.tv_end_time);
        tvInfo = findViewById(R.id.tv_info);
        btSignin = findViewById(R.id.bt_signin);
        btAddUser = findViewById(R.id.bt_add_user);
        btSignin.setOnClickListener(this);
        btAddUser.setOnClickListener(this);

        recyclerView = findViewById(R.id.rv_participant_list);
        userItemList = new ArrayList<>();
        userItemAdapter = new UserItemAdapter(userItemList);
        userItemAdapter.setItemClickListener(new UserItemAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                UserInfo.actionActivity(ActivityInfo.this, userItemList.get(position).getAccount(), id, requestCode);
            }
        });
        recyclerView.setAdapter(userItemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ActivityInfo.this));

        requestCode = 0;
        signStarted = false;
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_task:
                EditActivity.actionActivity(ActivityInfo.this);
                break;
            case R.id.bt_signin:
                if (creater.equals(User.getAccount())) {
                    // 打开服务器
                    if (!signStarted) {
                        signStarted = true;
                        bindSignService();
                        startService(new Intent(ActivityInfo.this, SignService.class));
                        btSignin.setText("停止签到");
                    } else {
                        signStarted = false;
                        unbindService(serviceConnection);
                        stopService(new Intent(ActivityInfo.this, SignService.class));
                        btSignin.setText("开始签到");
                    }
                } else {
                    // 搜索、连接
                    new SignTask(User.getAccount()).execute(getIP());
                }
                break;
            case R.id.bt_add_user:
                SearchUserActivity.actionActivity(ActivityInfo.this, id, requestCode);
                break;
            default:
                break;
        }
    }

    private void onActivityInfo() {
        HttpsUtil.sendPostRequest(HttpsUtil.activityInfoAddr, getJsonData(), new HttpsListener() {
            @Override
            public void onSuccess(final String response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.e(TAG, response);
                            JSONObject data = new JSONObject(response);
                            String result = data.getString("result");
                            String reason = data.getString("reason");
                            userItemList.clear();
                            if (result.equals("true")) {
                                name = data.getString("activity_name");
                                place = data.getString("activity_place");
                                startTime = data.getString("activity_time");
                                endTime = data.getString("activity_endTime");
                                creater = data.getString("creater");

                                tvName.setText(name);
                                tvPlace.setText(place);
                                tvStartTime.setText("正式开始时间:" + getTime(startTime));
                                tvEndTime.setText("签到截止时间:" + getTime(endTime));

                                if (creater.equals(User.getAccount())) {
                                    requestCode = 1;
                                    btSignin.setText("开始签到");
                                    bindSignService();
                                } else {
                                    requestCode = 0;
                                    btAddUser.setVisibility(View.INVISIBLE);
                                    btSignin.setText("签到");
                                    wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                                }

                                JSONArray array = new JSONArray(data.getString("participant"));
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    String pName = object.getString("name");
                                    String pRealname = object.getString("realname");
                                    String pAccount = object.getString("account");
                                    userItemList.add(new UserItem(pName, pRealname, pAccount));
                                }
                            } else {
                                toast(reason);
                                finish();
                            }
                            userItemAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            Log.e(TAG, "onActivityInfo/onSuccess:" + e.toString());
                        }
                    }
                });
            }

            @Override
            public void onFailure(Exception exception) {
                Log.e(TAG, "onActivityInfo/onFailure:" + exception.toString());
            }
        });
    }

    private JSONObject getJsonData() {
        JSONObject data = new JSONObject();
        try {
            data.put("id", id);
        } catch (JSONException e) {
            Log.e(TAG, "getJsonData:" + e.toString());
        }
        return data;
    }

    private String getTime(String timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String time = sdf.format(new Date(Long.valueOf(timestamp)));
        return time;
    }

    private void bindSignService() {
        Intent intent = new Intent(ActivityInfo.this, SignService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private String getIP() {
        int ip = wifiManager.getDhcpInfo().serverAddress;
        return (0xff & ip) + "." + (0xff & ip >> 8) + "." + (0xff & ip >> 16) + "." + (0xff & ip >> 24);
    }

    public static void actionActivity(Context context, String activityID) {
        Intent intent = new Intent(context, ActivityInfo.class);
        intent.putExtra("id", activityID);
        context.startActivity(intent);
    }
}
