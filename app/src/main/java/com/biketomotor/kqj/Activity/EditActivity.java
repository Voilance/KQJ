package com.biketomotor.kqj.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.biketomotor.kqj.R;

public class EditActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "TagEditActivity";

    private EditText etName;
    private EditText etPlace;
    private TextView tvStartDate;
    private TextView tvStartTime;
    private TextView tvEndDate;
    private TextView tvEndTime;
    private EditText etInfo;
    private Button   btEdit;
    private Button btDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setContentView(R.layout.activity_edit);

        findViewById(R.id.title_number).setVisibility(View.INVISIBLE);
        findViewById(R.id.title_task).setVisibility(View.INVISIBLE);
        TextView tvTitleName = findViewById(R.id.title_name);
        tvTitleName.setText("修改活动信息");

        etName = findViewById(R.id.et_name);
        etPlace = findViewById(R.id.et_place);
        tvStartDate = findViewById(R.id.tv_start_date);
        tvStartTime = findViewById(R.id.tv_start_time);
        tvEndDate = findViewById(R.id.tv_end_date);
        tvEndTime = findViewById(R.id.tv_end_time);
        etInfo = findViewById(R.id.et_info);
        btEdit = findViewById(R.id.bt_edit);
        btDelete = findViewById(R.id.bt_delete);

        tvStartDate.setOnClickListener(this);
        tvStartTime.setOnClickListener(this);
        tvEndTime.setOnClickListener(this);
        tvEndDate.setOnClickListener(this);
        btEdit.setOnClickListener(this);
        btDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_start_date:
                break;
            case R.id.tv_start_time:
                break;
            case R.id.tv_end_date:
                break;
            case R.id.tv_end_time:
                break;
            case R.id.bt_edit:
                break;
            case R.id.bt_delete:
                break;
            default:
                break;
        }
    }

    public static void actionActivity(Context context) {
        context.startActivity(new Intent(context, EditActivity.class));
    }
}
