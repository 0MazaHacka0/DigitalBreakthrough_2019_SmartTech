package com.smarttech.doingtogether.data.network;

import com.smarttech.doingtogether.data.network.model.DomainResponse;
import com.smarttech.doingtogether.data.network.model.LoginRequest;
import com.smarttech.doingtogether.data.network.model.LoginResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiHelper {

    @POST("login")
    Observable<LoginResponse> login(@Body LoginRequest request);

    @GET("domain")
    Observable<DomainResponse> getIconByDomain(@Query("domain") String domain);

}
