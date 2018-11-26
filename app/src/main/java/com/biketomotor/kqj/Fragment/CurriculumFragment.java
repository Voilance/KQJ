package com.biketomotor.kqj.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.biketomotor.kqj.Activity.MainActivity;
import com.biketomotor.kqj.R;

public class CurriculumFragment
        extends Fragment {
    private static final String TAG = "TagCurriculum";

    private static MainActivity mainActivity;
    private RelativeLayout curriculum;
    private int height;
    private int width;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test_fragment_curriculum, container, false);
        initView(view);

        return view;
    }

    private void initView(View view) {
        mainActivity = (MainActivity)getActivity();
        curriculum = view.findViewById(R.id.rl_curriculum);
        curriculum.measure(0, 0);
        height = curriculum.getMeasuredHeight() / 11;
        width = curriculum.getMeasuredWidth() / 7;
        f();
    }

    private void f() {

        int color = getResources().getColor(R.color.colorLightBlue);
        TextView tv = new TextView(mainActivity);
        tv.setText("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
        ;
        tv.setBackgroundColor(color);
//        tv.layout(0, 2 * height, 3 * width, 4 * height);
        ViewGroup.LayoutParams p = new RelativeLayout.LayoutParams(width, height);
        curriculum.addView(tv, p);
    }
}
