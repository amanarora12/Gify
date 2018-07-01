package com.amanarora.gify.trendinggifs;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amanarora.gify.R;
import com.amanarora.gify.api.GlideService;
import com.amanarora.gify.databinding.TrendingGifListItemBinding;
import com.amanarora.gify.models.GifObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

public class TrendingGifsAdapter extends RecyclerView.Adapter<TrendingGifsAdapter.GifViewHolder> {
    private static final String LOG_TAG = TrendingGifsAdapter.class.getSimpleName();
    private List<GifObject> trendingGifsList = new ArrayList<>();
    private GlideService glideService;
    private Callback callback;

    public interface Callback {
        void onItemSelected(String url);
    }

    TrendingGifsAdapter(GlideService glideService, Callback callback) {
        this.glideService = glideService;
        this.callback = callback;
    }

    @Override
    public GifViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.trending_gif_list_item,
                parent,
                false
        );
        return new GifViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull GifViewHolder holder, int position) {
        GifObject gifObject = getItem(position);
        glideService.loadGif(gifObject.getImages().getPreviewGif().getUrl())
                .listener(new RequestListener<GifDrawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                Target<GifDrawable> target,
                                                boolean isFirstResource) {
                        holder.binding.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GifDrawable resource, Object model,
                                                   Target<GifDrawable> target, DataSource dataSource,
                                                   boolean isFirstResource) {
                        holder.binding.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.binding.gifImageView);
    }

    @Override
    public int getItemCount() {
        return trendingGifsList == null? 0 : trendingGifsList.size();
    }

    @Override
    public void onViewRecycled(@NonNull GifViewHolder holder) {
        super.onViewRecycled(holder);
        Glide.with(holder.binding.getRoot().getContext()).clear(holder.binding.gifImageView);
        holder.binding.gifImageView.setImageDrawable(null);
        holder.binding.progressBar.setVisibility(View.VISIBLE);
    }

    void addAllGifs(List<GifObject> gifs) {
        trendingGifsList.addAll(gifs);
        notifyItemRangeInserted(trendingGifsList.size()-1, gifs.size());
    }

    GifObject getItem(int position) {
        return trendingGifsList.get(position);
    }


    class GifViewHolder extends RecyclerView.ViewHolder {
        TrendingGifListItemBinding binding;

        GifViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            if (binding != null) {
                binding.getRoot().setOnClickListener(view -> {
                    String gifUrl = trendingGifsList.get(getAdapterPosition()).getImages().getFixedHeight().getMp4();
                    callback.onItemSelected(gifUrl);
                });
            }
        }

    }
}
