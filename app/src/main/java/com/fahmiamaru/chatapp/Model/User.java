package com.fahmiamaru.chatapp.Model;

public class User {
    String id;
    String Username;
    String ImageURL;
    String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public User(String id, String Username, String ImageURL) {
        this.id = id;
        this.Username = Username;
        this.ImageURL = ImageURL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public User() {
    }

}