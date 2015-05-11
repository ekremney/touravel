package com.example.touravel.app;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.async.AsyncCreateUser;

/**
 * Created by gokhancs on 02/05/15.
 */

public class RegisterActivity extends Activity
{

    protected EditText tvUsername, tvEmail, tvPassword, tvPasswordV, tvBirthDate;

    public static Context cnt;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        cnt = getApplicationContext();

        tvUsername = (EditText) findViewById(R.id.tvUsername);
        tvEmail = (EditText) findViewById(R.id.tvEmail);
        tvPassword = (EditText) findViewById(R.id.tvPassword);
        tvPasswordV = (EditText) findViewById(R.id.tvPasswordV);
        tvBirthDate = (EditText) findViewById(R.id.tvBirthDate);
/*
        tvUsername.setText("gggh");
        tvEmail.setText("sad@sad.cs");
        tvPassword.setText("dddddd");
        tvPasswordV.setText("dddddd");
        tvBirthDate.setText("1990-10-10");*/
    }


    public void btnOnClick(View v) {

        switch(v.getId())
        {
            // Create User
            case R.id.btnCreateUser:
            {
                String url = getResources().getString(R.string.url_create_user);
                String username = tvUsername.getText().toString();
                String email = tvEmail.getText().toString();
                String password = tvPassword.getText().toString();
                String passwordV = tvPasswordV.getText().toString();
                String birthDate = tvBirthDate.getText().toString();

                new AsyncCreateUser().execute(url, username, email, password, passwordV, birthDate);
                break;
            }
            default:
                break;

        }
    }
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }
}