
package com.amanarora.gify.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Images {

    @SerializedName("fixed_height_still")
    @Expose
    private FixedHeightStill fixedHeightStill;
    @SerializedName("preview")
    @Expose
    private Preview preview;
    @SerializedName("fixed_height")
    @Expose
    private FixedHeight fixedHeight;
    @SerializedName("looping")
    @Expose
    private Looping looping;
    @SerializedName("preview_gif")
    @Expose
    private PreviewGif previewGif;

    public FixedHeightStill getFixedHeightStill() {
        return fixedHeightStill;
    }

    public void setFixedHeightStill(FixedHeightStill fixedHeightStill) {
        this.fixedHeightStill = fixedHeightStill;
    }

    public Preview getPreview() {
        return preview;
    }

    public void setPreview(Preview preview) {
        this.preview = preview;
    }

    public FixedHeight getFixedHeight() {
        return fixedHeight;
    }

    public void setFixedHeight(FixedHeight fixedHeight) {
        this.fixedHeight = fixedHeight;
    }

    public Looping getLooping() {
        return looping;
    }

    public void setLooping(Looping looping) {
        this.looping = looping;
    }

    public PreviewGif getPreviewGif() {
        return previewGif;
    }

    public void setPreviewGif(PreviewGif previewGif) {
        this.previewGif = previewGif;
    }

}
