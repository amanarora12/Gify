package com.amanarora.gify.trendinggifs;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.amanarora.gify.api.GiphyService;
import com.amanarora.gify.models.GiphyResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class TrendingViewModelTest {

    private TrendingViewModel trendingViewModel;

    private GiphyResponse giphyResponse;

    @Mock
    private MutableLiveData liveData;

    @Mock
    private GiphyService giphyService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        trendingViewModel = new TrendingViewModel(giphyService);
        giphyResponse = new GiphyResponse();
    }

    @Test
    public void loadTrendingGifsTest() {
        when(liveData.getValue()).thenReturn(giphyResponse);
        when(giphyService.getTrendingGifs(anyInt())).thenReturn(liveData);
        LiveData<GiphyResponse> responseLiveData =  trendingViewModel.loadTrendingGifs(anyInt());
        verify(giphyService).getTrendingGifs(anyInt());
        assertNotNull(responseLiveData);
        assertEquals(responseLiveData.getValue(), giphyResponse);
    }
}