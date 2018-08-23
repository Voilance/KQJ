package com.biketomotor.kqj.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.biketomotor.kqj.R;
import com.biketomotor.kqj.network.HttpCallBackListener;
import com.biketomotor.kqj.network.HttpConnect;
import com.biketomotor.kqj.object.Cur;

import org.json.JSONException;
import org.json.JSONObject;

public class activity_home extends AppCompatActivity implements View.OnClickListener {

    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;

    private final String address = "http://biketomotor.cn:3000/api/UrgentActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_home);

        button1 = (Button)findViewById(R.id.layout_home_linear_bottom_button1);
        button2 = (Button)findViewById(R.id.layout_home_linear_bottom_button2);
        button3 = (Button)findViewById(R.id.layout_home_linear_bottom_button3);
        button4 = (Button)findViewById(R.id.layout_home_linear_bottom_button4);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);

        JSONObject jsonData = getJson();

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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_home_linear_bottom_button1:
                break;
            case R.id.layout_home_linear_bottom_button2:
                break;
            case R.id.layout_home_linear_bottom_button3:
                break;
            case R.id.layout_home_linear_bottom_button4:
                break;
        }
    }

    private JSONObject getJson() {
        JSONObject json = new JSONObject();
        String account = Cur.getAccount();
        try {
            json.put("participant", account);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    private void catchResponse(final String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity_home.this, response, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
