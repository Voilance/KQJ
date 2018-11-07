package com.biketomotor.kqj.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.biketomotor.kqj.Class.ActivityMgr;
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
    private String nickname;
    private String realname;
    private String tel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        etAccount.setText(Sys.getAccount());
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login:
                if (isInfoValid()) {
                    onLogin();
                }
                break;
            case R.id.tv_forget_password:
                toast(Sys.getPassword());
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

    private void onLogin() {
        HttpsUtil.sendPostRequest(HttpsUtil.loginAddr, getJsonData(), new HttpsListener() {
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
                            // 如果登陆成功，则利用后端返回的信息（包括用户所有的基本信息）修改User
                            if (result.equals("true")) {
                                JSONObject userInfo = data.getJSONObject("info");
                                nickname = userInfo.getString("name");
                                realname = userInfo.getString("realname");
                                tel = userInfo.getString("telnumber");
                                User.init(account, password, nickname, realname, tel);
                                User.setOnline(true);
                                Sys.init(account, password);
                                Sys.setLogin(true);
                                Sys.writeSP(getSharedPreferences(Sys.SPName, Context.MODE_PRIVATE));
                                MainActivity.editView();
                                finish();
                            } else {
                                toast(reason);
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "onLogin/onSuccess:" + e.toString());
                        }
                    }
                });
            }

            @Override
            public void onFailure(Exception exception) {
                Log.e(TAG, "onLogin/onFailure:" + exception.toString());
            }
        });
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
            ActivityMgr.finishAllActivities();
        }
    }

    public static void actionActivity(Context context) {
        context.startActivity(new Intent(context, LoginActivity.class));
    }

}
