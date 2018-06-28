package com.amanarora.gify.api;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.amanarora.gify.models.GifObject;
import com.amanarora.gify.models.GiphyResponse;

import java.util.List;
import java.util.Objects;

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
        giphyApiService.getTrendingGifs(0, 25).enqueue(new Callback<GiphyResponse>() {
            @Override
            public void onResponse(Call<GiphyResponse> call, Response<GiphyResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        //TODO Transform Model
                        Log.d(LOG_TAG, "onResponse: success"  );
                        List<GifObject> objects = Objects.requireNonNull(response.body()).getData();
                        data.setValue(objects);
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
}
