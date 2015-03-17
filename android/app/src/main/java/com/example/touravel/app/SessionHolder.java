package com.example.touravel.app;

/**
 * Created by gokhancs on 17/03/15.
 */

import android.content.Context;
import android.content.SharedPreferences;

public class SessionHolder {

    private static SessionHolder instance;

    private static final String PREFERENCE_NAME = "APP_SESSION";
    private final Context context;

    public static SharedPreferences getSession(Context context)
    {
        if (instance == null)
        {
            instance = new SessionHolder(context);
        }

        return instance.getSession();
    }

    private SessionHolder(Context context)
    {
        this.context = context;
    }


    public SharedPreferences getSession()
    {
        return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

}