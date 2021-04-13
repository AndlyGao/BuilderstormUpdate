package com.builderstrom.user.views.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.ListPopupWindow;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.data.retrofit.api.DataNames;
import com.builderstrom.user.data.retrofit.modals.CatListing;
import com.builderstrom.user.data.retrofit.modals.DigitalDoc;
import com.builderstrom.user.data.retrofit.modals.PojoMyItem;
import com.builderstrom.user.data.retrofit.modals.User;
import com.builderstrom.user.viewmodels.DigitalDocumentVM;
import com.builderstrom.user.views.activities.BaseActivity;
import com.builderstrom.user.views.adapters.DigitalDocMyItemsListAdapter;
import com.builderstrom.user.views.adapters.DigitalDocumentListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class DigitalDocumentListFragment extends BaseRecyclerViewFragment {

    @BindView(R.id.tvViewType)
    TextView tvViewType;
    @BindView(R.id.tvDataSource)
    TextView tvDataSource;
    @BindView(R.id.etCategory)
    EditText etCategory;
    @BindView(R.id.etUsers)
    EditText etUsers;
    @BindView(R.id.viewDocTemplate)
    View viewDocTemplate;
    @BindView(R.id.llDocHeader)
    View llDocHeader;
    @BindView(R.id.llMyItemHeader)
    View llMyItemHeader;

    private RecyclerView.Adapter mAdapter;
    private List<DigitalDoc> documentList = new ArrayList<>();
    private List<CatListing> categories = new ArrayList<>();
    private List<User> userList = new ArrayList<>();
    private List<PojoMyItem> myItemsList = new ArrayList<>();
    private DigitalDocumentVM viewModel;
    private String categoryId = "0";
    private String issuedBy = "0";
    private boolean isMyItemsSelected = false;
    private CatListing catModel;
    private User userModel;
    /* BroadCasts Sections */
    private BroadcastReceiver projectUpdateBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context mContext, final Intent intent) {
            requireActivity().runOnUiThread(() -> {
                try {
                    if (intent.hasExtra("KEY_FLAG")) {
                        pullDownToRefresh();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_document;
    }

    @Override
    protected void bindView(View view) {
        viewModel = new ViewModelProvider(this).get(DigitalDocumentVM.class);
        observeViewModel();
        registerAllBroadcasts();
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

        viewModel.categoriesLD.observe(getViewLifecycleOwner(), catListings -> {
            if (null != catListings) {
                categories.clear();
                categories.addAll(catListings);
                catModel = categories.get(0);
                callCategoryListing();
            }
        });

        viewModel.usersLD.observe(getViewLifecycleOwner(), usersListings -> {

            userList.clear();
            if (null != usersListings && !usersListings.isEmpty()) {
                userList.addAll(usersListings);
                userModel = userList.get(0);
            }
        });

        viewModel.isDocumentAdapterLD.observe(getViewLifecycleOwner(), adapterLd -> {
            if (adapterLd != null) {
                myItemsList.clear();
                documentList.clear();
                if (adapterLd) {
                    mAdapter = new DigitalDocumentListAdapter(getActivity(), documentList);
                    ((DigitalDocumentListAdapter) mAdapter).setSyncDocument((templateId, position) -> {
                        DigitalDoc doc = documentList.get(position);
                        doc.setSynced(true);
                        viewModel.syncDocument(doc.getCategory() == null || doc.getCategory().isEmpty() ? "0" : doc.getCategory(), templateId,
                                doc.getTemplateTitle(), doc.getRecurrence_type(), "");
                    });
                } else {
                    mAdapter = new DigitalDocMyItemsListAdapter(getActivity(), myItemsList, viewModel);
                }
                if (recyclerView != null) {
                    recyclerView.setAdapter(mAdapter);
                }
            }
        });

        viewModel.docsLD.observe(getViewLifecycleOwner(), model -> {
            if (model != null) {
                if (!isMyItemsSelected) {
                    documentList.clear();
                    documentList.addAll(model.getDocList());
                    ((DigitalDocumentListAdapter) mAdapter).setOffline(model.isOffline());
                    mAdapter.notifyDataSetChanged();
                    tvDataSource.setVisibility(!model.getDocList().isEmpty() && getActivity() != null
                            && isInternetAvailable() && model.isOffline() ? View.VISIBLE : View.GONE);
                    if (documentList.isEmpty()) {
                        showNoDataTextView(model.isOffline() ? (isInternetAvailable() ?
                                R.string.no_data_online : R.string.no_data_found)
                                : R.string.no_data_found);
                    } else {
                        hideNoDataTextView();
                    }
                }
            }
        });

        viewModel.myItemsLD.observe(getViewLifecycleOwner(), myItemModel -> {
            if (myItemModel != null) {
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
        /* change if needed */
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
        setSelectedView();
    }

    private void setSelectedView() {
        if (isNotRefreshing()) {
            tvViewType.setSelected(isMyItemsSelected);
            tvViewType.setText(getString(isMyItemsSelected ? R.string.back_to_doc : R.string.my_items));

            tvViewType.setCompoundDrawablesWithIntrinsicBounds(
                    isMyItemsSelected ? R.drawable.ic_back_white : R.drawable.ic_book, 0, 0, 0);
            llDocHeader.setVisibility(isMyItemsSelected ? View.GONE : View.VISIBLE);
            llMyItemHeader.setVisibility(isMyItemsSelected ? View.VISIBLE : View.GONE);
            if (mAdapter != null) {
                myItemsList.clear();
                documentList.clear();
                mAdapter.notifyDataSetChanged();
            }
        }
        pullDownToRefresh();
    }

    @Override
    protected void pullDownToRefresh() {
        if (isAdded()) {
            viewDocTemplate.setVisibility(isInternetAvailable() ? View.VISIBLE : View.GONE);
            etUsers.setVisibility(isMyItemsSelected ? View.VISIBLE : View.GONE);

            viewModel.updateRowIdList();

            if (userList.isEmpty()) viewModel.getDocumentUsers();

            if (isMyItemsSelected) {
                viewModel.getMyDocuments(categoryId, issuedBy, null, null, offset, LIMIT);

            } else {
                if (categories.isEmpty()) viewModel.getDocumentCategories();
                else viewModel.getAllDigitalDocs(categoryId);
            }
        }
    }

    @OnClick({R.id.tvViewType, R.id.etCategory, R.id.etUsers})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.tvViewType:
                if (isNotRefreshing()) {
                    isMyItemsSelected = !isMyItemsSelected;
                    setSelectedView();
                }

                break;
            case R.id.etCategory:
                showCategoryDropdown();

                break;
            case R.id.etUsers:
                showUsersDropdown();
                break;
        }
    }

    private void showCategoryDropdown() {
        if (getActivity() != null && !categories.isEmpty()) {
            ListPopupWindow listPopupWindow = new ListPopupWindow(getActivity());
            listPopupWindow.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.row_dropdown, R.id.tvDropDown, categories));
            listPopupWindow.setAnchorView(etCategory);
            listPopupWindow.setModal(true);
            listPopupWindow.setWidth(ListPopupWindow.WRAP_CONTENT);
            listPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
            listPopupWindow.setOnItemClickListener((parent, view, position, id) -> {
                catModel = categories.get(position);
                callCategoryListing();
                listPopupWindow.dismiss();
            });
            listPopupWindow.show();
        }
    }

    private void showUsersDropdown() {
        if (getActivity() != null && !userList.isEmpty()) {
            ListPopupWindow listPopupWindow = new ListPopupWindow(getActivity());
            listPopupWindow.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.row_dropdown, R.id.tvDropDown, userList));
            listPopupWindow.setAnchorView(etUsers);
            listPopupWindow.setModal(true);
            listPopupWindow.setWidth(ListPopupWindow.WRAP_CONTENT);
            listPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
            listPopupWindow.setOnItemClickListener((parent, view, position, id) -> {
                userModel = userList.get(position);
                callCategoryListing();
                listPopupWindow.dismiss();
            });
            listPopupWindow.show();
        }
    }

    private void callCategoryListing() {
        etCategory.setVisibility(View.VISIBLE);
        if (catModel != null) {
            etCategory.setText(catModel.getTitle());
            categoryId = catModel.getId();
        }
        if (null != userModel) {
            etUsers.setText(userModel.getName());
            issuedBy = userModel.getUserId();
        }

        pullDownToRefresh();
    }

    private void registerAllBroadcasts() {
        if (null != getActivity()) {
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(projectUpdateBroadcast, new IntentFilter(DataNames.INTENT_ACTION_ALL_PROJECT_UPDATE));
        }
    }

    private void unregisterAllBroadcasts() {
        if (null != getActivity()) {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(projectUpdateBroadcast);
        }
    }

    @Override
    public void onDestroyView() {
        unregisterAllBroadcasts();
        super.onDestroyView();
    }

}
