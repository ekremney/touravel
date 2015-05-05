package com.android.interfaces;

import com.example.touravel.app.User;

import java.util.List;

/**
 * Created by Ekrem on 5/5/15.
 */
public interface OnTaskCompleted {
    void onTaskCompleted(List<User> users);
    void onTaskCompleted();
}
