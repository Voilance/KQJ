package com.biketomotor.kqj.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.biketomotor.kqj.R;

public class RegisterActivity
        extends BaseActivity {
    private static final String TAG = "TagRegister";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initView() {
        setContentView(R.layout.activity_register);
    }

    public static void actionActivity(Context context) {
        context.startActivity(new Intent(context, RegisterActivity.class));
    }
}
