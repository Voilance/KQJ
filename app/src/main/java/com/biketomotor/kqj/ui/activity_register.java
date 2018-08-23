package com.biketomotor.kqj.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.biketomotor.kqj.R;
import com.biketomotor.kqj.network.HttpCallBackListener;
import com.biketomotor.kqj.network.HttpConnect;
import com.biketomotor.kqj.object.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class activity_register extends AppCompatActivity implements View.OnClickListener {

    private EditText edittext_account;
    private EditText edittext_password1;
    private EditText edittext_password2;
    private EditText edittext_nickname;
    private EditText edittext_tel;
    private Button   button_register;

    private String account;
    private String password1;
    private String password2;
    private String nickname;
    private String tel;

    private User user;
    private Gson gson;
    private final String address = "http://biketomotor.cn:3000/api/UserSignUp";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);

        edittext_account   = (EditText)findViewById(R.id.layout_register_edittext_account);
        edittext_password1 = (EditText)findViewById(R.id.layout_register_edittext_password1);
        edittext_password2 = (EditText)findViewById(R.id.layout_register_edittext_password2);
        edittext_nickname  = (EditText)findViewById(R.id.layout_register_edittext_nickname);
        edittext_tel       = (EditText)findViewById(R.id.layout_register_edittext_tel);
        button_register    = (Button)findViewById(R.id.layout_register_button_register);

        button_register.setOnClickListener(this);

        gson = new Gson();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_register_button_register:
                account   = edittext_account.getText().toString();
                password1 = edittext_password1.getText().toString();
                password2 = edittext_password2.getText().toString();
                nickname  = edittext_nickname.getText().toString();
                tel       = edittext_tel.getText().toString();

                if (password1.compareTo(password2) == 0) {
                    user = new User(account,password1, tel, nickname);
                    JSONObject jsonData = getJson();

                    /* Use HttpUrlConnection */
                    HttpConnect.sendHttpRequest(address, "POST", jsonData, new HttpCallBackListener() {
                        @Override
                        public void success(String response) {
                            catchResponse(response);
                        }

                        @Override
                        public void error(Exception exception) {
                            exception.printStackTrace();
                        }
                    });

                } else {
                    Toast.makeText(activity_register.this, "Passwords are difference!", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private JSONObject getJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("account", account);
            json.put("pwd", password1);
            json.put("telnumber", tel);
            json.put("name", nickname);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    private void catchResponse(final String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                Toast.makeText(activity_register.this, response, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonData = new JSONObject(response);
                    String result = jsonData.getString("result");
                    String reason = jsonData.getString("reason");
                    if (result.compareTo("true") == 0) {
                        Toast.makeText(activity_register.this, reason, Toast.LENGTH_SHORT).show();
//                        Intent to_login = new Intent(activity_register.this, activity_login.class);
//                        startActivity(to_login);
//                        finish();
                    } else {
                        Toast.makeText(activity_register.this, reason, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
