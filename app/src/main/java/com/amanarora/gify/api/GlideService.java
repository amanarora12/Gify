package com.amanarora.gify.api;

import android.content.Context;

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

    @Inject
    GlideService(Context context) {
        this.context = context;
        height = context.getResources().getDimensionPixelOffset(R.dimen.gif_height);
        width = height;
    }

    public RequestBuilder<GifDrawable> loadGif(String url) {
        return Glide.with(context)
                .asGif()
                .apply(RequestOptions.noTransformation()
                .error(R.mipmap.ic_launcher)
                .fallback(R.mipmap.ic_launcher)
                .override(width,height)
                .diskCacheStrategy(DiskCacheStrategy.ALL))
                .load(url);
    }
}
