package com.amanarora.gify.api;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.amanarora.gify.Constants;
import com.amanarora.gify.models.GiphyResponse;
import com.amanarora.gify.models.repository.GifRepository;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GiphyService implements GifRepository{
    private static final String LOG_TAG = GiphyService.class.getSimpleName();
    private GiphyApiService giphyApiService;

    @Inject
    public GiphyService(GiphyApiService giphyApiService) {
        this.giphyApiService = giphyApiService;
    }

    @Override
    public LiveData<GiphyResponse> getTrendingGifs(int offset) {
        final MutableLiveData<GiphyResponse> data = new MutableLiveData<>();
        giphyApiService.getTrendingGifs(offset, Constants.LIMIT_PER_REQUEST).enqueue(new Callback<GiphyResponse>() {
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

    @Override
    public LiveData<String> getRandomGif(ScheduledThreadPoolExecutor executor) {
        final MutableLiveData<String> data = new MutableLiveData<>();
        long period = 10;
        executor.scheduleAtFixedRate(() -> {
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
        }, period, period, TimeUnit.SECONDS);
        return data;
    }
}
