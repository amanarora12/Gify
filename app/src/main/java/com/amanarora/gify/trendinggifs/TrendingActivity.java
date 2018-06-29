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
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.amanarora.gify.Constants;
import com.amanarora.gify.R;
import com.amanarora.gify.api.GlideService;
import com.amanarora.gify.databinding.ActivityTrendingBinding;
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
    public static final int LIMIT_PER_REQUEST = 25;
    private boolean isLoading = false;
    private boolean isLastResult = false;
    private int totalResults = 100;
    private int currentOffset = 0;

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
        if (binding.content == null) {
            return;
        }
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        RecyclerView recyclerView = binding.content.gifList;
        recyclerView.setLayoutManager(manager);
        adapter = new TrendingGifsAdapter(glideService, new TrendingGifsAdapter.Callback() {
            @Override
            public void onItemSelected(String url) {
                startActivity(randomGifActivityIntent(url));
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(LIMIT_PER_REQUEST);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.addOnScrollListener(new PaginationScrollListener(manager) {
            @Override
            int getTotalResultCount() {
                return totalResults;
            }

            @Override
            void loadMoreResults() {
                isLoading = true;
                currentOffset += LIMIT_PER_REQUEST;
                populateRecyclerViewWithGifs(currentOffset, LIMIT_PER_REQUEST);
            }

            @Override
            boolean isLastResult() {
                return isLastResult;
            }

            @Override
            boolean isLoading() {
                return isLoading;
            }
        });
        loadInitialResults();
        binding.content.progressBar.setVisibility(View.GONE);
    }

    private void loadInitialResults() {
        populateRecyclerViewWithGifs(currentOffset, LIMIT_PER_REQUEST);
    }

    private Intent randomGifActivityIntent(String url) {
        Intent intent = new Intent(this, GifsActivity.class);
        intent.putExtra(Constants.EXTRA_GIF_URL_KEY, url);
        return intent;
    }

    private void populateRecyclerViewWithGifs(final int offset, final int limit) {
        viewModel.loadTrendingGifs(offset, limit).observe(this, new Observer<List<GifObject>>() {
            @Override
            public void onChanged(@Nullable List<GifObject> gifObjects) {
                if (gifObjects != null && !gifObjects.isEmpty()) {
                    adapter.addAllGifs(gifObjects);
                }
                isLoading = false;
                if ((offset + limit) > totalResults) {
                    isLastResult = true;
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
