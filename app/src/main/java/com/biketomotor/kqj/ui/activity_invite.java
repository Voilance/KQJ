package com.biketomotor.kqj.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.biketomotor.kqj.R;
import com.biketomotor.kqj.network.HttpCallBackListener;
import com.biketomotor.kqj.network.HttpConnect;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.BufferUnderflowException;

public class activity_invite extends AppCompatActivity implements View.OnClickListener {

    private EditText et_account;
    private Button bt_invite;
    private String account;
    private String id;
    private String address = "http://biketomotor.cn:3000/api/InviteUser";

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
                HttpConnect.sendHttpRequest(address, "POST", jsonData, new HttpCallBackListener() {
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
}
