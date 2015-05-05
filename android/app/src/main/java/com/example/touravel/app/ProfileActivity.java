package com.example.touravel.app;

/**
 * Created by gokhancs on 17/03/15.
 */
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.async.AsyncLogin;



public class ProfileActivity extends ActionBarActivity {

    private TextView crew,following,followers;
    private ImageView settings, profilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }

}
