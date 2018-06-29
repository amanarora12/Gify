package com.amanarora.gify.models.repository;

import android.arch.lifecycle.LiveData;

import com.amanarora.gify.models.GiphyResponse;

import java.util.concurrent.ScheduledThreadPoolExecutor;

public interface GifRepository {
    LiveData<GiphyResponse> getTrendingGifs(int offset);

    LiveData<String> getRandomGif(ScheduledThreadPoolExecutor executor);
}
