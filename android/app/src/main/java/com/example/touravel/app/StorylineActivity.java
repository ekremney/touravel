package com.example.touravel.app;

/**
 * Created by gokhancs on 17/03/15.
 */
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.android.async.AsyncGetRoute;
import com.android.async.AsyncGetTimeline;
import com.android.async.AsyncPostTimeline;
import com.android.interfaces.OnTaskCompleted;
import com.custom.CustomStorylineList;
import com.custom.CustomTimelineList;

import java.util.List;

public class StorylineActivity extends ActionBarActivity implements OnTaskCompleted {

    public static String[] usernames;
    public static String[] data;
    public static String[] types;
    public static String avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storyline);

        receiveStories();

        /*
            like ve comment'ide üstteki gibi tanımlayıp listener eklerseniz tıklanma işlemlerini için fonksiyonlar
            tanımlayabilirsiniz. Yeni veri geldikçe arrayi güncelleyin dummy arrayinizi mesala sonra. notifyDataSetChabged diceksiniz listview da.
         */

        findViewById(R.id.storyline_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                receiveStories();
            }
        });
    }

    public void receiveStories(){
        final StorylineActivity that = this;
        new AsyncGetRoute(new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(List<User> users) {
            }

            @Override
            public void onTaskCompleted() {
                CustomStorylineList adapterTimeline = new CustomStorylineList(that.getParent(), StorylineActivity.usernames);
                ListView storyLineList = (ListView) findViewById(R.id.storylineListView);
                storyLineList.setAdapter(adapterTimeline);

            }
        }).execute(getResources().getString(R.string.url_get_route), SplashScreen.auth, BackgroundService.dayText);

    }

    @Override
    public void onTaskCompleted(List<User> users) {

    }

    @Override
    public void onTaskCompleted() {

    }
}
