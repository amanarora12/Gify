package com.amanarora.gify.randomgifs;

import android.content.Context;
import android.net.Uri;

import com.amanarora.gify.DataUtils;
import com.amanarora.gify.R;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
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

class ExoplayerManager {

    private SimpleExoPlayer exoPlayer;
    private Context context;
    private String userAgent;
    private ExoplayerListener listener;

    ExoplayerManager(Context context, ExoplayerListener listener) {
        this.context = context;
        this.listener = listener;
        userAgent = Util.getUserAgent(context, context.getResources().getString(R.string.app_name));
    }

    void initializeExoplayer(String url) {
        if (DataUtils.isNullOrEmpty(url)) {
            listener.onUrlError();
            return;
        }
        DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(context);
        TrackSelector trackSelector = new DefaultTrackSelector();
        exoPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector);
        MediaSource mediaSource = new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory(userAgent))
                .createMediaSource(Uri.parse(url));
        exoPlayer.prepare(mediaSource);
        exoPlayer.setPlayWhenReady(true);
        exoPlayer.setRepeatMode(Player.REPEAT_MODE_ONE);
        listener.bindPlayer(exoPlayer);
        exoPlayer.addListener(new PlayerEventListener());
    }

    void changeMediaSource(String url){
        if (exoPlayer != null) {
            exoPlayer.stop();
            MediaSource mediaSource = new ExtractorMediaSource.Factory(
                    new DefaultHttpDataSourceFactory(userAgent))
                    .createMediaSource(Uri.parse(url));
            exoPlayer.prepare(mediaSource);
        } else {
            initializeExoplayer(url);
        }
    }

    void releaseExoplayer() {
        if (exoPlayer != null) {
            exoPlayer.release();
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
            listener.onPlayerStateChanged(playbackState);
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

    interface ExoplayerListener {
        void onUrlError();
        void bindPlayer(ExoPlayer exoPlayer);
        void onPlayerStateChanged(int playBackState);
    }
}
