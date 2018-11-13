package com.biketomotor.kqj.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.biketomotor.kqj.Class.HttpsUtil;
import com.biketomotor.kqj.Interface.HttpsListener;
import com.biketomotor.kqj.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

    private String                             id;
    private String                             name;
    private String                             place;
    private String                             sTime;
    private String                             startDate;
    private String                             startTime;
    private String                             eTime;
    private String                             endDate;
    private String                             endTime;
    private String                             info;
    private String                             creater;
    private DatePickerDialog.OnDateSetListener onStartDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            String y = String.valueOf(year);
            String m = month < 9 ? "0" + String.valueOf(month + 1) : String.valueOf(month + 1);
            String d = day < 10 ? "0" + String.valueOf(day) : String.valueOf(day);
            startDate = y + "-" + m + "-" + d;
            tvStartDate.setText(startDate);
        }
    };
    private TimePickerDialog.OnTimeSetListener onStartTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            String h = hour < 10 ? "0" + String.valueOf(hour) : String.valueOf(hour);
            String m = minute < 10 ? "0" + String.valueOf(minute) : String.valueOf(minute);
            startTime = h + ":" + m;
            tvStartTime.setText(startTime);
        }
    };
    private DatePickerDialog.OnDateSetListener onEndDateListener   = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            String y = String.valueOf(year);
            String m = month < 9 ? "0" + String.valueOf(month + 1) : String.valueOf(month + 1);
            String d = day < 10 ? "0" + String.valueOf(day) : String.valueOf(day);
            endDate = y + "-" + m + "-" + d;
            tvEndDate.setText(endDate);
        }
    };
    private TimePickerDialog.OnTimeSetListener onEndTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            String h = hour < 10 ? "0" + String.valueOf(hour) : String.valueOf(hour);
            String m = minute < 10 ? "0" + String.valueOf(minute) : String.valueOf(minute);
            endTime = h + ":" + m;
            tvEndTime.setText(endTime);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setContentView(R.layout.activity_edit);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        name = intent.getStringExtra("name");
        place = intent.getStringExtra("place");
        sTime = intent.getStringExtra("startTime");
        eTime = intent.getStringExtra("endTime");
        getTime();
        info = intent.getStringExtra("info");
        creater = intent.getStringExtra("creater");

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

        etName.setText(name);
        etPlace.setText(place);
        tvStartDate.setText(startDate);
        tvStartTime.setText(startTime);
        tvEndDate.setText(endDate);
        tvEndTime.setText(endTime);
        etInfo.setText(info);
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
                new DatePickerDialog(EditActivity.this,
                        AlertDialog.THEME_HOLO_LIGHT,
                        onStartDateListener,
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.tv_start_time:
                new TimePickerDialog(EditActivity.this,
                        AlertDialog.THEME_HOLO_LIGHT,
                        onStartTimeListener,
                        Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                        Calendar.getInstance().get(Calendar.MINUTE),
                        true).show();
                break;
            case R.id.tv_end_date:
                new DatePickerDialog(EditActivity.this,
                        AlertDialog.THEME_HOLO_LIGHT,
                        onEndDateListener,
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.tv_end_time:
                new TimePickerDialog(EditActivity.this,
                        AlertDialog.THEME_HOLO_LIGHT,
                        onEndTimeListener,
                        Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                        Calendar.getInstance().get(Calendar.MINUTE),
                        true).show();
                break;
            case R.id.bt_edit:
                if (isInfoValid()) {
                    onEditActivity();
                }
                break;
            case R.id.bt_delete:
                onDeleteActivity();
                break;
            default:
                break;
        }
    }

    private boolean isInfoValid() {
        name = etName.getText().toString().trim();
        if (name.length() == 0) {
            toast("请输入活动名");
            return false;
        }
        place = etPlace.getText().toString().trim();
        if (place.length() == 0) {
            toast("请输入活动地点");
            return false;
        }
        if (startDate.length() == 0) {
            toast("请输入活动开始日期");
            return false;
        }
        if (startTime.length() == 0) {
            toast("请输入活动开始时间");
            return false;
        }
        if (endDate.length() == 0) {
            toast("请输入签到截至日期");
            return false;
        }
        if (endTime.length() == 0) {
            toast("请输入签到截至时间");
            return false;
        }
        if (getTimestamp(endDate, endTime) <= getTimestamp(startDate, startTime)) {
            toast("签到截止时间不可以早于活动开始时间");
            return false;
        }
        info = etInfo.getText().toString().trim();
        if (info == null || info.length() == 0) {
            info = "没有简介。";
        }
        return true;
    }

    private void onEditActivity() {
        HttpsUtil.sendPostRequest(HttpsUtil.editActivityAddr, getJsonDataForEditActivity(), new HttpsListener() {
            @Override
            public void onSuccess(final String response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject data = new JSONObject(response);
                            String result = data.getString("result");
                            String reason = data.getString("reason");
                            if (result.equals("true")) {
                                finish();
                            } else {
                                toast(reason);
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "onEditActivity/onSuccess:" + e.toString());
                        }
                    }
                });
            }

            @Override
            public void onFailure(Exception exception) {
                Log.e(TAG, "onEditActivity/onFailure:" + exception.toString());
            }
        });
    }

    private void onDeleteActivity() {
        HttpsUtil.sendPostRequest(HttpsUtil.deleteActivityAddr, getJsonDataForDeleteActivity(), new HttpsListener() {
            @Override
            public void onSuccess(final String response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject data = new JSONObject(response);
                            String result = data.getString("result");
                            if (result.equals("true")) {
                                finish();
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "onDeleteActivity/onSuccess:" + e.toString());
                        }
                    }
                });
            }

            @Override
            public void onFailure(Exception exception) {
                Log.e(TAG, "onDeleteActivity/onFailure:" + exception.toString());
            }
        });
    }

    private JSONObject getJsonDataForEditActivity() {
        JSONObject data = new JSONObject();
        try {
            data.put("id", id);
            data.put("name", name);
            data.put("place", place);
            data.put("time", String.valueOf(getTimestamp(startDate, startTime)));
            data.put("endTime", String.valueOf(getTimestamp(endDate, endTime)));
            data.put("info", info);
        } catch (JSONException e) {
            Log.e(TAG, "getJsonDataForEditActivity:" + e.toString());
        }
        return data;
    }

    private JSONObject getJsonDataForDeleteActivity() {
        JSONObject data = new JSONObject();
        try {
            data.put("id", id);
        } catch (JSONException e) {
            Log.e(TAG, "getJsonDataForDeleteActivity:" + e.toString());
        }
        return data;
    }

    private long getTimestamp(String date, String time) {
        long timestamp = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        sdf.setTimeZone(Calendar.getInstance().getTimeZone());
        try {
            Date d = sdf.parse(date + " " + time);
            timestamp = d.getTime();
        } catch (ParseException e) {
            Log.e(TAG, "getTimestamp:" + e.toString());
        }
        return timestamp;
    }

    private void getTime() {
        SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String Ts = sdf.format(new Date(Long.valueOf(sTime)));
        String Te = sdf.format(new Date(Long.valueOf(eTime)));
        String[] ts = Ts.split(" ");
        String[] te = Te.split(" ");
        startDate = ts[0];
        startTime = ts[1];
        endDate = te[0];
        endTime = te[1];
    }

    public static void actionActivity(
            Context context,
            String id,
            String name,
            String place,
            String startTime,
            String endTime,
            String info,
            String creater
    ) {
        Intent intent = new Intent(context, EditActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("name", name);
        intent.putExtra("place", place);
        intent.putExtra("startTime", startTime);
        intent.putExtra("endTime", endTime);
        intent.putExtra("info", info);
        intent.putExtra("creater", creater);
        context.startActivity(intent);
    }
}