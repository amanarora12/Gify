package com.amanarora.gify.trendinggifs;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amanarora.gify.R;
import com.amanarora.gify.api.GlideService;
import com.amanarora.gify.databinding.TrendingGifListItemBinding;
import com.amanarora.gify.models.GifObject;

import java.util.ArrayList;
import java.util.List;

public class TrendingGifsAdapter extends RecyclerView.Adapter<TrendingGifsAdapter.GifViewHolder>{
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

    void updateList(List<GifObject> trendingGifs) {
        if (trendingGifs != null && !trendingGifs.isEmpty()) {
            trendingGifsList.addAll(trendingGifs);
        }
        notifyDataSetChanged();
    }

    @NonNull
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
        GifObject gifObject = trendingGifsList.get(position);
        glideService.loadGif(gifObject.getImages().getPreviewGif().getUrl())
                .into(holder.binding.gifImageView);
    }

    @Override
    public int getItemCount() {
        return trendingGifsList.size();
    }

    @Override
    public void onViewRecycled(@NonNull GifViewHolder holder) {
        super.onViewRecycled(holder);
        glideService.clearImageView(holder.binding.gifImageView);
        holder.binding.gifImageView.setImageDrawable(null);
    }

    class GifViewHolder extends RecyclerView.ViewHolder {
        TrendingGifListItemBinding binding;
        GifViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String gifUrl = trendingGifsList.get(getAdapterPosition()).getImages().getFixedHeight().getMp4();
                    callback.onItemSelected(gifUrl);
                }
            });
        }

    }
}
