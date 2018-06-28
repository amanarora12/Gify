package com.amanarora.gify.randomgifs;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.amanarora.gify.Constants;
import com.amanarora.gify.R;
import com.amanarora.gify.api.GiphyService;
import com.amanarora.gify.api.GlideService;
import com.amanarora.gify.databinding.ActivityGifsBinding;

import java.util.concurrent.ScheduledThreadPoolExecutor;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class GifsActivity extends AppCompatActivity {
    private static final String LOG_TAG = GifsActivity.class.getSimpleName();
    private ActivityGifsBinding binding;
    private GifsViewModel viewModel;

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
        String url = intent.getStringExtra(Constants.EXTRA_GIF_URL_KEY);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_gifs);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(GifsViewModel.class);

        if (url != null && !url.isEmpty()) {
            glideService.loadRandomGif(url).into(binding.randomGifImageView);
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
                    glideService.loadRandomGif(s).into(binding.randomGifImageView);
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
}
