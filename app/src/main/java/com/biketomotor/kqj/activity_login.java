package com.biketomotor.kqj;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class activity_login extends AppCompatActivity implements View.OnClickListener {

    private EditText edittext_accout;
    private EditText edittext_password;
    private Button   button_login;
    private Button   button_forget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);

        edittext_accout   = (EditText)findViewById(R.id.layout_login_edittext_account);
        edittext_password = (EditText)findViewById(R.id.layout_login_edittext_password);
        button_login      = (Button)findViewById(R.id.layout_login_button_login);
        button_forget     = (Button)findViewById(R.id.layout_login_button_forget);

        button_login.setOnClickListener(this);
        button_forget.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_login_button_login:
                break;
            case R.id.layout_login_button_forget:
                break;
        }
    }
}
