package com.example.touravel.app;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

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

        User user = users.get(i);

        name.setText(user.getName());
        username.setText(user.getUsername());

        byte[] decodedString = Base64.decode(user.getAvatar_thumb(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        avatar_thumb.setImageBitmap(decodedByte);

        return userListView;
    }
}
