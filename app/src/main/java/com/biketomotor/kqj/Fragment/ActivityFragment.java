package com.biketomotor.kqj.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.biketomotor.kqj.Activity.ActivityInfo;
import com.biketomotor.kqj.Activity.MainActivity;
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

public class ActivityFragment
        extends Fragment implements View.OnClickListener {
    private static final String TAG = "TagActivity";

    private static MainActivity mainActivity;
    private static TextView tvMCreate;
    private static TextView tvMParticipant;
    private static TextView tvMSupervisor;
    private static View vMCreate;
    private static View vMParticipant;
    private static View vMSupervisor;
    private static int colorBlue;
    private static int colorBlack;
    private static List<ActivityItem> mCreateList;
    private static List<ActivityItem> mParticipantList;
    private static List<ActivityItem> mSupervisorList;
    private static List<ActivityItem> activityItemList;
    private static ActivityItemAdapter activityItemAdapter;
    private static RecyclerView recyclerView;

    private static int isSupervisor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity, container, false);

        mainActivity = (MainActivity)getActivity();
        colorBlue = mainActivity.getResources().getColor(R.color.colorLightBlue);
        colorBlack = mainActivity.getResources().getColor(R.color.colorBlack);
        tvMCreate = view.findViewById(R.id.tv_mcreate);
        tvMCreate.setTextColor(colorBlue);
        tvMParticipant = view.findViewById(R.id.tv_mparticipant);
        tvMSupervisor = view.findViewById(R.id.tv_msupervisor);
        tvMCreate.setOnClickListener(this);
        tvMParticipant.setOnClickListener(this);
        tvMSupervisor.setOnClickListener(this);
        tvMCreate.setClickable(false);
        vMCreate = view.findViewById(R.id.v_mcreate);
        vMCreate.setVisibility(View.VISIBLE);
        vMParticipant = view.findViewById(R.id.v_mparticipant);
        vMSupervisor = view.findViewById(R.id.v_msupervisor);

        recyclerView = view.findViewById(R.id.rv_activity_list);
        activityItemList = new ArrayList<>();
        activityItemAdapter = new ActivityItemAdapter(activityItemList);
        activityItemAdapter.setItemClickListener(new ActivityItemAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                ActivityInfo.actionActivity(getContext(), activityItemList.get(position).getId(), isSupervisor);
            }
        });
        recyclerView.setAdapter(activityItemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mCreateList = new ArrayList<>();
        mParticipantList = new ArrayList<>();
        mSupervisorList = new ArrayList<>();

        isSupervisor = 0;

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_mcreate:
                isSupervisor = 0;
                tvMCreate.setClickable(false);
                tvMParticipant.setClickable(true);
                tvMSupervisor.setClickable(true);
                tvMCreate.setTextColor(colorBlue);
                tvMParticipant.setTextColor(colorBlack);
                tvMSupervisor.setTextColor(colorBlack);
                vMCreate.setVisibility(View.VISIBLE);
                vMParticipant.setVisibility(View.INVISIBLE);
                vMSupervisor.setVisibility(View.INVISIBLE);
                activityItemList.clear();
                activityItemList.addAll(mCreateList);
                activityItemAdapter.notifyDataSetChanged();
                break;
            case R.id.tv_mparticipant:
                isSupervisor = 0;
                tvMCreate.setClickable(true);
                tvMParticipant.setClickable(false);
                tvMSupervisor.setClickable(true);
                tvMCreate.setTextColor(colorBlack);
                tvMParticipant.setTextColor(colorBlue);
                tvMSupervisor.setTextColor(colorBlack);
                vMCreate.setVisibility(View.INVISIBLE);
                vMParticipant.setVisibility(View.VISIBLE);
                vMSupervisor.setVisibility(View.INVISIBLE);
                activityItemList.clear();
                activityItemList.addAll(mParticipantList);
                activityItemAdapter.notifyDataSetChanged();
                break;
            case R.id.tv_msupervisor:
                isSupervisor = 1;
                tvMCreate.setClickable(true);
                tvMParticipant.setClickable(true);
                tvMSupervisor.setClickable(false);
                tvMCreate.setTextColor(colorBlack);
                tvMParticipant.setTextColor(colorBlack);
                tvMSupervisor.setTextColor(colorBlue);
                vMCreate.setVisibility(View.INVISIBLE);
                vMParticipant.setVisibility(View.INVISIBLE);
                vMSupervisor.setVisibility(View.VISIBLE);
                activityItemList.clear();
                activityItemList.addAll(mSupervisorList);
                activityItemAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    public static void editView() {
        getParticipantActivity();
    }

    private static void getParticipantActivity() {
        HttpsUtil.sendPostRequest(HttpsUtil.getAllActivityAddr, getJsonData(), new HttpsListener() {
            @Override
            public void onSuccess(final String response) {
                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.e(TAG, response);
                            JSONObject data = new JSONObject(response);
                            String result = data.getString("result");
                            String reason = data.getString("reason");
                            List<String> IDs = new ArrayList<>();
                            IDs.clear();
                            activityItemList.clear();
                            mCreateList.clear();
                            mParticipantList.clear();
                            mSupervisorList.clear();
                            if (result.equals("true")) {
                                JSONArray array = new JSONArray(data.getString("activity"));
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    Log.e(TAG, "activity detail:" + object.toString());
                                    String name = object.getString("activity_name");
                                    String id = object.getString("activity_id");
                                    String creater = object.getString("creater");
                                    String supervisor = object.getString("supervisor");
                                    String startTime = object.getString("activity_time");
                                    String endTime = object.getString("activity_endTime");
//                                    activityItemList.add(new ActivityItem(name, id, creater, startTime, endTime));
                                    if (IDs.contains(id)) {
                                        continue;
                                    } else {
                                        IDs.add(id);
                                    }
                                    if (supervisor.equals(User.getAccount())) {
                                        mSupervisorList.add(new ActivityItem(name, id, creater, startTime, endTime));
                                    } else {
                                        if (creater.equals(User.getAccount())) {
                                            mCreateList.add(new ActivityItem(name, id, creater, startTime, endTime));
                                        }
                                        mParticipantList.add(new ActivityItem(name, id, creater, startTime, endTime));
                                    }
                                }
                            }
                            Collections.sort(mCreateList);
                            Collections.sort(mParticipantList);
                            Collections.sort(mSupervisorList);
                            activityItemList.addAll(mCreateList);
                            activityItemAdapter.notifyDataSetChanged();
                            tvMCreate.setClickable(false);
                            tvMParticipant.setClickable(true);
                            tvMSupervisor.setClickable(true);
                            tvMCreate.setTextColor(colorBlue);
                            tvMParticipant.setTextColor(colorBlack);
                            tvMSupervisor.setTextColor(colorBlack);
                            vMCreate.setVisibility(View.VISIBLE);
                            vMParticipant.setVisibility(View.INVISIBLE);
                            vMSupervisor.setVisibility(View.INVISIBLE);
                            isSupervisor = 0;
                            mainActivity.editTitleNumber(activityItemAdapter.getItemCount());
                        } catch (JSONException e) {
                            Log.e(TAG, "onSuccess:" + e.toString());
                        }
                    }
                });
            }

            @Override
            public void onFailure(Exception exception) {
                Log.e(TAG, "onFailure:" + exception.toString());
            }
        });
    }

    private static JSONObject getJsonData() {
        JSONObject data = new JSONObject();
        try {
            data.put("stu_id", User.getAccount());
        } catch (JSONException e) {
            Log.e(TAG, "getJsonData:" + e.toString());
        }
        return data;
    }
}
