package com.example.touravel.app;

/**
 * Created by gokhancs on 17/03/15.
 */
import android.app.ListActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


public class SettingsActivity extends ListActivity {

    private CustomAdapter mAdapter,mAdapterListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new CustomAdapter(this);
        /*for (int i = 1; i < 30; i++) {
            mAdapter.addItem("Row Item #" + i);
            if (i % 4 == 0) {
                mAdapter.addSectionHeaderItem("Section #" + i);
            }
        }*/
        mAdapter.addSectionHeaderItem("Account Settings");
        mAdapter.addItem("Change Name");
        mAdapter.addItem("Change Surname");
        mAdapter.addItem("Change Birthdate");
        mAdapter.addItem("Current Password");
        mAdapter.addItem("New Password");
        mAdapter.addItem("Verify New Password");
        mAdapter.addItem("Current Mail");
        mAdapter.addItem("New Mail");
        mAdapter.addItem("Verify New Mail");

        mAdapter.addSectionHeaderItem("Application Settings");
        mAdapter.addItem("Change Name");
        mAdapter.addItem("Change Surname");
        mAdapter.addItem("Change Birthdate");
        mAdapter.addItem("Current Password");
        mAdapter.addItem("New Password");
        mAdapter.addItem("Verify New Password");
        mAdapter.addItem("Current Mail");
        mAdapter.addItem("New Mail");
        mAdapter.addItem("Verify New Mail");
        mAdapter.addItem("Change Name");
        mAdapter.addItem("Change Surname");
        mAdapter.addItem("Change Birthdate");
        mAdapter.addItem("Current Password");
        mAdapter.addItem("New Password");
        mAdapter.addItem("Verify New Password");
        mAdapter.addItem("Current Mail");
        mAdapter.addItem("New Mail");
        mAdapter.addItem("Verify New Mail");
        setListAdapter(mAdapter);
        ListView lv = (ListView) findViewById(android.R.id.list);
        View footerView = getLayoutInflater().inflate(R.layout.footer_layout, lv, false);
        lv.addFooterView(footerView);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v,
                                    int position, long id) {
                switch(position) {
                    case 1:
                    mAdapter.getPosition(position);
                    mAdapter.notifyDataSetInvalidated();

                        break;

                    case 2:
                        mAdapter.getPosition(position);
                        mAdapter.notifyDataSetInvalidated();

                        break;

                    case 3:
                        mAdapter.getPosition(position);
                        mAdapter.notifyDataSetInvalidated();

                        break;
                    case 4:
                        mAdapter.getPosition(position);
                        mAdapter.notifyDataSetInvalidated();

                        break;
                    case 5:
                        mAdapter.getPosition(position);
                        mAdapter.notifyDataSetInvalidated();

                        break;
                    case 6:
                        mAdapter.getPosition(position);
                        mAdapter.notifyDataSetInvalidated();

                        break;
                    case 7:
                        mAdapter.getPosition(position);
                        mAdapter.notifyDataSetInvalidated();

                        break;
                    case 8:
                        mAdapter.getPosition(position);
                        mAdapter.notifyDataSetInvalidated();

                        break;
                    case 9:
                        mAdapter.getPosition(position);
                        mAdapter.notifyDataSetInvalidated();

                        break;

                    default:
                        break;
                }
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
