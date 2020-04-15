package com.example.atomwallet;

public class User {
    private String name,phoneNumber,userId,zPassword;

    public User(){}

    public User(String name, String phoneNumber, String userId, String zPassword) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.userId = userId;
        this.zPassword = zPassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getzPassword() {
        return zPassword;
    }

    public void setzPassword(String zPassword) {
        this.zPassword = zPassword;
    }
}
