package com.smarttech.doingtogether.data.network;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.smarttech.doingtogether.utils.AppConstants.API_BASE_URL;
import static com.smarttech.doingtogether.utils.AppConstants.CALL_TIMEOUT_SECONDS;
import static com.smarttech.doingtogether.utils.AppConstants.CONNECT_TIMEOUT_SECONDS;
import static com.smarttech.doingtogether.utils.AppConstants.READ_TIMEOUT_SECONDS;
import static com.smarttech.doingtogether.utils.AppConstants.WRITE_TIMEOUT_SECONDS;

public class AppApiHelper {

    // Api interface
    private static ApiHelper sApiInterface = null;

    @NonNull
    private static ApiHelper createRetrofitAdapter() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .retryOnConnectionFailure(true)
                .callTimeout(CALL_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .build();

        // Set data converter
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return retrofit.create(ApiHelper.class);
    }

    /**
     * Singleton for ApiInterface
     *
     * @return ApiInterface instance
     */
    public static ApiHelper getRetrofitAdapter() {
        if (sApiInterface == null)
            sApiInterface = createRetrofitAdapter();
        return sApiInterface;
    }

}
