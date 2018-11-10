package com.biketomotor.kqj.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.biketomotor.kqj.Class.HttpsUtil;
import com.biketomotor.kqj.Interface.HttpsListener;
import com.biketomotor.kqj.R;

import org.json.JSONException;
import org.json.JSONObject;

public class UserInfo
        extends BaseActivity
        implements View.OnClickListener {
    private static final String TAG = "TagUserInfo";

    private TextView tvNickname;
    private TextView tvAccount;
    private TextView tvRealname;
    private TextView tvTel;
    private RelativeLayout rlAddFriend;
    private RelativeLayout rlInvite;
    private Button btAddFriend;
    private Button btInvite;

    private String account;
    private String id;
    private int requestCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setContentView(R.layout.user_info);

        findViewById(R.id.title_number).setVisibility(View.INVISIBLE);
        findViewById(R.id.title_task).setVisibility(View.INVISIBLE);
        TextView tvTitleName = findViewById(R.id.title_name);
        tvTitleName.setText("简介");

        tvNickname = findViewById(R.id.tv_nickname);
        tvAccount = findViewById(R.id.tv_account);
        tvRealname = findViewById(R.id.tv_realname);
        tvTel = findViewById(R.id.tv_tel);
        rlAddFriend = findViewById(R.id.rl_add_friend);
        rlInvite = findViewById(R.id.rl_invite);
        btAddFriend = findViewById(R.id.bt_add_friend);
        btInvite = findViewById(R.id.bt_invite);
        btAddFriend.setOnClickListener(this);
        btInvite.setOnClickListener(this);

        Intent intent = getIntent();
        account = intent.getStringExtra("account");
        id = intent.getStringExtra("id");
        requestCode = intent.getIntExtra("requestCode", 0);

        // 从ActivityInfo跳转过来邀请用户加入活动
        if (requestCode == 1) {
            rlInvite.setVisibility(View.VISIBLE);
        }

        getUserInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_add_friend:
                break;
            case R.id.bt_invite:
                inviteUser();
                break;
            default:
                break;
        }
    }

    private void getUserInfo() {
        HttpsUtil.sendPostRequest(HttpsUtil.getUserInfoAddr, getJsonData(), new HttpsListener() {
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
                            if (result.equals("true")) {
                                JSONObject userInfo = data.getJSONObject("info");
                                tvNickname.setText(userInfo.getString("name"));
                                tvAccount.setText(userInfo.getString("account"));
                                tvRealname.setText("姓名:" + userInfo.getString("realname"));
                                tvTel.setText("电话:" + userInfo.getString("telnumber"));
                            } else {
                                toast(reason);
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "getUserInfo/Onsuccess:" + e.toString());
                        }
                    }
                });
            }

            @Override
            public void onFailure(Exception exception) {
                Log.e(TAG, "getUserInfo/OnFailure:" + exception.toString());
            }
        });
    }

    private void inviteUser() {
        HttpsUtil.sendPostRequest(HttpsUtil.inviteUserAddr, getInviteJsonData(), new HttpsListener() {
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
                            if (result.equals("true")) {
                                rlInvite.setVisibility(View.INVISIBLE);
                                toast("成功添加");
                            } else {
                                toast(reason);
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "inviteUser/OnSuccess:" + e.toString());
                        }
                    }
                });
            }

            @Override
            public void onFailure(Exception exception) {
                Log.e(TAG, "inviteUser/OnFailure:" + exception.toString());
            }
        });
    }

    private JSONObject getJsonData() {
        JSONObject data = new JSONObject();
        try {
            data.put("account", account);
        } catch (JSONException e) {
            Log.e(TAG, "getJsonData:" + e.toString());
        }
        return data;
    }

    private JSONObject getInviteJsonData() {
        JSONObject data = new JSONObject();
        try {
            data.put("participant", account);
            data.put("id", id);
        } catch (JSONException e) {
            Log.e(TAG, "getJsonData:" + e.toString());
        }
        return data;
    }

    public static void actionActivity(Context context, String acount, String activityID, int code) {
        Intent intent = new Intent(context, UserInfo.class);
        intent.putExtra("account", acount);
        intent.putExtra("id", activityID);
        intent.putExtra("requestCode", code);
        context.startActivity(intent);
    }
}
