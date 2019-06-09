package com.smarttech.doingtogether.data.network.model;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {

    @SerializedName("login")
    private String mLogin;

    @SerializedName("password")
    private String mPassword;

    public LoginRequest(String login, String password) {
        mLogin = login;
        mPassword = password;
    }

}
