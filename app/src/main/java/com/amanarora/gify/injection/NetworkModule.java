package com.amanarora.gify.injection;

import android.content.Context;

import com.amanarora.gify.BuildConfig;
import com.amanarora.gify.Constants;
import com.amanarora.gify.NetworkStateProvider;
import com.amanarora.gify.R;
import com.amanarora.gify.api.GiphyApiService;
import com.amanarora.gify.api.GiphyService;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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

    @Provides
    public Cache provideCache(Context context) {
        return new Cache(context.getCacheDir(), 10*1024*1024);
    }

    @Provides
    @Named("cache")
    public Interceptor provideCacheInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());

                CacheControl cacheControl = new CacheControl.Builder()
                        .maxAge(2, TimeUnit.MINUTES)
                        .build();
                return response.newBuilder()
                        .header("Cache-Control", cacheControl.toString())
                        .build();
            }
        };
    }

    @Provides
    @Named("offline_cache")
    public Interceptor provideOfflineCacheInterceptor(Context context) {
        final boolean hasNetwork = new NetworkStateProvider(context).isNetworkConnected();
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!hasNetwork) {
                    CacheControl cacheControl = new CacheControl.Builder()
                            .maxStale(1, TimeUnit.DAYS)
                            .build();
                    request = request.newBuilder()
                            .cacheControl(cacheControl)
                            .build();
                }
                return chain.proceed(request);
            }
        };
    }

    @Provides
    public OkHttpClient provideOkHttpClient(Cache cache,
                                            @Named("cache") Interceptor interceptor,
                                            @Named("offline_cache") Interceptor offlineCacheInterceptor) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        return new OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(offlineCacheInterceptor)
                .addNetworkInterceptor(interceptor)
                .cache(cache)
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
