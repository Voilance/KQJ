package com.biketomotor.kqj.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biketomotor.kqj.Activity.EditCourse;
import com.biketomotor.kqj.Activity.MainActivity;
import com.biketomotor.kqj.Class.CourseList;
import com.biketomotor.kqj.Class.HttpsUtil;
import com.biketomotor.kqj.Class.User;
import com.biketomotor.kqj.Interface.HttpsListener;
import com.biketomotor.kqj.R;

import org.json.JSONException;
import org.json.JSONObject;


public class CurriculumFragment
        extends Fragment implements View.OnClickListener {
    private static final String TAG = "TagCurriculum";

    private TextView tvWeek;
    private static GridLayout curriculum;
    private static int col = 7; // GridLayout里子控件的列数
    private static int row = 11; // GridLayout里子控件的行数
    private static int height = 0; // GridLayout里子控件的高度
    private static int width = 0; // GridLayout里子控件的宽度
    private static int backgroundColor = 0;
    private static MainActivity mainActivity;

    private static int curWeek;
    private final String[] weeks = {
            "第一周", "第二周", "第三周", "第四周", "第五周",
            "第六周", "第七周", "第八周", "第九周", "第十周",
            "第十一周", "第十二周", "第十三周", "第十四周", "第十五周",
            "第十六周", "第十七周", "第十八周", "第十九周", "第二十周"};
    private AlertDialog weeksDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_curriculum, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mainActivity = (MainActivity)getActivity();
        curWeek = 1;

        tvWeek = view.findViewById(R.id.tv_week);
        tvWeek.setOnClickListener(this);
        curriculum = view.findViewById(R.id.gl_curriculum);
    }

    public static void editView() {
        getCourseList();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_week:
                selectWeek();
                break;
            default:
                break;
        }
    }

    private void selectWeek() {
        final int temp = curWeek;
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setTitle("您想查看哪一周的课程？");
        builder.setSingleChoiceItems(weeks, curWeek, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                curWeek = which;
            }
        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tvWeek.setText(weeks[curWeek]);
                curWeek += 1;
                initChildView();
                addChildView();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                curWeek = temp;
            }
        });
        weeksDialog = builder.create();
        weeksDialog.show();
    }

    private static void initChildView() {
        curriculum.removeAllViews();
        height = curriculum.getHeight() / row;
        width = curriculum.getWidth() / col;
        for (int i = 0; i < col; i++) {
            for (int j = 0; j < row; j++) {
                TextView tv = new TextView(mainActivity);
                GridLayout.LayoutParams gp = new GridLayout.LayoutParams();
                gp.width = width;
                gp.height = height;
                gp.columnSpec = GridLayout.spec(i);
                gp.rowSpec = GridLayout.spec(j);
                curriculum.addView(tv, gp);
            }
        }
        backgroundColor = mainActivity.getResources().getColor(R.color.colorLightBlue);
    }

    private static void addChildView() {
        for (final int courseID : CourseList.courseMap.keySet()) {
            final CourseList.Course course = CourseList.courseMap.get(courseID);
            if (course.weeks.contains(curWeek)) {
                int c = course.weekday - 1;
                int r = course.startLesson -1;
                int h = course.endLesson - r;

                final TextView tv = new TextView(mainActivity);
                tv.setTag(courseID);
                tv.setText(course.info());
                tv.setBackgroundColor(backgroundColor);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditCourse.actionActivity(mainActivity, courseID);
                    }
                });
                tv.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                        builder.setTitle("删除课程");
                        builder.setMessage("是否确认删除课程？");
                        builder.setCancelable(true);
                        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteCourse(courseID);
                                dialog.dismiss();
                            }
                        }).setNegativeButton("取消", null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        return true;
                    }
                });
                GridLayout.LayoutParams gp = new GridLayout.LayoutParams();
                gp.width = width;
                gp.height = height * h;
                gp.columnSpec = GridLayout.spec(c);
                gp.rowSpec = GridLayout.spec(r, h);
                curriculum.addView(tv, gp);
            }
        }
    }

    private static void getCourseList() {
        HttpsUtil.sendPostRequest(HttpsUtil.getAllCourseAddr, getJsonData(), new HttpsListener() {
            @Override
            public void onSuccess(final String response) {
                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "getCourseList/run: " + response);
                        try {
                            JSONObject data = new JSONObject(response);
                            String result = data.getString("result");
                            String reason = data.getString("reason");
                            if (result.equals("true")) {
                                CourseList.initCourseList(data);
                                initChildView();
                                addChildView();
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "getCourseList/onSuccess:" + e.toString());
                        }
                    }
                });
            }

            @Override
            public void onFailure(Exception exception) {
                Log.e(TAG, "getCourseList/onFailure:" + exception.toString());
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

    private static void deleteCourse(int courseId) {
        HttpsUtil.sendPostRequest(HttpsUtil.deleteCourseAddr, getJsonDataForDeleteCourse(courseId), new HttpsListener() {
            @Override
            public void onSuccess(final String response) {
                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.e(TAG, "run: " + response);
                            JSONObject data = new JSONObject(response);
                            String result = data.getString("result");
                            String reason = data.getString("reason");
                            if (result.equals("true")) {
                                MainActivity.editView();
                                Toast.makeText(mainActivity, "成功删除", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mainActivity, reason, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "deleteCourse/onSuccess:" + e.toString());
                        }
                    }
                });
            }

            @Override
            public void onFailure(Exception exception) {
                Log.e(TAG, "deleteCourse/onFailure:" + exception.toString());
            }
        });
    }

    private static JSONObject getJsonDataForDeleteCourse(int courseId) {
        JSONObject data = new JSONObject();
        try {
            data.put("stu_id", User.getAccount());
            data.put("courseId", courseId);
        } catch (JSONException e) {
            Log.e(TAG, "getJsonDataForDeleteCourse:" + e.toString());
        }
        return data;
    }
}
