package com.biketomotor.kqj.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.biketomotor.kqj.R;
import com.biketomotor.kqj.adapter.ActivityAdapter;
import com.biketomotor.kqj.network.HttpCallBackListener;
import com.biketomotor.kqj.network.HttpConnect;
import com.biketomotor.kqj.object.Activity;
import com.biketomotor.kqj.object.Cur;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class activity_activity1 extends AppCompatActivity implements View.OnClickListener {

    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private String address = "https://app.biketomotor.cn/api/GetParticipantActivity";

    private List<Activity> activityList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity1);

        button1 = (Button)findViewById(R.id.layout_activity1_linear_bottom_button1);
        button2 = (Button)findViewById(R.id.layout_activity1_linear_bottom_button2);
        button3 = (Button)findViewById(R.id.layout_activity1_linear_bottom_button3);
        button4 = (Button)findViewById(R.id.layout_activity1_linear_bottom_button4);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);

        init();
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.layout_activity1_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        ActivityAdapter adapter = new ActivityAdapter(activityList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_activity1_linear_bottom_button1:
                break;
            case R.id.layout_activity1_linear_bottom_button2:
                break;
            case R.id.layout_activity1_linear_bottom_button3:
                break;
            case R.id.layout_activity1_linear_bottom_button4:
                break;
        }
    }


    private void init() {

        Activity aa = new Activity(R.drawable.ua, "A activity", "aaaa");
        activityList.add(aa);
        Activity bb = new Activity(R.drawable.ub, "B activity", "bbbb");
        activityList.add(bb);
        Activity cc = new Activity(R.drawable.uc, "C activity", "cccc");
        activityList.add(cc);
        Activity dd = new Activity(R.drawable.ud, "D activity", "dddd");
        activityList.add(dd);
        Activity ee = new Activity(R.drawable.ue, "E activity", "eeee");
        activityList.add(ee);
        Activity ff = new Activity(R.drawable.uf, "F activity", "ffff");
        activityList.add(ff);
        Activity gg = new Activity(R.drawable.ug, "G activity", "gggg");
        activityList.add(gg);
        Activity hh = new Activity(R.drawable.uh, "H activity", "hhhh");
        activityList.add(hh);

//        JSONObject jsonData = getJson();
//
//        HttpConnect.sendHttpRequest(address, "POST", jsonData, new HttpCallBackListener() {
//            @Override
//            public void success(String response) {
//                catchResponse(response);
//            }
//
//            @Override
//            public void error(Exception exception) {
//                exception.printStackTrace();
//            }
//        });
    }

    private JSONObject getJson() {
        JSONObject json = new JSONObject();
        String participant = Cur.getAccount();
        try {
            json.put("participant", participant);
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
                    Toast.makeText(activity_activity1.this, reason, Toast.LENGTH_SHORT).show();
                    if (result.compareTo("true") == 0) {
//                        String activities = jsonData.getString("activity");
//                        JSONArray jsonArray = new JSONArray(activities);
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject jsonObject = jsonArray.getJSONObject(i);
//                            String name = jsonObject.getString("activity_name");
//                            String id = jsonObject.getString("_id");
//                            activityList.add(new Activity(R.drawable.ua, name, id));
//                        }
                        activityList.add(new Activity(R.drawable.ua, "a", "ida"));
                    } else {
                        Toast.makeText(activity_activity1.this, reason, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
