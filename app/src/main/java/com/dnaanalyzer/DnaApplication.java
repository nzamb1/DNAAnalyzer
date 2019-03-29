package com.dnaanalyzer;

import android.app.Application;
import android.content.Context;

import com.dnaanalyzer.api.Api;
import com.dnaanalyzer.provider.ApiProvider;

public class DnaApplication extends Application {
    private String uid;
    private Api api;

    @Override
    public void onCreate() {
        super.onCreate();
        api = ApiProvider.provide();
    }

    public Api getApi() {
        return api;
    }

    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }

    public static DnaApplication get(Context context) {
        return (DnaApplication) context.getApplicationContext();
    }
}
