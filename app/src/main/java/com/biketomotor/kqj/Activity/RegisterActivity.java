package com.biketomotor.kqj.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.biketomotor.kqj.Class.HttpsUtil;
import com.biketomotor.kqj.Class.Sys;
import com.biketomotor.kqj.Class.User;
import com.biketomotor.kqj.Interface.HttpsListener;
import com.biketomotor.kqj.R;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity
        extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "TagRegister";

    private EditText etNickname;
    private EditText etRealname;
    private EditText etTel;
    private EditText etAccount;
    private EditText etPassword;
    private EditText etConfirm;
    private Button btRegister;

    private String nickname;
    private String realname;
    private String tel;
    private String account;
    private String password;
    private String confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setContentView(R.layout.activity_register);

        findViewById(R.id.title_number).setVisibility(View.INVISIBLE);
        findViewById(R.id.title_task).setVisibility(View.INVISIBLE);
        TextView tvTitleName = findViewById(R.id.title_name);
        tvTitleName.setText("欢迎注册");

        etNickname = findViewById(R.id.et_nickname);
        etRealname = findViewById(R.id.et_realname);
        etTel = findViewById(R.id.et_tel);
        etAccount = findViewById(R.id.et_account);
        etPassword = findViewById(R.id.et_password);
        etConfirm = findViewById(R.id.et_confirm);
        btRegister = findViewById(R.id.bt_register);
        btRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_register:
                if (isInfoValid()) {
                    onRegister();
                }
                break;
            default:
                break;
        }
    }

    private boolean isInfoValid() {

        nickname = etNickname.getText().toString();
        if (nickname.contains(" ")) {
            toast("昵称不可以有空格");
            return false;
        }
        if (nickname.length() == 0 || nickname == null) {
            toast("请输入您的昵称");
            return false;
        }

        realname = etRealname.getText().toString();
        if (realname.contains(" ")) {
            toast("真实姓名不可以有空格");
            return false;
        }
        if (realname.trim().length() == 0 || realname == null) {
            toast("请输入您的真实姓名");
            return false;
        }

        tel = etTel.getText().toString();
        if (tel.contains(" ")) {
            toast("手机号码不可以有空格");
            return false;
        }
        if (tel.trim().length() == 0 || tel == null) {
            toast("请输入您的手机号码");
            return false;
        }
        if (!tel.matches("[1][3578]\\d{9}")) {
            toast("请输入合法的手机号码");
            return false;
        }

        account = etAccount.getText().toString();
        if (account.contains(" ")) {
            toast("学号不可以有空格");
            return false;
        }
        if (account.trim().length() == 0 || account == null) {
            toast("请输入您的学号");
            return false;
        }

        password = etPassword.getText().toString();
        if (password.contains(" ")) {
            toast("密码不可以有空格");
            return false;
        }
        if (password.trim().length() == 0 || password == null) {
            toast("请输入您的密码");
            return false;
        }

        confirm = etConfirm.getText().toString();
        if (confirm.contains(" ")) {
            toast("密码不可以有空格");
            return false;
        }
        if (!confirm.equals(password)) {
            toast("两次密码不一致");
            return false;
        }
        return true;
    }

    private void onRegister() {
        HttpsUtil.sendPostRequest(HttpsUtil.registerAddr, getJsonData(), new HttpsListener() {
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
                                Sys.setAccount(account);
                                finish();
                            } else {
                                toast(reason);
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "onRegister/onSuccess:" + e.toString());
                        }
                    }
                });
            }

            @Override
            public void onFailure(Exception exception) {
                Log.e(TAG, "onRegister/onFailure:" + exception.toString());
            }
        });
    }

    private JSONObject getJsonData() {
        JSONObject data = new JSONObject();
        try {
            data.put("name", nickname);
            data.put("realname", realname);
            data.put("telnumber", tel);
            data.put("account", account);
            data.put("pwd", password);
        } catch (JSONException e) {
            Log.e(TAG, "getJsonData:" + e.toString());
        }
        return data;
    }

    public static void actionActivity(Context context) {
        context.startActivity(new Intent(context, RegisterActivity.class));
    }
}
