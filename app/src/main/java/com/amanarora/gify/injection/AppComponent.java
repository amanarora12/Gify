package com.amanarora.gify.injection;

import com.amanarora.gify.trendinggifs.TrendingActivity;
import com.amanarora.gify.trendinggifs.TrendingViewModel;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class})
public interface AppComponent {
    void inject(TrendingActivity target);
    void inject(TrendingViewModel target);
}
