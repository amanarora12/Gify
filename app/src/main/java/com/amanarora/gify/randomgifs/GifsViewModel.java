package com.amanarora.gify.randomgifs;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.amanarora.gify.api.GiphyService;

import java.util.concurrent.ScheduledThreadPoolExecutor;

import javax.inject.Inject;

public class GifsViewModel extends ViewModel {

    private GiphyService giphyService;

    @Inject
    GifsViewModel(GiphyService giphyService) {
        this.giphyService = giphyService;
    }

    LiveData<String> loadRandomGifPeriodically(ScheduledThreadPoolExecutor executor){
        return giphyService.getRandomGif(executor);
    }
}
