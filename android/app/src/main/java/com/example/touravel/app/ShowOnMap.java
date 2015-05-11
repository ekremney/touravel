package com.example.touravel.app;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;


public class ShowOnMap extends ActionBarActivity  implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_on_map);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.showMap);
        mapFragment.getMapAsync(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_on_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng loc;
        //googleMap.setMyLocationEnabled(true);

        if(BackgroundService.tempType) {
            BackgroundService.tempRoute.draw(googleMap);
            loc = new LatLng(BackgroundService.tempRoute.getLocation(0).getLatitude(),
                    BackgroundService.tempRoute.getLocation(0).getLongitude());
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                    .target(loc).zoom((float) 15.0).bearing(0).tilt(0).build()));
        }
        else{
            loc = new LatLng(BackgroundService.tempLoc.latitude, BackgroundService.tempLoc.longitude);
            MapActivity.putDot(googleMap, loc.latitude, loc.longitude);
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                    .target(loc).zoom((float) 16.0).bearing(0).tilt(0).build()));
        }

    }
}
