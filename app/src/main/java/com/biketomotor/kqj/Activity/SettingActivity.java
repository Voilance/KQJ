package com.biketomotor.kqj.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.biketomotor.kqj.Class.Sys;
import com.biketomotor.kqj.Class.User;
import com.biketomotor.kqj.R;

public class SettingActivity
        extends BaseActivity
        implements View.OnClickListener {
    private static final String TAG = "TagSetting";

    private TextView tvLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setContentView(R.layout.activity_setting);

        findViewById(R.id.title_number).setVisibility(View.INVISIBLE);
        findViewById(R.id.title_task).setVisibility(View.INVISIBLE);
        TextView tvTitleName = findViewById(R.id.title_name);
        tvTitleName.setText("设置");

        tvLogout = findViewById(R.id.tv_logout);
        tvLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_logout:
                User.setOnline(false);
                Sys.setLogin(false);
                Sys.writeSP(getSharedPreferences(Sys.SPName, Context.MODE_PRIVATE));
                finish();
                break;
            default:
                break;
        }
    }

    public static void actionActivity(Context context) {
        context.startActivity(new Intent(context, SettingActivity.class));
    }

}
