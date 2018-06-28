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

    public LiveData<List<GifObject>> loadTrendingGifs() {
        final MutableLiveData<List<GifObject>> data = new MutableLiveData<>();
        giphyApiService.getTrendingGifs(25).enqueue(new Callback<GiphyResponse>() {
            @Override
            public void onResponse(Call<GiphyResponse> call, Response<GiphyResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        //TODO Transform Model
                        Log.d(LOG_TAG, "onResponse: success"  );
                        List<GifObject> objects = Objects.requireNonNull(response.body()).getData();
                        data.postValue(objects);
                    }
                }
            }

            @Override
            public void onFailure(Call<GiphyResponse> call, Throwable t) {
                Log.e(LOG_TAG, "Request Failed. ", t );
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
                    Log.d(LOG_TAG, "Executing every 10 seconds ");
                    String url = giphyApiService.getRandomGif().execute().body().getData().getImages().getFixedHeight().getUrl();
                    data.postValue(url);
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Cannot retrieve gif. ", e );
                }
            }
        }, period, period, TimeUnit.SECONDS);
        return data;
    }
}
