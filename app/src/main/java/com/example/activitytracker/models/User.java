package com.example.activitytracker.models;

public class User {

    private String username;
    private String email;
    private String fullName;

    public User(){

    }
    public User(String username, String email, String fullName){
        this.username = username;
        this.fullName = fullName;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
