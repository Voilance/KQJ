package com.biketomotor.kqj.ui;

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

public class activity_create extends AppCompatActivity implements View.OnClickListener {

    private EditText et_name;
    private EditText et_year;
    private EditText et_month;
    private EditText et_day;
    private EditText et_hour;
    private EditText et_minute;
    private EditText et_endyear;
    private EditText et_endmonth;
    private EditText et_endday;
    private EditText et_endhour;
    private EditText et_endminute;
    private EditText et_place;
    private Button   bt_create;

    private String name;
    private String year;
    private String month;
    private String day;
    private String hour;
    private String minute;
    private String endyear;
    private String endmonth;
    private String endday;
    private String endhour;
    private String endminute;
    private String place;



    private final String address = "https://app.biketomotor.cn/api/CreateActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_create);

        et_name = (EditText)findViewById(R.id.layout_create_activity_edittext_name);
        et_year = (EditText)findViewById(R.id.layout_create_activity_edittext_year);
        et_month = (EditText)findViewById(R.id.layout_create_activity_edittext_month);
        et_day = (EditText)findViewById(R.id.layout_create_activity_edittext_day);
        et_hour = (EditText)findViewById(R.id.layout_create_activity_edittext_hour);
        et_minute = (EditText)findViewById(R.id.layout_create_activity_edittext_minute);
        et_endyear = (EditText)findViewById(R.id.layout_create_activity_edittext_endyear);
        et_endmonth = (EditText)findViewById(R.id.layout_create_activity_edittext_endmonth);
        et_endday = (EditText)findViewById(R.id.layout_create_activity_edittext_endday);
        et_endhour = (EditText)findViewById(R.id.layout_create_activity_edittext_endhour);
        et_endminute = (EditText)findViewById(R.id.layout_create_activity_edittext_endminute);
        et_place = (EditText)findViewById(R.id.layout_create_activity_edittext_place);
        bt_create = (Button)findViewById(R.id.layout_create_activity_button_create);
        bt_create.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_create_activity_button_create:
                name   = et_name.getText().toString();
                year   = et_year.getText().toString();
                month  = et_month.getText().toString();
                day    = et_day.getText().toString();
                hour   = et_hour.getText().toString();
                minute = et_minute.getText().toString();
                endyear   = et_endyear.getText().toString();
                endmonth  = et_endmonth.getText().toString();
                endday    = et_endday.getText().toString();
                endhour   = et_endhour.getText().toString();
                endminute = et_endminute.getText().toString();
                place  = et_place.getText().toString();

                JSONObject jsonData = getJson();
                Toast.makeText(activity_create.this, jsonData.toString(), Toast.LENGTH_SHORT).show();
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

                break;
        }
    }

    private String getTime() {
        int y = Integer.parseInt(year.trim());
        int m = Integer.parseInt(month.trim());
        int d = Integer.parseInt(day.trim());
        int h = Integer.parseInt(hour.trim());
        int n = Integer.parseInt(minute.trim());
        Calendar calendar = Calendar.getInstance();
        calendar.set(y, m - 1, d, h, n, 0);
        //calendar.set(2018, 8, 1, 15, 30, 0);
        long millis = calendar.getTimeInMillis();
        return String.valueOf(millis);
    }

    private String getEndTime() {
        int y = Integer.parseInt(endyear.trim());
        int m = Integer.parseInt(endmonth.trim());
        int d = Integer.parseInt(endday.trim());
        int h = Integer.parseInt(endhour.trim());
        int n = Integer.parseInt(endminute.trim());
        Calendar calendar = Calendar.getInstance();
        calendar.set(y, m - 1, d, h, n, 0);
        //calendar.set(2018, 8, 1, 15, 30, 0);
        long millis = calendar.getTimeInMillis();
        return String.valueOf(millis);
    }

    private JSONObject getJson() {
        String date = getTime();
        String endate = getEndTime();
        String creater = Cur.getAccount();
        JSONObject json = new JSONObject();
        try {
            json.put("name", name);
            json.put("time", date);
            json.put("endtime", endate);
            json.put("place", place);
            json.put("creater", creater);
            json.put("participant", creater);
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
                        Toast.makeText(activity_create.this, reason, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(activity_create.this, reason, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
