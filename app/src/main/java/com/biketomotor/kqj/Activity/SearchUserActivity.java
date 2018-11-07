package com.biketomotor.kqj.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.biketomotor.kqj.Class.HttpsUtil;
import com.biketomotor.kqj.Interface.HttpsListener;
import com.biketomotor.kqj.R;

import org.json.JSONException;
import org.json.JSONObject;

public class SearchUserActivity
        extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "TagSearchUser";

    private EditText etAccount;
    private Button btSearch;

    private String id;
    private int requestCode;
    private String account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setContentView(R.layout.search_user);

        etAccount = findViewById(R.id.et_search_user);
        btSearch = findViewById(R.id.bt_search_user);
        btSearch.setOnClickListener(this);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        requestCode = intent.getIntExtra("requestCode", 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        etAccount.setText("");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_search_user:
                if (isInfoValid()) {
                    onSearchUser();
                }
                break;
            default:
                break;
        }
    }

    private boolean isInfoValid() {
        account = etAccount.getText().toString().trim();
        if (account == null || account.contains(" ") || account.length() == 0) {
            toast("请合法输入用户账号");
            return false;
        }
        return true;
    }

    private void onSearchUser() {
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
                                UserInfo.actionActivity(SearchUserActivity.this, account, id, requestCode);
                            } else {
                                toast(reason);
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "onSuccess:" + e.toString());
                        }
                    }
                });
            }

            @Override
            public void onFailure(Exception exception) {
                Log.e(TAG, "onFailure:" + exception.toString());
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

    public static void actionActivity(Context context, String activityID, int code) {
        Intent intent = new Intent(context, SearchUserActivity.class);
        intent.putExtra("id", activityID);
        intent.putExtra("requestCode", code);
        context.startActivity(intent);
    }
}
