package com.kma.securechatapp.repository;

import org.jetbrains.annotations.NotNull;

import kotlin.jvm.internal.Intrinsics;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class ApiRetrofit {
    public static final Object createRetrofit(@NotNull String baseLink, @NotNull Class clazz) {
        return (new Retrofit.Builder()).baseUrl(baseLink).addConverterFactory((Converter.Factory) GsonConverterFactory.create()).build().create(clazz);
    }
}
