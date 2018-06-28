package com.amanarora.gify.trendinggifs.paging;

import android.arch.paging.PositionalDataSource;
import android.support.annotation.NonNull;
import android.util.Log;

import com.amanarora.gify.api.GiphyApiService;
import com.amanarora.gify.models.GifObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

public class GiphyDataSource extends PositionalDataSource<GifObject> {
    private String LOG_TAG = GiphyDataSource.class.getSimpleName();
    private GiphyApiService giphyApiService;

    @Inject
    public GiphyDataSource(GiphyApiService giphyApiService) {
        this.giphyApiService = giphyApiService;
    }

    private List<GifObject> loadRangeInternal(int startPosition, int loadCount) {
        List<GifObject> gifObjects = new ArrayList<>();
        Log.e(LOG_TAG, "loadRangeInternal: " + startPosition + " " + loadCount );
        try {
            gifObjects = Objects.requireNonNull(giphyApiService
                    .getTrendingGifs(startPosition, loadCount)
                    .execute()
                    .body())
                    .getData();
        } catch (IOException e) {
            Log.e(LOG_TAG, "loadRangeInternal: ",e );
        }
        return gifObjects;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<GifObject> callback) {
        int totalCount = 1000;
        int position = computeInitialLoadPosition(params, totalCount);
        int loadCount = computeInitialLoadSize(params, position, totalCount);
        Log.e(LOG_TAG, "loadInitial: " + totalCount + " " + position + " " + loadCount );
        callback.onResult(loadRangeInternal(position, loadCount),position, totalCount);
    }

    @Override
    public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<GifObject> callback) {
        Log.e(LOG_TAG, "loadInitial: " + params.startPosition + " " + params.loadSize );
        callback.onResult(loadRangeInternal(params.startPosition, params.loadSize));
    }
}
