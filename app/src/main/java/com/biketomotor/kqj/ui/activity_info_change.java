package com.biketomotor.kqj.ui;

import android.content.Intent;
import android.provider.ContactsContract;
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
import com.biketomotor.kqj.object.Cur;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

public class activity_info_change extends AppCompatActivity implements View.OnClickListener {

    private EditText et_name;
    private EditText et_year;
    private EditText et_month;
    private EditText et_day;
    private EditText et_hour;
    private EditText et_minute;
    private EditText et_place;
    private Button   bt_change;

    private String id;
    private String name;
    private String time;
    private String year;
    private String month;
    private String day;
    private String hour;
    private String minute;
    private String place;

    private String address = "https://app.biketomotor.cn/api/EditActivity";
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_info_change);

        calendar = Calendar.getInstance();
        et_name = (EditText)findViewById(R.id.layout_info_change_name);
        et_year = (EditText)findViewById(R.id.layout_info_change_year);
        et_month = (EditText)findViewById(R.id.layout_info_change_month);
        et_day = (EditText)findViewById(R.id.layout_info_change_day);
        et_hour = (EditText)findViewById(R.id.layout_info_change_hour);
        et_minute = (EditText)findViewById(R.id.layout_info_change_minute);
        et_place = (EditText)findViewById(R.id.layout_info_change_place);
        bt_change = (Button)findViewById(R.id.layout_info_change_change);
        bt_change.setOnClickListener(this);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        name = intent.getStringExtra("name");
        time = intent.getStringExtra("time");
        place = intent.getStringExtra("place");
        Toast.makeText(activity_info_change.this, place, Toast.LENGTH_SHORT).show();
        init();
    }

    private void init() {
        long t = Long.valueOf(time);
        Date date = new Date(t);
        calendar.setTime(date);
        year = String.valueOf(calendar.get(Calendar.YEAR));
        et_year.setText(year);
        month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        et_month.setText(month);
        day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        et_day.setText(day);
        hour = String.valueOf(calendar.get(Calendar.HOUR));
        et_hour.setText(hour);
        minute = String.valueOf(calendar.get(Calendar.MINUTE));
        et_minute.setText(minute);
        et_name.setText(name);
        et_place.setText(place);
    }

    private String getTime() {
        int y = Integer.parseInt(year.trim());
        int m = Integer.parseInt(month.trim());
        int d = Integer.parseInt(day.trim());
        int h = Integer.parseInt(hour.trim());
        int n = Integer.parseInt(minute.trim());
        calendar.set(y, m - 1, d, h, n, 0);
        //calendar.set(2018, 8, 1, 15, 30, 0);
        long millis = calendar.getTimeInMillis();
        return String.valueOf(millis);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_info_change_change:
                name = et_name.getText().toString();
                place = et_place.getText().toString();
                year = et_year.getText().toString();
                month = et_month.getText().toString();
                day = et_day.getText().toString();
                hour = et_hour.getText().toString();
                minute = et_minute.getText().toString();
                JSONObject jsonData = getJson();
                Toast.makeText(activity_info_change.this, jsonData.toString(), Toast.LENGTH_SHORT).show();
                HttpsConnect.sendHttpsRequest(address, "POST", jsonData, new HttpsCallBackListener() {
                    @Override
                    public void success(String response) {
                            catchResponse(response);
                            finish();
                    }

                    @Override
                    public void error(Exception exception) {
                        exception.printStackTrace();
                    }
                });
                break;
            default:
                break;
        }
    }

    private JSONObject getJson() {
        String date = getTime();
        JSONObject json = new JSONObject();
        try {
            json.put("id", id);
            json.put("name", name);
            json.put("time", date);
            json.put("place", place);
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
                    Toast.makeText(activity_info_change.this, reason, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
