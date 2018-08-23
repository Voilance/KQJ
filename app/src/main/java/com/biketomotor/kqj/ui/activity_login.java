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
import com.biketomotor.kqj.object.Cur;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class activity_login extends AppCompatActivity implements View.OnClickListener {

    private EditText edittext_accout;
    private EditText edittext_password;
    private Button   button_login;
    private Button   button_forget;

    private String account;
    private String password;
    private final String address = "http://biketomotor.cn:3000/api/UserSignIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);

        edittext_accout   = (EditText)findViewById(R.id.layout_login_edittext_account);
        edittext_password = (EditText)findViewById(R.id.layout_login_edittext_password);
        button_login      = (Button)findViewById(R.id.layout_login_button_login);
        button_forget     = (Button)findViewById(R.id.layout_login_button_forget);

        button_login.setOnClickListener(this);
        button_forget.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_login_button_login:
                account = edittext_accout.getText().toString();
                password = edittext_password.getText().toString();
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

                break;
            case R.id.layout_login_button_forget:
                break;
        }
    }


    private JSONObject getJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("account", account);
            json.put("pwd", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    private void catchResponse(final String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonData = new JSONObject(response);
                    String result = jsonData.getString("result");
                    String reason = jsonData.getString("reason");
                    if (result.compareTo("true") == 0) {
                        Cur.setAccount(account);
                        Toast.makeText(activity_login.this, reason, Toast.LENGTH_SHORT).show();
//                        Intent to_home = new Intent(activity_login.this, activity_home.class);
//                        startActivity(to_home);
//                        finish();
                    } else {
                        Toast.makeText(activity_login.this, reason, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
