package com.builderstrom.user.views.dialogFragments;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.builderstrom.user.R;
import com.builderstrom.user.data.retrofit.modals.PDocsDataModel;
import com.builderstrom.user.utils.CommonMethods;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import butterknife.BindView;
import butterknife.OnClick;

public class ProjectMoreInfoDialogFragment extends BaseDialogFragment {

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvUploader)
    TextView tvUploader;
    @BindView(R.id.tvDateTime)
    TextView tvDate;
    @BindView(R.id.tvCategory)
    TextView tvCategory;
    @BindView(R.id.tvRevision)
    TextView tvRevision;
    private PDocsDataModel projectDoc;

//    public static ProjectMoreInfoDialogFragment newInstance(int type, Parcelable info) {
//        ProjectMoreInfoDialogFragment dialogFragment = new ProjectMoreInfoDialogFragment();
//        Bundle bundle = new Bundle();
//        bundle.putInt("type", type);
//        bundle.putParcelable("data", info);
//        dialogFragment.setArguments(bundle);
//        return dialogFragment;
//    }

    public static ProjectMoreInfoDialogFragment newInstance(int type, String info) {
        ProjectMoreInfoDialogFragment dialogFragment = new ProjectMoreInfoDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putString("data", info);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Override
    protected boolean isFullScreenDialog() {
        return false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_fragment_company_more_info;
    }

    @Override
    protected void bindViews(View view) {
        if (null != getArguments()) {
            if (getArguments().getInt("type") == 1) {
//                projectDoc = getArguments().getParcelable("data");
                projectDoc = new Gson().fromJson(getArguments().getString("data"), new TypeToken<PDocsDataModel>() {
                }.getType());
            }
        }
    }

    @OnClick(R.id.ivClose)
    public void onClick() {
        dismiss();
    }


    @Override
    protected void init() {
        if (projectDoc != null) {
            tvTitle.setText(String.format("More Information - %s", projectDoc.getTitle()));
            tvUploader.setText(CommonMethods.spannedText(String.format("Uploader : %s", checkNull(projectDoc.getUploadername())), 0, 11));
            tvDate.setText(CommonMethods.spannedText(String.format("Date/Time : %s", checkNull(CommonMethods.convertDate(CommonMethods.DF_1, projectDoc.getCreatedOn(), CommonMethods.DF_14))), 0, 12));
            tvCategory.setText(CommonMethods.spannedText(String.format("Category : %s", checkNull(projectDoc.getDocumentCategory())), 0, 10));
            tvRevision.setText(CommonMethods.spannedText(String.format("Revision : %s", checkNull(projectDoc.getRevision())), 0, 10));
        }


    }
}
