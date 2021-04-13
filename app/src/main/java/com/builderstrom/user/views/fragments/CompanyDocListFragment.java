package com.builderstrom.user.views.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.widget.ListPopupWindow;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.repository.retrofit.api.DataNames;
import com.builderstrom.user.repository.retrofit.modals.CatListing;
import com.builderstrom.user.repository.retrofit.modals.CompanyDocument;
import com.builderstrom.user.repository.retrofit.modals.PDocsDataModel;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.viewmodels.CompanyViewModel;
import com.builderstrom.user.views.activities.BaseActivity;
import com.builderstrom.user.views.activities.DigitalFormActivity;
import com.builderstrom.user.views.adapters.CompanyDocListAdapter;
import com.builderstrom.user.views.viewInterfaces.UpdateCompanyDocsListing;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.builderstrom.user.views.fragments.HomeMenuFragment.userPermissions;

public class CompanyDocListFragment extends BaseRecyclerViewFragment implements UpdateCompanyDocsListing {

    /* views */
    @BindView(R.id.ivSyncAll)
    ImageView ivSyncAll;
    @BindView(R.id.etDocuments)
    EditText etDocuments;
    @BindView(R.id.etCategory)
    EditText etCategory;
    @BindView(R.id.llSync)
    LinearLayout llSync;
    @BindView(R.id.btnAddDocument)
    Button btnAddDocument;

    private List<CompanyDocument> docList = new ArrayList<>();
    private List<CatListing> categories = new ArrayList<>();

    private CompanyDocListAdapter mAdapter;
    private CompanyViewModel viewModel;

    private String searchTitle = null;
    private String categoryId = null;
    private String categoryTitle = "";
    private BroadcastReceiver projectUpdateBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context mContext, final Intent intent) {
            requireActivity().runOnUiThread(() -> {
                try {
                    if (intent.hasExtra("KEY_FLAG")) {
                        pullDownToRefresh();
                        if (isAdded() && !viewModel.isAlreadyScheduleComDocsJob()
                                && llSync.getVisibility() == View.VISIBLE) {
                            llSync.setVisibility(View.GONE);
                        }
                    } else {
                        if (isAdded() && viewModel.isAlreadyScheduleComDocsJob()
                                && llSync.getVisibility() == View.GONE) {
                            llSync.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_company_documents;
    }

    @Override
    protected void bindView(View view) {
        /* Add Document Visibility*/
        if (userPermissions != null) {
            CommonMethods.checkVisiblePermission(userPermissions.getCloudStorage().getCreateFile(), btnAddDocument);
        }
        viewModel = new ViewModelProvider(this).get(CompanyViewModel.class);
        observeViewModel();
    }

    @Override
    protected void pagination() {
        if (isInternetAvailable()) {
            viewModel.getCompanyDocList(/*searchTitle,*/ categoryId, offset + LIMIT, LIMIT);
        }
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return null;
    }

    @Override
    protected RecyclerView.Adapter getRecyclerAdapter() {
        if (null == mAdapter) {
            mAdapter = new CompanyDocListAdapter(this, docList, this, viewModel, new CompanyDocListAdapter.Callback() {
                @Override
                public void callEditFunction(PDocsDataModel pDocsDataModel) {

                }

                @Override
                public void callEditDigitalDoc(String pDocId, String template_id, Integer customDocId) {
                    getActivity().startActivity(new Intent(getActivity(), DigitalFormActivity.class)
                            .putExtra("ProDocId", Integer.parseInt(pDocId))
                            .putExtra("TemplateId", template_id)
                            .putExtra("CustomDocId", customDocId)
                    );
                }
            });
        }
        return mAdapter;
    }

    @Override
    protected void setData() {
        viewModel.getCompanyCategories();
        viewModel.getCompanyDocList(categoryId, offset, LIMIT);
    }

    @Override
    protected void pullDownToRefresh() {
        offset = 0;
        categoryId = null;
        searchTitle = null;
        viewModel.getCompanyDocList(/*searchTitle, */ categoryId, offset, LIMIT);
        setUpEditTexts();
    }

    @OnClick({R.id.etDocuments, R.id.etCategory, R.id.btnAddDocument, R.id.ivSyncAll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.etDocuments:
                openAllDocumentsPopUp();
                break;
            case R.id.etCategory:
                openAllCategoriesPopUp();
                break;
            case R.id.btnAddDocument:
                AddCompanyDocumentFragment fragment = new AddCompanyDocumentFragment();
                fragment.setCallBack(this::pullDownToRefresh);
                getParentFragmentManager().beginTransaction().add(R.id.flTestContainer,
                        fragment, "add doc").addToBackStack("add doc").commit();
                break;
            case R.id.ivSyncAll:
                if (isNotRefreshing()) {
                    if (CommonMethods.downloadFile(getActivity())) {
                        viewModel.syncAllProjectDocuments(docList);
                    }
                }
                break;
        }
    }

    private void openAllDocumentsPopUp() {
        if (null != getActivity()) {
            if (!docList.isEmpty()) {
                // set up List
                List<CompanyDocument> filterDocList = new ArrayList<>();
                filterDocList.add(new CompanyDocument("-1", "All documents"));
                filterDocList.addAll(docList);

                // show popup
                ListPopupWindow listPopupWindow = new ListPopupWindow(getActivity());
                listPopupWindow.setAdapter(new ArrayAdapter<>(getActivity(),
                        R.layout.row_dropdown, R.id.tvDropDown, filterDocList));
                listPopupWindow.setAnchorView(etDocuments);
                listPopupWindow.setModal(true);
                listPopupWindow.setWidth(ListPopupWindow.WRAP_CONTENT);
                listPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
                listPopupWindow.setOnItemClickListener((parent, view, position, id) -> {
                    searchTitle = filterDocList.get(position).getTitle();
                    etDocuments.setText(searchTitle);
                    mAdapter.getFilter().filter(searchTitle);
                    listPopupWindow.dismiss();
                });
                listPopupWindow.show();
            } else {
                errorMessage("No Documents Found", null, false);
            }
        }
    }

    private void openAllCategoriesPopUp() {
        if (null != getActivity()) {
            if (!categories.isEmpty()) {
                // set up List
                List<CatListing> filterCatList = new ArrayList<>();
                filterCatList.add(new CatListing(null, "All Categories"));
                filterCatList.addAll(categories);

                // show popup
                ListPopupWindow listPopupWindow = new ListPopupWindow(getActivity());
                listPopupWindow.setAdapter(new ArrayAdapter<>(getActivity(),
                        R.layout.row_dropdown, R.id.tvDropDown, filterCatList));
                listPopupWindow.setAnchorView(etCategory);
                listPopupWindow.setModal(true);
                listPopupWindow.setWidth(ListPopupWindow.WRAP_CONTENT);
                listPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
                listPopupWindow.setOnItemClickListener((parent, view, position, id) -> {
                    categoryTitle = filterCatList.get(position).getTitle();
                    etCategory.setText(categoryTitle);
                    categoryId = filterCatList.get(position).getId();
                    viewModel.getCompanyDocList(/*searchTitle, */ categoryId, 0, LIMIT);
                    listPopupWindow.dismiss();
                });
                listPopupWindow.show();
            } else {
                errorMessage("No Categories Found", null, false);
            }
        }
    }

    private void setUpEditTexts() {
        etCategory.setText(categoryId == null ? null : categoryTitle);
        etDocuments.setText(searchTitle);
    }

    private void observeViewModel() {

        viewModel.isLoadingLD.observe(getViewLifecycleOwner(), aBoolean -> {
            if (null != aBoolean) {
                if (aBoolean) showProgress();
                else dismissProgress();
            }
        });

        viewModel.isRefreshingLD.observe(getViewLifecycleOwner(), aBoolean -> {
            if (null != aBoolean) {
                if (aBoolean) showRefreshing();
                else hideRefreshing();
            }
        });

        viewModel.companyDocListLD.observe(getViewLifecycleOwner(), companyDocuments -> {
            if (companyDocuments != null) {
                if (offset <= 1) {
                    docList.clear();
                }
                docList.addAll(companyDocuments);
                mAdapter.notifyDataSetChanged();
                mAdapter.getFilter().filter(searchTitle == null ? "" : searchTitle);
                if (docList.isEmpty()) {
                    showNoDataTextView(R.string.no_data_found);
                } else {
                    hideNoDataTextView();
                }
                loading = !companyDocuments.isEmpty() && companyDocuments.size() == LIMIT;
            }
        });

        viewModel.syncVisibilityLD.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean != null && isAdded()) {
                ivSyncAll.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
            }
        });
        viewModel.companyCategoryLD.observe(getViewLifecycleOwner(), companyStatuses -> {
            if (companyStatuses != null) {
                categories.clear();
                categories.addAll(companyStatuses);
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

        viewModel.notifyAdapterLD.observe(getViewLifecycleOwner(), notifyAdapter -> {
            if (notifyAdapter != null) {
                mAdapter.notifyDataSetChanged();

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        llSync.setVisibility(viewModel.isInternetAvailable() && viewModel.isAlreadyScheduleComDocsJob() ? View.VISIBLE : View.GONE);
        registerBroadcasts();
    }

    @Override
    public void onPause() {
        unregisterBroadcasts();
        super.onPause();
    }

    private void registerBroadcasts() {
        if (getActivity() != null) {
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(projectUpdateBroadcast, new IntentFilter(DataNames.INTENT_ACTION_ALL_PROJECT_UPDATE));
        }
    }

    private void unregisterBroadcasts() {
        if (getActivity() != null) {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(projectUpdateBroadcast);
        }
    }

    @Override
    public void onUpdateBack() {
        pullDownToRefresh();
    }
}

