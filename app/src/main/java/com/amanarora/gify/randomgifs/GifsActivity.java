package com.amanarora.gify.randomgifs;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;

import com.amanarora.gify.Constants;
import com.amanarora.gify.R;
import com.amanarora.gify.api.GlideService;
import com.amanarora.gify.databinding.ActivityGifsBinding;

import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class GifsActivity extends AppCompatActivity implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener{
    private static final String LOG_TAG = GifsActivity.class.getSimpleName();
    private ActivityGifsBinding binding;
    private GifsViewModel viewModel;
    private MediaPlayer mediaPlayer;
    private SurfaceHolder surfaceHolder;
    private String url;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    GlideService glideService;

    private ScheduledThreadPoolExecutor executor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        url = intent.getStringExtra(Constants.EXTRA_GIF_URL_KEY);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_gifs);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(GifsViewModel.class);

        mediaPlayer = new MediaPlayer();
        surfaceHolder = binding.gifSurfaceView.getHolder();
        surfaceHolder.addCallback(this);

        if (url != null && !url.isEmpty()) {
            //glideService.loadRandomGif(url).into(binding.randomGifImageView);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        executor = new ScheduledThreadPoolExecutor(1);
        viewModel.loadRandomGifPeriodically(executor).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if (s != null && !s.isEmpty()) {
                   // glideService.loadRandomGif(s).into(binding.randomGifImageView);
                    try {
                        mediaPlayer.reset();
                        mediaPlayer.setDataSource(s);
                        mediaPlayer.prepareAsync();
                        mediaPlayer.setOnPreparedListener(GifsActivity.this);
                        mediaPlayer.setLooping(true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (executor != null) {
            executor.shutdown();
            executor = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer();
    }

    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mediaPlayer.setDisplay(surfaceHolder);
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(GifsActivity.this);
            mediaPlayer.setLooping(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }
}
