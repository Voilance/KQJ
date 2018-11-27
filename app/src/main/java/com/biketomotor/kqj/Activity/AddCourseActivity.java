package com.biketomotor.kqj.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.biketomotor.kqj.Class.HttpsUtil;
import com.biketomotor.kqj.Class.User;
import com.biketomotor.kqj.Interface.HttpsListener;
import com.biketomotor.kqj.R;

import org.json.JSONException;
import org.json.JSONObject;

public class AddCourseActivity extends BaseActivity {
    private static final String TAG = "TagAddCourse";

    private String courseName;
    private String classroom;
    private String teacher;
    private Integer weekday;
    private Integer startLesson;
    private Integer endLesson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setContentView(R.layout.activity_add_course);

        findViewById(R.id.title_number).setVisibility(View.INVISIBLE);
        findViewById(R.id.title_task).setVisibility(View.INVISIBLE);
        TextView tvTitleName = findViewById(R.id.title_name);
        tvTitleName.setText("添加课程");
    }

    private void addCourse() {
        HttpsUtil.sendPostRequest(HttpsUtil.addCourseAddr, getJsonData(), new HttpsListener() {
            @Override
            public void onSuccess(final String response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, response);
                    }
                });
            }

            @Override
            public void onFailure(Exception exception) {
                Log.e(TAG, "addCourse/onFailure:" + exception.toString());
            }
        });
    }

    private JSONObject getJsonData() {
        JSONObject data = new JSONObject();
        try {
            data.put("stu_id", User.getAccount());
            JSONObject courseData = new JSONObject();
            courseData.put("courseName", courseName);
            courseData.put("classroom", classroom);
            courseData.put("teacher", teacher);
        } catch (JSONException e) {
            Log.e(TAG, "getJsonData:" + e.toString());
        }
        return data;
    }

    public static void actionActivity(Context context) {
        Intent intent = new Intent(context, AddCourseActivity.class);
        context.startActivity(intent);
    }
}
