package com.amanarora.gify.injection.viewmodelinjection;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.amanarora.gify.randomgifs.GifsViewModel;
import com.amanarora.gify.trendinggifs.TrendingViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    public abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);

    @Binds
    @IntoMap
    @ViewModelKey(TrendingViewModel.class)
    public abstract ViewModel bindTrendingViewModel(TrendingViewModel trendingViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(GifsViewModel.class)
    public abstract ViewModel bindGifsViewModel(GifsViewModel gifsViewModel);
}
