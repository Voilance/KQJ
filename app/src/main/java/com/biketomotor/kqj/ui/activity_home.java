package com.biketomotor.kqj.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.biketomotor.kqj.R;
import com.biketomotor.kqj.network.HttpCallBackListener;
import com.biketomotor.kqj.network.HttpConnect;
import com.biketomotor.kqj.network.HttpsCallBackListener;
import com.biketomotor.kqj.network.HttpsConnect;
import com.biketomotor.kqj.object.Cur;

import org.json.JSONException;
import org.json.JSONObject;

public class activity_home extends AppCompatActivity implements View.OnClickListener {

    private TextView textview_info;
    private TextView textview_result;
    private Button button_flesh;

    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;

    private final String address = "https://app.biketomotor.cn/api/UrgentActivity";
    private final String readdress = "https://app.biketomotor.cn/api/ActivityPassed";
    private long curtime = System.currentTimeMillis();
    private String id;
    private String act_name;
    private boolean ok = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_home);

        textview_info = (TextView)findViewById(R.id.layout_home_textview_info);
        textview_result = (TextView)findViewById(R.id.layout_home_textview_result);
        button_flesh = (Button)findViewById(R.id.layout_home_button_flesh);
        button1 = (Button)findViewById(R.id.layout_home_linear_bottom_button1);
        button2 = (Button)findViewById(R.id.layout_home_linear_bottom_button2);
        button3 = (Button)findViewById(R.id.layout_home_linear_bottom_button3);
        button4 = (Button)findViewById(R.id.layout_home_linear_bottom_button4);

        button_flesh.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);

        JSONObject jsonData = getJson();

        HttpsConnect.sendHttpsRequest(address, "POST", jsonData, new HttpsCallBackListener() {
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
            case R.id.layout_home_button_flesh:
                if (ok) {
                    ;
                } else {
                    HttpsConnect.sendHttpsRequest(readdress, "POST", getReJson(), new HttpsCallBackListener() {
                        @Override
                        public void success(String response) {
//                            Toast.makeText(activity_home.this, "已更新", Toast.LENGTH_SHORT).show();
                            catchResponse(response);
                        }

                        @Override
                        public void error(Exception exception) {
                            exception.printStackTrace();
                        }
                    });
                    HttpsConnect.sendHttpsRequest(address, "POST", getJson(), new HttpsCallBackListener() {
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
                break;
            case R.id.layout_home_linear_bottom_button1:
                break;
            case R.id.layout_home_linear_bottom_button2:
                break;
            case R.id.layout_home_linear_bottom_button3:
                break;
            case R.id.layout_home_linear_bottom_button4:
                break;
            default:
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

    private JSONObject getReJson() {
        JSONObject json = new JSONObject();
        String account = Cur.getAccount();
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
//                Toast.makeText(activity_home.this, response, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject json = new JSONObject(response);
                    String result = json.getString("result");
                    String reason = json.getString("reason");
                    id = json.getString("id");
                    act_name = json.getString("name");
                    textview_info.setText(act_name);
                    String strtime = json.getString("time");
                    long acttime = Long.valueOf(strtime);
                    if (acttime >= curtime) {
                        ok = true;
                        textview_result.setText("点击签到");
                        button_flesh.setText("签到");
                    } else {
                        ok = false;
                        textview_result.setText("该活动已过期，点击刷新");
                        button_flesh.setText("我知道了");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
