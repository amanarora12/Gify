package com.amanarora.gify.trendinggifs;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;

import com.amanarora.gify.Constants;
import com.amanarora.gify.R;
import com.amanarora.gify.api.GlideService;
import com.amanarora.gify.databinding.ActivityTrendingBinding;
import com.amanarora.gify.injection.viewmodelinjection.ViewModelFactory;
import com.amanarora.gify.models.GifObject;
import com.amanarora.gify.randomgifs.GifsActivity;

import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class TrendingActivity extends AppCompatActivity {
    private static final String LOG_TAG = TrendingActivity.class.getSimpleName();
    private ActivityTrendingBinding binding;
    private TrendingViewModel viewModel;
    private TrendingGifsAdapter adapter;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    GlideService glideService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_trending);
        setSupportActionBar(binding.toolbar);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(TrendingViewModel.class);

        setupTrendingGifsRecyclerView();
    }

    private void setupTrendingGifsRecyclerView() {
        binding.content.gifList.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new TrendingGifsAdapter(glideService, new TrendingGifsAdapter.Callback() {
            @Override
            public void onItemSelected(String url) {
                startActivity(randomGifActivityIntent(url));
            }
        });
        binding.content.gifList.setAdapter(adapter);
        populateRecyclerViewWithGifs();
    }

    private Intent randomGifActivityIntent(String url) {
        Intent intent = new Intent(this, GifsActivity.class);
        intent.putExtra(Constants.EXTRA_GIF_URL_KEY, url);
        return intent;
    }

    private void populateRecyclerViewWithGifs() {
        viewModel.loadTrendingGifs().observe(this, new Observer<List<GifObject>>() {
            @Override
            public void onChanged(@Nullable List<GifObject> gifObjects) {
                if (gifObjects != null && !gifObjects.isEmpty()) {
                    adapter.updateList(gifObjects);
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_trending, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
