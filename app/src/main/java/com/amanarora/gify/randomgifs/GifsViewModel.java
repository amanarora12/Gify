package com.amanarora.gify.randomgifs;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.amanarora.gify.api.GiphyService;
import com.amanarora.gify.models.repository.GifRepository;

import java.util.concurrent.ScheduledThreadPoolExecutor;

import javax.inject.Inject;

public class GifsViewModel extends ViewModel {

    private GifRepository giphyService;

    @Inject
    GifsViewModel(GifRepository giphyService) {
        this.giphyService = giphyService;
    }

    LiveData<String> loadRandomGifPeriodically(ScheduledThreadPoolExecutor executor){
        return giphyService.getRandomGif(executor);
    }
}
