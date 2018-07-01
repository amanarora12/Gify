package com.amanarora.gify.injection;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    @Singleton
    @Provides
    public Context provideContext(Application application) {
        return application;
    }
}
