package com.example.sayyes;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Post {
    private String postID;
    private String postTitle;
    private String postStory;
    private String postImage;
    private Integer postLiked;
    private List<String> postHashtags;
    private Boolean isHome;
    private String locationDescription;

    public Post(String postID, String postTitle, String postImage, String postStory, List<String> postHashtags,
                Boolean isHome, String locationDescription){
        this.postID = postID;
        this.postTitle = postTitle;
        this.postImage = postImage;
        this.postStory = postStory;
        this.postHashtags = postHashtags;
        this.isHome = isHome;
        this.locationDescription = locationDescription;
        this.postLiked = 0;
        Log.d("PRINT", "POST object created");
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostImage(){ return postImage; }

    public String getPostStory() {
        return postStory;
    }

    public void setPostStory(String postStory) {
        this.postStory = postStory;
    }

    public List<String> getPostHashtags() {
        return postHashtags;
    }

    public void setPostHashtags(List<String> postHashtags) {
        this.postHashtags = postHashtags;
    }

    public Boolean getHome() {
        return isHome;
    }

    public void setHome(Boolean home) {
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
