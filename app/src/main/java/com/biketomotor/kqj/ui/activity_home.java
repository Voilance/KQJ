package com.biketomotor.kqj.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.biketomotor.kqj.R;

public class activity_home extends AppCompatActivity implements View.OnClickListener {

    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_home);

        button1 = (Button)findViewById(R.id.layout_home_linear_bottom_button1);
        button2 = (Button)findViewById(R.id.layout_home_linear_bottom_button2);
        button3 = (Button)findViewById(R.id.layout_home_linear_bottom_button3);
        button4 = (Button)findViewById(R.id.layout_home_linear_bottom_button4);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_home_linear_bottom_button1:
                break;
            case R.id.layout_home_linear_bottom_button2:
                break;
            case R.id.layout_home_linear_bottom_button3:
                break;
            case R.id.layout_home_linear_bottom_button4:
                break;
        }
    }
}
