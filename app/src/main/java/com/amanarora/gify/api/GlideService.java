package com.amanarora.gify.api;

import android.content.Context;
import android.widget.ImageView;

import com.amanarora.gify.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestOptions;

import javax.inject.Inject;

public class GlideService {
    private Context context;
    private final int height;
    private final int width;
    private final int randomGifHeight;
    private final int randomGifWidth;

    @Inject
    GlideService(Context context) {
        this.context = context;
        height = context.getResources().getDimensionPixelOffset(R.dimen.gif_height);
        width = height;
        randomGifHeight = context.getResources().getDimensionPixelOffset(R.dimen.random_gif_height);
        randomGifWidth = randomGifHeight;
    }

    public RequestBuilder<GifDrawable> loadGif(String url) {
        return Glide.with(context)
                .asGif()
                .apply(RequestOptions.noTransformation()
                .error(R.mipmap.ic_launcher)
                .fallback(R.mipmap.ic_launcher)
                .override(width,height)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                .load(url);
    }

    public RequestBuilder<GifDrawable> loadRandomGif(String url) {
        return Glide.with(context)
                .asGif()
                .apply(RequestOptions.noTransformation()
                        .error(R.mipmap.ic_launcher)
                        .fallback(R.mipmap.ic_launcher)
                        .override(randomGifWidth,randomGifHeight)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                .load(url);
    }

    public void clearImageView(ImageView imageView) {
        Glide.with(context).clear(imageView);
    }
}
