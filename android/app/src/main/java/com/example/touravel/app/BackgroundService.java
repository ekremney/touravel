package com.example.touravel.app;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;

import java.util.ArrayList;
import java.util.Date;

public class BackgroundService extends Service implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{

    private GoogleApiClient mLocationClient;
    LocationRequest mLocationRequest;
    private Location currentLocation;


    private static final String TAG = "BroadcastService";
    public static final String BROADCAST_ACTION = "com.example.touravel.app.broadcast";
    private final Handler handler = new Handler();
    Intent intent;

    private final IBinder theBinder = new LocalBinder();

    @Override
    public void onCreate() {
        //print("Service is born");
        mLocationClient = new GoogleApiClient.Builder(getApplicationContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(20000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setFastestInterval(10000);

        intent = new Intent(BROADCAST_ACTION);
    }

    @Override
    public void onLocationChanged(Location location) {
        //print("Location changed. New one: " + location.getLatitude() + "" + location.getLongitude());
        currentLocation = location;
        sendLocInfo();
    }

    private void sendLocInfo() {
        intent.putExtra("lat", currentLocation.getLatitude());
        intent.putExtra("long", currentLocation.getLongitude());
        sendBroadcast(intent);
    }

    public void print(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

//----------------------------------------------------------------------------------------------------------


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //print("Service is alive.");
        mLocationClient.connect();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        //print("Service is dead.");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public class LocalBinder extends Binder {
        BackgroundService getService() {
            return BackgroundService.this;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        LocationServices.FusedLocationApi.requestLocationUpdates(mLocationClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        print("Disconnected. Please re-connect.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult arg0) {
        print("Failed. Please re-connect.");
    }



}
