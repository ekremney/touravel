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
        crew = (TextView) findViewById(R.id.crew_count);
        following = (TextView) findViewById(R.id.following_count);
        followers = (TextView) findViewById(R.id.followers_count);
        settings = (ImageView) findViewById(R.id.settings);
        profilePic = (ImageView) findViewById(R.id.avatar);

        crew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // crew'e basınca yapılacaklar.

                Intent intent = new Intent(ProfileActivity.this, TimelineActivity.class);
                startActivity(intent);
                //setContentView(R.layout.crew_layout);
            }
        });

        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // following'e basınca yapılacaklar.
                Intent intent = new Intent(ProfileActivity.this, TimelineActivity.class);
                startActivity(intent);
                //setContentView(R.layout.following_layout);
            }
        });

        followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // followers'a basınca yapılacaklar.
                Intent intent = new Intent(ProfileActivity.this, TimelineActivity.class);
                startActivity(intent);
                //setContentView(R.layout.followed_layout);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // settings'e basınca yapılacaklar.

                Intent intent = new Intent(ProfileActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });


    }
}
