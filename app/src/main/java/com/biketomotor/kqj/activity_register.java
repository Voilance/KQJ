package com.biketomotor.kqj;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class activity_register extends AppCompatActivity implements View.OnClickListener {

    private EditText edittext_account;
    private EditText edittext_password1;
    private EditText edittext_password2;
    private EditText edittext_nickname;
    private EditText edittext_email;
    private Button   button_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);

        edittext_account   = (EditText)findViewById(R.id.layout_register_edittext_account);
        edittext_password1 = (EditText)findViewById(R.id.layout_register_edittext_password1);
        edittext_password2 = (EditText)findViewById(R.id.layout_register_edittext_password2);
        edittext_nickname  = (EditText)findViewById(R.id.layout_register_edittext_nickname);
        edittext_email     = (EditText)findViewById(R.id.layout_register_edittext_email);
        button_register    = (Button)findViewById(R.id.layout_register_button_register);

        button_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_register_button_register:
                break;
        }
    }
}
