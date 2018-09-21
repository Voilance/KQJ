package com.biketomotor.kqj.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.Toast;

import com.biketomotor.kqj.R;
import com.biketomotor.kqj.adapter.ActivityAdapter;
import com.biketomotor.kqj.network.HttpCallBackListener;
import com.biketomotor.kqj.network.HttpConnect;
import com.biketomotor.kqj.network.HttpsCallBackListener;
import com.biketomotor.kqj.network.HttpsConnect;
import com.biketomotor.kqj.object.Activity;
import com.biketomotor.kqj.object.Cur;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class activity_activity2 extends AppCompatActivity implements View.OnClickListener {

    private String address = "https://app.biketomotor.cn/api/GetCreateActivity";

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;

    private List<Activity> activityList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity2);
        recyclerView = (RecyclerView)findViewById(R.id.layout_check_recyclerview);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

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
        }
    }

    private JSONObject getJson() {
        JSONObject json = new JSONObject();
        String creater = Cur.getAccount();
        try {
            json.put("creater", creater);
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
                    String acts = json.getString("activity");
                    Toast.makeText(activity_activity2.this, acts, Toast.LENGTH_SHORT).show();
                    if (result.compareTo("true") == 0) {
                        JSONArray jsonArray = new JSONArray(acts);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String name = jsonObject.getString("activity_name");
                            String id = jsonObject.getString("activity_id");
                            String creater = jsonObject.getString("creater");
                            activityList.add(new Activity(R.drawable.ua, name, id, creater));
                        }
                        recyclerView.setLayoutManager(layoutManager);
                        ActivityAdapter adapter = new ActivityAdapter(activityList);
                        recyclerView.setAdapter(adapter);
                    } else {
                        Toast.makeText(activity_activity2.this, reason, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
