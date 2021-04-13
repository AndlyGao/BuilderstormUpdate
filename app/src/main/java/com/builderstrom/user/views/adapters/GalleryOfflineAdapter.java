package com.builderstrom.user.views.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.repository.retrofit.modals.GalleryData;
import com.builderstrom.user.repository.retrofit.modals.GalleryPicModel;
import com.builderstrom.user.viewmodels.ProjectPhotosVM;
import com.builderstrom.user.views.activities.ImageFullScreenActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.builderstrom.user.views.fragments.HomeMenuFragment.userPermissions;

public class GalleryOfflineAdapter extends RecyclerView.Adapter<GalleryOfflineAdapter.MyViewHolder> {

    private boolean clicked = false;
    private Context context;
    private List<GalleryData> galleryModelData;
    private int SelectedPosition;
    private CallbackGallery mCallbackGallery;
    private ProjectPhotosVM viewModel;


    public GalleryOfflineAdapter(Context context, List<GalleryData> galleryModelData,
                                 CallbackGallery mCallbackGallery, ProjectPhotosVM viewModel) {
        this.context = context;
        this.galleryModelData = galleryModelData;
        this.mCallbackGallery = mCallbackGallery;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvTitle.setText(galleryModelData.get(position).getTitle());
        holder.recyclerViewChild.setAdapter(new GalleryOfflineChildAdapter(context, galleryModelData.get(position).getPics(), this, (imagePath, imageView) -> {
            if (imagePath != null && !imagePath.isEmpty()) {
                Intent intent = new Intent(context, ImageFullScreenActivity.class);
                intent.putExtra("filePath", imagePath);
                context.startActivity(intent);

//                Intent intent = new Intent(context, ImageFullScreenActivity.class);
//                intent.putExtra("filePath", imagePath);
//                if (CommonMethods.hasLollipop()) {
//                    ActivityOptionsCompat options = ActivityOptionsCompat.
//                            makeSceneTransitionAnimation((Activity) context, imageView, "profile");
//                    context.startActivity(intent, options.toBundle());
//                } else {
//                    context.startActivity(intent);
//                }
            }
        }));

        holder.recyclerViewParent.setAdapter(new GalleryItemParentAdapter(context, galleryModelData.get(position).getPics(), () -> {
            if (!galleryModelData.get(position).getPics().isEmpty()) {
                if (clicked) {
                    holder.relativeLayoutItemChild.setVisibility(View.GONE);
                    clicked = false;
                } else {
                    holder.relativeLayoutItemChild.setVisibility(View.VISIBLE);
                    clicked = true;
                }
            }
        }));

        if (!galleryModelData.get(position).getPics().isEmpty()) {
            holder.relativeLayoutItemParent.setOnClickListener(view -> {
                if (clicked) {
                    holder.relativeLayoutItemChild.setVisibility(View.GONE);
                    clicked = false;
                } else {
                    holder.relativeLayoutItemChild.setVisibility(View.VISIBLE);
                    clicked = true;
                }
            });
        }
        holder.ivRefresh.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return galleryModelData.size();
    }

    private void dialogDeleteGallery(String title, final String galleryID) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_gallery_operations);
        dialog.setCanceledOnTouchOutside(false);
        TextView tvMessage = dialog.findViewById(R.id.tvMessage);
        tvMessage.setText(String.format("Do you want to delete the '%s' gallery item?", title));
        dialog.findViewById(R.id.btnConfirm).setOnClickListener(view -> {
            viewModel.deleteGalleryAPI(galleryID, SelectedPosition);
            dialog.dismiss();
        });
        dialog.findViewById(R.id.btnCancel).setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    public interface CallbackGallery {
        void updateGallery(String rowID, String title, List<GalleryPicModel> pics, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.ivRefresh) ImageView ivRefresh;
        @BindView(R.id.recyclerViewParent) RecyclerView recyclerViewParent;
        @BindView(R.id.recyclerViewChild) RecyclerView recyclerViewChild;
        @BindView(R.id.relativeLayoutItemParent) RelativeLayout relativeLayoutItemParent;
        @BindView(R.id.relativeLayoutItemChild) RelativeLayout relativeLayoutItemChild;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.btnAction)
        public void onClick(View view) {
            PopupMenu popup = new PopupMenu(context, view);
            popup.getMenuInflater().inflate(R.menu.popupmenu, popup.getMenu());
            Menu popupMenu = popup.getMenu();
            if (userPermissions != null) {
                popupMenu.findItem(R.id.item_upload_more).setVisible(userPermissions.getPhotoGallery().getUploadPhoto() != null
                        && userPermissions.getPhotoGallery().getUploadPhoto().equalsIgnoreCase("on"));

                popupMenu.findItem(R.id.item_delete_gallery).setVisible(userPermissions.getPhotoGallery().getDeleteGallery() != null
                        && userPermissions.getPhotoGallery().getDeleteGallery().equalsIgnoreCase("on"));
            }
            popup.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.item_upload_more:
                        mCallbackGallery.updateGallery(galleryModelData.get(getAdapterPosition()).getRowId(),
                                galleryModelData.get(getAdapterPosition()).getTitle(), galleryModelData.get(getAdapterPosition()).getPics(), getAdapterPosition());
                        break;
                    case R.id.item_delete_gallery:
                        SelectedPosition = getAdapterPosition();
                        dialogDeleteGallery(galleryModelData.get(getAdapterPosition()).getTitle(), galleryModelData.get(getAdapterPosition()).getRowId());
                        break;
                }
                return true;
            });
            popup.show();
        }

    }

}


