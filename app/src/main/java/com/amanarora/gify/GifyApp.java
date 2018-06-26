package com.amanarora.gify;

import android.app.Application;

import com.amanarora.gify.injection.AppComponent;
import com.amanarora.gify.injection.AppModule;
import com.amanarora.gify.injection.DaggerAppComponent;
import com.amanarora.gify.injection.NetworkModule;

public class GifyApp extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule())
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
