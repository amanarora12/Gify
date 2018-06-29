package com.amanarora.gify.injection;

import android.content.Context;

import com.amanarora.gify.BuildConfig;
import com.amanarora.gify.Constants;
import com.amanarora.gify.R;
import com.amanarora.gify.api.GiphyApiService;
import com.amanarora.gify.api.GiphyService;

import java.util.concurrent.ScheduledThreadPoolExecutor;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
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
    public OkHttpClient provideLoggingCapableClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        return new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
    }

    @Singleton
    @Provides
    Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
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
}
