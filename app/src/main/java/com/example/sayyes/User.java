package com.example.sayyes;

public class User {

    private String userID;
    private String name;
    private String email;
    private String imageURL;

    public User(String userID, String name, String email, String imageURL) {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.imageURL = imageURL;
    }

    public User(){

    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
