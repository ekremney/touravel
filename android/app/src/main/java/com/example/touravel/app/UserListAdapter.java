package com.example.touravel.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.async.AsyncSearchByUsername;
import com.android.interfaces.OnTaskCompleted;

import java.util.List;

import static android.support.v4.app.ActivityCompat.startActivity;

/**
 * Created by Ekrem on 5/5/15.
 */
public class UserListAdapter extends BaseAdapter{

    private LayoutInflater mInflater;
    private List<User> users;

    public UserListAdapter (Activity activity, List<User> users) {
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.users = users;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int i) {
        return users.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View userListView;
        userListView = mInflater.inflate(R.layout.list_user, null);

        TextView name = (TextView) userListView.findViewById(R.id.user_name);
        TextView username = (TextView) userListView.findViewById(R.id.user_username);
        ImageView avatar_thumb = (ImageView) userListView.findViewById(R.id.user_avatar);

        final User user = users.get(i);

        name.setText(user.getName());
        username.setText(user.getUsername());

        byte[] decodedString = Base64.decode(user.getAvatar_thumb(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        avatar_thumb.setImageBitmap(decodedByte);

        final int clickedRow = i;

        userListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SplashScreen.cnt, "" + clickedRow , Toast.LENGTH_LONG).show();
                Bundle info = new Bundle();
                info.putString("username", user.getUsername());
                info.putString("name", user.getName());
                info.putString("location", user.getLocation());
                info.putString("about_me", user.getAbout_me());
                info.putString("avatar_thumb", user.getAvatar_thumb());
                info.putString("follower_count", ""+user.getFollowerCount());
                info.putString("following_count", ""+user.getFollowingCount());
                info.putString("route_count", ""+user.getRoute_count());
                Intent intent = new Intent(view.getContext(), ProfileActivity.class);
                intent.putExtra("user_bundle", info);
                view.getContext().startActivity(intent);
            }
        });

        return userListView;
    }

}
