package com.builderstrom.user.views.dialogFragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.repository.retrofit.modals.CatListing;
import com.builderstrom.user.repository.retrofit.modals.User;
import com.builderstrom.user.views.adapters.CategoriesListAdapter;
import com.builderstrom.user.views.adapters.StatusListAdapter;
import com.builderstrom.user.views.adapters.UserListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class ToDoFilterDialogFragment extends BaseDialogFragment {

    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.tvNoComment) TextView tvNoData;
    @BindView(R.id.rvComments) RecyclerView rvComments;

    private RecyclerView.Adapter mAdapter;
    private List<CatListing> categoryList = new ArrayList<>();
    private List<User> userListing = new ArrayList<>();
    private List<String> statusList = new ArrayList<>();
    private int actionId = 0;
    private FilterValues filterValues;


    public static ToDoFilterDialogFragment newInstance(int action, String title, List<CatListing> categoryList, List<User> userListing) {
        ToDoFilterDialogFragment dialogFragment = new ToDoFilterDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("action", action);
        bundle.putString("title", title);
        bundle.putParcelableArrayList("cat_listing", (ArrayList<? extends Parcelable>) categoryList);
        bundle.putParcelableArrayList("user_listing", (ArrayList<? extends Parcelable>) userListing);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Override
    protected boolean isFullScreenDialog() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_fragment_filtration_todo;
    }

    @Override
    protected void bindViews(View view) {
        statusList.clear();
        categoryList.clear();
        userListing.clear();
        statusList.add(0, "Normal");
        statusList.add(1, "Warning");
        statusList.add(2, "Overdue");
        statusList.add(3, "Completed");

        if (getArguments() != null) {
            actionId = getArguments().getInt("action");
            tvTitle.setText(getArguments().getString("title"));
            categoryList.addAll(getArguments().getParcelableArrayList("cat_listing"));
            userListing.addAll(getArguments().getParcelableArrayList("user_listing"));

            switch (actionId) {
                case 0:
                    mAdapter = new StatusListAdapter(getActivity(), statusList, statusId -> {
                        filterValues.callValues(statusId + 1, statusList.get(statusId), 0);
                        dismissWithHideKeyboard();
                    });
                    break;
                case 1:
                    mAdapter = new CategoriesListAdapter(getActivity(), categoryList, (categoryId, category) -> {
                        filterValues.callValues(Integer.parseInt(categoryId), category, 1);
                        dismissWithHideKeyboard();
                    });
                    break;
                case 2:
                    mAdapter = new UserListAdapter(getActivity(), userListing, (categoryId, category) -> {
                        filterValues.callValues(Integer.parseInt(categoryId), category, 1);
                        dismissWithHideKeyboard();
                    });
                    break;

            }
            rvComments.setAdapter(mAdapter);
        }
    }

    @OnClick({R.id.ivClose})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivClose:
                dismissWithHideKeyboard();
                break;
        }
    }

    @Override
    protected void init() {

    }

    public void setFilterValues(FilterValues filterValues) {
        this.filterValues = filterValues;
    }

    public interface FilterValues {
        void callValues(Integer id, String name, int type);
    }


}
