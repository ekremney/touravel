package com.custom;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.touravel.app.R;

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
        View rowView = inflater.inflate(R.layout.timeline_item, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.username);
        txtTitle.setText(nameList[position]);

        return rowView;
    }
}