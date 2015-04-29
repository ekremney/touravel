package com.example.touravel.app;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.plus.Plus;

import java.util.Date;


public class MapActivity extends ActionBarActivity implements OnMapReadyCallback
{

    public GoogleMap theMap;

    private int timeInt = 5000;
    private Handler theHandler;
    private static final String TAG = "BroadcastTest";
    private Intent intent;
    boolean firstTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        theHandler = new Handler();
        //intent = new Intent(this, BackgroundService.class);
        registerReceiver(broadcastReceiver, new IntentFilter(BackgroundService.BROADCAST_ACTION));

        firstTime = true;

    }

    @Override
    public void onMapReady(GoogleMap map) {
        theMap = map;
        theMap.setMyLocationEnabled(true);
        moveCam(30.0, 25.0, 0.0);

    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            takeLocation(intent);
        }
    };

    private void takeLocation(Intent intent) {
        double latitude = intent.getDoubleExtra("lat", 0.0);
        double longitude = intent.getDoubleExtra("long", 0.0);

        print("New Location: " + latitude + "" + longitude);
        putDot(latitude, longitude);
        animCam(latitude, longitude, 15.8);

    }

    public void onResume() {
        super.onResume();
        //registerReceiver(broadcastReceiver, new IntentFilter(BackgroundService.BROADCAST_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();
        //unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    Runnable timeWatcher = new Runnable() {
        @Override
        public void run() {
            theHandler.postDelayed(timeWatcher, timeInt);
        }
    };

    public void putDot(double  latitude, double longitude){
        theMap.addCircle(new CircleOptions()
                .center(new LatLng(latitude, longitude))
                .radius(15)
                .strokeColor(Color.RED)
                .fillColor(Color.RED));
    }

    public void putMarker(double  latitude, double longitude, String text){
        theMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(text));
    }

    public void moveCam(double  latitude, double longitude, double zoom){
        theMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), (float) zoom));
    }

    public void animCam(double  latitude, double longitude, double zoom){
        theMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude)).zoom((float) zoom).bearing(0).tilt(0).build()));
    }

    public void print(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

/*
    private Location getLocation() {
    Location lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(mLocationClient);
    return lastKnownLocation;
}
*/



// ------------------------------------------------------------------------------------------------------------------------





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == R.id.action_settings) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
