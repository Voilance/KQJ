package com.biketomotor.kqj.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.biketomotor.kqj.Class.ActivityManager;
import com.biketomotor.kqj.Class.HttpsUtil;
import com.biketomotor.kqj.Class.Sys;
import com.biketomotor.kqj.Class.User;
import com.biketomotor.kqj.Interface.HttpsListener;
import com.biketomotor.kqj.R;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity
        extends BaseActivity
        implements View.OnClickListener {
    private static final String TAG = "TagLogin";

    private EditText etAccount;
    private EditText etPassword;
    private Button btLogin;
    private TextView tvForgetPassword;
    private TextView tvRegister;

    private String account;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setContentView(R.layout.activity_login);
        etAccount = findViewById(R.id.et_account);
        etPassword = findViewById(R.id.et_password);
        btLogin = findViewById(R.id.bt_login);
        tvForgetPassword = findViewById(R.id.tv_forget_password);
        tvRegister = findViewById(R.id.tv_register);

        btLogin.setOnClickListener(this);
        tvForgetPassword.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        etAccount.setText(User.getAccount());
//        etPassword.setText(User.getPassword());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login:
                if (isInfoValid()) {
                    HttpsUtil.sendPostRequest(HttpsUtil.loginAddress, getJsonData(), new HttpsListener() {
                        @Override
                        public void onSuccess(final String response) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        JSONObject data = new JSONObject(response);
                                        String result = data.getString("result");
                                        String reason = data.getString("reason");
                                        // 如果登陆成功，则利用后端返回的信息（包括用户所有的基本信息）修改User
                                        if (result.equals("true")) {
//                                            User.readJSON(data);
                                            User.setAccount(account);
                                            User.setPassword(password);
                                            User.setOnline(true);
                                            User.writeSP(getSharedPreferences(User.getAccount(), Context.MODE_PRIVATE));
                                            Sys.setLogin(true);
                                            Sys.writeSP(getSharedPreferences("user", Context.MODE_PRIVATE));
                                            finish();
                                        } else {
                                            toast(reason);
                                        }
                                    } catch (JSONException e) {
                                        Log.e(TAG, "activity_login/onSuccess:" + e.toString());
                                    }
                                }
                            });
                        }

                        @Override
                        public void onFailure(Exception exception) {
                            Log.e(TAG, "activity_login/onFailure:" + exception.toString());
                        }
                    });
                }
                break;
            case R.id.tv_forget_password:
                toast(User.getPassword());
                break;
            case R.id.tv_register:
                RegisterActivity.actionActivity(LoginActivity.this);
                break;
            default:
                break;
        }
    }

    private boolean isInfoValid() {
        account = etAccount.getText().toString();
        if (account.contains(" ")) {
            toast("学号不可以有空格");
            return false;
        }
        if (account.length() == 0 || account == null) {
            toast("账号不可为空！");
            return false;
        }

        password = etPassword.getText().toString();
        if (password.contains(" ")) {
            toast("密码不可以有空格");
            return false;
        }
        if (password.length() == 0 || password == null) {
            toast("密码不可为空！");
            return false;
        }
        return true;
    }

    private JSONObject getJsonData() {
        JSONObject data = new JSONObject();
        try {
            data.put("account", account);
            data.put("pwd", password);
        } catch (JSONException e) {
            Log.e(TAG, "getJsonData:" + e.toString());
        }
        return data;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!User.isOnline()) {
            ActivityManager.finishAllActivities();
        }
    }

    public static void actionActivity(Context context) {
        context.startActivity(new Intent(context, LoginActivity.class));
    }

}
