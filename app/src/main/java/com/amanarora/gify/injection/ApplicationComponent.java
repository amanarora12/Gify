package com.amanarora.gify.injection;

import android.app.Application;

import com.amanarora.gify.GifyApp;
import com.amanarora.gify.injection.viewmodelinjection.ViewModelModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {AndroidSupportInjectionModule.class,
        ApplicationModule.class,
        NetworkModule.class,
        ViewModelModule.class,
        BuildersModule.class})
public interface ApplicationComponent {
    @Component.Builder
    interface Builder{
        @BindsInstance
        Builder application(Application application);
        ApplicationComponent build();
    }

    void inject(GifyApp app);
}
