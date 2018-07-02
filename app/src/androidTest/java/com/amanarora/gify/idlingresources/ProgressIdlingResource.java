package com.amanarora.gify.idlingresources;

import android.support.test.espresso.IdlingResource;

import com.amanarora.gify.trendinggifs.TrendingActivity;

public class ProgressIdlingResource implements IdlingResource{
    private ResourceCallback callback;
    private TrendingActivity trendingActivity;
    private TrendingActivity.ProgressListener progressListener;

    public ProgressIdlingResource(TrendingActivity trendingActivity) {
        this.trendingActivity = trendingActivity;
        progressListener = new TrendingActivity.ProgressListener() {
            @Override
            public void onProgressBarVisible() {

            }

            @Override
            public void onProgressBarGone() {
                if (callback == null) {
                    return;
                }
                callback.onTransitionToIdle();
            }
        };
        trendingActivity.setProgressListener(progressListener);
    }
    @Override
    public String getName() {
        return ProgressIdlingResource.class.getSimpleName();
    }

    @Override
    public boolean isIdleNow() {
        return !trendingActivity.isProgressBarVisible();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.callback = callback;
    }
}
