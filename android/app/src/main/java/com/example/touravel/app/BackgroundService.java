package com.example.touravel.app;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

public class BackgroundService extends Service implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{

    private GoogleApiClient theLocationClient;
    LocationRequest theLocationRequest;
    public static Location lastLocation = null;
    public static String CURRENT_FILE_NAME = null;
    public static String DIRECTORY_NAME = null;
    public static double MIN_DISTANCE_BTW_LOCS = 20;
    public static double MAX_DISTANCE_BTW_LOCS = 200;
    public static double MAX_ACCURACY = 20;
    public static double STOP_TIME = 30*60*100;
    public static double STOP_DISTANCE = 50;
    public static long CHECK_INTERVAL = 5*1000;
    public static long FAST_CHECK_INTERVAL = 3*1000;
    public static Route curRoute;
    public static String username = "username";
    public static Calendar today;
    public static int day;
    public static int month;
    public static int year;
    File f;

    public static final String BROADCAST_ACTION = "com.example.touravel.app.broadcast";
    Intent intent;

    @Override
    public void onCreate() {
        //print("Service is created.");
        theLocationClient = new GoogleApiClient.Builder(getApplicationContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        theLocationRequest = new LocationRequest();
        theLocationRequest.setInterval(CHECK_INTERVAL);
        theLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        theLocationRequest.setFastestInterval(FAST_CHECK_INTERVAL);

        today = Calendar.getInstance();
        day = today.get(Calendar.DAY_OF_MONTH);
        month = today.get(Calendar.MONTH) + 1;
        year = today.get(Calendar.YEAR);

        intent = new Intent(BROADCAST_ACTION);
        DIRECTORY_NAME = getFilesDir() + "";
        CURRENT_FILE_NAME = DIRECTORY_NAME + "/" + username + day + "-" + month + "-" + year;

        f = new File(CURRENT_FILE_NAME);
        if(f.exists() && Route.readFile() != null && f.length() > 35) {
            curRoute = Route.fromString(Route.readFile());
            lastLocation = curRoute.getLocation(curRoute.getLocationNo() - 1);
        }
        else
            curRoute = new Route(username, day, month, year);
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location.getAccuracy() < MAX_ACCURACY && (lastLocation == null ||
                (lastLocation != null && lastLocation.distanceTo(location) > MIN_DISTANCE_BTW_LOCS
                && lastLocation != null && lastLocation.distanceTo(location) < MAX_DISTANCE_BTW_LOCS))) {

            boolean stopStart = false, stopStop = true;
            int i, j;
            for(i = curRoute.getLocationNo() - 1; i > curRoute.lastCheckin; i--)
                if(location.getTime() - curRoute.getLocation(i).getTime() > STOP_TIME){
                    stopStart = true;
                    break;
                }
            if(stopStart)
                for(j = i; j < curRoute.getLocationNo(); j++)
                    if(curRoute.getLocation(j).distanceTo(location) > STOP_DISTANCE){
                        stopStart = false;
                        break;
                    }
            if(stopStart && stopStop && curRoute.lastCheckin < i + 1) {
                curRoute.addStop(curRoute.getLocation(i + 1));
                curRoute.lastCheckin = curRoute.getLocationNo();
            }


            lastLocation = location;
            if(curRoute == null)
                curRoute = new Route(username, day, month, year);
            curRoute.addLocation(lastLocation);
            saveLocation();
            broadcastLoc();
        }
    }

    public void saveLocation(){
        try
        {
            String filename = CURRENT_FILE_NAME;
            FileWriter fw = new FileWriter(filename);
            fw.write(curRoute.toString());
            fw.close();
        }
        catch(IOException ioe)
        {
            System.err.println("Location couldn't be saved.");
        }
    }

    private void broadcastLoc() {
        intent.putExtra("location", Route.locToString(lastLocation));
        sendBroadcast(intent);
    }

    public void print(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

//----------------------------------------------------------------------------------------------------------

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //print("Service is started.");
        theLocationClient.connect();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        //print("Service is stopped.");
        theLocationClient.disconnect();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnected(Bundle bundle) {
        LocationServices.FusedLocationApi.requestLocationUpdates(theLocationClient, theLocationRequest, this);
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
