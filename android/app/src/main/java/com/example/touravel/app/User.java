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
    private String avatar;
    private int follower_count, following_count, route_count;

    public User() {
        username = null;
        name = null;
        location = null;
        about_me = null;
        avatar_thumb = null;
        avatar = null;
        follower_count = 0;
        following_count = 0;
        route_count = 0;
    }

    public User (String username, String name, String location, String about_me, String avatar_thumb) {
        this.username = username;
        this.name = name;
        this.location = location;
        this.about_me = about_me;
        this.avatar_thumb = avatar_thumb;
    }

    public User (String username, String name, String location, String about_me, String avatar_thumb, String avatar, int follower_count, int following_count, int route_count) {
        this.username = username;
        this.name = name;
        this.location = location;
        this.about_me = about_me;
        this.avatar_thumb = avatar_thumb;
        this.avatar = avatar;
        this.follower_count = follower_count;
        this.following_count = following_count;
        this.route_count = route_count;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getAbout_me(){

        return about_me;
    }

    public String getAvatar(){
        return avatar;
    }

    public String getAvatar_thumb(){
        return avatar_thumb;
    }

    public int getFollowerCount(){
        return follower_count;
    }

    public int getFollowingCount(){
        return following_count;
    }

    public int getRoute_count(){
        return route_count;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setAbout_me(String about_me) {
        this.about_me = about_me;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setFollowing_count (int following_count) {
        this.following_count = following_count;
    }
}