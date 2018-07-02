package com.amanarora.gify.randomgifs;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.amanarora.gify.Constants;
import com.amanarora.gify.DataUtils;
import com.amanarora.gify.R;
import com.amanarora.gify.api.GlideService;
import com.amanarora.gify.databinding.ActivityGifsBinding;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;

import java.util.concurrent.ScheduledThreadPoolExecutor;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class GifsActivity extends AppCompatActivity implements ExoplayerManager.ExoplayerListener{
    private static final String LOG_TAG = GifsActivity.class.getSimpleName();
    private ActivityGifsBinding binding;
    private GifsViewModel viewModel;
    private String url;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    GlideService glideService;

    @Inject
    ExoplayerManager exoplayerManager;

    private ScheduledThreadPoolExecutor executor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        url = intent.getStringExtra(Constants.EXTRA_GIF_URL_KEY);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_gifs);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(GifsViewModel.class);
    }

    private void showErrorScreen() {
        binding.playerView.setVisibility(View.GONE);
        binding.errorScreen.errorLayout.setVisibility(View.VISIBLE);
        binding.errorScreen.errorBtnRetry.setVisibility(View.GONE);
    }


    @Override
    protected void onStart() {
        super.onStart();
        executor = new ScheduledThreadPoolExecutor(1);
        viewModel.loadRandomGifPeriodically(executor).observe(this, s -> {
            if (DataUtils.isNotNullOrEmpty(s)) {
                exoplayerManager.changeMediaSource(s);
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            exoplayerManager.initializeExoplayer(url);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            exoplayerManager.initializeExoplayer(url);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            exoplayerManager.releaseExoplayer();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (executor != null) {
            executor.shutdown();
            executor = null;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            exoplayerManager.releaseExoplayer();
        }
    }

    @Override
    public void onUrlError() {
        showErrorScreen();
    }

    @Override
    public void bindPlayer(ExoPlayer exoPlayer) {
        binding.playerView.setPlayer(exoPlayer);
    }

    @Override
    public void onPlayerStateChanged(int playBackState) {
        if (playBackState == Player.STATE_BUFFERING) {
            binding.playerView.setShowBuffering(true);
        } else {
            binding.playerView.setShowBuffering(false);
        }
    }
}
