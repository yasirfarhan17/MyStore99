package com.example.networkmodule.model;

public class User {

    private String name;
    private String password;
    private String uid;
    private Boolean isOtpVerified;

    public String getName() {
        return name;
    }

    public User(String name, String password, String uid, Boolean isOtpVerified) {
        this.name = name;
        this.password = password;
        this.uid = uid;
        this.isOtpVerified = isOtpVerified;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Boolean getOtpVerified() {
        return isOtpVerified;
    }

    public void setOtpVerified(Boolean otpVerified) {
        isOtpVerified = otpVerified;
    }

    public User() {

    }


}
