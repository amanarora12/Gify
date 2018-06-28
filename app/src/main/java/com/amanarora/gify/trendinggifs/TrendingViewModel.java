package com.amanarora.gify.trendinggifs;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.amanarora.gify.GifyApp;
import com.amanarora.gify.api.GiphyService;
import com.amanarora.gify.models.GifObject;

import java.util.List;

import javax.inject.Inject;

public class TrendingViewModel extends AndroidViewModel {

    @Inject
    GiphyService giphyService;

    public TrendingViewModel(@NonNull Application application) {
        super(application);
        ((GifyApp)application).getAppComponent().inject(this);
    }


    LiveData<List<GifObject>> loadTrendingGifs() {
        return giphyService.loadTrendingGifs();
    }
}
