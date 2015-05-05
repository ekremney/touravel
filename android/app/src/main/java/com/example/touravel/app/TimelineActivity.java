package com.example.touravel.app;

/**
 * Created by gokhancs on 17/03/15.
 */
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.custom.CustomTimelineList;

public class TimelineActivity extends ActionBarActivity {

    public static String[] dummyList = {
            "Ekrem Doğan",
            "Utku Bozoklu",
            "Gökhan G.",
            "Gökhan Ç.",
            "Behsat Ç."

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        CustomTimelineList adapterTimeline = new CustomTimelineList(getParent(), dummyList);

        ListView timeLineList = (ListView) findViewById(R.id.timelineListView);
        timeLineList.setAdapter(adapterTimeline);

        /*
            like ve comment'ide üstteki gibi tanımlayıp listener eklerseniz tıklanma işlemlerini için fonksiyonlar
            tanımlayabilirsiniz. Yeni veri geldikçe arrayi güncelleyin dummy arrayinizi mesala sonra. notifyDataSetChabged diceksiniz listview da.
         */


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
