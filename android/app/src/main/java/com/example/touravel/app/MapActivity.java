package com.example.touravel.app;

/**
 * Created by gokhancs on 17/03/15.
 */
import android.content.Context;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
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


public class MapActivity extends ActionBarActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener
{

    public GoogleMap theMap;
    private GoogleApiClient mLocationClient;
    private Location mCurrentLocation;
    LocationRequest mLocationRequest;

    boolean firstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mLocationClient = new GoogleApiClient.Builder(getApplicationContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(20000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setFastestInterval(10000);

        firstTime = true;
    }


    @Override
    protected void onStart() {
        super.onStart();
        mLocationClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocationServices.FusedLocationApi.removeLocationUpdates(mLocationClient,this);
    }

    protected void onResume() {
        super.onResume();
        if(mLocationClient.isConnected()){
            LocationServices.FusedLocationApi.requestLocationUpdates(mLocationClient,mLocationRequest,this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocationServices.FusedLocationApi.removeLocationUpdates(mLocationClient, this);
        mLocationClient.disconnect();
    }

    private Location getLocation() {
    Location lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(mLocationClient);
    return lastKnownLocation;
}

    @Override
    public void onLocationChanged(Location location) {
        if(firstTime) {
            animCam(location.getLatitude(), location.getLongitude(), 15.4);
            firstTime = false;
        }
        Toast.makeText(this, "Location changed", Toast.LENGTH_SHORT).show();
        putDot(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onConnected(Bundle bundle) {
        // Display the connection status
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
        LocationServices.FusedLocationApi.requestLocationUpdates(mLocationClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Disconnected. Please re-connect.",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult arg0) {
        Toast.makeText(this, "Failed. Please re-connect.",
                Toast.LENGTH_SHORT).show();
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
        if(id == R.id.action_settings) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        theMap = map;
        theMap.setMyLocationEnabled(true);
        moveCam(30.0, 25.0, 0.0);
    }


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

}
