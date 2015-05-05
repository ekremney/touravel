package com.example.touravel.app;

import android.graphics.Color;
import android.location.Location;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by gokg on 30.4.2015.
 */
public class Route {

    private ArrayList<Location> locations = null;
    private ArrayList<Location> stops = null;
    private ArrayList<Circle> dots = null;
    private ArrayList<CircleOptions> circles = null;
    private Polyline line = null;
    private PolylineOptions lineOptions = null;
    public static final int CIRCLE_RADIUS = 5;
    public static final int ROUTE_COLOR = Color.RED;
    public int lastCheckin;
    public String username;
    public int day;
    public int month;
    public int year;

    public Route (String user, int d, int m, int y){
        locations = new ArrayList<Location>();
        stops = new ArrayList<Location>();
        dots = new ArrayList<Circle>();
        circles = new ArrayList<CircleOptions>();
        lineOptions = new PolylineOptions();
        username = user;
        day = d;
        month = m;
        year = y;
        lastCheckin = -1;
    }

    public Route(Route r){
        username = r.getUsername();
        day = r.getDay();
        month = r.getMonth();
        year = r.getYear();
        locations = new ArrayList<Location>();
        circles = new ArrayList<CircleOptions>();
        dots = new ArrayList<Circle>();
        lastCheckin = r.lastCheckin;

        for(int i = 0; i < r.getLocationNo(); i++)
            locations.add(r.getLocation(i));

        for(int i = 0; i < r.getCircles().size(); i++)
            circles.add((r.getCircles()).get(i));

        for(int i = 0; i < r.getDots().size(); i++)
            dots.add(r.getDots().get(i));

        lineOptions = r.getLineOpt();
        line = r.getLine();
    }

    public void addLocation(Location location){
        locations.add(location);
        circles.add(new CircleOptions()
                .center(new LatLng(location.getLatitude(), location.getLongitude()))
                .radius(CIRCLE_RADIUS)
                .strokeColor(ROUTE_COLOR)
                .fillColor(ROUTE_COLOR));
        lineOptions.add(new LatLng(location.getLatitude(), location.getLongitude()));
    }

    public void addStop(Location location){
        stops.add(location);
    }

    public ArrayList<Location> getStops(){
        return stops;
    }

    public void draw(GoogleMap map){
        clear();

        lineOptions = new PolylineOptions();
        for(int i = 0; i < getLocationNo(); i++)
            lineOptions.add(
                    new LatLng(locations.get(i).getLatitude(), locations.get(i).getLongitude()));
        lineOptions.width(CIRCLE_RADIUS);
        lineOptions.color(ROUTE_COLOR);
        line = map.addPolyline(lineOptions);


        for(int i = 0; i < getLocationNo(); i++) {
            Circle c = map.addCircle(circles.get(i));
            dots.add(c);
        }

    }

    public void clear(){
        while(dots.size() > 0){
            dots.get(0).remove();
            dots.remove(0);
        }
        if(line != null)
            line.remove();
    }

    public ArrayList<CircleOptions> getCircles(){
        return circles;
    }

    public ArrayList<Circle> getDots(){
        return dots;
    }

    public PolylineOptions getLineOpt(){
        return lineOptions;
    }

    public Polyline getLine(){
        return line;
    }

    public String getUsername(){
        return username;
    }

    public int getDay(){
        return day;
    }

    public int getMonth(){
        return month;
    }

    public int getYear(){
        return year;
    }

    public Location getLocation(int i){
        return locations.get(i);
    }

    public int getLocationNo(){
        return locations.size();
    }

    public static String readFile(){
        String rString = null;
        try {
            InputStream in = new FileInputStream(new File(BackgroundService.CURRENT_FILE_NAME));
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            try {
                while ((line = reader.readLine()) != null && line.length() > 2) {
                    rString = line;
                }
            } catch (IOException e) {
            }
            try {
                reader.close();
            } catch (IOException e) {
            }
        } catch (FileNotFoundException e) {
        }

        return rString;
    }

    public void delete(){
        File f = new File(BackgroundService.DIRECTORY_NAME + "/" + username
                + day + "-" + month + "-" + year);
        if(!f.exists())
            System.out.print("Dosya bulunamadı");
        else{
            clear();
            BackgroundService.curRoute = new Route(BackgroundService.username, BackgroundService.day,
                    BackgroundService.month, BackgroundService.year);
            f.delete();
        }
    }

    public String toString(){
        String result = "{\"username\":\"" + username + "\",\"day\":\""
                + day + "-" + month + "-" + year + "\",\"route\":\"";
        for(int i = 0; i < locations.size(); i++)
            result += locToString(locations.get(i));

        result += "\"\"stops\":\"";
        for(int i = 0; i < stops.size(); i++)
            result += locToString(stops.get(i));
        return result + "\"}";
    }

    public static Route fromString(String string){
        String input = string;
        String user = input.substring(input.indexOf(':') + 2, input.indexOf(',') - 1);
        input = input.substring(input.indexOf(',') + 1);
        int d = Integer.parseInt(input.substring(input.indexOf(':') + 2, input.indexOf('-')));
        input = input.substring(input.indexOf('-') + 1);
        int m = Integer.parseInt(input.substring(0, input.indexOf('-')));
        input = input.substring(input.indexOf('-') + 1);
        int y = Integer.parseInt(input.substring(0, input.indexOf(',') - 1));

        Route r = new Route(user, d, m, y);
        input = input.substring(input.indexOf(',') + 10);

        while(input.contains("+\"\"stops")){
            r.addLocation(stringToLoc(input.substring(0, input.indexOf('+') + 1)));
            input = input.substring(input.indexOf('+') + 1);
        }
        input = input.substring(input.indexOf(':') + 2);

        while(!input.equals("\"}")){
            r.addStop(stringToLoc(input.substring(0, input.indexOf('+') + 1)));
            input = input.substring(input.indexOf('+') + 1);
        }

        return r;
    }

    public static String locToString(Location location){
        return location.getLatitude() + "," +  location.getLongitude() + "," +  location.getTime()
                + "," + location.getSpeed() + "," +  location.getAccuracy()
                + "," + location.getAltitude() + "+";
    }

    public static Location stringToLoc(String string){
        String input = string;
        Location loc = new Location("fused");
        loc.setLatitude(Double.parseDouble(input.substring(0, input.indexOf(','))));
        input = input.substring(input.indexOf(',') + 1);
        loc.setLongitude(Double.parseDouble(input.substring(0, input.indexOf(','))));
        input = input.substring(input.indexOf(',') + 1);
        loc.setTime(Long.parseLong(input.substring(0, input.indexOf(','))));
        input = input.substring(input.indexOf(',') + 1);
        loc.setSpeed(Float.parseFloat(input.substring(0, input.indexOf(','))));
        input = input.substring(input.indexOf(',') + 1);
        loc.setAccuracy(Float.parseFloat(input.substring(0, input.indexOf(','))));
        input = input.substring(input.indexOf(',') + 1);
        loc.setAltitude(Double.parseDouble(input.substring(0, input.indexOf('+'))));

        return loc;
    }
}