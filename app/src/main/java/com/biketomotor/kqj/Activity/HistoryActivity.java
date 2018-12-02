package com.biketomotor.kqj.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.biketomotor.kqj.Adapter.ActivityItemAdapter;
import com.biketomotor.kqj.Class.ActivityItem;
import com.biketomotor.kqj.Class.HttpsUtil;
import com.biketomotor.kqj.Class.User;
import com.biketomotor.kqj.Interface.HttpsListener;
import com.biketomotor.kqj.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistoryActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "TagHistory";

    private int colorBlue;
    private int colorBlack;
    private TextView tvMCreated;
    private TextView tvMParticipated;
    private TextView tvMSupervised;
    private View vMCreated;
    private View vMParticipated;
    private View vMSupervised;
    private List<ActivityItem> mCreatedList;
    private List<ActivityItem> mParticipatedList;
    private List<ActivityItem> mSupervisedList;
    private List<ActivityItem> activityItemList;
    private ActivityItemAdapter activityItemAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setContentView(R.layout.activity_history);

        findViewById(R.id.title_number).setVisibility(View.INVISIBLE);
        findViewById(R.id.title_task).setVisibility(View.INVISIBLE);
        TextView tvTitleName = findViewById(R.id.title_name);
        tvTitleName.setText("历史活动");

        colorBlue = getResources().getColor(R.color.colorLightBlue);
        colorBlack = getResources().getColor(R.color.colorBlack);
        tvMCreated = findViewById(R.id.tv_mcreated);
        tvMParticipated = findViewById(R.id.tv_mparticipated);
        tvMSupervised = findViewById(R.id.tv_msupervised);
        vMCreated = findViewById(R.id.v_mcreated);
        vMParticipated = findViewById(R.id.v_mparticipated);
        vMSupervised = findViewById(R.id.v_msupervised);
        tvMCreated.setOnClickListener(this);
        tvMParticipated.setOnClickListener(this);
        tvMSupervised.setOnClickListener(this);

        mCreatedList = new ArrayList<>();
        mParticipatedList = new ArrayList<>();
        mSupervisedList = new ArrayList<>();
        activityItemList = new ArrayList<>();
        activityItemAdapter = new ActivityItemAdapter(activityItemList);
        activityItemAdapter.setItemClickListener(new ActivityItemAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                ActivityInfo.actionActivity(HistoryActivity.this, activityItemList.get(position).getId(), 1);
            }
        });
        recyclerView = findViewById(R.id.rv_activity_list);
        recyclerView.setAdapter(activityItemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));

        getHistoryActivity();
    }

    private void getHistoryActivity() {
        HttpsUtil.sendPostRequest(HttpsUtil.getAllHistoryActivityAddr, getJsonData(), new HttpsListener() {
            @Override
            public void onSuccess(final String response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject data = new JSONObject(response);
                            String result = data.getString("result");
                            List<String> IDs = new ArrayList<>();
                            IDs.clear();
                            mCreatedList.clear();
                            mParticipatedList.clear();
                            mSupervisedList.clear();
                            activityItemList.clear();
                            if (result.equals("true")) {
                                JSONArray array = new JSONArray(data.getString("activity"));
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    String name = object.getString("activity_name");
                                    String id = object.getString("activity_id");
                                    String startTime = object.getString("activity_time");
                                    String endTime = object.getString("activity_endTime");
                                    String creater = object.getString("creater");
                                    String supervisor = object.getString("supervisor");

                                    if (IDs.contains(id)) {
                                        continue;
                                    } else {
                                        IDs.add(id);
                                    }
                                    if (supervisor.equals(User.getAccount())) {
                                        mSupervisedList.add(new ActivityItem(name, id, creater, startTime, endTime));
                                    } else {
                                        if (creater.equals(User.getAccount())) {
                                            mCreatedList.add(new ActivityItem(name, id, creater, startTime, endTime));
                                        }
                                        mParticipatedList.add(new ActivityItem(name, id, creater, startTime, endTime));
                                    }
                                }
                            }
                            Collections.sort(mCreatedList);
                            Collections.sort(mParticipatedList);
                            Collections.sort(mSupervisedList);
                            activityItemList.addAll(mCreatedList);
                            activityItemAdapter.notifyDataSetChanged();

                            tvMCreated.setClickable(false);
                            tvMParticipated.setClickable(true);
                            tvMSupervised.setClickable(true);
                            tvMCreated.setTextColor(colorBlue);
                            tvMParticipated.setTextColor(colorBlack);
                            tvMSupervised.setTextColor(colorBlack);
                            vMCreated.setVisibility(View.VISIBLE);
                            vMParticipated.setVisibility(View.INVISIBLE);
                            vMSupervised.setVisibility(View.INVISIBLE);
                        } catch (JSONException e) {
                            Log.e(TAG, "getHistoryActivity/onSuccess:" + e.toString());
                        }
                    }
                });
            }

            @Override
            public void onFailure(Exception exception) {
                Log.e(TAG, "getHistoryActivity/onFailure:" + exception.toString());
            }
        });
    }

    private JSONObject getJsonData() {
        JSONObject data = new JSONObject();
        try {
            data.put("stu_id", User.getAccount());
        } catch (JSONException e) {
            Log.e(TAG, "getJsonData:" + e.toString());
        }
        return data;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_mcreated:
                tvMCreated.setClickable(false);
                tvMParticipated.setClickable(true);
                tvMSupervised.setClickable(true);
                tvMCreated.setTextColor(colorBlue);
                tvMParticipated.setTextColor(colorBlack);
                tvMSupervised.setTextColor(colorBlack);
                vMCreated.setVisibility(View.VISIBLE);
                vMParticipated.setVisibility(View.INVISIBLE);
                vMSupervised.setVisibility(View.INVISIBLE);
                activityItemList.clear();
                activityItemList.addAll(mCreatedList);
                activityItemAdapter.notifyDataSetChanged();
                break;
            case R.id.tv_mparticipated:
                tvMCreated.setClickable(true);
                tvMParticipated.setClickable(false);
                tvMSupervised.setClickable(true);
                tvMCreated.setTextColor(colorBlack);
                tvMParticipated.setTextColor(colorBlue);
                tvMSupervised.setTextColor(colorBlack);
                vMCreated.setVisibility(View.INVISIBLE);
                vMParticipated.setVisibility(View.VISIBLE);
                vMSupervised.setVisibility(View.INVISIBLE);
                activityItemList.clear();
                activityItemList.addAll(mParticipatedList);
                activityItemAdapter.notifyDataSetChanged();
                break;
            case R.id.tv_msupervised:
                tvMCreated.setClickable(true);
                tvMParticipated.setClickable(true);
                tvMSupervised.setClickable(false);
                tvMCreated.setTextColor(colorBlack);
                tvMParticipated.setTextColor(colorBlack);
                tvMSupervised.setTextColor(colorBlue);
                vMCreated.setVisibility(View.INVISIBLE);
                vMParticipated.setVisibility(View.INVISIBLE);
                vMSupervised.setVisibility(View.VISIBLE);
                activityItemList.clear();
                activityItemList.addAll(mSupervisedList);
                activityItemAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    public static void actionActivity(Context context) {
        Intent intent = new Intent(context, HistoryActivity.class);
        context.startActivity(intent);
    }
}
