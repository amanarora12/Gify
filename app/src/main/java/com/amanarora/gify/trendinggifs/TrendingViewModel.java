package com.amanarora.gify.trendinggifs;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.amanarora.gify.models.GiphyResponse;
import com.amanarora.gify.models.repository.GifRepository;

import javax.inject.Inject;

public class TrendingViewModel extends ViewModel {
    private GifRepository giphyService;

    @Inject
    TrendingViewModel(GifRepository giphyService) {
        this.giphyService = giphyService;
    }

    LiveData<GiphyResponse> loadTrendingGifs(int offset) {
        return giphyService.getTrendingGifs(offset);
    }
}
