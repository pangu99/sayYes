package com.example.sayyes;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Post {
    private String userID;
    private String postID;
    private String postTitle;
    private String postStory;
    private String postImage;
    private Integer postLiked;
    private List<String> postHashtags;
    private Boolean isHome;
    private String locationDescription;

    public Post(String userID, String postID, String postTitle, String postStory, List<String> postHashtags,
                Boolean isHome, String locationDescription){
        this.userID = userID;
        this.postID = postID;
        this.postTitle = postTitle;
        this.postStory = postStory;
        this.postHashtags = postHashtags;
        this.isHome = isHome;
        this.locationDescription = locationDescription;
        this.postLiked = 0;
        Log.d("PRINT", "POST object created");
    }

    public Post() {

    }

    public String getUserid() {
        return userID;
    }

    public void setUserid(String userID) {
        this.userID = userID;
    }

    public String getPostid() {
        return postID;
    }

    public void setPostid(String postID) {
        this.postID = postID;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getPostStory() {
        return postStory;
    }

    public void setPostStory(String postStory) {
        this.postStory = postStory;
    }

    public List<String> getPostHashTags() {
        return postHashtags;
    }

    public void setPostHashTags(List<String> postHashtags) {
        this.postHashtags = postHashtags;
    }

    public Boolean getIsHome() {
        return isHome;
    }

    public void setIsHome(Boolean home) {
        isHome = home;
    }

    public String getLocationDescription() {
        return locationDescription;
    }

    public void setLocationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
    }

    public Integer getPostLiked() {
        return postLiked;
    }

    public void incrementPostLiked() {
        this.postLiked++;
    }
}