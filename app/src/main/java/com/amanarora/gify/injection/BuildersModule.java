package com.amanarora.gify.injection;

import com.amanarora.gify.randomgifs.GifsActivity;
import com.amanarora.gify.randomgifs.GifsActivityModule;
import com.amanarora.gify.trendinggifs.TrendingActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class BuildersModule {

    @ContributesAndroidInjector
    public abstract TrendingActivity bindTrendingActivity();

    @ContributesAndroidInjector (modules = GifsActivityModule.class)
    public abstract GifsActivity bindGifActivity();
}
