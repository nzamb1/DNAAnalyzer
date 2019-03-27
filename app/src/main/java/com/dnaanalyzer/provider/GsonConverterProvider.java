package com.dnaanalyzer.provider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.converter.gson.GsonConverterFactory;

public final class GsonConverterProvider {
    public static GsonConverterFactory provide() {
        Gson gson = new GsonBuilder()
                .create();

        return GsonConverterFactory.create(gson);
    }
}