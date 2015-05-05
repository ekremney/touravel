package com.example.touravel.app;

/**
 * Created by gokhancs on 17/03/15.
 */
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
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
