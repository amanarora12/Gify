package com.amanarora.gify.api;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.amanarora.gify.models.GiphyResponse;
import com.amanarora.gify.models.RandomGiphyResponse;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.ScheduledThreadPoolExecutor;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GiphyServiceTest {

    private GiphyService giphyService;

    private GiphyResponse giphyResponse;

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private GiphyApiService giphyApiService;

    @Mock
    private Call<GiphyResponse> mockGiphyResponseCall;

    @Mock
    private ResponseBody responseBody;

    @Mock
    private Call<RandomGiphyResponse> mockRandomGiphyResponseCall;

    @Captor
    private ArgumentCaptor<Callback<GiphyResponse>> argumentCaptor;


    private LiveData<GiphyResponse> giphyResponseLiveData;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        giphyService = new GiphyService(giphyApiService);
        giphyResponseLiveData = new MutableLiveData<>();
        giphyResponse = new GiphyResponse();
    }

    @Test
    public void getTrendingGifsSuccessful() {
        int offset = 0;
        int limit = 25;
        when(giphyApiService.getTrendingGifs(offset, limit)).thenReturn(mockGiphyResponseCall);
        Response<GiphyResponse> response = Response.success(giphyResponse);


        giphyResponseLiveData = giphyService.getTrendingGifs(offset);

        verify(mockGiphyResponseCall).enqueue(argumentCaptor.capture());
        argumentCaptor.getValue().onResponse(mockGiphyResponseCall, response);

        assertEquals(giphyResponseLiveData.getValue(), response.body());
    }

    @Test
    public void getTrendingGifsRequestFailed() {
        int offset = 0;
        int limit = 25;
        when(giphyApiService.getTrendingGifs(offset, limit)).thenReturn(mockGiphyResponseCall);
        Response<GiphyResponse> response = Response.error(500, responseBody);

        giphyResponseLiveData = giphyService.getTrendingGifs(offset);

        verify(mockGiphyResponseCall).enqueue(argumentCaptor.capture());
        argumentCaptor.getValue().onResponse(mockGiphyResponseCall, response);

        assertNull(giphyResponseLiveData.getValue());
    }

    @Test
    public void testRandomGifRequested() {
        when(giphyApiService.getRandomGif()).thenReturn(mockRandomGiphyResponseCall);
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        giphyService.getRandomGif(executor);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        verify(giphyApiService, atLeastOnce()).getRandomGif();
    }
}