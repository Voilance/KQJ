package com.biketomotor.kqj.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.biketomotor.kqj.R;

public class CreateActivity
        extends BaseActivity {

    private EditText etName;
    private EditText etPlace;
    private Button btCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setContentView(R.layout.activity_create);

        findViewById(R.id.title_number).setVisibility(View.INVISIBLE);
        findViewById(R.id.title_task).setVisibility(View.INVISIBLE);
        TextView tvTitleName = findViewById(R.id.title_name);
        tvTitleName.setText("新建活动");

        etName = findViewById(R.id.et_name);
        etPlace = findViewById(R.id.et_place);
        btCreate = findViewById(R.id.bt_create);
    }

    public static void actionActivity(Context context) {
        context.startActivity(new Intent(context, CreateActivity.class));
    }
}
