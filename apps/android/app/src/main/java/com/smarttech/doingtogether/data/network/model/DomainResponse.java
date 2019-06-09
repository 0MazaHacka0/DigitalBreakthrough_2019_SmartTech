package com.smarttech.doingtogether.data.network.model;

import com.google.gson.annotations.SerializedName;

public class DomainResponse {

    @SerializedName("icon")
    private String mIconUrl;

    public DomainResponse(String iconUrl) {
        mIconUrl = iconUrl;
    }

    public String getIconUrl() {
        return mIconUrl;
    }

}
