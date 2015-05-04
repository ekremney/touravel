package com.example.touravel.app;

/**
 * Created by gokhancs on 17/03/15.
 */

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.android.async.AsyncChangeEmail;
import com.android.async.AsyncChangePassword;


public class SettingsActivity extends Activity {


    protected static EditText tvChangeEmail, tvChangeEmailAgain;
    protected static EditText tvOldPassword, tvNewPassword, tvNewPasswordAgain;
    protected Context cnt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.footer_layout);
        cnt = getApplicationContext();

        tvChangeEmail = (EditText) findViewById(R.id.tvChangeEmail);
        tvChangeEmailAgain = (EditText) findViewById(R.id.tvChangeEmailAgain);
        tvOldPassword = (EditText) findViewById(R.id.textOldPassword);
        tvNewPassword = (EditText) findViewById(R.id.textNewPassword);
        tvNewPasswordAgain = (EditText) findViewById(R.id.textNewPasswordV);
    }

    public static void clearEmailForm()
    {
        tvChangeEmail.setText("");
        tvChangeEmailAgain.setText("");
    }

    public static void clearPasswordForm()
    {
        tvOldPassword.setText("");
        tvNewPassword.setText("");
        tvNewPasswordAgain.setText("");
    }

    public void btnOnClick(View v) {

        switch(v.getId())
        {
            case R.id.btnChangeEmail:
            {
                String url = getResources().getString(R.string.url_change_email);
                String email = tvChangeEmail.getText().toString();
                String emailV = tvChangeEmailAgain.getText().toString();

                new AsyncChangeEmail().execute(url, SplashScreen.auth, email, emailV);
                break;
            }
            case R.id.btnChangePassword:
            {
                String url = getResources().getString(R.string.url_change_password);
                String oldPassword = tvOldPassword.getText().toString();
                String newPassword = tvNewPassword.getText().toString();
                String newPasswordAgain = tvNewPasswordAgain.getText().toString();

                new AsyncChangePassword().execute(url, SplashScreen.auth, oldPassword, newPassword, newPasswordAgain);
                break;
            }
            default:
                break;


        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
