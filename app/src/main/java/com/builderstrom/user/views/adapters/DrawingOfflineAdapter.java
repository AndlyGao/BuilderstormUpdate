package com.builderstrom.user.views.adapters;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.repository.retrofit.modals.Datum;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.views.activities.BaseActivity;

import java.util.List;

public class DrawingOfflineAdapter extends RecyclerView.Adapter<DrawingOfflineAdapter.MyViewHolder> {

    private Context context;
    private List<Datum> drawingList;
    private DrawingCallback mCallback;

    public DrawingOfflineAdapter(Context context, List<Datum> drawingList, DrawingCallback mCallback) {
        this.context = context;
        this.drawingList = drawingList;
        this.mCallback = mCallback;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drawing_offline, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.ll_Container.setBackgroundColor(Color.parseColor("#eeeeee"));
        Datum model = drawingList.get(position);
        holder.tvTitle.setText(model.getName());
        holder.tvRevision.setText(model.getRevision());
        holder.tvDrawingNumber.setText(model.getDrawingNo());

        /* *********************Pdf file********************/
        if (model.getPdfname() == null || model.getPdfname().equals("")) {
            holder.ivPDF.setImageResource(R.drawable.ic_pdf_grey);
        } else {
            holder.ivPDF.setImageResource(R.drawable.ic_pdf_red);
            holder.ivPDF.setOnClickListener(view -> {
                if (!(null != model.getPdfname() && model.getPdfname().equals(""))) {
                    try {
                        Uri outputFileUri = CommonMethods.getUriFromFileName(context, model.getPdfname());
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(outputFileUri, "application/pdf");
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        context.startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Preview PDF");
                        builder.setMessage("To preview this PDF file a 3rd party application needs to be installed. Are you happy for this to be installed?");
                        builder.setPositiveButton("Yes, Install", (dialog, which) -> {
                            Intent marketIntent = new Intent(Intent.ACTION_VIEW);
                            marketIntent.setData(Uri.parse("market://details?id=com.adobe.reader"));
                            context.startActivity(marketIntent);
                        });
                        builder.setNegativeButton("No, Thanks", null);
                        builder.create().show();
                    }
                } else {
                    ((BaseActivity) context).errorMessage("", R.string.no_preview_available, false);
                }
            });
        }

        /* *********************Drawing file********************/
        if (model.getDwgname() == null || model.getDwgname().equals("")) {
            holder.ivBWG.setImageResource(R.drawable.ic_dwg_gray);
        } else {
            holder.ivBWG.setImageResource(R.drawable.ic_dwg_blue);
            holder.ivBWG.setOnClickListener(view -> {
                if (!(model.getDwgname() != null && model.getDwgname().isEmpty())) {
                    boolean installed = CommonMethods.appInstalledOrNot(context, "com.gstarmc.android");
                    if (installed) {
                        Intent LaunchIntent = context.getPackageManager().getLaunchIntentForPackage("com.gstarmc.android");
                        context.startActivity(LaunchIntent);
                    } else {
                        new AlertDialog.Builder(context)
                                .setTitle("Preview DWG")
                                .setMessage("To preview this DWG file a 3rd party application needs to be installed. Are you happy for this to be installed?")
                                .setPositiveButton("Yes, Install", (dialog, which) -> {
                                    Intent marketIntent = new Intent(Intent.ACTION_VIEW);
                                    marketIntent.setData(Uri.parse("market://details?id=com.gstarmc.android"));
                                    context.startActivity(marketIntent);
                                })
                                .setNegativeButton("No, Thanks", null)
                                .create().show();
                    }
                } else {
                    ((BaseActivity) context).errorMessage("", R.string.no_preview_available, false);
                }
            });
        }

        /* *********************Other File file********************/
        if (model.getOftname() == null || model.getOftname().equals("")) {
            holder.ivOFT.setImageResource(R.drawable.ic_otf);
        } else {
            String extension = model.getOftname().substring(model.getOftname().lastIndexOf("."));
            if (CommonMethods.getAllExtensions().contains(extension)) {
                int extensionIndex = CommonMethods.getAllExtensions().indexOf(extension);
                holder.ivOFT.setImageResource(CommonMethods.getAllIcons().get(extensionIndex));
            } else {
                holder.ivOFT.setImageResource(R.drawable.ic_file);
            }
        }

        holder.ivMoreInfo.setOnClickListener(view -> mCallback.callMoreInfo(position));

        /* *********************Preview file******************* */
        holder.ivPreview.setOnClickListener(view -> imagePreview(model));

        /* *********************Local Database Sync********************/
//        holder.ivRefresh.setVisibility((model.isSync()) ? View.GONE : View.VISIBLE);
        holder.ivRefresh.setImageResource(!model.isSync() ? R.drawable.ic_un_uploaded : R.drawable.ic_refresh_not_found);
    }

    @Override
    public int getItemCount() {
        return drawingList.size();
    }

    private void imagePreview(Datum model) {

        if (model.getPdfname() != null && !model.getPdfname().equals("")) {
            try {
                Uri outputFileUri = CommonMethods.getUriFromFileName(context, model.getPdfname());
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(outputFileUri, "application/pdf");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                new AlertDialog.Builder(context)
                        .setTitle("Preview PDF")
                        .setMessage("To preview this PDF file a 3rd party application needs to be installed. Are you happy for this to be installed?")
                        .setPositiveButton("Yes, Install", (dialog, which) -> {
                            Intent marketIntent = new Intent(Intent.ACTION_VIEW);
                            marketIntent.setData(Uri.parse("market://details?id=com.adobe.reader"));
                            context.startActivity(marketIntent);
                        })
                        .setNegativeButton("No, Thanks", null)
                        .create().show();
            }
        } else if (model.getDwgname() != null && !model.getDwgname().equals("")) {
            if (CommonMethods.appInstalledOrNot(context, "com.gstarmc.android")) {
                context.startActivity(context.getPackageManager().getLaunchIntentForPackage("com.gstarmc.android"));
            } else {
                new AlertDialog.Builder(context)
                        .setTitle("Preview DWG")
                        .setMessage("To preview this DWG file a 3rd party application needs to be installed. Are you happy for this to be installed?")
                        .setPositiveButton("Yes, Install", (dialog, which) -> {
                            Intent marketIntent = new Intent(Intent.ACTION_VIEW);
                            marketIntent.setData(Uri.parse("market://details?id=com.gstarmc.android"));
                            context.startActivity(marketIntent);
                        })
                        .setNegativeButton("No, Thanks", null)
                        .create().show();
            }

        } else {
            ((BaseActivity) context).errorMessage("", R.string.no_preview_available, false);
        }

    }

    //Callback interface
    public interface DrawingCallback {
        void callMoreInfo(int position);

        void syncLocalDataBase(int position, String RowID);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvRevision, tvDrawingNumber, tvTitle;
        private ImageView ivPDF, ivBWG, ivOFT, ivPreview, ivMoreInfo, ivRefresh;
        private LinearLayout ll_Container;

        public MyViewHolder(View view) {
            super(view);
            ll_Container = view.findViewById(R.id.ll_Container);
            tvTitle = view.findViewById(R.id.tvTitle);
            tvRevision = view.findViewById(R.id.tvRevision);
            tvDrawingNumber = view.findViewById(R.id.tvDrawingNumber);
            ivPDF = view.findViewById(R.id.ivPDF);
            ivBWG = view.findViewById(R.id.ivBWG);
            ivOFT = view.findViewById(R.id.ivOFT);
            ivPreview = view.findViewById(R.id.ivPreview);
            ivMoreInfo = view.findViewById(R.id.ivMoreInfo);
            ivRefresh = view.findViewById(R.id.ivRefresh);
        }
    }
}