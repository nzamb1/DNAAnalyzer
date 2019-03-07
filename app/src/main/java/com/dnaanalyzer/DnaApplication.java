package com.dnaanalyzer;

import android.app.Application;

public class DnaApplication extends Application {
    private String uid;
    private String backendUrl = "http://18.184.180.140:5000";

    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getbackendUrl() {
        return backendUrl;
    }


}
