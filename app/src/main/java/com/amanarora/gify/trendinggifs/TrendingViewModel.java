package com.amanarora.gify.trendinggifs;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.amanarora.gify.api.GiphyService;
import com.amanarora.gify.models.GifObject;

import java.util.List;

import javax.inject.Inject;

public class TrendingViewModel extends ViewModel {
    private GiphyService giphyService;

    @Inject
    TrendingViewModel(GiphyService giphyService) {
        this.giphyService = giphyService;
    }

    LiveData<List<GifObject>> loadTrendingGifs(int offset, int limit) {
        return giphyService.loadTrendingGifs(offset, limit);
    }
}
