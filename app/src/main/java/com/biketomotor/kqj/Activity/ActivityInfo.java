package com.biketomotor.kqj.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.biketomotor.kqj.Adapter.UserItemAdapter;
import com.biketomotor.kqj.Class.HttpsUtil;
import com.biketomotor.kqj.Class.UserItem;
import com.biketomotor.kqj.Interface.HttpsListener;
import com.biketomotor.kqj.R;

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

    private void initView() {
        setContentView(R.layout.activity_info);

        findViewById(R.id.title_number).setVisibility(View.INVISIBLE);
        findViewById(R.id.title_task).setVisibility(View.INVISIBLE);
        TextView tvTitleName = findViewById(R.id.title_name);
        tvTitleName.setText("详情");

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
                UserInfo.actionActivity(ActivityInfo.this, userItemList.get(position).getAccount(), id, 0);
            }
        });
        recyclerView.setAdapter(userItemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ActivityInfo.this));

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_signin:
                break;
            case R.id.bt_add_user:
                SearchUserActivity.actionActivity(ActivityInfo.this, id, 1);
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

    public static void actionActivity(Context context, String activityID) {
        Intent intent = new Intent(context, ActivityInfo.class);
        intent.putExtra("id", activityID);
        context.startActivity(intent);
    }
}
