package com.biketomotor.kqj.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import com.biketomotor.kqj.Activity.MainActivity;
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
    private static int height; // GridLayout里子控件的高度
    private static int width; // GridLayout里子控件的宽度

    private static MainActivity mainActivity;
    private static int curWeek;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test_fragment_curriculum, container, false);
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
                break;
            default:
                break;
        }
    }

    private static void initChildView() {
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
    }

    private void addChildView(int w, int h, int c, int r) {

    }

    private static void getCourseList() {
        HttpsUtil.sendPostRequest(HttpsUtil.getCourseAddr, getJsonDataForCourseList(), new HttpsListener() {
            @Override
            public void onSuccess(final String response) {
                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "getCourseList/run: " + response);
                        initChildView();
                    }
                });
            }

            @Override
            public void onFailure(Exception exception) {
                Log.e(TAG, "getCourseList/onFailure:" + exception.toString());
            }
        });
    }

    private static JSONObject getJsonDataForCourseList() {
        JSONObject data = new JSONObject();
        try {
            data.put("stu_id", User.getAccount());
            data.put("week", curWeek);
        } catch (JSONException e) {
            Log.e(TAG, "getJsonData:" + e.toString());
        }
        return data;
    }

//    private void addCourse() {
//        HttpsUtil.sendPostRequest();
//    }

    private JSONObject getJsonDataForAddCourse() {
        JSONObject data = new JSONObject();
        return data;
    }
}
