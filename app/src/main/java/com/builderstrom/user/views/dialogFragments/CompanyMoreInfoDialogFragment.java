package com.builderstrom.user.views.dialogFragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.TextView;

import com.builderstrom.user.R;
import com.builderstrom.user.data.retrofit.modals.CompanyDocument;
import com.builderstrom.user.utils.CommonMethods;

import butterknife.BindView;
import butterknife.OnClick;

public class CompanyMoreInfoDialogFragment extends BaseDialogFragment {

    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.tvUploader) TextView tvUploader;
    @BindView(R.id.tvDateTime) TextView tvDate;
    @BindView(R.id.tvCategory) TextView tvCategory;
    @BindView(R.id.tvRevision) TextView tvRevision;
    private CompanyDocument companyDoc;

    public static CompanyMoreInfoDialogFragment newInstance(int type, Parcelable info) {
        CompanyMoreInfoDialogFragment dialogFragment = new CompanyMoreInfoDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putParcelable("data", info);
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
            companyDoc = getArguments().getParcelable("data");
        }
    }

    @OnClick(R.id.ivClose)
    public void onClick() {
        dismiss();
    }

    @Override
    protected void init() {
        if (companyDoc != null) {
            tvTitle.setText(String.format("More Information - %s", companyDoc.getTitle()));
            tvUploader.setText(CommonMethods.spannedText(String.format("Uploader : %s", checkNull(companyDoc.getUploaderName())), 0, 11));
            tvDate.setText(CommonMethods.spannedText(String.format("Date/Time : %s", checkNull(CommonMethods.convertDate(CommonMethods.DF_1, companyDoc.getCreated_on(), CommonMethods.DF_14))), 0, 12));
            tvCategory.setText(CommonMethods.spannedText(String.format("Category : %s", checkNull(companyDoc.getCat_title())), 0, 10));
            tvRevision.setText(CommonMethods.spannedText(String.format("Revision : %s", checkNull(companyDoc.getRevision())), 0, 10));
        }


    }
}
