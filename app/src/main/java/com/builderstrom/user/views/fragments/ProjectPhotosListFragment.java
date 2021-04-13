package com.builderstrom.user.views.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.data.retrofit.api.DataNames;
import com.builderstrom.user.data.retrofit.modals.GalleryData;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.viewmodels.ProjectPhotosVM;
import com.builderstrom.user.views.activities.AddGalleryActivity;
import com.builderstrom.user.views.activities.BaseActivity;
import com.builderstrom.user.views.adapters.GalleryOfflineAdapter;
import com.builderstrom.user.views.adapters.GalleryOnlineAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.builderstrom.user.views.fragments.HomeMenuFragment.userPermissions;

public class ProjectPhotosListFragment extends BaseRecyclerViewFragment {

    @BindView(R.id.tvDataSource) TextView tvDataSource;
    @BindView(R.id.btnAddGallery) Button btnAddGallery;
    private ProjectPhotosVM viewModel;
    private RecyclerView.Adapter mPhotosAdapter;
    private List<GalleryData> galleryList = new ArrayList<>();
    private Integer limit = 10;
    private BroadcastReceiver projectUpdateBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            if (null != getActivity()) {
                getActivity().runOnUiThread(() -> {
                    try {
                        if (intent.hasExtra("KEY_FLAG")) {
                            pullDownToRefresh();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_gallery;
    }

    @Override
    protected void bindView(View view) {
        viewModel = new ViewModelProvider(this).get(ProjectPhotosVM.class);
        observeViewModel();
        registerAllBroadcasts();
    }

    @OnClick(R.id.btnAddGallery)
    public void onClick() {
        startActivity(new Intent(getActivity(), AddGalleryActivity.class));
    }

    private void observeViewModel() {

        viewModel.isLoadingLD.observe(getViewLifecycleOwner(), loading -> {
            if (loading != null) {
                if (loading) showProgress();
                else dismissProgress();
            }
        });

        viewModel.isRefreshingLD.observe(getViewLifecycleOwner(), refreshing -> {
            if (refreshing != null) {
                if (refreshing) showRefreshing();
                else hideRefreshing();
            }
        });

        viewModel.offsetLD.observe(getViewLifecycleOwner(), apiOffset -> {
            if (apiOffset != null) {
                offset = apiOffset;
            }
        });

        viewModel.isOfflineAdapterLD.observe(getViewLifecycleOwner(), isOfflineAdapter -> {
            if (isOfflineAdapter != null) {
                galleryList.clear();
                if (isOfflineAdapter) {
                    mPhotosAdapter = new GalleryOfflineAdapter(getActivity(), galleryList, (rowID, title, picsList, position) ->
                            startActivity(new Intent(getActivity(), AddGalleryActivity.class)
                                    .putExtra("isEdit", true)
                                    .putExtra("data", new Gson().toJson(galleryList.get(position)))),
                            viewModel);

                } else {
                    mPhotosAdapter = new GalleryOnlineAdapter(getActivity(), galleryList, (galleryID, title, picsList, position) ->
                            startActivity(new Intent(getActivity(), AddGalleryActivity.class)
                                    .putExtra("isEdit", true)
                                    .putExtra("data", new Gson().toJson(galleryList.get(position)))),
                            viewModel);

                }
                if (recyclerView != null) {
                    recyclerView.setAdapter(mPhotosAdapter);
                }
            }
        });

        viewModel.allGalleriesListLD.observe(getViewLifecycleOwner(), photosDataModel -> {
            if (photosDataModel != null) {
                galleryList.addAll(photosDataModel.getDataList());
                mPhotosAdapter.notifyDataSetChanged();
                tvDataSource.setVisibility(!galleryList.isEmpty() && getActivity() != null && isInternetAvailable() && photosDataModel.isOffline() ? View.VISIBLE : View.GONE);
                if (galleryList.isEmpty()) {
                    showNoDataTextView(getString(photosDataModel.isOffline() ? (isInternetAvailable() ? R.string.no_data_online : R.string.no_data_found) : R.string.no_data_found));
                } else {
                    hideNoDataTextView();
                }


                loading = photosDataModel.getDataList().size() == limit;

                Log.e("loading", photosDataModel.getDataList().size() + "");
            }
        });

        viewModel.errorModelLD.observe(getViewLifecycleOwner(), errorModel -> {
            if (errorModel != null && getActivity() != null) {
                ((BaseActivity) getActivity()).handleErrorModel(errorModel);
            }
        });

        viewModel.deleteGallerySuccessLD.observe(getViewLifecycleOwner(), deletePosition -> {
            if (deletePosition != null) {
                if (!galleryList.isEmpty() && deletePosition < galleryList.size()) {
                    galleryList.remove((int) deletePosition);
                    mPhotosAdapter.notifyDataSetChanged();
                }
            }
        });

        viewModel.successDeleteGalleryImageLD.observe(getViewLifecycleOwner(), model -> {
            if (null != model) {
                if (model.getGroupPosition() < galleryList.size()) {
                    galleryList.get(model.getGroupPosition()).setPics(model.getPicList());
                    mPhotosAdapter.notifyDataSetChanged();
                }

            }
        });

    }

    @Override
    protected void pagination() {
        viewModel.callGalleryAPI(offset + limit + 1, limit);
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
        if (userPermissions != null) {
            CommonMethods.checkVisiblePermission(userPermissions.getPhotoGallery().getCreateGallery(), btnAddGallery);
        }
        pullDownToRefresh();
    }

    @Override
    protected void pullDownToRefresh() {
        offset = 0;
        viewModel.callGalleryAPI(offset, limit);
    }

    /* Broadcasts Section */

    @Override
    public void onDestroyView() {
        unregisterAllBroadcasts();
        super.onDestroyView();
    }

    private void registerAllBroadcasts() {
        if (getActivity() != null) {
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(projectUpdateBroadcast, new IntentFilter(DataNames.INTENT_ACTION_ALL_PROJECT_UPDATE));
        }
    }

    private void unregisterAllBroadcasts() {
        if (null != getActivity()) {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(projectUpdateBroadcast);
        }
    }


}
