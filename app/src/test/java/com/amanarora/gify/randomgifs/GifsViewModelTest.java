package com.amanarora.gify.randomgifs;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.MutableLiveData;

import com.amanarora.gify.api.GiphyService;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.ScheduledThreadPoolExecutor;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GifsViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private GifsViewModel gifsViewModel;

    @Mock
    private GiphyService mockGiphyService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        gifsViewModel = new GifsViewModel(mockGiphyService);
    }

    @Test
    public void testLoadRandomGif() {
        MutableLiveData<String> liveData = new MutableLiveData<>();
        liveData.postValue("TestString");
        when(mockGiphyService.getRandomGif(new ScheduledThreadPoolExecutor(1))).thenReturn(liveData);
        gifsViewModel.loadRandomGifPeriodically(new ScheduledThreadPoolExecutor(1));
        verify(mockGiphyService).getRandomGif(any());
    }
}