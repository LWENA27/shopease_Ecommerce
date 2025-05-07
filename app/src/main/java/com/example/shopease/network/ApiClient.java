package com.example.shopease.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String BASE_URL = "https://api.example.com/";
    private static Retrofit retrofit = null;

    // Thread-safe method to get the Retrofit instance
    public static Retrofit getClient() {
        if (retrofit == null) {
            synchronized (ApiClient.class) {
                if (retrofit == null) {
                    // Configure OkHttpClient
                    OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                            .connectTimeout(30, TimeUnit.SECONDS)
                            .readTimeout(30, TimeUnit.SECONDS)
                            .writeTimeout(30, TimeUnit.SECONDS);

                    // Add logging interceptor for debugging
                    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                    httpClient.addInterceptor(logging);

                    // Configure Gson
                    Gson gson = new GsonBuilder()
                            .setLenient()
                            .create();

                    // Initialize Retrofit
                    retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .client(httpClient.build())
                            .build();
                }
            }
        }
        return retrofit;
    }

    // Method to get ApiService instance
    public static ApiService getApiService() {
        return getClient().create(ApiService.class);
    }
}