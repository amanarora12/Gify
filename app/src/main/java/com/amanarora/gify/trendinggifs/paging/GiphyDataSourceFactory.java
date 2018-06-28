package com.amanarora.gify.trendinggifs.paging;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;

import javax.inject.Inject;

public class GiphyDataSourceFactory extends DataSource.Factory {
    private MutableLiveData<GiphyDataSource> sourceData = new MutableLiveData<>();
    private GiphyDataSource giphyDataSource;

    @Inject
    public GiphyDataSourceFactory(GiphyDataSource giphyDataSource) {
        this.giphyDataSource = giphyDataSource;
    }

    @Override
    public DataSource create() {
        sourceData.postValue(giphyDataSource);
        return giphyDataSource;
    }
}
