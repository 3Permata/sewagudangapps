package com.tigapermata.sewagudangapps.api;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public static final String BASE_URL = "https://apps.sewagudang.id/";

    private static Retrofit retrofit = null;

    private static Context context;

    public static Retrofit getClient(){
        if (retrofit == null){
//            Gson gson = new GsonBuilder().setLenient().create();
            Gson gson = new GsonBuilder().serializeNulls().create();

            retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}
