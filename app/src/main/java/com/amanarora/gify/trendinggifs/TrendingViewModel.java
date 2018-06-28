package com.amanarora.gify.trendinggifs;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.amanarora.gify.api.GiphyService;
import com.amanarora.gify.models.GifObject;
import com.amanarora.gify.trendinggifs.paging.GiphyDataSource;
import com.amanarora.gify.trendinggifs.paging.GiphyDataSourceFactory;

import java.util.List;

import javax.inject.Inject;

public class TrendingViewModel extends ViewModel {
    private GiphyService giphyService;
    private GiphyDataSourceFactory giphyDataSourceFactory;
    private LiveData<PagedList<GifObject>> gifs;

    @Inject
    public TrendingViewModel(GiphyService giphyService, GiphyDataSourceFactory dataSourceFactory) {
        this.giphyService = giphyService;
        this.giphyDataSourceFactory = dataSourceFactory;
    }

    void init() {
        PagedList.Config pagedListConfig = new PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setPageSize(25)
                .setInitialLoadSizeHint(10)
                .setPrefetchDistance(25)
                .build();
        gifs = new LivePagedListBuilder(giphyDataSourceFactory, pagedListConfig).build();
    }

   /* LiveData<List<GifObject>> loadTrendingGifs() {
        return new LivePagedListBuilder(giphyDataSourceFactory, );
    }*/

    public LiveData<PagedList<GifObject>> getGifs() {
        return gifs;
    }
}
