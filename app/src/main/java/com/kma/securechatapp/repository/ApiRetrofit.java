package com.kma.securechatapp.repository;

import org.jetbrains.annotations.NotNull;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class ApiRetrofit {
    public static final Object createRetrofit(@NotNull String baseLink, @NotNull Class clazz) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(logging).build();

        return (new Retrofit.Builder()).baseUrl(baseLink).addConverterFactory((Converter.Factory) GsonConverterFactory.create()).client(httpClient).build().create(clazz);
    }
}
