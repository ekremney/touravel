package com.example.touravel.app;

        import android.app.TabActivity;
        import android.content.Intent;
        import android.graphics.Color;
        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.Menu;
        import android.view.MenuInflater;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.ImageView;
        import android.widget.TabHost;
        import android.widget.TabWidget;
        import android.widget.TextView;
        import android.widget.Toast;


@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {
    TabHost tabHost;
    /** Called when the activity is first created. */


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this, BackgroundService.class));
        tabHost = getTabHost();
        setTabs();
    }
    private void setTabs()
    {
        addTab("Profile", R2.drawable.tab_home, ProfileActivity.class);
        addTab("Timeline", R2.drawable.tab_search, TimelineActivity.class);
        addTab("Map", R2.drawable.tab_search, MapActivity.class);
        addTab("Storyline", R2.drawable.tab_home, StorylineActivity.class);
        addTab("Settings", R2.drawable.tab_search, SettingsActivity.class);
    }
    private void addTab(String labelId, int drawableId, Class<?> c)
    {
        Intent intent = new Intent(this, c);
        TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);

        View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, getTabWidget(), false);
        TextView title = (TextView) tabIndicator.findViewById(R.id.title);
        title.setText(labelId);
        ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
        icon.setImageResource(drawableId);
        spec.setIndicator(tabIndicator);
        spec.setContent(intent);
        tabHost.addTab(spec);
    }
    public void openMapActivity(View b)
    {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_search:
                Intent intent = new Intent(this, ListUsersActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}