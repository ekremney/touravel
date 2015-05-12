package com.example.touravel.app;

import android.graphics.Color;
import android.location.Location;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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
    private ArrayList<LatLng> stops = null;
    private ArrayList<Marker> markers = null;
    private ArrayList<String> texts = null;
    private ArrayList<Circle> dots = null;
    private ArrayList<CircleOptions> circles = null;
    private Polyline line = null;
    private PolylineOptions lineOptions = null;
    public static final int CIRCLE_RADIUS = 6;
    public static final int DRAW_COLOR = Color.parseColor("#088A08");
    public int lastCheckin;

    public Route (){
        locations = new ArrayList<Location>();
        stops = new ArrayList<LatLng>();
        markers = new ArrayList<Marker>();
        texts = new ArrayList<String>();
        dots = new ArrayList<Circle>();
        circles = new ArrayList<CircleOptions>();
        lineOptions = new PolylineOptions();
        lastCheckin = -1;
    }

    public Route(Route r){
        locations = new ArrayList<Location>();
        stops = new ArrayList<LatLng>();
        circles = new ArrayList<CircleOptions>();
        dots = new ArrayList<Circle>();
        texts = new ArrayList<String>();
        lastCheckin = r.lastCheckin;

        for(int i = 0; i < r.getLocationNo(); i++)
            locations.add(r.getLocation(i));

        for(int i = 0; i < r.getStopList().size(); i++)
            stops.add(r.getStopList().get(i));

        for(int i = 0; i < r.getTexts().size(); i++)
            texts.add(r.getTexts().get(i));

        for(int i = 0; i < r.getCircles().size(); i++)
            circles.add((r.getCircles()).get(i));

        for(int i = 0; i < r.getDots().size(); i++)
            dots.add(r.getDots().get(i));

        lineOptions = r.getLineOpt();
        line = r.getLine();
    }

    public boolean chdddddddddeck(Route r){
        boolean result = (getLocationNo() == r.getLocationNo() && stops.size() == r.getStopList().size());
        if(!result)
            return false;

        for(int i = 0; i < r.getLocationNo(); i++)
            if(locations.get(i) != r.getLocation(i))
                result = false;

        for(int i = 0; i < r.getStopList().size(); i++)
            if(stops.get(i) != r.getStopList().get(i))
                result = false;

        return result;
    }

    public void addLocation(Location location){
        locations.add(location);
        circles.add(new CircleOptions()
                .center(new LatLng(location.getLatitude(), location.getLongitude()))
                .radius(CIRCLE_RADIUS)
                .strokeColor(DRAW_COLOR)
                .fillColor(DRAW_COLOR));
        lineOptions.add(new LatLng(location.getLatitude(), location.getLongitude()));
    }

    public void addStop(LatLng location, String text){
        stops.add(location);
        texts.add(text);
    }

    public ArrayList<LatLng> getStopList(){
        return stops;
    }

    public void draw(GoogleMap map){
        clear();

        lineOptions = new PolylineOptions();
        for(int i = 0; i < getLocationNo(); i++)
            lineOptions.add(
                    new LatLng(locations.get(i).getLatitude(), locations.get(i).getLongitude()));
        lineOptions.width((int) (CIRCLE_RADIUS * 1.5));
        lineOptions.color(DRAW_COLOR);
        line = map.addPolyline(lineOptions);


        for(int i = 0; i < getLocationNo(); i++) {
            Circle c = map.addCircle(circles.get(i));
            dots.add(c);
        }

        for(int i = 0; i < getStopList().size(); i++) {
            markers.add(MapActivity.putMarker(map, stops.get(i).latitude, stops.get(i).longitude, texts.get(i)));
        }
    }

    public void clear(){
        while(dots.size() > 0){
            dots.get(0).remove();
            dots.remove(0);
        }
        while(markers.size() > 0){
            markers.get(0).remove();
            markers.remove(0);
        }
        if(line != null)
            line.remove();
    }

    public ArrayList<CircleOptions> getCircles(){
        return circles;
    }

    public ArrayList<String> getTexts(){
        return texts;
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

   /* public String getUsername(){
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
    }*/

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
        File f = new File(BackgroundService.CURRENT_FILE_NAME);
        if(!f.exists())
            System.out.print("File couldn't be found.");
        else{
            clear();
            BackgroundService.curRoute = new Route();
            f.delete();
        }
    }

    public String getLocations(){
        String result = "";
        for(int i = 0; i < locations.size(); i++)
            result += locToString(locations.get(i));
        return result;
    }

    public String getStops(){
        String result = "";
        for(int i = 0; i < stops.size(); i++)
            result += stops.get(i).latitude + "," +  stops.get(i).longitude + "," + texts.get(i) + "+";
        return result;
    }

    public String toString(){
        return "\"route\":\"" + getLocations() + "\",\"stops\":\"" + getStops() + "\"";
    }

    public static Route fromString(String string){
        String input = string;
        Route r = new Route();
        input = input.substring(9);

        while(input.contains("+\",\"stops")){
            r.addLocation(stringToLoc(input.substring(0, input.indexOf('+') + 1)));
            input = input.substring(input.indexOf('+') + 1);
        }
        input = input.substring(input.indexOf(':') + 2);
        input = input.substring(0, input.indexOf('"'));
        r.enterAllStops(input);
        return r;
    }

    public static String locToString(Location location){
        return location.getLatitude() + "," +  location.getLongitude() + "," +  location.getTime()
                + "," + location.getSpeed() + "," +  location.getAccuracy()
                + "," + location.getAltitude() + "+";
    }

    public void enterAllStops(String string){
        String input = string + "!";
        while(!input.equals("!")){
            double lat = Double.parseDouble(input.substring(0, input.indexOf(',')));
            input.substring(input.indexOf(',') + 1);
            double lon = Double.parseDouble(input.substring(0, input.indexOf(',')));
            input.substring(input.indexOf(',') + 1);
            String txt = input.substring(0, input.indexOf(','));
            input.substring(input.indexOf('+') + 1);
            addStop(new LatLng(lat, lon), txt);
        }
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