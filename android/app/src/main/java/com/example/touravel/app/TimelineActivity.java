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
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.async.AsyncGetTimeline;
import com.android.async.AsyncLike;
import com.android.async.AsyncUnlike;
import com.android.interfaces.OnTaskCompleted;
import com.custom.CustomTimelineList;

import java.util.ArrayList;
import java.util.List;

public class TimelineActivity extends ActionBarActivity implements OnTaskCompleted {

    public static String[] ids;
    public static String[] users;
    public static String[] avatars;
    public static String[] types;
    public static String[] data;
    public static String[] likes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        receiveTimeline();

        /*
            like ve comment'ide üstteki gibi tanımlayıp listener eklerseniz tıklanma işlemlerini için fonksiyonlar
            tanımlayabilirsiniz. Yeni veri geldikçe arrayi güncelleyin dummy arrayinizi mesala sonra. notifyDataSetChabged diceksiniz listview da.
         */

        findViewById(R.id.timeline_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                receiveTimeline();
            }
        });
    }

    public void show(int pos){
        Intent intt = new Intent(this, MapActivity.class);
        intt.putExtra("route", BackgroundService.curRoute.toString());//TimelineActivity.data[pos]);
        startActivity(intt);
    }

    public void receiveTimeline(){
        final TimelineActivity that = this;
        new AsyncGetTimeline(new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(List<User> users) {
            }

            @Override
            public void onTaskCompleted() {
                CustomTimelineList adapterTimeline = new CustomTimelineList(that.getParent(), TimelineActivity.users);
                ListView timeLineList = (ListView) findViewById(R.id.timelineListView);
                timeLineList.setAdapter(adapterTimeline);

            }
        }).execute(getResources().getString(R.string.url_get_timeline), SplashScreen.auth);

    }

    public void like(int pos){
        new AsyncLike().execute(getResources().getString(R.string.url_get_timeline) + "/like",
                SplashScreen.auth, ids[pos]);
        likes[pos] = (Integer.parseInt(likes[pos]) + 1) + "";
    }

    public void unlike(int pos){
        new AsyncUnlike().execute(getResources().getString(R.string.url_get_timeline) + "/unlike",
                SplashScreen.auth, ids[pos]);
        likes[pos] = (Integer.parseInt(likes[pos]) - 1) + "";
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

    @Override
    public void onTaskCompleted(List<User> users) {

    }

    @Override
    public void onTaskCompleted() {
    }
}
