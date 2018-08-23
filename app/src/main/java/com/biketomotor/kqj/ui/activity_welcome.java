package com.biketomotor.kqj.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.biketomotor.kqj.R;
import com.biketomotor.kqj.object.Cur;

import org.json.JSONException;
import org.json.JSONObject;

public class activity_welcome extends AppCompatActivity implements View.OnClickListener {

    private Button button_login;
    private Button button_register;
    private Button button_home;
    private Button button_create;
    private Button button_check;
    private Button button_mycreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_welcome);

        button_login    = (Button)findViewById(R.id.layout_welcome_button_login);
        button_register = (Button)findViewById(R.id.layout_welcome_button_register);
        button_home     = (Button)findViewById(R.id.layout_welcome_button_home);
        button_create   = (Button)findViewById(R.id.layout_welcome_button_create);
        button_check    = (Button)findViewById(R.id.layout_welcome_button_check);
        button_mycreate = (Button)findViewById(R.id.layout_welcome_button_mycreate);

        button_login.setOnClickListener(this);
        button_register.setOnClickListener(this);
        button_home.setOnClickListener(this);
        button_create.setOnClickListener(this);
        button_check.setOnClickListener(this);
        button_mycreate.setOnClickListener(this);
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
            case R.id.layout_welcome_button_home:
                Intent to_home = new Intent(activity_welcome.this, activity_home.class);
                startActivity(to_home);
                break;
            case R.id.layout_welcome_button_create:
                Intent to_create = new Intent(activity_welcome.this, activity_create.class);
                startActivity(to_create);
                break;
            case R.id.layout_welcome_button_check:
                Intent to_check = new Intent(activity_welcome.this, activity_check.class);
                startActivity(to_check);
                break;
            case R.id.layout_welcome_button_mycreate:
                Intent to_activity2 = new Intent(activity_welcome.this, activity_activity2.class);
                startActivity(to_activity2);
                break;
        }
    }
}
