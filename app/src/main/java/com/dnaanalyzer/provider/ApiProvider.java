package com.dnaanalyzer.provider;

import com.dnaanalyzer.Constants;
import com.dnaanalyzer.api.Api;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class ApiProvider {
    public static Api provide() {
        return new Retrofit.Builder()
                .baseUrl(Constants.BACKEND_URL)
                .addConverterFactory(GsonConverterProvider.provide())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(OkHttpClientProvider.provide())
                .build()
                .create(Api.class);
    }
}
