package com.custom;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.touravel.app.R;
import com.example.touravel.app.StorylineActivity;
import com.example.touravel.app.TimelineActivity;

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

        ImageView iv = (ImageView) rowView.findViewById(R.id.line_avatar);
        byte[] decodedString = Base64.decode(StorylineActivity.avatar, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        iv.setImageBitmap(decodedByte);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.line_name);
        txtTitle.setText(nameList[position]);

        txtTitle = (TextView) rowView.findViewById(R.id.line_text);
        if(StorylineActivity.types[position].equals("0"))
            txtTitle.setText(nameList[position] + " shared a location.");
        else
            txtTitle.setText(nameList[position] + " shared a route.");

        TextView like = (TextView) rowView.findViewById(R.id.like_text);
        like.setText("Share");
        View v = rowView.findViewById(R.id.like);
        final int pos = position;
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView like = (TextView) rowView.findViewById(R.id.like_text);
                if(((String)like.getText()).equals("Share")){
                    new StorylineActivity().share(pos);
                    like.setText("Shared");
                }
            }
        });

        return rowView;
    }
}



