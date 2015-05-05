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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.async.AsyncLogin;



public class ProfileActivity extends ActionBarActivity {

    private TextView crew,following,followers;
    private ImageView settings, profilePic;
    private ImageButton btn_settings;
    private Button btn_routes, btn_following,btn_followers;
    private TextView text_routes, text_following,text_followers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        settings = (ImageButton) findViewById(R.id.btn_settings);
        btn_routes = (Button) findViewById(R.id.btn_routes);
        btn_following = (Button) findViewById(R.id.btn_following);
        btn_followers = (Button) findViewById(R.id.btn_followers);
        text_routes = (TextView) findViewById(R.id.text_routes);
        text_following = (TextView) findViewById(R.id.text_following);
        text_followers = (TextView) findViewById(R.id.text_followers);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        btn_routes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Intent intent = new Intent(ProfileActivity.this, SettingsActivity.class);
               // startActivity(intent);
            }
        });

        text_routes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Intent intent = new Intent(ProfileActivity.this, SettingsActivity.class);
               // startActivity(intent);
            }
        });

        btn_following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent intent = new Intent(ProfileActivity.this, SettingsActivity.class);
                // startActivity(intent);
            }
        });

        text_following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent intent = new Intent(ProfileActivity.this, SettingsActivity.class);
                // startActivity(intent);
            }
        });

        btn_followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent intent = new Intent(ProfileActivity.this, SettingsActivity.class);
                // startActivity(intent);
            }
        });

        text_followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent intent = new Intent(ProfileActivity.this, SettingsActivity.class);
                // startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed()
    {

        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs
    }

}
