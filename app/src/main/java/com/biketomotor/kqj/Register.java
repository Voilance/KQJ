package com.biketomotor.kqj;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Register extends AppCompatActivity implements View.OnClickListener{

    private EditText  account_edit;
    private EditText  password_edit;
    private EditText  password_again_edit;
    private EditText  email_edit;
    private String    account_string;
    private String    password_string;
    private String    password_again_string;
    private String    email_string;
    private Button    register_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        account_edit        = (EditText)findViewById(R.id.register_account_edit);
        password_edit       = (EditText)findViewById(R.id.register_password_edit);
        password_again_edit = (EditText)findViewById(R.id.register_password_again_edit);
        email_edit          = (EditText)findViewById(R.id.register_email_edit);
        register_button     = (Button)findViewById(R.id.register_register_button);
        register_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_register_button:
                account_string = account_edit.getText().toString();
                password_string = password_edit.getText().toString();
                password_again_string = password_again_edit.getText().toString();
                email_string = email_edit.getText().toString();
                if (password_string.compareTo(password_again_string) == 0) {
                    JSONObject json = getJson(account_string, password_string, email_string);
                }
                break;
            default:
                break;
        }
    }

    private JSONObject getJson(final String account, final String password, final String email) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("object", "user");
            JSONObject data_value = new JSONObject();
            data_value.put("account", account);
            data_value.put("password", password);
            data_value.put("email", email);
            jsonObject.put("data", data_value);
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
        return jsonObject;
    }

    private void catchResponse(final String response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject = new JSONObject();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    jsonObject = jsonArray.getJSONObject(0);
                    Toast.makeText(Register.this, jsonObject.toString(), Toast.LENGTH_SHORT).show();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
    }
}












