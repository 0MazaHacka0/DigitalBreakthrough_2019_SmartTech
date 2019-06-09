package com.smarttech.doingtogether.data.network;

import com.smarttech.doingtogether.data.network.model.DomainResponse;
import com.smarttech.doingtogether.data.network.model.DriverWaypoint;
import com.smarttech.doingtogether.data.network.model.LoginRequest;
import com.smarttech.doingtogether.data.network.model.LoginResponse;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

import static com.smarttech.doingtogether.utils.AppConstants.API_AUTHORIZATION_HEADER;

public interface ApiHelper {

    @POST("login")
    Observable<LoginResponse> login(@Body LoginRequest request);

    @GET("domain")
    Observable<DomainResponse> getIconByDomain(@Query("domain") String domain);

    @GET("drivers")
    Observable<ArrayList<DriverWaypoint>> getDrivers(@Header(API_AUTHORIZATION_HEADER) String mAuth,
                                                     @Query("lat") Double lat,
                                                     @Query("lng") Double lng,
                                                     @Query("direction") int direction);

}
