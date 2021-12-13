package com.example.sayyes;

import java.util.List;

public class search_info {

    public boolean isHome;
    public String locationDescription, postImage, postStory, postTitle, postid, userid;
    public List<String> postHashTags;

    public search_info(){

    }

    public boolean isHome() {
        return isHome;
    }

    public void setHome(boolean home) {
        isHome = home;
    }

    public String getLocationDescription() {
        return locationDescription;
    }

    public void setLocationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
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

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public List<String> getPostHashTags() {
        return postHashTags;
    }

    public void setPostHashTags(List<String> postHashTags) {
        this.postHashTags = postHashTags;
    }

    public search_info(boolean isHome, String locationDescription, String postImage, String postStory, String postTitle, String postid, String userid, List<String> postHashTags) {
        this.isHome = isHome;
        this.locationDescription = locationDescription;
        this.postImage = postImage;
        this.postStory = postStory;
        this.postTitle = postTitle;
        this.postid = postid;
        this.userid = userid;
        this.postHashTags = postHashTags;
    }
}
