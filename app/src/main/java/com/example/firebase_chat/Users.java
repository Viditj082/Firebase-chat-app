package com.example.firebase_chat;

public class Users
{
    private String username;
    private String profile_pic;
    private String  userid;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Users(String username, String profile_pic, String userid) {
        this.username = username;
        this.profile_pic = profile_pic;
        this.userid = userid;
    }
}
