package com.amanarora.gify.trendinggifs;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

abstract class PaginationScrollListener extends RecyclerView.OnScrollListener{
    private static final String LOG_TAG = PaginationScrollListener.class.getSimpleName();
    private GridLayoutManager layoutManager;

    PaginationScrollListener(GridLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
        if (!isLoading() && !isLastResult()) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0) {
                loadMoreResults();
            }
        }
    }

    abstract int getTotalResultCount();

    abstract void loadMoreResults();

    abstract boolean isLastResult();

    abstract boolean isLoading();
}
