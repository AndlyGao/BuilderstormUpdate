package com.builderstrom.user.views.fragments;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.builderstrom.user.R;

import butterknife.BindView;

public abstract class BaseRecyclerViewFragment extends BaseFragment {

    @Nullable @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout mSwipeRefreshLayout;
    @Nullable @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @Nullable @BindView(R.id.tvNoDataFound) TextView tvNoData;
    protected boolean loading = true;
    private int pastVisibleItems, visibleItemCount, totalItemCount;
    Integer LIMIT = 15;
    int offset = 0;

    @Override
    protected void init() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, android.R.color.holo_green_dark,
                    android.R.color.holo_orange_dark, android.R.color.holo_blue_dark);
            mSwipeRefreshLayout.setOnRefreshListener(this::pullDownToRefresh);
        }

        if (recyclerView != null) {
            if (getLayoutManager() != null) {
                recyclerView.setLayoutManager(getLayoutManager());
            }
            if (getRecyclerAdapter() != null) {
                recyclerView.setAdapter(getRecyclerAdapter());
            }

            if (recyclerView.getLayoutManager() != null && recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                LinearLayoutManager mLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (mLayoutManager != null) {
                    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                            super.onScrollStateChanged(recyclerView, newState);
                        }

                        @Override
                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);
                            if (dy > 0) {
                                visibleItemCount = mLayoutManager.getChildCount();
                                totalItemCount = mLayoutManager.getItemCount();
                                pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();
                                if (loading && (visibleItemCount + pastVisibleItems) >= totalItemCount) {
                                    loading = false;
                                    pagination();
                                }
                            }
                        }
                    });
                }
            }

            /* set further data*/
            setData();
        }

    }

    protected abstract void pagination();

    protected abstract RecyclerView.LayoutManager getLayoutManager();

    protected abstract RecyclerView.Adapter getRecyclerAdapter();

    protected abstract void setData();

    protected abstract void pullDownToRefresh();


    public void hideRefreshing() {
        if (null != mSwipeRefreshLayout && mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    public void showRefreshing() {
        if (null != mSwipeRefreshLayout && !mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(true);
        }
    }

    public boolean isNotRefreshing() {
        return mSwipeRefreshLayout != null && !mSwipeRefreshLayout.isRefreshing();
    }

    void showNoDataTextView(String noDataText) {
        if (null != getActivity() && null != tvNoData) {
            tvNoData.setVisibility(View.VISIBLE);
            tvNoData.setText(noDataText);
        }
    }

    void showNoDataTextView(@StringRes int noDataText) {
        if (null != getActivity() && null != tvNoData) {
            tvNoData.setVisibility(View.VISIBLE);
            tvNoData.setText(noDataText);
        }
    }

    void hideNoDataTextView() {
        if (null != getActivity() && null != tvNoData) {
            tvNoData.setVisibility(View.GONE);
        }
    }


}
