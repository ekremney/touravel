package com.example.touravel.app;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;

public class BackgroundService extends Service {

    public class LocalBinder extends Binder {
        BackgroundService getService() {

            return BackgroundService.this;
        }
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "Background Service is Created", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Background Service is Started", Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Background Service is Stopped", Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return theBinder;
    }

    private final IBinder theBinder = new LocalBinder();


    @Override
    public void onStart(Intent intent, int startid) {
        Toast.makeText(this, "Background Service is Started", Toast.LENGTH_SHORT).show();
        // Acquire a reference to the system Location Manager

    }
}
