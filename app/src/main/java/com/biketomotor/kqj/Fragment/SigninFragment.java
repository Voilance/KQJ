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
import java.util.Date;

public class SigninFragment
        extends Fragment
        implements View.OnClickListener {
    private static final String TAG = "TagSignin";

    private static MainActivity mainActivity;
    private static TextView tvTips;
    private static TextView tvName;
    private static Button btSignin;

    private static String id;
    private static String name;
    private static String startTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signin, container, false);

        mainActivity = (MainActivity)getActivity();
        tvName = view.findViewById(R.id.tv_name);
        tvTips = view.findViewById(R.id.tv_tips);
        btSignin = view.findViewById(R.id.bt_signin);
        btSignin.setOnClickListener(this);

        id = "";
        name = "";
        startTime = "";

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
                    ActivityInfo.actionActivity(mainActivity, id);
                }
                break;
            default:
                break;
        }
    }

    private static void onUrgentActivity() {
        HttpsUtil.sendPostRequest(HttpsUtil.urgentActivityAddr, getJsonData(), new HttpsListener() {
            @Override
            public void onSuccess(final String response) {
                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.e(TAG, response);
                            JSONObject data = new JSONObject(response);
                            String result = data.getString("result");
                            if (result.equals("true")) {
                                id = data.getString("id");
                                name = data.getString("name");
                                startTime = data.getString("time");
                                tvName.setText(name);
                                btSignin.setText("签到\n" + getTime(startTime));
                            } else {
                                id = "";
                                name = "";
                                startTime = "";
                                tvName.setText("暂无活动");
                                btSignin.setText("闲");
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

    private static JSONObject getJsonData() {
        JSONObject data = new JSONObject();
        try {
            data.put("participant", User.getAccount());
        } catch (JSONException e) {
            Log.e(TAG, "getJsonData:" + e.toString());
        }
        return data;
    }

    private static String getTime(String timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd\nHH:mm");
        String time = sdf.format(new Date(Long.valueOf(timestamp)));
        return time;
    }
}
