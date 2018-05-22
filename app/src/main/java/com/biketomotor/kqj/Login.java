package com.biketomotor.kqj;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import network.HttpCallBackListener;
import network.HttpConnect;

public class Login extends AppCompatActivity implements View.OnClickListener{

    private EditText  account_edit;
    private EditText  password_edit;
    private String    account_string;
    private String    password_string;
    private Button    login_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        account_edit  = (EditText)findViewById(R.id.login_account_edit);
        password_edit = (EditText)findViewById(R.id.login_password_edit);
        login_button  = (Button)findViewById(R.id.login_login_button);
        login_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_login_button:
                account_string  = account_edit.getText().toString();
                password_string = password_edit.getText().toString();
                JSONObject json = getJson(account_string, password_string);
                HttpConnect.sendHttpRequest1(
                        "http://biketomotor.com",
                        "PUT",
                        json,
                        new HttpCallBackListener() {
                    @Override
                    public void onFinish(String response) {
                        catchResponse(response);
                    }

                    @Override
                    public void onError(Exception exception) {
                        exception.printStackTrace();
                    }
                });
                break;
            default:
                break;
        }
    }

    private JSONObject getJson(final String account, final String password) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("object", "usr");
            JSONObject data_value = new JSONObject();
            data_value.put("account", account);
            data_value.put("password", password);
            jsonObject.put("data", data_value);
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
        return jsonObject;
    }

    private void catchResponse(final String response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject = new JSONObject();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    jsonObject = jsonArray.getJSONObject(0);
                    Toast.makeText(Login.this, jsonObject.toString(), Toast.LENGTH_SHORT).show();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
    }
}











