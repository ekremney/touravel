package com.example.touravel.app;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.android.async.AsyncFollowTask;
import com.android.async.AsyncSearchByUsername;
import com.android.interfaces.OnTaskCompleted;

import java.util.List;


public class FollowResultActivity extends ActionBarActivity {


    private TextView followResult = null;
    private ListView followResultList = null;
    private UserListAdapter userListAdapter = null;

    private AsyncTask getUserTask = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.following_layout);


        prepareComponents();

        prepateListUsers();

    }

    private void prepareComponents() {

        followResult = (TextView) findViewById(R.id.txtFollowState);

        followResultList = (ListView) findViewById(R.id.listUsersResult);
    }


    public void getUsers(String stateInfo) {

        String url = "";


        followResult.setText(stateInfo);
        if (stateInfo.equals("following")) {

            url = getResources().getString(R.string.url_get_followed);
        } else if (stateInfo.equals("follower")) {

            url = getResources().getString(R.string.url_get_followers);

        } else {

            //Log error.
        }
        final FollowResultActivity that = this;

        new AsyncFollowTask(new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(List<User> users) {
                userListAdapter = new UserListAdapter(that, users);
                followResultList.setAdapter(userListAdapter);
            }

            @Override
            public void onTaskCompleted() {

            }
        }).execute(url, SplashScreen.auth);
    }

    public void prepateListUsers() {

        Bundle b = getIntent().getBundleExtra("bundle");
        String stateInfo = "";


        if (b != null) {
            stateInfo = b.getString("state");

            getUsers(stateInfo);

        }


    }


}
