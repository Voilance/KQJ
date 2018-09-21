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
import com.biketomotor.kqj.network.HttpsCallBackListener;
import com.biketomotor.kqj.network.HttpsConnect;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.BufferUnderflowException;

public class activity_invite extends AppCompatActivity implements View.OnClickListener {

    private EditText et_account;
    private Button bt_invite;
    private String account;
    private String id;
    private String address = "https://app.biketomotor.cn/api/InviteUser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_invite);

        et_account = (EditText)findViewById(R.id.layout_invite_account);
        bt_invite = (Button)findViewById(R.id.layout_invite_invite);

        bt_invite.setOnClickListener(this);

        Intent intent = getIntent();
        id = intent.getStringExtra("id").toString();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_invite_invite:
                account = et_account.getText().toString();
                JSONObject jsonData = getJson();
                HttpsConnect.sendHttpsRequest(address, "POST", jsonData, new HttpsCallBackListener() {
                    @Override
                    public void success(String response) {
                        finish();
                    }

                    @Override
                    public void error(Exception exception) {
                        exception.printStackTrace();
                    }
                });
                break;
        }
    }

    private JSONObject getJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("participant", account);
            json.put("id", id);
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
                    JSONObject json = new JSONObject(response);
                    String result = json.getString("result");
                    String reason = json.getString("reason");
                    Toast.makeText(activity_invite.this, result + reason, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
