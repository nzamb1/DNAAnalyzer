package com.dnaanalyzer;

import android.app.Application;
import android.content.Context;

import com.dnaanalyzer.api.Api;
import com.dnaanalyzer.provider.ApiProvider;
import com.facebook.appevents.AppEventsLogger;

public class DnaApplication extends Application {
    private String uid;
    private Api api;

    @Override
    public void onCreate() {
        super.onCreate();
        api = ApiProvider.provide();
        AppEventsLogger.activateApp(this);
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
