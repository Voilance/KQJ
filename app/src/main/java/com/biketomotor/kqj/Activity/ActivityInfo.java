package com.biketomotor.kqj.Activity;

import android.app.AlertDialog;
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
import com.biketomotor.kqj.Thread.SignThread;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
    private String info;
    private String creater;
    private String supervisor;
    private int requestCode;
    private int isSupervisor;
    private boolean signStarted;
    private HashMap<String, Long> signMap;
    private AlertDialog recordsDialog;

    private WifiManager wifiManager;
    private SignThread signThread;
    private SignThread.SignThreadListener cListener = new SignThread.SignThreadListener() {
        @Override
        public void onFinished(final String m) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    btSignin.setText(m);
                    onUserStatus();
                }
            });
        }
    };
    private SignService signService;
    private SignService.SignServiceListener sListener = new SignService.SignServiceListener() {
        @Override
        public void onConnect(final String m) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    toast(m);
                    btSignin.setText("停止签到");
                    long curTime = Calendar.getInstance().getTimeInMillis();
                    for (final String key : signMap.keySet()) {
                        long t = signMap.get(key);
                        if (curTime > (t + 30000)) { // 离开超过30s
                            toast("send left");
                            HttpsUtil.sendPostRequest(HttpsUtil.userLeftAddr, getJsonDataForSign(key), null);
                            toast("update status");
                            onUserStatus();
                            toast("over");
                            signMap.remove(key);
                        }
                    }
                    if (signMap.get(m) == null) { // 新到同学
                        signMap.put(m, curTime);
                        toast("send arrive");
                        HttpsUtil.sendPostRequest(HttpsUtil.userArriveAddr, getJsonDataForSign(m), null);
                        toast("update status");
                        onUserStatus();
                        toast("over");
                    } else {
                        signMap.put(m, curTime);
                    }
                }
            });
        }

        @Override
        public void onDisconnect(final String m) {
        }

        @Override
        public void onFinish() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    toast("finished");
                    btSignin.setText("开始签到");
                }
            });
        }
    };
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            SignService.SignServiceBinder binder = (SignService.SignServiceBinder)iBinder;
            signService = binder.getService();
            signService.setListener(sListener);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.e(TAG, "onServiceDisconnected: ");
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
        MainActivity.editView();
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
        ivTask.setVisibility(View.INVISIBLE);

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

            @Override
            public void onItemLongClick(int position) {
                final int posi = position;
                HttpsUtil.sendPostRequest(HttpsUtil.userClockInfoAddr, getJsonDataForRecord(userItemList.get(position).getAccount()), new HttpsListener() {
                    @Override
                    public void onSuccess(final String response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject data = new JSONObject(response);
                                    String result = data.getString("result");
                                    String reason = data.getString("reason");
                                    if (result.equals("true")) {
                                        String records = "";
                                        JSONArray array = new JSONArray(data.getString("records"));
                                        for (int i = 0; i < array.length(); i++) {
                                            JSONObject object = array.getJSONObject(i);
                                            String io = object.getString("inorout");
                                            String time = getTime(object.getString("time"));
                                            if (io.equals("in")) {
                                                records = records + "到    :";
                                            } else {
                                                records = records + "    走:";
                                            }
                                            records = records + time + "\n";
                                        }
                                        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityInfo.this);
                                        builder.setTitle(userItemList.get(posi).getRealname() + "到课情况");
                                        builder.setMessage(records);
                                        recordsDialog = builder.create();
                                        recordsDialog.show();
                                    }
                                } catch (JSONException e) {
                                    Log.e(TAG, "userClockInfo/onSuccess:" + e.toString());
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailure(Exception exception) {
                        Log.e(TAG, "userClockInfo/onFailure:" + exception.toString());
                    }
                });
            }
        });
        recyclerView.setAdapter(userItemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ActivityInfo.this));

        requestCode = 0;
        signStarted = false;
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        isSupervisor = intent.getIntExtra("isSupervisor", 0);
        if (isSupervisor != 0) {
            btSignin.setClickable(false);
            btSignin.setVisibility(View.INVISIBLE);
        }

        signThread = new SignThread();
        signThread.setListener(cListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_task:
                EditActivity.actionActivity(ActivityInfo.this, id, name, place, startTime, endTime, info, creater, supervisor);
                break;
            case R.id.bt_signin:
                if (creater.equals(User.getAccount())) {
                    // 打开服务器
                    if (!signStarted) {
                        signStarted = true;
                        signMap = new HashMap<>();
                        signService.setConnectFlag(true);
                        bindSignService();
                        startService(new Intent(ActivityInfo.this, SignService.class));
                        HttpsUtil.sendPostRequest(HttpsUtil.userArriveAddr, getJsonDataForSign(User.getAccount()), null);
                        btSignin.setText("停止签到");
                    } else {
                        signStarted = false;
                        signService.setConnectFlag(false);
                        unbindService(serviceConnection);
                        stopService(new Intent(ActivityInfo.this, SignService.class));
                        HttpsUtil.sendPostRequest(HttpsUtil.userLeftAddr, getJsonDataForSign(User.getAccount()), null);
                        btSignin.setText("开始签到");
                    }
                } else {
                    // 搜索、连接
                    if (!signStarted) {
                        signStarted = true;
                        signThread.setConnectFlag(true);
                        signThread.sign(getIP());
                    } else {
                        signStarted = false;
                        signThread.setConnectFlag(false);
                    }
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
                            JSONObject data = new JSONObject(response);
                            String result = data.getString("result");
                            String reason = data.getString("reason");
                            userItemList.clear();
                            if (result.equals("true")) {
                                name = data.getString("activity_name");
                                place = data.getString("activity_place");
                                startTime = data.getString("activity_time");
                                endTime = data.getString("activity_endTime");
                                info = data.getString("activity_info");
                                creater = data.getString("creater");
                                supervisor = data.getString("supervisor");

                                tvName.setText(name);
                                tvPlace.setText(place);
                                tvStartTime.setText("正式开始时间:" + getTime(startTime));
                                tvEndTime.setText("签到截止时间:" + getTime(endTime));
                                tvInfo.setText(info);

                                if (creater.equals(User.getAccount())) {
                                    requestCode = 1;
                                    btSignin.setText("开始签到");
                                    ivTask.setVisibility(View.VISIBLE);
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
                                    String pStatus = object.getString("status");
                                    userItemList.add(new UserItem(pName, pRealname, pAccount, pStatus));
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

    private void onUserStatus() {
        HttpsUtil.sendPostRequest(HttpsUtil.activityInfoAddr, getJsonData(), new HttpsListener() {
            @Override
            public void onSuccess(final String response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject data = new JSONObject(response);
                            String result = data.getString("result");
                            userItemList.clear();
                            if (result.equals("true")) {
                                JSONArray array = new JSONArray(data.getString("participant"));
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    String pName = object.getString("name");
                                    String pRealname = object.getString("realname");
                                    String pAccount = object.getString("account");
                                    String pStatus = object.getString("status");
                                    userItemList.add(new UserItem(pName, pRealname, pAccount, pStatus));
                                }
                            }
                            userItemAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            Log.e(TAG, "onUserStatus/onSuccess:" + e.toString());
                        }
                    }
                });
            }

            @Override
            public void onFailure(Exception exception) {
                Log.e(TAG, "onUserStatus/onFailure:" + exception.toString());
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

    private JSONObject getJsonDataForSign(String account) {
        JSONObject data = new JSONObject();
        try {
            data.put("account", account);
            data.put("activity_id", id);
        } catch (JSONException e) {
            Log.e(TAG, "getJsonDataForSign:" + e.toString());
        }
        return data;
    }

    private JSONObject getJsonDataForRecord(String account) {
        JSONObject data = new JSONObject();
        try {
            data.put("activity_id", id);
            data.put("user_id", account);
        } catch (JSONException e) {
            Log.e(TAG, "getJsonDataForRecord:" + e.toString());
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

    public static void actionActivity(Context context, String activityID, int isVisor) {
        Intent intent = new Intent(context, ActivityInfo.class);
        intent.putExtra("id", activityID);
        intent.putExtra("isSupervisor", isVisor);
        context.startActivity(intent);
    }
}
