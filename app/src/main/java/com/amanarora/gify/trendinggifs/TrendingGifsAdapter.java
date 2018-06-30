package com.amanarora.gify.trendinggifs;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amanarora.gify.R;
import com.amanarora.gify.api.GlideService;
import com.amanarora.gify.databinding.TrendingGifListItemBinding;
import com.amanarora.gify.models.GifObject;

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

    void setTrendingGifsList(List<GifObject> trendingGifs) {
        this.trendingGifsList = trendingGifs;
        notifyDataSetChanged();
    }

    List<GifObject> getTrendingGifsList() {
        return trendingGifsList;
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
        GifObject gifObject = trendingGifsList.get(position);
        glideService.loadGif(gifObject.getImages().getPreviewGif().getUrl())
                .into(holder.binding.gifImageView);
    }

    @Override
    public int getItemCount() {
        return trendingGifsList == null? 0 : trendingGifsList.size();
    }

    @Override
    public void onViewRecycled(@NonNull GifViewHolder holder) {
        super.onViewRecycled(holder);
        glideService.clearImageView(holder.binding.gifImageView);
        holder.binding.gifImageView.setImageDrawable(null);
    }

    void addGif(GifObject gif) {
        trendingGifsList.add(gif);
        notifyItemInserted(trendingGifsList.size()-1);
    }

    void addAllGifs(List<GifObject> gifs) {
        trendingGifsList.addAll(gifs);
        notifyItemRangeInserted(trendingGifsList.size()-1, gifs.size());
    }

    void remove(GifObject gif) {
        int pos = trendingGifsList.indexOf(gif);
        if (pos > -1) {
            trendingGifsList.remove(gif);
            notifyItemRemoved(pos);
        }
    }

    void clear() {
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    boolean isEmpty() {
        return getItemCount() == 0;
    }

    GifObject getItem(int position) {
        return trendingGifsList.get(position);
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
