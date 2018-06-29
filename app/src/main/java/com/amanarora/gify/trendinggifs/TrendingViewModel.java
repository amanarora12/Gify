package com.amanarora.gify.trendinggifs;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.amanarora.gify.api.GiphyService;
import com.amanarora.gify.models.GiphyResponse;

import javax.inject.Inject;

public class TrendingViewModel extends ViewModel {
    private GiphyService giphyService;

    @Inject
    TrendingViewModel(GiphyService giphyService) {
        this.giphyService = giphyService;
    }

    LiveData<GiphyResponse> loadTrendingGifs(int offset) {
        return giphyService.getTrendingGifs(offset);
    }
}
