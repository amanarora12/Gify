package com.amanarora.gify.randomgifs;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.amanarora.gify.Constants;
import com.amanarora.gify.DataUtils;
import com.amanarora.gify.R;
import com.amanarora.gify.api.GlideService;
import com.amanarora.gify.databinding.ActivityGifsBinding;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.concurrent.ScheduledThreadPoolExecutor;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class GifsActivity extends AppCompatActivity {
    private static final String LOG_TAG = GifsActivity.class.getSimpleName();
    private ActivityGifsBinding binding;
    private GifsViewModel viewModel;
    private SimpleExoPlayer exoPlayer;
    private String url;
    private String userAgent;

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
    }

    private void initializeExoplayer(String url) {
        DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(this);
        TrackSelector trackSelector = new DefaultTrackSelector();
        exoPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector);
        userAgent = Util.getUserAgent(this, getResources().getString(R.string.app_name));
        MediaSource mediaSource = new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory(userAgent))
                .createMediaSource(Uri.parse(url));
        exoPlayer.prepare(mediaSource);
        exoPlayer.setPlayWhenReady(true);
        exoPlayer.setRepeatMode(Player.REPEAT_MODE_ONE);
        binding.playerView.setPlayer(exoPlayer);
        exoPlayer.addListener(new PlayerEventListener());
    }

    private void releaseExoplayer() {
        if (exoPlayer != null) {
            exoPlayer.release();
        }
    }

    private void changeMediaSource(String url){
        if (exoPlayer != null) {
            exoPlayer.stop();
            MediaSource mediaSource = new ExtractorMediaSource.Factory(
                    new DefaultHttpDataSourceFactory(userAgent))
                    .createMediaSource(Uri.parse(url));
            exoPlayer.prepare(mediaSource);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        executor = new ScheduledThreadPoolExecutor(1);
        viewModel.loadRandomGifPeriodically(executor).observe(this, s -> {
            if (DataUtils.isNotNullOrEmpty(s)) {
                changeMediaSource(s);
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            initializeExoplayer(url);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            initializeExoplayer(url);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            releaseExoplayer();
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
            releaseExoplayer();
        }
    }

    private class PlayerEventListener implements Player.EventListener {

        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

        }

        @Override
        public void onLoadingChanged(boolean isLoading) {

        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            if (playbackState == Player.STATE_BUFFERING) {
                binding.playerView.setShowBuffering(true);
            } else {
                binding.playerView.setShowBuffering(false);
            }
        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {

        }

        @Override
        public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {

        }

        @Override
        public void onPositionDiscontinuity(int reason) {

        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

        }

        @Override
        public void onSeekProcessed() {

        }
    }
}
