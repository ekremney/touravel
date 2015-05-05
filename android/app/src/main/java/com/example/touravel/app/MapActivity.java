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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


import com.android.async.AsyncGetRoute;
import com.android.async.AsyncPostRoute;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;



public class MapActivity extends ActionBarActivity implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMapLongClickListener
{

    public GoogleMap theMap;
    boolean toZoom;
    public Location theLocation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        registerReceiver(broadcastReceiver, new IntentFilter(BackgroundService.BROADCAST_ACTION));

        toZoom = true;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        theMap = map;
        theMap.setMyLocationEnabled(true);
        //theMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        theMap.setOnMapClickListener(this);
        theMap.setOnMapLongClickListener(this);
        moveCam(30.0, 37.0, 2.0);

        if(BackgroundService.curRoute.getLocationNo() > 0){
            BackgroundService.curRoute.draw(theMap);
            theLocation = BackgroundService.curRoute.getLocation(BackgroundService.curRoute.getLocationNo() - 1);
            animCam(theLocation.getLatitude(), theLocation.getLongitude(), 16.8);
            toZoom = false;
        }
    }

    @Override
    public void onMapClick(LatLng point) {
        print("Route posted.");
        String url = getResources().getString(R.string.url_login);
        Route r = new Route(BackgroundService.curRoute);
        new AsyncPostRoute().execute(url, SplashScreen.auth, BackgroundService.curRoute.toString());
        /*
        BackgroundService.curRoute.delete();
        AsyncGetRoute agr = new AsyncGetRoute();
        agr.execute(url, SplashScreen.auth, BackgroundService.day + "-" + BackgroundService.month
                                                + "-" + BackgroundService.year);
        BackgroundService.curRoute = Route.fromString(agr.getResult());
        if(r.check(BackgroundService.curRoute))
            print("Eşleşme tamam.");
        else
            print("Eşleşme olmadı.");
            */
    }


    @Override
    public void onMapLongClick(LatLng point) {
        BackgroundService.curRoute.delete();
        print("Route deleted.");
    }


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            BackgroundService.curRoute.draw(theMap);
            if(toZoom) {
                theLocation = Route.stringToLoc(intent.getStringExtra("location"));
                animCam(theLocation.getLatitude(), theLocation.getLongitude(), 16.8);
                toZoom = false;
            }
        }
    };

    @Override
    public void onDestroy(){
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    public void putDot(double  latitude, double longitude){
        theMap.addCircle(new CircleOptions()
                .center(new LatLng(latitude, longitude))
                .radius(10)
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


}
