package com.builderstrom.user.views.fragments;

import android.view.View;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.data.retrofit.modals.NotificationListModel;
import com.builderstrom.user.viewmodels.NotificationViewModel;
import com.builderstrom.user.views.activities.BaseActivity;
import com.builderstrom.user.views.adapters.NotificationAdaptor;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class NotificationFragment extends BaseRecyclerViewFragment {

    @BindView(R.id.tvNoDataFound)
    TextView tvNoDataFound;
    NotificationViewModel viewModel;
    List<NotificationListModel> notificationList = new ArrayList<>();
    private RecyclerView.Adapter adapter;

    //    @Override
//    protected void pagination() {
//        if (isInternetAvailable() && null != notificationList && notificationList.size() >= LIMIT) {
//            viewModel.getAllNotification(offset + LIMIT, LIMIT);
//        }
//    }
    @Override
    protected void pagination() {

        viewModel.getAllNotification(offset + LIMIT + 1, LIMIT);
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
        viewModel.getAllNotification(offset, LIMIT);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_notification;
    }

    @Override
    protected void bindView(View view) {
        viewModel = new ViewModelProvider(this).get(NotificationViewModel.class);
        adapter = new NotificationAdaptor(getActivity(), notificationList, notificationId -> viewModel.readNotification(notificationId));
        if (recyclerView != null) {
            recyclerView.setAdapter(adapter);
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

        viewModel.notificationLiveData.observe(getViewLifecycleOwner(), notificationListModels -> {
            if (notificationListModels != null) {
                if (offset <= 1) {
                    notificationList.clear();
                }
                tvNoDataFound.setVisibility(!notificationList.isEmpty() && getActivity() != null && isInternetAvailable() ? View.VISIBLE : View.GONE);
                notificationList.addAll(notificationListModels);
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                } else {
                    adapter = new NotificationAdaptor(getActivity(), notificationList, notificationId -> viewModel.readNotification(notificationId));
                }

                loading = !notificationListModels.isEmpty();
            }

        });

        viewModel.offsetLD.observe(getViewLifecycleOwner(), integer -> {
            if (null != integer) {
                offset = integer;
            }
        });

        viewModel.errorModelLD.observe(getViewLifecycleOwner(), errorModel -> {
            if (errorModel != null && getActivity() != null) {
                ((BaseActivity) getActivity()).handleErrorModel(errorModel);
            }
        });
    }
}
