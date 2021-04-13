package com.builderstrom.user.views.adapters;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.repository.retrofit.modals.Datum;
import com.builderstrom.user.utils.CommonMethods;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DrawingOnlineAdapter extends RecyclerView.Adapter<DrawingOnlineAdapter.MyViewHolder> {

    private Context context;
    private LayoutInflater mLayoutInflater;
    private DrawingCallback mCallback;
    private List<Datum> drawingList;
    private List<String> rowIdsList;

    public DrawingOnlineAdapter(Context context, List<Datum> drawingList, ArrayList<String> rowIdsList, DrawingCallback mCallback) {
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.drawingList = drawingList;
        this.rowIdsList = rowIdsList;
        this.mCallback = mCallback;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(mLayoutInflater.inflate(R.layout.item_drawing, parent,
                false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        holder.ll_Container.setBackgroundColor(ContextCompat.getColor(context, position % 2 == 0 ?
                R.color.colorDrawingListBack : R.color.status_bar));

        Datum model = drawingList.get(position);
        holder.tvTitle.setText(model.getName());
        holder.tvRevision.setText(model.getRevision());
        holder.tvDrawingNumber.setText(model.getDrawingNo());

        /* *********************Pdf file******************* */
        if (model.getPdflocation() == null || model.getPdflocation().isEmpty()) {
            holder.ivPDF.setImageResource(R.drawable.ic_pdf_grey);
            holder.ivPdfDownload.setVisibility(View.GONE);
        } else {
            if (rowIdsList.contains(model.getId()) || model.isSync()) {
                holder.ivPDF.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_pdf_red));
                holder.ivPdfDownload.setVisibility(View.GONE);
            } else {
                if (model.isPdfClick()) {
                    holder.ivPDF.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_pdf_red));
                    holder.ivPdfDownload.setVisibility(View.VISIBLE);
                } else {
                    holder.ivPdfDownload.setVisibility(View.GONE);
                    holder.ivPDF.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_pdf_red));
                    holder.ivPDF.setOnClickListener(view -> mCallback.downloadSingleFile(position,
                            true, model.getPdflocation(), model.getPdfname()));
                }
                if (null != model.getPdfname() && !model.getPdfname().isEmpty()) {
                    boolean isPdfDownloaded = CommonMethods.isFileDownloaded(model.getPdfname());
                    holder.ivPdfDownload.setVisibility(isPdfDownloaded ? View.VISIBLE : View.GONE);
                    model.setPdfClick(isPdfDownloaded);
                }
            }
        }

        /* *********************Drawing file******************* */
        if (null == model.getDwglocation() || model.getDwglocation().isEmpty()) {
            holder.ivBWG.setImageResource(R.drawable.ic_dwg_gray);
            holder.ivDwgDownload.setVisibility(View.GONE);
        } else {
            if (rowIdsList.contains(model.getId()) || model.isSync()) {
                holder.ivBWG.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_dwg_blue));
                holder.ivDwgDownload.setVisibility(View.GONE);
            } else {
                if (model.isDwgClick()) {
                    holder.ivBWG.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_dwg_blue));
                    holder.ivDwgDownload.setVisibility(View.VISIBLE);
                } else {
                    holder.ivDwgDownload.setVisibility(View.GONE);
                    holder.ivBWG.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_dwg_blue));
                    holder.ivBWG.setOnClickListener(view -> mCallback.downloadSingleFile(position,
                            false, model.getDwglocation(), model.getDwgname()));
                }
                if (null != model.getDwgname() && !model.getDwgname().isEmpty()) {
                    boolean isDwgDownloaded = CommonMethods.isFileDownloaded(model.getDwgname());
                    holder.ivDwgDownload.setVisibility(isDwgDownloaded ? View.VISIBLE : View.GONE);
                    model.setDwgClick(isDwgDownloaded);
                }
            }
        }

        /* *********************Other file type ********************** */
        if (null == model.getOftname() || model.getOftname().isEmpty()) {
            holder.ivOFT.setImageResource(R.drawable.ic_otf);
            holder.ivOftDownload.setVisibility(View.GONE);
        } else {

            holder.ivOFT.setImageResource(CommonMethods.getFileImageFromName(model.getOftname()));

            if (rowIdsList.contains(model.getId()) || model.isSync()) {
                holder.ivOftDownload.setVisibility(View.GONE);
            } else {
                if (model.isOftClick()) {
                    holder.ivOftDownload.setVisibility(View.VISIBLE);
                } else {
                    holder.ivOftDownload.setVisibility(View.GONE);
                    holder.ivOFT.setOnClickListener(view -> mCallback.downloadSingleFile(position,
                            false, model.getOftlocation(), model.getOftname()));
                }
                if (!model.getOftname().isEmpty() && model.getOftname() != null) {
                    boolean isOftDownloaded = CommonMethods.isFileDownloaded(model.getOftname());
                    holder.ivOftDownload.setVisibility(isOftDownloaded ? View.VISIBLE : View.GONE);
                    model.setOftClick(isOftDownloaded);
                }
            }
        }

        /* *********************Local Database Sync********************/
        if (rowIdsList.contains(model.getId())) {
            holder.ivRefresh.setEnabled(false);
            holder.ivRefresh.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_refresh_not_found));
        } else {
            if (model.isSync()) {
                holder.ivRefresh.setEnabled(false);
                holder.ivRefresh.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_refresh_not_found));
            } else {
                holder.ivRefresh.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_refresh));
                holder.ivRefresh.setOnClickListener(view -> mCallback.syncLocalDataBase(position, model.getId(), holder.ivRefresh));
            }
        }
    }

    @Override
    public int getItemCount() {
        return drawingList.size();
    }

    public List<Datum> getDrawingList() {
        return drawingList;
    }

    private void imagePreview(Datum model) {

        if (model.getPdflocation() != null && !model.getPdflocation().equals("")) {
            try {
                String urlDecode = URLDecoder.decode(model.getPdflocation());
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.parse(urlDecode);
                intent.setDataAndType(uri, "application/pdf");
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                // No application to view, ask to download one
                new AlertDialog.Builder(context)
                        .setTitle("Preview PDF")
                        .setMessage("To preview this PDF file a 3rd party application needs to be installed. Are you happy for this to be installed?")
                        .setPositiveButton("‘Yes, Install", (dialog, which) -> {
                            Intent marketIntent = new Intent(Intent.ACTION_VIEW);
                            marketIntent.setData(Uri.parse("market://details?id=com.adobe.reader"));
                            context.startActivity(marketIntent);
                        })
                        .setNegativeButton("No, Thanks", null)
                        .create().show();
                //e.printStackTrace();
            }
        } else if (model.getDwglocation() != null && !model.getDwglocation().isEmpty()) {
            boolean installed = CommonMethods.appInstalledOrNot(context, "com.gstarmc.android");
            if (installed) {
                //This intent will help you to launch if the package is already installed
                Intent LaunchIntent = context.getPackageManager().getLaunchIntentForPackage("com.gstarmc.android");
                context.startActivity(LaunchIntent);
            } else {
                new AlertDialog.Builder(context)
                        .setTitle("Preview DWG")
                        .setMessage("To preview this DWG file a 3rd party application needs to be installed. Are you happy for this to be installed?")
                        .setPositiveButton("Yes, Please", (dialog, which) -> {
                            Intent marketIntent = new Intent(Intent.ACTION_VIEW);
                            marketIntent.setData(Uri.parse("market://details?id=com.gstarmc.android"));
                            context.startActivity(marketIntent);
                        })
                        .setNegativeButton("No, Thanks", null)
                        .create().show();
            }

        } else {
            Toast.makeText(context, "There is no preview available", Toast.LENGTH_SHORT).show();
        }

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ll_Container) LinearLayout ll_Container;
        @BindView(R.id.tvRevision) TextView tvRevision;
        @BindView(R.id.tvDrawingNumber) TextView tvDrawingNumber;
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.ivRefresh) ImageView ivRefresh;
        @BindView(R.id.ivPDF) ImageView ivPDF;
        @BindView(R.id.ivBWG) ImageView ivBWG;
        @BindView(R.id.ivOFT) ImageView ivOFT;
        @BindView(R.id.iv_pdfDownlaod) ImageView ivPdfDownload;
        @BindView(R.id.iv_dwgDownlaod) ImageView ivDwgDownload;
        @BindView(R.id.iv_oftDownlaod) ImageView ivOftDownload;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.ivPreview, R.id.ivAdd, R.id.ivMoreInfo, R.id.ivDot})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ivPreview:
                    imagePreview(drawingList.get(getAdapterPosition()));
                    break;
                case R.id.ivAdd:
                    CommonMethods.displayToast(context, "work in progress");
                    break;
                case R.id.ivMoreInfo:
                    if (null != mCallback) {
                        mCallback.callMoreInfo(getAdapterPosition());
                    }
                    break;
                case R.id.ivDot:
                    if (null != mCallback) {
                        mCallback.callMoreDot(getAdapterPosition());
                    }
                    break;
            }
        }

    }

    public interface DrawingCallback {

        void callMoreInfo(int position);

        void syncLocalDataBase(int position, String RowID, ImageView ivRefresh);

        void downloadSingleFile(int position, boolean isPdfDownload, String link, String filename);

        void callMoreDot(int position);

    }

}





