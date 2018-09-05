package com.biketomotor.kqj.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
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

public class activity_check extends AppCompatActivity implements View.OnClickListener {

    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private String address = "http://biketomotor.cn:3000/api/GetParticipantActivity";

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;

    private List<Activity> activityList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_check);

        button1 = (Button)findViewById(R.id.layout_check_linear_bottom_button1);
        button4 = (Button)findViewById(R.id.layout_check_linear_bottom_button2);
        button3 = (Button)findViewById(R.id.layout_check_linear_bottom_button3);
        button2 = (Button)findViewById(R.id.layout_check_linear_bottom_button4);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);

//        activityList.add(new Activity(R.drawable.ua, "A activity", "aaaa"));
//        activityList.add(new Activity(R.drawable.ub, "B activity", "bbbb"));
//        activityList.add(new Activity(R.drawable.uc, "C activity", "cccc"));
//        activityList.add(new Activity(R.drawable.ud, "D activity", "dddd"));
//        activityList.add(new Activity(R.drawable.ue, "E activity", "eeee"));
//        activityList.add(new Activity(R.drawable.uf, "F activity", "ffff"));
//        activityList.add(new Activity(R.drawable.ug, "G activity", "gggg"));
//        activityList.add(new Activity(R.drawable.uh, "H activity", "hhhh"));
//        activityList.add(new Activity(R.drawable.ui, "I activity", "iiii"));
//
        recyclerView = (RecyclerView)findViewById(R.id.layout_check_recyclerview);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
//        ActivityAdapter adapter = new ActivityAdapter(activityList);
//        recyclerView.setAdapter(adapter);

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
            case R.id.layout_check_linear_bottom_button1:
                break;
            case R.id.layout_check_linear_bottom_button2:
                break;
            case R.id.layout_check_linear_bottom_button3:
                break;
            case R.id.layout_check_linear_bottom_button4:
                break;
        }
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
                    JSONObject json = new JSONObject(response);
                    Toast.makeText(activity_check.this, json.toString(), Toast.LENGTH_SHORT).show();
                    String result = json.getString("result");
                    String reason = json.getString("reason");
                    String acts = json.getString("activity");
                    if (result.compareTo("true") == 0) {
                        JSONArray jsonArray = new JSONArray(acts);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String name = jsonObject.getString("activity_name").toString();
                            String id = jsonObject.getString("activity_id").toString();
                            String creater = jsonObject.getString("creater");
                            activityList.add(new Activity(R.drawable.ua, name, id, creater));
                        }

                        recyclerView.setLayoutManager(layoutManager);
                        ActivityAdapter adapter = new ActivityAdapter(activityList);
                        recyclerView.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
