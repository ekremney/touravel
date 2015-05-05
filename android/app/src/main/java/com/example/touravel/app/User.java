package com.example.touravel.app;

/**
 * Created by Ekrem on 5/5/15.
 */
public class User {
    private String username;
    private String name;
    private String location;
    private String about_me;
    private String avatar_thumb;

    public User() {
        username = null;
        name = null;
        location = null;
        about_me = null;
        avatar_thumb = null;
    }

    public User (String username, String name, String location, String about_me, String avatar_thumb) {
        this.username = username;
        this.name = name;
        this.location = location;
        this.about_me = about_me;
        this.avatar_thumb = avatar_thumb;
    }

    String getUsername() {
        return username;
    }

    String getName() {
        return name;
    }

    String getLocation() {
        return location;
    }

    String getAbout_me(){
        return about_me;
    }

    String getAvatar_thumb(){
        return avatar_thumb;
    }
}
