package com.moovel.service;

import android.support.annotation.NonNull;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIService {
    public static final String BASE_URL = "https://api.github.com/";
    private OkHttpClient httpClient = null;

    public APIInterface getService() {
        if (httpClient == null) {
            httpClient = getOkHttpClient();
        }

        return new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(httpClient)
                .build()
                .create(APIInterface.class);
    }

    private static OkHttpClient getOkHttpClient() {
        // for staging and dev environments print out the responses
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // response body log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);


        return new OkHttpClient.Builder().addInterceptor(
                chain -> {
                    Request original = chain.request();
                    Request.Builder builder = original.newBuilder()
                            .method(original.method(), original.body());
                    return chain.proceed(builder.build());
                })
                .addInterceptor(logging)
                .build();
    }

}

