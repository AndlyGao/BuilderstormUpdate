package com.builderstrom.user.views.fragments;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.data.retrofit.modals.NotificationListModel;
import com.builderstrom.user.data.retrofit.modals.PojoMyItem;
import com.builderstrom.user.viewmodels.DigitalDocumentVM;
import com.builderstrom.user.views.activities.BaseActivity;
import com.builderstrom.user.views.adapters.DigitalDocMyItemsListAdapter;

import java.util.ArrayList;
import java.util.List;

public class ShortDocsFragment extends BaseRecyclerViewFragment {

    //    @BindView(R.id.tvDataSource)
//    TextView tvDataSource;
    DigitalDocumentVM viewModel;
    List<NotificationListModel> notificationList = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    private List<PojoMyItem> myItemsList = new ArrayList<>();
    private String categoryId = "0";
    private String catSection = "0";

    public static ShortDocsFragment newInstance(String categoryId, String catSection) {
        ShortDocsFragment fragment = new ShortDocsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("categoryId", categoryId);
        bundle.putString("catSection", catSection);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_short_docs;
    }


    @Override
    protected void bindView(View view) {
        if (getArguments() != null) {
            categoryId = getArguments().getString("categoryId");
            catSection = getArguments().getString("catSection");
        }

        viewModel = new ViewModelProvider(this).get(DigitalDocumentVM.class);
        mAdapter = new DigitalDocMyItemsListAdapter(getActivity(), myItemsList, viewModel, catSection, categoryId);
        if (recyclerView != null) {
            recyclerView.setAdapter(mAdapter);
        }
        observeViewModel();
    }

    private void observeViewModel() {

        viewModel.isRefreshingLD.observe(getViewLifecycleOwner(), refreshing -> {
            if (refreshing != null) {
                if (refreshing) showRefreshing();
                else hideRefreshing();
            }
        });

//        viewModel.notificationLiveData.observe(getViewLifecycleOwner(), notificationListModels -> {
//            if (notificationListModels != null) {
//                if (offset <= 1) {
//                    notificationList.clear();
//                }
//                tvNoDataFound.setVisibility(!notificationList.isEmpty() && getActivity() != null && isInternetAvailable() ? View.VISIBLE : View.GONE);
//                notificationList.addAll(notificationListModels);
//                if (adapter != null) {
//                    adapter.notifyDataSetChanged();
//                } else {
//                    adapter = new NotificationAdaptor(getActivity(), notificationList, notificationId -> viewModel.readNotification(notificationId));
//                }
//
//                loading = !notificationListModels.isEmpty();
//            }
//
//        });
//
        viewModel.offsetLD.observe(getViewLifecycleOwner(), integer -> {
            if (null != integer) {
                offset = integer;
            }
        });

        viewModel.myItemsLD.observe(getViewLifecycleOwner(), myItemModel -> {
            if (myItemModel != null) {
                if (offset == 0) {
                    myItemsList.clear();
                }
                myItemsList.addAll(myItemModel.getMyItems());
                ((DigitalDocMyItemsListAdapter) mAdapter).setOffline(myItemModel.isOfflineStatus());
                mAdapter.notifyDataSetChanged();
                if (myItemsList.isEmpty()) {
                    showNoDataTextView(getString(R.string.no_data_found));
                } else {
                    hideNoDataTextView();
                }
            }
        });

        viewModel.notifyAdapterLD.observe(getViewLifecycleOwner(), notifyAdapter -> {
            if (null != notifyAdapter && null != mAdapter) {
                mAdapter.notifyDataSetChanged();
            }
        });

        viewModel.errorModelLD.observe(getViewLifecycleOwner(), errorModel -> {
            if (errorModel != null && getActivity() != null) {
                ((BaseActivity) getActivity()).handleErrorModel(errorModel);
            }
        });
    }

    @Override
    protected void pagination() {
        if (isInternetAvailable() && null != myItemsList && myItemsList.size() >= LIMIT) {
            viewModel.getMyDocuments("", "", catSection, categoryId, offset + LIMIT, LIMIT);
        }
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return null;
    }

    @Override
    protected RecyclerView.Adapter getRecyclerAdapter() {
        return null;
    }

    @Override
    protected void setData() {
        pullDownToRefresh();
    }

    @Override
    protected void pullDownToRefresh() {
        offset = 0;
        viewModel.updateRecentDocsList(catSection, categoryId);/* changes regarding recent doc on 7/1/2020*/
        viewModel.getMyDocuments("", "", catSection, categoryId, offset, LIMIT);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
