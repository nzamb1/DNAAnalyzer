package com.dnaanalyzer;

import android.app.Application;

public class DnaApplication extends Application {
    private String uid;

    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }

}
