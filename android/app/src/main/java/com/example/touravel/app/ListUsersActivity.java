package com.example.touravel.app;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.async.AsyncChangeEmail;
import com.android.async.AsyncSearchByUsername;
import com.android.interfaces.OnTaskCompleted;

import java.util.ArrayList;
import java.util.List;


public class ListUsersActivity extends ActionBarActivity implements OnTaskCompleted {

    List<User> users = new ArrayList<User>();
    public static ListView userlist;
    protected static EditText tvUsernameSearch;
    protected Context cnt;

    //private Button profileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_users);

        cnt = getApplicationContext();
        tvUsernameSearch = (EditText) findViewById(R.id.usernameSearch);
        userlist = (ListView) findViewById(R.id.userlist);

    }

    public void btnOnClick(View v) {
        switch(v.getId()) {
            case R.id.usernameSearchButton: {
                String url = getResources().getString(R.string.url_serach_by_username);
                String username = tvUsernameSearch.getText().toString();
                Log.i("jamal", username);

                final ListUsersActivity that = this;

                new AsyncSearchByUsername(new OnTaskCompleted() {
                    @Override
                    public void onTaskCompleted(List<User> users) {
                        UserListAdapter adapter = new UserListAdapter(that, users);
                        userlist.setAdapter(adapter);
                    }

                    @Override
                    public void onTaskCompleted() {

                    }
                }).execute(url, SplashScreen.auth, username);
                break;
            }
            /*case R.id.user_profile:
                profileButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ListUsersActivity.this, SettingsActivity.class);
                        startActivity(intent);
                    }
                });
                break;*/
            default:
                break;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_users, menu);
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

    @Override
    public void onTaskCompleted(List<User> users) {

    }

    @Override
    public void onTaskCompleted() {

    }
}
