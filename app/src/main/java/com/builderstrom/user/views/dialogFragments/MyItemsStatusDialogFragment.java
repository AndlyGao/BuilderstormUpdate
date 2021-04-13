package com.builderstrom.user.views.dialogFragments;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.data.retrofit.modals.MyItemsStatusModel;
import com.builderstrom.user.views.adapters.MyItemsStatusListAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class MyItemsStatusDialogFragment extends BaseDialogFragment {

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvNoDataFound)
    TextView tvNoDataFound;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private ArrayList<MyItemsStatusModel> itemsList = new ArrayList<>();


    public static MyItemsStatusDialogFragment newInstance(String title, ArrayList<MyItemsStatusModel> list) {
        MyItemsStatusDialogFragment dialogFragment = new MyItemsStatusDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putParcelableArrayList("statusList", list);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }


    @Override
    protected boolean isFullScreenDialog() {
        return false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_fragment_my_item_issue_status;
    }

    @Override
    protected void bindViews(View view) {

        if (getArguments() != null) {
            tvTitle.setText(getArguments().getString("title"));
            if (getArguments().getParcelableArrayList("statusList") != null)
                itemsList.addAll(getArguments().getParcelableArrayList("statusList"));
        }


    }

    @Override
    protected void init() {
        tvNoDataFound.setVisibility(itemsList == null || itemsList.isEmpty() ? View.VISIBLE : View.GONE);
        if (itemsList != null && !itemsList.isEmpty()) {
            recyclerView.setAdapter(new MyItemsStatusListAdapter(getActivity(), itemsList));
        }
    }

    @OnClick(R.id.ivClose)
    public void onClick() {
        dismiss();
    }

}


