package com.smarttech.doingtogether.data.network.model;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("key")
    private String mKey;

    public LoginResponse(String key) {
        mKey = key;
    }

    @Nullable
    public String getKey() {
        return mKey;
    }
}
