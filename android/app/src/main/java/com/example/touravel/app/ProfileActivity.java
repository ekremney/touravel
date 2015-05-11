package com.example.touravel.app;

/**
 * Created by gokhancs on 17/03/15.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.async.AsyncGetInfo;
import com.android.async.AsyncLogin;
import com.android.interfaces.ProfileInterface;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;


public class ProfileActivity extends ActionBarActivity implements ProfileInterface {

    private TextView crew, following, followers;
    private ImageView settings, imageView2;
    private ImageButton btn_settings;
    private Button btn_routes, btn_following, btn_followers;
    private TextView text_routes, text_following, text_followers, textView2, textView6, textView7;


    public void startListUserActivity(String state) {


        Intent intent = new Intent(ProfileActivity.this, FollowResultActivity.class);


        Bundle b = createBundle(state);

        if (b != null) {

            intent.putExtra("bundle", b);

        }

        startActivity(intent);


    }

    public Bundle createBundle(String state) {


        Bundle info = new Bundle();


        if (state.equals("following")) {
            info.putString("state", "following");


        } else {
            info.putString("state", "follower");
        }


        return info;

    }

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
        textView2 = (TextView) findViewById(R.id.textView2);
        textView6 = (TextView) findViewById(R.id.textView6);
        textView7 = (TextView) findViewById(R.id.textView7);
        imageView2 = (ImageView) findViewById(R.id.imageView2);

        btn_routes.setText("" + SplashScreen.user.getRoute_count());
        btn_followers.setText("" + SplashScreen.user.getFollowerCount());
        btn_following.setText("" + SplashScreen.user.getFollowingCount());
        textView2.setText("" + SplashScreen.user.getName());
        textView6.setText("" + SplashScreen.user.getLocation());
        textView7.setText("" + SplashScreen.user.getAbout_me());

        byte[] decodedString = Base64.decode(SplashScreen.user.getAvatar(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imageView2.setImageBitmap(decodedByte);
        imageView2.getLayoutParams().height = 500;
        imageView2.getLayoutParams().width = 500;

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
                Intent intent = new Intent(ProfileActivity.this, StorylineActivity.class);
                startActivity(intent);
            }
        });

        text_routes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, StorylineActivity.class);
                startActivity(intent);
            }
        });


        btn_following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                startListUserActivity("following");

            }
        });

        text_following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startListUserActivity("following");

            }
        });

        btn_followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startListUserActivity("follower");
            }
        });

        text_followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startListUserActivity("follower");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        btn_routes.setText("" + SplashScreen.user.getRoute_count());
        btn_followers.setText("" + SplashScreen.user.getFollowerCount());
        btn_following.setText("" + SplashScreen.user.getFollowingCount());
        textView2.setText("" + SplashScreen.user.getName());
        textView6.setText("" + SplashScreen.user.getLocation());
        textView7.setText("" + SplashScreen.user.getAbout_me());

        byte[] decodedString = Base64.decode(SplashScreen.user.getAvatar(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imageView2.setImageBitmap(decodedByte);
        imageView2.getLayoutParams().height = 500;
        imageView2.getLayoutParams().width = 500;
    }

    @Override
    public void onBackPressed() {

        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs
    }

    @Override
    public void onTaskCompleted(String response) {

    }
}
