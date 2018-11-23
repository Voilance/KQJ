package com.biketomotor.kqj.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.biketomotor.kqj.Activity.ActivityInfo;
import com.biketomotor.kqj.Activity.MainActivity;
import com.biketomotor.kqj.Class.HttpsUtil;
import com.biketomotor.kqj.Class.User;
import com.biketomotor.kqj.Interface.HttpsListener;
import com.biketomotor.kqj.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SigninFragment
        extends Fragment
        implements View.OnClickListener {
    private static final String TAG = "TagSignin";

    private static MainActivity mainActivity;
    private static TextView tvTips;
    private static TextView tvName;
    private static Button btSignin;

    private static String id = "";
    private static String name = "";
    private static String startTime = "";
    private static String endTime = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signin, container, false);

        mainActivity = (MainActivity)getActivity();
        tvName = view.findViewById(R.id.tv_name);
        tvTips = view.findViewById(R.id.tv_tips);
        btSignin = view.findViewById(R.id.bt_signin);
        btSignin.setOnClickListener(this);

        return view;
    }

    public static void editView() {
        onUrgentActivity();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_signin:
                if (id.length() != 0) {
                    long cT = Calendar.getInstance().getTimeInMillis();
                    long eT = Long.valueOf(endTime);
                    if (cT < eT) { // 活动还没结束
                        ActivityInfo.actionActivity(mainActivity, id);
                    } else {
                        onActivityPassed();
                    }
                }
                break;
            default:
                break;
        }
    }

    private static void onUrgentActivity() {
        HttpsUtil.sendPostRequest(HttpsUtil.urgentActivityAddr, getJsonDataForUrgentActivity(), new HttpsListener() {
            @Override
            public void onSuccess(final String response) {
                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject data = new JSONObject(response);
                            String result = data.getString("result");
                            if (result.equals("true")) {
                                Log.e(TAG, response);
                                id = data.getString("id");
                                name = data.getString("name");
                                startTime = data.getString("time");
                                endTime = data.getString("endTime");
                                tvName.setText(name);
                                long cT = Calendar.getInstance().getTimeInMillis();
                                long sT = Long.valueOf(startTime);
                                if ((cT + 1800000) < sT) {
                                    tvTips.setText("淡定~大兄弟");
                                    btSignin.setText("签到\n" + getTime(startTime));
                                } else if ((cT + 1800000) > sT && cT < sT) {
                                    tvTips.setText("跑起来~兄嘚");
                                    btSignin.setText("签到\n" + getTime(startTime));
                                } else {
                                    tvTips.setText("怎么萎事啊小老弟？");
                                    btSignin.setText("活动开始了\n" + getTime(startTime));
                                }
                            } else {
                                id = "";
                                name = "";
                                startTime = "";
                                endTime = "";
                                tvName.setText("暂无活动");
                                btSignin.setText("真闲呐~");
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "onUrgentActivity/onSuccess:" + e.toString());
                        }
                    }
                });
            }

            @Override
            public void onFailure(Exception exception) {
                Log.e(TAG, "onUrgentActivity/onFailure:" + exception.toString());
            }
        });
    }

    private static JSONObject getJsonDataForUrgentActivity() {
        JSONObject data = new JSONObject();
        try {
            data.put("participant", User.getAccount());
        } catch (JSONException e) {
            Log.e(TAG, "getJsonData:" + e.toString());
        }
        return data;
    }

    private void onActivityPassed() {
        HttpsUtil.sendPostRequest(HttpsUtil.activityPassedAddr, getJsonDataForActivityPassed(), new HttpsListener() {
            @Override
            public void onSuccess(final String response) {
                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "hi");
                        onUrgentActivity();
                    }
                });
            }

            @Override
            public void onFailure(Exception exception) {
                Log.e(TAG, "onActivityPassed/onFailure:" + exception.toString());
            }
        });
    }

    private JSONObject getJsonDataForActivityPassed() {
        JSONObject data = new JSONObject();
        try {
            data.put("participant", User.getAccount());
            data.put("id", id);
        } catch (JSONException e) {
            Log.e(TAG, "getJsonDataForActivityPassed:" + e.toString());
        }
        return data;
    }

    private static String getTime(String timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd\nHH:mm");
        String time = sdf.format(new Date(Long.valueOf(timestamp)));
        return time;
    }
}
