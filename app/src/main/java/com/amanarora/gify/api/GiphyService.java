package com.amanarora.gify.api;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.amanarora.gify.models.GifObject;
import com.amanarora.gify.models.GiphyResponse;
import com.amanarora.gify.models.RandomGiphyResponse;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GiphyService {
    private static final String LOG_TAG = GiphyService.class.getSimpleName();
    private GiphyApiService giphyApiService;

    @Inject
    public GiphyService(GiphyApiService giphyApiService) {
        this.giphyApiService = giphyApiService;
    }

    public LiveData<GiphyResponse> loadTrendingGifs(int offset, int limit) {
        final MutableLiveData<GiphyResponse> data = new MutableLiveData<>();
        giphyApiService.getTrendingGifs(offset, limit).enqueue(new Callback<GiphyResponse>() {
            @Override
            public void onResponse(Call<GiphyResponse> call, Response<GiphyResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.d(LOG_TAG, "Response successful."  );
                        data.postValue(response.body());
                    }
                } else {
                    Log.e(LOG_TAG, "Response not successful.");
                    data.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<GiphyResponse> call, Throwable t) {
                Log.e(LOG_TAG, "Request Failed. ", t );
                data.postValue(null);
            }
        });
        return data;
    }

    public LiveData<String> loadRandomGifPeriodically(ScheduledThreadPoolExecutor executor) {
        final MutableLiveData<String> data = new MutableLiveData<>();
        long period = 10;
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = Objects.requireNonNull(giphyApiService
                            .getRandomGif()
                            .execute()
                            .body())
                            .getData()
                            .getImages()
                            .getFixedHeight()
                            .getUrl();
                    data.postValue(url);
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Cannot retrieve gif. ", e );
                }
            }
        }, period, period, TimeUnit.SECONDS);
        return data;
    }
}
