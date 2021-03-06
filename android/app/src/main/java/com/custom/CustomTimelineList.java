package com.custom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.touravel.app.BackgroundService;
import com.example.touravel.app.MapActivity;
import com.example.touravel.app.R;
import com.example.touravel.app.Route;
import com.example.touravel.app.ShowOnMap;
import com.example.touravel.app.SplashScreen;
import com.example.touravel.app.StorylineActivity;
import com.example.touravel.app.TimelineActivity;
import com.google.android.gms.maps.model.LatLng;

public class CustomTimelineList extends ArrayAdapter<String>
{
    private final Activity context;
    private final String[] nameList;

    public CustomTimelineList(Activity context, String[] nameList)
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
        TimelineActivity.likes[pos] = "" + (Integer.parseInt(TimelineActivity.likes[pos]) - 1);

        ImageView iv = (ImageView) rowView.findViewById(R.id.line_avatar);
        byte[] decodedString = Base64.decode(TimelineActivity.avatars[position], Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        iv.setImageBitmap(decodedByte);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.line_name);
        txtTitle.setText(nameList[position]);

        txtTitle = (TextView) rowView.findViewById(R.id.line_text);

        if(TimelineActivity.types[position].equals("0"))
            txtTitle.setText(nameList[position] + " shared a location.\n\nClick to view.");
        else
            txtTitle.setText(nameList[position] + " shared a route.\n\nClick to view.");



        BackgroundService.tempType = TimelineActivity.types[pos].equals("1");

        ImageView symbol = (ImageView)rowView.findViewById(R.id.center_image);
        if(BackgroundService.tempType) {
            symbol.setImageDrawable(context.getResources().getDrawable(R.drawable.route_sym));

            final float scale = getContext().getResources().getDisplayMetrics().density;
            int width = (int) (250 * scale + 0.5f);
            int height = (int) (125 * scale + 0.5f);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
            layoutParams.gravity= Gravity.CENTER;
            symbol.setLayoutParams(layoutParams);

        }
        else {
            symbol.setImageDrawable(context.getResources().getDrawable(R.drawable.loc_sym));
            /*
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(30, 30);
            symbol.setLayoutParams(layoutParams);
            */
        }

        rowView.findViewById(R.id.middle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackgroundService.tempType = TimelineActivity.types[pos].equals("1");
                if (BackgroundService.tempType)
                    BackgroundService.tempRoute = Route.fromString("\"route\":\"" +
                            TimelineActivity.data[pos] + "\",\"stops\":\"\"");
                else {
                    String input = TimelineActivity.data[pos];
                    double lat = Double.parseDouble(input.substring(0, input.indexOf(',')));
                    input = input.substring(input.indexOf(',') + 1);
                    double lon = Double.parseDouble(input.substring(0, input.indexOf(',')));
                    input = input.substring(input.indexOf(',') + 1);
                    String txt = input.substring(0, input.indexOf('+'));
                    input = input.substring(input.indexOf('+') + 1);
                    BackgroundService.tempLoc = new LatLng(lat, lon);
                    BackgroundService.tempText = txt;
                }

                context.startActivity(new Intent(context, ShowOnMap.class));
            }});

        TextView like = (TextView) rowView.findViewById(R.id.like_text);
        if (Integer.parseInt(TimelineActivity.likes[position]) > 0)
            like.setText("Like (" + TimelineActivity.likes[position] + ")");
        View v = rowView.findViewById(R.id.like);
        v.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View view) {
                                     TextView like = (TextView) rowView.findViewById(R.id.like_text);
                                     if (((String) like.getText()).length() == 4 || like.getText().charAt(4) != 'd') {
                                        TimelineActivity.likes[pos] = (Integer.parseInt(TimelineActivity.likes[pos]) + 1) + "";
                                        like.setText("Liked (" + TimelineActivity.likes[pos] + ")");
                                     } else {
                                        TimelineActivity.likes[pos] = (Integer.parseInt(TimelineActivity.likes[pos]) - 1) + "";
                                        like.setText("Like");
                                        if (Integer.parseInt(TimelineActivity.likes[pos]) > 0)
                                            like.setText("Like (" + TimelineActivity.likes[pos] + ")");
                                     }
        }
                                     }

                );

                return rowView;
            }
        }



