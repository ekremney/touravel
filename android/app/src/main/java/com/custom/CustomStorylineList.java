package com.custom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.async.AsyncPostTimeline;
import com.example.touravel.app.BackgroundService;
import com.example.touravel.app.R;
import com.example.touravel.app.Route;
import com.example.touravel.app.ShowOnMap;
import com.example.touravel.app.SplashScreen;
import com.example.touravel.app.StorylineActivity;
import com.example.touravel.app.TimelineActivity;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by gokg on 6.5.2015.
 */
public class CustomStorylineList extends ArrayAdapter<String>
{
    private final Activity context;
    private final String[] nameList;

    public CustomStorylineList(Activity context, String[] nameList)
    {
        super(context, R.layout.timeline_item, nameList);
        this.context = context;
        this.nameList = nameList;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        LayoutInflater inflater = context.getLayoutInflater();
        final View rowView = inflater.inflate(R.layout.timeline_item, null, true);
        final int pos = position;
        rowView.findViewById(R.id.like_image).setVisibility(View.INVISIBLE);
/*
        ImageView iv = (ImageView) rowView.findViewById(R.id.line_avatar);
        byte[] decodedString = Base64.decode(StorylineActivity.avatar, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        iv.setImageBitmap(decodedByte);
*/
        TextView txtTitle = (TextView) rowView.findViewById(R.id.line_name);
        txtTitle.setText(nameList[position]);

        txtTitle = (TextView) rowView.findViewById(R.id.line_text);

        if(StorylineActivity.types[position].equals("0"))
            txtTitle.setText("A location is saved.\n\nClick to view.");
        else
            txtTitle.setText("A route is saved.\n\nClick to view.");

        txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackgroundService.tempType = StorylineActivity.types[pos].equals("1");
                if (BackgroundService.tempType)
                    BackgroundService.tempRoute = Route.fromString("\"route\":\"" +
                            StorylineActivity.data[pos] + "\",\"stops\":\"\"");
                else
                    BackgroundService.tempLoc = new LatLng(Route.stringToLoc(StorylineActivity.data[pos]).getLatitude(),
                            Route.stringToLoc(StorylineActivity.data[pos]).getLongitude());

                context.startActivity(new Intent(context, ShowOnMap.class));
            }
        });


        TextView like = (TextView) rowView.findViewById(R.id.like_text);
        like.setText("Share");
        View v = rowView.findViewById(R.id.like);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView like = (TextView) rowView.findViewById(R.id.like_text);
                if(((String)like.getText()).equals("Share")){
                    new AsyncPostTimeline().execute(context.getResources().getString(R.string.url_post_timeline),
                            SplashScreen.auth, StorylineActivity.types[pos], StorylineActivity.data[pos]);
                    like.setText("Shared");
                }
            }
        });

        return rowView;
    }
}



