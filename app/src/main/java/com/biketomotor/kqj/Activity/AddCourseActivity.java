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
    private TextView tvStartLesson;
    private TextView tvEndLesson;
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
    private final String[] lessons = {
            "第1节", "第2节", "第3节", "第4节", "第5节", "第6节",
            "第7节", "第8节", "第9节", "第10节", "第11节"};

    private AlertDialog weeksDialog;
    private AlertDialog weekdaysDialog;
    private AlertDialog startLessonDialog;
    private AlertDialog endLessonDialog;

    private String courseName;
    private String classroom;
    private String teacher;
    private final ArrayList<Integer> selectedWeeks = new ArrayList<>(0);
    private Integer selectedWeekday;
    private Integer selectedStartLesson;
    private Integer selectedEndLesson;


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
        tvStartLesson = findViewById(R.id.tv_start_lesson);
        tvEndLesson = findViewById(R.id.tv_end_lesson);
        btAddCourse = findViewById(R.id.bt_add_course);
        tvSelectWeeks.setOnClickListener(this);
        tvSelectWeekdays.setOnClickListener(this);
        tvStartLesson.setOnClickListener(this);
        tvEndLesson.setOnClickListener(this);
        btAddCourse.setOnClickListener(this);

        selectedWeekday = -1;
        selectedStartLesson = -1;
        selectedEndLesson = -1;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_select_weeks:
                selectWeeks();
                break;
            case R.id.tv_select_weekdays:
                selectWeekdays();
                break;
            case R.id.tv_start_lesson:
                selectStartLesson();
                break;
            case R.id.tv_end_lesson:
                selectEndLesson();
                break;
            case R.id.bt_add_course:
                if (isInfoValid()) {
                    addCourse();
                }
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
                if (selectedWeeks.size() > 0) {
                    tvSelectWeeks.setText("共" + String.valueOf(selectedWeeks.size()) + "周");
                } else {
                    tvSelectWeeks.setText("");
                }
            }
        }).setNegativeButton("取消", null);
        weeksDialog = builder.create();
        weeksDialog.show();
    }

    private void selectWeekdays() {
        final Integer temp = selectedWeekday;
        AlertDialog.Builder builder = new AlertDialog.Builder(AddCourseActivity.this);
        builder.setTitle("选择上课周天");
        builder.setSingleChoiceItems(weekdays, selectedWeekday, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedWeekday = which;
            }
        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tvSelectWeekdays.setText(weekdays[selectedWeekday]);
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedWeekday = temp;
            }
        });
        weekdaysDialog = builder.create();
        weekdaysDialog.show();
    }

    private void selectStartLesson() {
        final Integer temp = selectedStartLesson;
        AlertDialog.Builder builder = new AlertDialog.Builder(AddCourseActivity.this);
        builder.setTitle("选择上课节次");
        builder.setSingleChoiceItems(lessons, selectedStartLesson, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedStartLesson = which;
            }
        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tvStartLesson.setText(lessons[selectedStartLesson]);
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedStartLesson = temp;
            }
        });
        startLessonDialog = builder.create();
        startLessonDialog.show();
    }

    private void selectEndLesson() {
        final Integer temp = selectedEndLesson;
        AlertDialog.Builder builder = new AlertDialog.Builder(AddCourseActivity.this);
        builder.setTitle("选择上课节次");
        builder.setSingleChoiceItems(lessons, selectedEndLesson, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedEndLesson = which;
            }
        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tvEndLesson.setText(lessons[selectedEndLesson]);
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedEndLesson = temp;
            }
        });
        endLessonDialog = builder.create();
        endLessonDialog.show();
    }

    private boolean isInfoValid() {
        courseName = etCourseName.getText().toString().trim();
        if (courseName.length() == 0) {
            toast("请输入课程名称");
            return false;
        }
        classroom = etClassroom.getText().toString().trim();
        if (classroom.length() == 0) {
            toast("请输入上课教室");
            return false;
        }
        teacher = etTeacher.getText().toString().trim();
        if (teacher.length() == 0) {
            toast("请输入任课教室");
            return false;
        }
        if (selectedWeeks.size() == 0) {
            toast("请输入上课周数");
            return false;
        }
        if (selectedWeekday < 0) {
            toast("请输入上课周天");
            return false;
        }
        if (selectedStartLesson < 0) {
            toast("请输入上课节次");
            return false;
        }
        if (selectedEndLesson < 0) {
            toast("请输入下课节次");
            return false;
        }
        if (selectedEndLesson < selectedStartLesson) {
            toast("下课节次不应该早于上课节次");
            return false;
        }
        return true;
    }

    private void addCourse() {
//        Log.e(TAG, "addCourse: " + getJsonData().toString());
        HttpsUtil.sendPostRequest(HttpsUtil.addCourseAddr, getJsonData(), new HttpsListener() {
            @Override
            public void onSuccess(final String response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, response);
                        try {
                            JSONObject data = new JSONObject(response);
                            String result = data.getString("result");
                            String reason = data.getString("reason");
                            if (result.equals("true")) {
                                toast("添加成功");
                                MainActivity.editView();
                                finish();
                            } else {
                                toast(reason);
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "addCourse/onSuccess:" + e.toString());
                        }
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
            courseData.put("weeks", selectedWeeks);
            courseData.put("weekDay", selectedWeekday + 1);
            courseData.put("startTime", selectedStartLesson + 1);
            courseData.put("endTime", selectedEndLesson + 1);
            data.put("courseData", courseData);
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
