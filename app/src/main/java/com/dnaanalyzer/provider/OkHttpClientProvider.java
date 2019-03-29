package com.dnaanalyzer.provider;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public final class OkHttpClientProvider {

    public static OkHttpClient provide() {
        return new OkHttpClient.Builder()
                .addInterceptor(headersInterceptor())
                .addInterceptor(loggingInterceptor())
                .retryOnConnectionFailure(true)
                .build();
    }

    private static Interceptor loggingInterceptor() {
        return new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.HEADERS);
        //.setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    private static Interceptor headersInterceptor() {
        return chain -> chain.proceed(chain.request()
                .newBuilder()
                .addHeader("Connection", "Close")
                .build());
    }

}
