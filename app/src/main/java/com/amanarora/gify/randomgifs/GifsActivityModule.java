package com.amanarora.gify.randomgifs;

import dagger.Module;
import dagger.Provides;

@Module
public class GifsActivityModule {

    @Provides
    ExoplayerManager providesExoplayerManager(GifsActivity activity) {
        return new ExoplayerManager(activity, activity);
    }
}
