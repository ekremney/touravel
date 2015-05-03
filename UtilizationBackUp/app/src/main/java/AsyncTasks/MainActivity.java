package AsyncTasks;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.bozok.util.R;

import AsyncTasks.AsyncChangeEmail;
import AsyncTasks.AsyncChangePassword;
import AsyncTasks.AsyncCreateUser;
import AsyncTasks.AsyncLogin;



public class MainActivity extends ActionBarActivity {


    public static Context cnt;
    private static String authKey;


    protected EditText tvUsername, tvEmail, tvPassword, tvPasswordV, tvBirthDate;
    protected EditText tvLoginEmail, tvLoginPassword;
    protected EditText tvChangeEmail, tvChangeEmailAgain;
    protected EditText tvOldPassword, tvNewPassword, tvNewPasswordAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cnt = getApplicationContext();
        tvUsername = (EditText) findViewById(R.id.tvUsername);
        tvEmail = (EditText) findViewById(R.id.tvEmail);
        tvPassword = (EditText) findViewById(R.id.tvPassword);
        tvPasswordV = (EditText) findViewById(R.id.tvPasswordV);
        tvBirthDate = (EditText) findViewById(R.id.tvBirthDate);
        tvLoginEmail = (EditText) findViewById(R.id.tvLoginEmail);
        tvLoginPassword = (EditText) findViewById(R.id.tvLoginPassword);
        tvChangeEmail = (EditText) findViewById(R.id.tvChangeEmail);
        tvChangeEmailAgain = (EditText) findViewById(R.id.tvChangeEmailAgain);
        tvOldPassword = (EditText) findViewById(R.id.textOldPassword);
        tvNewPassword = (EditText) findViewById(R.id.textNewPassword);
        tvNewPasswordAgain = (EditText) findViewById(R.id.textNewPasswordV);
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
            case R.id.btnLogin:
            {
                String url = getResources().getString(R.string.url_login);
                String email = tvLoginEmail.getText().toString();
                String password = tvLoginPassword.getText().toString();

                new AsyncLogin().execute(url, email, password);
                break;
            }
            case R.id.btnChangeEmail:
            {
                String url = getResources().getString(R.string.url_change_email);
                String email = tvChangeEmail.getText().toString();
                String emailV = tvChangeEmailAgain.getText().toString();

                new AsyncChangeEmail().execute(url, authKey, email, emailV);
                break;
            }
            case R.id.btnChangePassword:
            {
                String url = getResources().getString(R.string.url_change_password);
                String oldPassword = tvOldPassword.getText().toString();
                String newPassword = tvNewPassword.getText().toString();
                String newPasswordAgain = tvNewPasswordAgain.getText().toString();

                new AsyncChangePassword().execute(url, authKey, oldPassword, newPassword, newPasswordAgain);
                break;
            }
            default:
                break;

        }


    }

    public static void setKey(String str)
    {
        int i = str.indexOf("auth-key");
        str = str.substring(i+12);
        str = str.substring(0,str.length()-3);
        authKey = str;
        System.out.println(str);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
