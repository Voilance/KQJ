package com.biketomotor.kqj.ui;

import android.content.Intent;
import android.os.TestLooperManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.biketomotor.kqj.R;
import com.biketomotor.kqj.network.HttpCallBackListener;
import com.biketomotor.kqj.network.HttpConnect;
import com.biketomotor.kqj.object.Cur;

import org.json.JSONException;
import org.json.JSONObject;

public class activity_activity_info extends AppCompatActivity implements View.OnClickListener {

    private TextView  textview_name;
    private ImageView imageview_image;
    private TextView  textview_time;
    private TextView  textview_place;
    private Button    button_sign;
    private Button    button_invite;
    private Button    button_delete;
    private Button    button_participant;
    private TextView  textview_info;
    private String id;
    private String creater;
    private String address ="http://biketomotor.cn:3000/api/GetActivityDetails";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_info);

        textview_name   = (TextView)findViewById(R.id.layout_activity_info_name);
        imageview_image = (ImageView)findViewById(R.id.layout_activity_info_image);
        textview_time   = (TextView)findViewById(R.id.layout_activity_info_time);
        textview_place  = (TextView)findViewById(R.id.layout_activity_info_place);
        button_sign     = (Button)findViewById(R.id.layout_activity_info_sign);
        button_invite   = (Button)findViewById(R.id.layout_activity_info_invite);
        button_delete   = (Button)findViewById(R.id.layout_activity_info_delete);
        button_participant = (Button)findViewById(R.id.layout_activity_info_participant);
        textview_info   = (TextView)findViewById(R.id.layout_activity_info_info);

        button_sign.setOnClickListener(this);
        button_delete.setOnClickListener(this);
        button_invite.setOnClickListener(this);
        button_participant.setOnClickListener(this);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        creater = intent.getStringExtra("creater");
        if (creater.compareTo(Cur.getAccount().toString()) != 0) {
            button_delete.setVisibility(View.GONE);
            button_invite.setVisibility(View.GONE);
        }

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

    private JSONObject getJson() {
        JSONObject json = new JSONObject();
        try {
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
                    if (result.compareTo("true") == 0) {
                        String name = json.getString("activity_name");
                        String time = json.getString("activity_time");
                        String place = json.getString("activity_place");
                        String creater = json.getString("creater");
                        textview_name.setText(name);
                        textview_time.setText(time);
                        textview_place.setText(place);
                    } else {
                        Toast.makeText(activity_activity_info.this, reason, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_activity_info_sign:
                break;
            case R.id.layout_activity_info_invite:
                Intent to_invite = new Intent(activity_activity_info.this, activity_invite.class);
                to_invite.putExtra("id", id);
                startActivity(to_invite);
                break;
            case R.id.layout_activity_info_delete:
                String del_address = "http://biketomotor.cn:3000/api/DeleteActivity";
                JSONObject delJson = getDelJson();
                HttpConnect.sendHttpRequest(del_address, "POST", delJson, new HttpCallBackListener() {
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
            case R.id.layout_activity_info_participant:
                String par_address = "http://biketomotor.cn:3000/api/ActivityParticipant";
                JSONObject parJson = getParJson();
                HttpConnect.sendHttpRequest(par_address, "POST", parJson, new HttpCallBackListener() {
                    @Override
                    public void success(String response) {
                        catchParResponse(response);
                    }

                    @Override
                    public void error(Exception exception) {
                        exception.printStackTrace();
                    }
                });
                break;
        }
    }

    private JSONObject getDelJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    private JSONObject getParJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    private void catchParResponse(final String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity_activity_info.this, response, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
