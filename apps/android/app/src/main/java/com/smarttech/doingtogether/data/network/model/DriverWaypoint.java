package com.smarttech.doingtogether.data.network.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

public class DriverWaypoint {

    @SerializedName("name")
    private String mName;

    @SerializedName("avatar")
    private String mAvatarUrl;

    @SerializedName("lat")
    private Double mLat;

    @SerializedName("lng")
    private Double mLng;

    private LatLng mGeoPos;

    public DriverWaypoint(String name, String avatar, Double lat, Double lng) {
        mName = name;
        mAvatarUrl = avatar;
        mLat = lat;
        mLng = lng;
        mGeoPos = new LatLng(lat, lng);
    }

    public LatLng getPosition() {
        return mGeoPos;
    }

    public Double getLat() {
        return mLat;
    }

    public Double getLng() {
        return mLng;
    }
}
