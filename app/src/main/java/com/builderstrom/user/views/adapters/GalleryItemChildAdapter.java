package com.builderstrom.user.views.adapters;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.data.retrofit.modals.GalleryPicModel;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.utils.GlideApp;
import com.builderstrom.user.viewmodels.ProjectPhotosVM;
import com.builderstrom.user.views.activities.BaseActivity;
import com.builderstrom.user.views.dialogFragments.GalleryCommentDialogFragment;
import com.bumptech.glide.request.RequestOptions;

import java.net.URLDecoder;
import java.util.List;

import static com.builderstrom.user.views.fragments.HomeMenuFragment.userPermissions;

public class GalleryItemChildAdapter extends RecyclerView.Adapter<GalleryItemChildAdapter.MyViewHolder> {

    private List<GalleryPicModel> picModel;
    private Context context;
    private int selectedImage;
    private CallbackImage mCallback;
    private ProjectPhotosVM viewModel;
    private Integer prevPosition;

    public GalleryItemChildAdapter(Context ctx, List<GalleryPicModel> picModel, int prevPosition,
                                   CallbackImage mCallback, ProjectPhotosVM viewModel) {
        this.context = ctx;
        this.picModel = picModel;
        this.mCallback = mCallback;
        this.viewModel = viewModel;
        this.prevPosition = prevPosition;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_gallery_child, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int i) {
        if (picModel.get(i).getImageName() != null) {
            GlideApp.with(context).load(URLDecoder.decode(picModel.get(i).getImageName()))
                    .apply(new RequestOptions().override(180, 180)
                            .placeholder(R.color.gray).error(R.color.gray).centerCrop()).into(holder.ivItemChild);

            holder.ivItemChild.setOnClickListener(view -> mCallback.openFullImage(picModel.get(i).getImageName(), holder.ivItemChild));

            holder.ivDeleteGalleryImage.setOnClickListener(view -> {
                selectedImage = holder.getAdapterPosition();
                dialogDeleteGallery(picModel.get(i).getImageId());
            });
            holder.tvComment.setOnClickListener(view -> dialogComment(picModel.get(i).getImageId()));
        } else if (picModel.get(i).getImageBitmap() != null) {
            holder.ivItemChild.setImageBitmap(picModel.get(i).getImageBitmap());
        }


    }

    @Override
    public int getItemCount() {
        return picModel.size();
    }

    private void dialogDeleteGallery(String imageID) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_gallery_operations);
        dialog.setCanceledOnTouchOutside(false);
        TextView tvMessage = dialog.findViewById(R.id.tvMessage);
        tvMessage.setText(context.getString(R.string.sure_to_delete_image));
        dialog.findViewById(R.id.btnConfirm).setOnClickListener(view -> {
            picModel.remove(selectedImage);
            viewModel.deleteGalleryImageAPI(imageID, prevPosition, picModel);
            dialog.dismiss();
        });
        dialog.findViewById(R.id.btnCancel).setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    private void dialogComment(String imageID) {
        GalleryCommentDialogFragment dialogFragment = GalleryCommentDialogFragment.newInstance(imageID);
        dialogFragment.show(((BaseActivity) context).getSupportFragmentManager(), "comment dialog Fragment");
    }


    public interface CallbackImage {
        void openFullImage(String imageUrl, ImageView imageView);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivItemChild, ivDeleteGalleryImage;
        public TextView tvComment;

        public MyViewHolder(View view) {
            super(view);
            ivItemChild = view.findViewById(R.id.ivItemChild);
            ivDeleteGalleryImage = view.findViewById(R.id.ivDeleteGalleryImage);
            tvComment = view.findViewById(R.id.tvComment);
            if (userPermissions != null)
                CommonMethods.checkVisiblePermission(userPermissions.getPhotoGallery().getAddComments(), tvComment);
        }
    }
}
