package com.biketomotor.kqj.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.biketomotor.kqj.R;

public class UserInfo
        extends BaseActivity {
    private static final String TAG = "TagUserInfo";

    private String account;
    private int requestCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setContentView(R.layout.user_info);

        findViewById(R.id.title_number).setVisibility(View.INVISIBLE);
        findViewById(R.id.title_task).setVisibility(View.INVISIBLE);
        TextView tvTitleName = findViewById(R.id.title_name);
        tvTitleName.setText("简介");

        Intent intent = getIntent();
        account = intent.getStringExtra("account");
        requestCode = intent.getIntExtra("requestCode", 0);
        toast(account);
    }

    public static void actionActivity(Context context, String acount, int code) {
        Intent intent = new Intent(context, UserInfo.class);
        intent.putExtra("account", acount);
        intent.putExtra("requestCode", code);
        context.startActivity(intent);
    }
}
