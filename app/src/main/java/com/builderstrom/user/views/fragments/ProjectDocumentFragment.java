package com.builderstrom.user.views.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Parcelable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.builderstrom.user.R;
import com.builderstrom.user.repository.retrofit.api.DataNames;
import com.builderstrom.user.repository.retrofit.modals.CatListing;
import com.builderstrom.user.repository.retrofit.modals.DocumentStatus;
import com.builderstrom.user.repository.retrofit.modals.PDocsDataModel;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.viewmodels.ProjectDocumentVM;
import com.builderstrom.user.views.activities.AddDocumentActivity;
import com.builderstrom.user.views.activities.BaseActivity;
import com.builderstrom.user.views.activities.DigitalFormActivity;
import com.builderstrom.user.views.adapters.ProjectDocumentAdapter;
import com.builderstrom.user.views.viewInterfaces.EditSuccessCallback;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.builderstrom.user.views.fragments.HomeMenuFragment.userPermissions;


public class ProjectDocumentFragment extends BaseFragment implements EditSuccessCallback {

    @BindView(R.id.etDocuments)
    EditText etDocuments;
    @BindView(R.id.btnAddDocument)
    Button btnAddDocument;
    @BindView(R.id.etCategory)
    EditText etCategory;
    @BindView(R.id.tvNoDataFound)
    TextView tvNoDataFound;
    @BindView(R.id.rvDocument)
    RecyclerView rvDocument;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeContainer;
    @BindView(R.id.ivSyncAll)
    ImageView ivSyncAll;
    @BindView(R.id.llSync)
    LinearLayout llSync;

    private int limit = 10;
    private int offset = 0;
    private String searchTitle = null;
    private String categoryId = null;
    private String categoryTitle = "";
    private ProjectDocumentVM viewModel;
    private List<PDocsDataModel> documentList = new ArrayList<>();
    private List<PDocsDataModel> docPopList = new ArrayList<>();
    private List<CatListing> docCatList = new ArrayList<>();
    private List<CatListing> categoryFilterList = new ArrayList<>();
    private List<DocumentStatus> statusList = new ArrayList<>();
    private ProjectDocumentAdapter mProjectDocumentAdapter;

    /* for pagination */
    private boolean loading = true;
    private int pastVisibleItems, visibleItemCount, totalItemCount;

    /* Broadcasts Section */
    private BroadcastReceiver projectUpdateBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context mContext, final Intent intent) {
            if (null != getActivity()) {
                getActivity().runOnUiThread(() -> {
                    try {
                        if (intent.hasExtra("KEY_FLAG")) {
                            pullDownToRefresh();
                            viewModel.projectDocCategories();
                            if (isAdded() && !viewModel.isAlreadySchedulePDocsJob()
                                    && llSync.getVisibility() == View.VISIBLE) {
                                llSync.setVisibility(View.GONE);
                            }
                        } else {
                            if (isAdded() && viewModel.isAlreadySchedulePDocsJob()
                                    && llSync.getVisibility() == View.GONE) {
                                llSync.setVisibility(View.VISIBLE);
                            }
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
        return R.layout.fragment_project_document;
    }

    @Override
    protected void bindView(View view) {
        viewModel = new ViewModelProvider(this).get(ProjectDocumentVM.class);
        observeViewModel();
        setSwipeRefreshView(swipeContainer);
        swipeContainer.setOnRefreshListener(this::pullDownToRefresh);
        mProjectDocumentAdapter = new ProjectDocumentAdapter(this, documentList, viewModel,
                new ProjectDocumentAdapter.Callback() {
                    @Override
                    public void callEditFunction(PDocsDataModel pDocsDataModel) {
                        ProjectDocumentFragment.this.startActivity(new Intent(ProjectDocumentFragment.this.getActivity(), AddDocumentActivity.class)
                                .putParcelableArrayListExtra("PDocsCategories", (ArrayList<? extends Parcelable>) docCatList)
                                .putParcelableArrayListExtra("PDocsStatus", (ArrayList<? extends Parcelable>) statusList)
                                .putExtra("EDIT_DOCS", new Gson().toJson(pDocsDataModel)));

                    }

                    @Override
                    public void callEditDigitalDoc(Integer pDocId, Integer template_id, Integer customDocId) {
                        getActivity().startActivity(new Intent(getActivity(), DigitalFormActivity.class)
                                .putExtra("ProDocId", pDocId)
                                .putExtra("TemplateId", String.valueOf(template_id))
                                .putExtra("CustomDocId", customDocId)
                        );
                    }
                });
        rvDocument.setAdapter(mProjectDocumentAdapter);
        setUpPagination();

    }

    private void observeViewModel() {

        viewModel.isLoadingLD.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean != null) {
                if (aBoolean) showRefreshing();
                else hideRefreshing();

            }
        });

        viewModel.isSyncing.observe(getViewLifecycleOwner(), loading -> {
            if (loading != null) {
                if (loading) showProgress();
                else dismissProgress();
            }
        });

        viewModel.projectDocLiveList.observe(getViewLifecycleOwner(), projectDocumentList -> {
            if (projectDocumentList != null) {
                if (offset <= 1) {
                    documentList.clear();
                }
                documentList.addAll(projectDocumentList);
                mProjectDocumentAdapter.notifyDataSetChanged();
                mProjectDocumentAdapter.getFilter().filter(searchTitle == null ? "" : searchTitle);
                tvNoDataFound.setVisibility(documentList.isEmpty() ? View.VISIBLE : View.GONE);
                tvNoDataFound.setText(getString(R.string.no_data_found));
                loading = !projectDocumentList.isEmpty();
                setDocsPopList(documentList);
            }
        });

        viewModel.categoryLiveList.observe(getViewLifecycleOwner(), catListings -> {
            docCatList.clear();
            categoryFilterList.clear();
            if (catListings != null) {
                docCatList.addAll(catListings);
                categoryFilterList.addAll(catListings);
            }
            categoryFilterList.add(0, new CatListing(null, "All Categories"));
        });

        viewModel.documentStatusLD.observe(this, documentStatus -> {
            statusList.clear();
            if (documentStatus != null) {
                statusList.addAll(documentStatus);
            }
        });

        viewModel.offsetLD.observe(getViewLifecycleOwner(), integer -> {
            if (null != integer) {
                offset = integer;
            }
        });

        viewModel.notifyAdapterLD.observe(getViewLifecycleOwner(), notifyAdapter -> {
            if (null != notifyAdapter && null != mProjectDocumentAdapter) {
                mProjectDocumentAdapter.notifyDataSetChanged();
            }
        });

        viewModel.errorModelLD.observe(getViewLifecycleOwner(), errorModel -> {
            if (errorModel != null && getActivity() != null) {
                ((BaseActivity) getActivity()).handleErrorModel(errorModel);
            }
        });
        viewModel.syncAllVisibilityLD.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean != null && isAdded()) {
                ivSyncAll.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    protected void init() {
        /* Add Document Visibility*/
        if (userPermissions != null) {
            CommonMethods.checkVisiblePermission(userPermissions.getProjects().getCreateProject(), btnAddDocument);
        }
        registerAllBroadcasts();
        viewModel.projectDocCategories();
        viewModel.getDocsStatusList();
        viewModel.projectDocuments(categoryId, offset, limit);
    }

    @Override
    public void onResume() {
        super.onResume();
        llSync.setVisibility(viewModel.isInternetAvailable() && viewModel.isAlreadySchedulePDocsJob() ? View.VISIBLE : View.GONE);
    }

    private void pullDownToRefresh() {
        offset = 0;
        categoryId = null;
        searchTitle = null;
        viewModel.projectDocuments(categoryId, offset, limit);
        setUpEditTexts();
    }

    private void setUpEditTexts() {
        etCategory.setText(categoryId == null ? null : categoryTitle);
        etDocuments.setText(searchTitle);

    }

    public void hideRefreshing() {
        if (null != swipeContainer && swipeContainer.isRefreshing()) {
            swipeContainer.setRefreshing(false);
        }
    }

    public void showRefreshing() {
        if (null != swipeContainer && !swipeContainer.isRefreshing()) {
            swipeContainer.setRefreshing(true);
        }
    }

    /* pop ups and drop downs */
    private void showDocumentDropdown() {
        if (null != getActivity()) {
            if (!documentList.isEmpty()) {

                ListPopupWindow listPopupWindow = new ListPopupWindow(getActivity());
                listPopupWindow.setAdapter(
                        new ArrayAdapter<>(getActivity(), R.layout.row_dropdown, R.id.tvDropDown, docPopList));
                listPopupWindow.setAnchorView(etDocuments);
                listPopupWindow.setModal(true);
                listPopupWindow.setWidth(ListPopupWindow.WRAP_CONTENT);
                listPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
                listPopupWindow.setOnItemClickListener((parent, view, position, id) -> {
                    searchTitle = docPopList.get(position).getTitle();
                    etDocuments.setText(searchTitle);
                    mProjectDocumentAdapter.getFilter().filter(searchTitle);
                    listPopupWindow.dismiss();
                });
                listPopupWindow.show();
            } else {
                errorMessage("No Documents Found", null, false);
            }
        }
    }

    /* pop ups and drop downs */
    private void showCategoryDropdown() {
        if (null != getActivity()) {
            if (!categoryFilterList.isEmpty()) {
                ListPopupWindow listPopupWindow = new ListPopupWindow(getActivity());
                listPopupWindow.setAdapter(
                        new ArrayAdapter<>(getActivity(), R.layout.row_dropdown, R.id.tvDropDown, categoryFilterList));
                listPopupWindow.setAnchorView(etCategory);
                listPopupWindow.setModal(true);
                listPopupWindow.setWidth(ListPopupWindow.WRAP_CONTENT);
                listPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
                listPopupWindow.setOnItemClickListener((parent, view, position, id) -> {
                    categoryTitle = categoryFilterList.get(position).getTitle();
                    etCategory.setText(categoryTitle);
                    categoryId = categoryFilterList.get(position).getId();
                    viewModel.projectDocuments(categoryId, offset, limit);
                    listPopupWindow.dismiss();

                });
                listPopupWindow.show();
            } else {
                errorMessage("No Category Found", null, false);
            }
        }
    }

    @Override
    public void successCallback(String msg) {
        CommonMethods.displayToast(getActivity(), msg);
        pullDownToRefresh();
    }

    @Override
    public void onDestroyView() {
        unregisterBroadcasts();
        super.onDestroyView();
    }

    private void registerAllBroadcasts() {
        if (null != getActivity()) {
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(projectUpdateBroadcast, new IntentFilter(DataNames.INTENT_ACTION_ALL_PROJECT_UPDATE));
        }
    }

    private void unregisterBroadcasts() {
        if (null != getActivity()) {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(projectUpdateBroadcast);
        }
    }


    private void setUpPagination() {
        LinearLayoutManager mLayoutManager = (LinearLayoutManager) rvDocument.getLayoutManager();
        if (mLayoutManager != null) {
            rvDocument.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                            paginate();
                        }
                    }
                }
            });
        }

    }

    private void paginate() {
        if (isInternetAvailable()) {
            if (null != documentList && documentList.size() >= limit)
                viewModel.projectDocuments(categoryId, offset + limit, limit);
        }
    }

    @OnClick({R.id.btnAddDocument, R.id.etDocuments, R.id.etCategory, R.id.ivSyncAll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddDocument:
                startActivity(new Intent(getActivity(), AddDocumentActivity.class)
                        .putParcelableArrayListExtra("PDocsCategories", (ArrayList<? extends Parcelable>) docCatList)
                        .putParcelableArrayListExtra("PDocsStatus", (ArrayList<? extends Parcelable>) statusList)
                );
                break;

            case R.id.etDocuments:
                showDocumentDropdown();
                break;
            case R.id.etCategory:
                showCategoryDropdown();
                break;
            case R.id.ivSyncAll:
                if (isNotRefreshing()) {
                    if (CommonMethods.downloadFile(getActivity())) {
                        viewModel.syncAllProjectDocuments(documentList);
                    }
                }
                break;
        }
    }

    private boolean isNotRefreshing() {
        return swipeContainer != null && !swipeContainer.isRefreshing();

    }

    /**
     * Method to setup of Document Popup list
     */
    private void setDocsPopList(List<PDocsDataModel> projectDocsList) {
        docPopList.clear();
        for (int i = 0; i < projectDocsList.size(); i++) {
            if (!docPopList.toString().contains(projectDocsList.get(i).getTitle())) {
                docPopList.add(projectDocsList.get(i));
            }
        }
        docPopList.add(0, new PDocsDataModel("-1", "All Documents"));
    }


}
