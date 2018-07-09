package com.biketomotor.kqj.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.biketomotor.kqj.R;

public class activity_welcome extends AppCompatActivity implements View.OnClickListener {

    private Button button_login;
    private Button button_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_welcome);

        button_login    = (Button)findViewById(R.id.layout_welcome_button_login);
        button_register = (Button)findViewById(R.id.layout_welcome_button_register);

        button_login.setOnClickListener(this);
        button_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_welcome_button_login:
                Intent to_login = new Intent(activity_welcome.this, activity_login.class);
                startActivity(to_login);
                break;
            case R.id.layout_welcome_button_register:
                Intent to_register = new Intent(activity_welcome.this, activity_register.class);
                startActivity(to_register);
                break;
        }
    }
}
