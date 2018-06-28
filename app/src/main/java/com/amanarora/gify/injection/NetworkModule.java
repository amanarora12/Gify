package com.amanarora.gify.injection;

import android.content.Context;

import com.amanarora.gify.Constants;
import com.amanarora.gify.R;
import com.amanarora.gify.api.GiphyApiService;
import com.amanarora.gify.api.GiphyService;
import com.amanarora.gify.trendinggifs.paging.GiphyDataSource;
import com.amanarora.gify.trendinggifs.paging.GiphyDataSourceFactory;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {

    @Provides
    @Named(Constants.Injection.Named.GIPHY_API_KEY)
    public String provideGiphyApiKey(Context context) {
         return context.getString(R.string.giphy_api_key);
    }


    @Singleton
    @Provides
    Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    GiphyApiService provideGiphyApiService(Retrofit retrofit) {
        return retrofit.create(GiphyApiService.class);
    }

    @Singleton
    @Provides
    GiphyService provideGiphyService(GiphyApiService giphyApiService) {
        return new GiphyService(giphyApiService);
    }

    @Provides
    GiphyDataSource provideGiphyDataSource(GiphyApiService giphyApiService) {
        return new GiphyDataSource(giphyApiService);
    }

    @Provides
    GiphyDataSourceFactory provideGiphyDataSourceFactory(GiphyDataSource giphyDataSource) {
        return new GiphyDataSourceFactory(giphyDataSource);
    }
}
