package com.biketomotor.kqj.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.biketomotor.kqj.Class.HttpsUtil;
import com.biketomotor.kqj.Class.User;
import com.biketomotor.kqj.Interface.HttpsListener;
import com.biketomotor.kqj.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddCourseActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "TagAddCourse";

    private EditText etCourseName;
    private EditText etClassroom;
    private EditText etTeacher;
    private TextView tvSelectWeeks;
    private TextView tvSelectWeekdays;
    private TextView tvStartTime;
    private TextView tvEndTime;
    private Button btAddCourse;

    private final String[] weeks = {
            "第一周", "第二周", "第三周", "第四周", "第五周",
            "第六周", "第七周", "第八周", "第九周", "第十周",
            "第十一周", "第十二周", "第十三周", "第十四周", "第十五周",
            "第十六周", "第十七周", "第十八周", "第十九周", "第二十周"};
    private boolean[] weeksStatus = {
            false, false, false, false, false,
            false, false, false, false, false,
            false, false, false, false, false,
            false, false, false, false, false};
    private final String[] weekdays = {
            "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期天"};
    private final String[] Lessons = {
            "第1节", "第2节", "第3节", "第4节", "第5节", "第6节",
            "第7节", "第8节", "第9节", "第10节", "第11节"};

    private AlertDialog weeksDialog;
    private AlertDialog weekdaysDialog;
    private AlertDialog startLessonDialog;
    private AlertDialog endLessonDialog;

    private final ArrayList<Integer> selectedWeeks = new ArrayList<>(0);
    private Integer selectedWeekday;
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

        etCourseName = findViewById(R.id.et_course_name);
        etClassroom = findViewById(R.id.et_classroom);
        etTeacher = findViewById(R.id.et_teacher);
        tvSelectWeeks = findViewById(R.id.tv_select_weeks);
        tvSelectWeekdays = findViewById(R.id.tv_select_weekdays);
        tvStartTime = findViewById(R.id.tv_start_time);
        tvEndTime = findViewById(R.id.tv_end_time);
        btAddCourse = findViewById(R.id.bt_add_course);
        tvSelectWeeks.setOnClickListener(this);
        tvSelectWeekdays.setOnClickListener(this);
        tvStartTime.setOnClickListener(this);
        tvEndTime.setOnClickListener(this);
        btAddCourse.setOnClickListener(this);

        selectedWeekday = -1;
        startLesson = -1;
        endLesson = -1;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_select_weeks:
                selectWeeks();
                break;
            case R.id.tv_select_weekdays:
                break;
            case R.id.tv_start_time:
                break;
            case R.id.tv_end_time:
                break;
            case R.id.bt_add_course:
                Log.e(TAG, "onClick: " + selectedWeeks.toString());
                break;
            default:
                break;
        }
    }

    private void selectWeeks() {
        final boolean[] temp = new boolean[weeksStatus.length];
        for (int i = 0; i < weeksStatus.length; i++) {
            temp[i] = weeksStatus[i];
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(AddCourseActivity.this);
        builder.setTitle("选择上课周数");
        builder.setMultiChoiceItems(weeks, temp, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                temp[i] = b;
            }
        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selectedWeeks.clear();
                for (int j = 0; j < weeksStatus.length; j++) {
                    weeksStatus[j] = temp[j];
                    if (weeksStatus[j]) {
                        selectedWeeks.add(j + 1);
                    }
                }
            }
        }).setNegativeButton("取消", null);
        weeksDialog = builder.create();
        weeksDialog.show();
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
