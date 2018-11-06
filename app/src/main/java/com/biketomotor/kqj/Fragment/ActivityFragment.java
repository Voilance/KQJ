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
        extends Fragment {
    private static final String TAG = "TagActivity";

    private MainActivity mainActivity;
    private List<ActivityItem> activityItemList;
    private ActivityItemAdapter activityItemAdapter;
    private RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity, container, false);

        mainActivity = (MainActivity)getActivity();
        recyclerView = view.findViewById(R.id.rv_activity_list);
        activityItemList = new ArrayList<>();
        activityItemAdapter = new ActivityItemAdapter(activityItemList);
        activityItemAdapter.setItemClickListener(new ActivityItemAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                ActivityInfo.actionActivity(getContext(), activityItemList.get(position).getId());
            }
        });
        recyclerView.setAdapter(activityItemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getParticipantActivity();
    }

    private void getParticipantActivity() {
        HttpsUtil.sendPostRequest(HttpsUtil.participantActivityAddress, getJsonData(), new HttpsListener() {
            @Override
            public void onSuccess(final String response) {
                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject data = new JSONObject(response);
                            String result = data.getString("result");
                            String reason = data.getString("reason");
                            activityItemList.clear();
                            if (result.equals("true")) {
                                JSONArray array = new JSONArray(data.getString("activity"));
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    String name = object.getString("activity_name");
                                    String id = object.getString("activity_id");
                                    String creater = object.getString("creater");
                                    String startTime = object.getString("activity_time");
                                    String endTime = object.getString("activity_endTime");
                                    activityItemList.add(new ActivityItem(name, id, creater, startTime, endTime));
                                }
                            } else {
//                                Toast.makeText(getContext(), reason, Toast.LENGTH_SHORT).show();
                            }
                            Collections.sort(activityItemList);
                            activityItemAdapter.notifyDataSetChanged();
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

    private JSONObject getJsonData() {
        JSONObject data = new JSONObject();
        try {
            data.put("participant", User.getAccount());
        } catch (JSONException e) {
            Log.e(TAG, "getJsonData:" + e.toString());
        }
        return data;
    }
}
