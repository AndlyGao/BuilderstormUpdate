package com.builderstrom.user.views.dialogFragments;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.builderstrom.user.R;
import com.builderstrom.user.data.retrofit.modals.Datum;
import com.builderstrom.user.utils.CommonMethods;

import butterknife.BindView;
import butterknife.OnClick;

public class DrawingInfoDialogFragment extends BaseDialogFragment {


    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.tvDrawingNumber) TextView tvDrawingNumber;
    @BindView(R.id.tvUploader) TextView tvUploader;
    @BindView(R.id.tvDateTime) TextView tvDate;
    @BindView(R.id.tvCategory) TextView tvCategory;
    @BindView(R.id.tvStatus) TextView tvStatus;
    @BindView(R.id.tvCompany) TextView tvCompany;
    @BindView(R.id.tvRevision) TextView tvRevision;
    @BindView(R.id.tvRevisionStatus) TextView tvRevisionStatus;

    private Datum drawingData;

    public static DrawingInfoDialogFragment newInstance(Datum datum) {
        DrawingInfoDialogFragment dialogFragment = new DrawingInfoDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", datum);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Override
    protected boolean isFullScreenDialog() {
        return false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_project_information;
    }

    @Override
    protected void bindViews(View view) {

        if (getArguments() != null) {
            drawingData = getArguments().getParcelable("data");
        }
    }

    @OnClick(R.id.ivClose)
    public void onClick() {
        dismiss();
    }

    @Override
    protected void init() {
        if (null != getDialog()) {
            getDialog().setCancelable(false);
        }

        setDataView();
    }

    private void setDataView() {
        if (null != drawingData) {
            tvTitle.setText(drawingData.getName());
            tvDrawingNumber.setText(CommonMethods.spannedText(String.format("Drawing Number : %s", checkNull(drawingData.getDrawingNo())), 0, 16));
            tvUploader.setText(CommonMethods.spannedText(String.format("Uploader : %s", checkNull(drawingData.getUploadername())), 0, 11));
            tvDate.setText(CommonMethods.spannedText(String.format("Date/Time : %s", checkNull(CommonMethods.convertDate(CommonMethods.DF_1, drawingData.getCreatedOn(), CommonMethods.DF_6))), 0, 12));
            tvCategory.setText(CommonMethods.spannedText(String.format("Category : %s", checkNull(drawingData.getCategoryName())), 0, 11));
            tvStatus.setText(CommonMethods.spannedText(String.format("Status : %s", checkNull(drawingData.getStatusTitle())), 0, 9));
            tvCompany.setText(CommonMethods.spannedText(String.format("Company : %s", checkNull(drawingData.getCompanyName())), 0, 10));
            tvRevision.setText(CommonMethods.spannedText(String.format("Revision : %s", checkNull(drawingData.getRevision())), 0, 11));
            tvRevisionStatus.setText(CommonMethods.spannedText(String.format("Revision Status : %s", checkNull(drawingData.getRevisionStatus())), 0, 18));
        }
    }



}
