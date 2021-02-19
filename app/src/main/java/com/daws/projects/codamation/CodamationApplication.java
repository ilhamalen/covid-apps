package com.daws.projects.codamation;

import androidx.multidex.MultiDexApplication;

import com.androidnetworking.AndroidNetworking;

public class CodamationApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidNetworking.initialize(this);
    }
}
