package com.amanarora.gify.api;

import com.amanarora.gify.models.GiphyResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GiphyApiService {

    @GET("/v1/gifs/trending?api_key=dc6zaTOxFJmzC")
    Call<GiphyResponse> getTrendingGifs(@Query("offset") int offset, @Query("limit") int limit);

    @GET("/v1/gifs/random?api_key=dc6zaTOxFJmzC")
    Call<GiphyResponse> getRandomGif();

}
