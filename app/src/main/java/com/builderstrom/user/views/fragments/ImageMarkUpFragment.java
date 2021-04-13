package com.builderstrom.user.views.fragments;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.ChangeBounds;
import androidx.transition.TransitionManager;

import com.builderstrom.user.R;
import com.builderstrom.user.utils.GlideApp;
import com.builderstrom.user.utils.PermissionUtils;
import com.builderstrom.user.views.fragments.ImageEditor.EditingToolsAdapter;
import com.builderstrom.user.views.fragments.ImageEditor.EmojiBSFragment;
import com.builderstrom.user.views.fragments.ImageEditor.FilterListener;
import com.builderstrom.user.views.fragments.ImageEditor.FilterViewAdapter;
import com.builderstrom.user.views.fragments.ImageEditor.PropertiesBSFragment;
import com.builderstrom.user.views.fragments.ImageEditor.StickerBSFragment;
import com.builderstrom.user.views.fragments.ImageEditor.TextEditorDialogFragment;
import com.builderstrom.user.views.fragments.ImageEditor.ToolType;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import ja.burhanrashid52.photoeditor.OnPhotoEditorListener;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
import ja.burhanrashid52.photoeditor.PhotoFilter;
import ja.burhanrashid52.photoeditor.SaveSettings;
import ja.burhanrashid52.photoeditor.TextStyleBuilder;
import ja.burhanrashid52.photoeditor.ViewType;

public class ImageMarkUpFragment extends BaseFragment implements OnPhotoEditorListener,
        EditingToolsAdapter.OnItemSelected, FilterListener {

    private static final String TAG = ImageMarkUpFragment.class.getName();
    private String imageUrl = "";
    private PhotoEditor mPhotoEditor;
    private ConstraintSet mConstraintSet = new ConstraintSet();
    private boolean mIsFilterVisible;
    private PermissionUtils permission;
    private Callback callback;


    public static ImageMarkUpFragment newInstance(String imageUrl) {
        ImageMarkUpFragment fragment = new ImageMarkUpFragment();
        Bundle bundle = new Bundle();
        bundle.putString("imageUrl", imageUrl);
        fragment.setArguments(bundle);
        return fragment;
    }

    @BindView(R.id.photoEditorView) PhotoEditorView mPhotoEditorView;
    @BindView(R.id.rvConstraintTools) RecyclerView mRvTools;
    @BindView(R.id.rvFilterView) RecyclerView mRvFilters;
    @BindView(R.id.rootView) ConstraintLayout mRootView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_image_markup;
    }

    @Override
    protected void bindView(View view) {
        if (getArguments() != null) {
            imageUrl = getArguments().getString("imageUrl");
            if (imageUrl != null) {
                GlideApp.with(this).asBitmap().load(imageUrl).into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        mPhotoEditorView.getSource().setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        Log.e(TAG, "Load failure");
                    }
                });
            }
        }
    }

    @Override
    protected void init() {
        permission = new PermissionUtils(this);
        mRvTools.setAdapter(new EditingToolsAdapter(this));
        mRvFilters.setAdapter(new FilterViewAdapter(this));

        mPhotoEditor = new PhotoEditor.Builder(getActivity(), mPhotoEditorView)
                .setPinchTextScalable(true) // set flag to make text scalable when pinch
                .build(); // build photo editor sdk

        mPhotoEditor.setOnPhotoEditorListener(this);
    }

    @OnClick({R.id.ivBack, R.id.imgUndo, R.id.imgRedo, R.id.imgSave})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgUndo:
                mPhotoEditor.undo();
                break;

            case R.id.imgRedo:
                mPhotoEditor.redo();
                break;

            case R.id.imgSave:
                saveImage();
                break;

            case R.id.ivBack:
                showSaveDialog();
                break;
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    private void closeFragment() {
        if (null != getFragmentManager()) {
            getFragmentManager().popBackStackImmediate();
        }
    }

    @Override
    public void onEditTextChangeListener(final View rootView, String text, int colorCode) {
        TextEditorDialogFragment textEditorDialogFragment =
                TextEditorDialogFragment.show((AppCompatActivity) getActivity(), text, colorCode);
        textEditorDialogFragment.setOnTextEditorListener((inputText, colorCode1) -> {
            final TextStyleBuilder styleBuilder = new TextStyleBuilder();
            styleBuilder.withTextColor(colorCode1);

            mPhotoEditor.editText(rootView, inputText, styleBuilder);
        });
    }


    @Override
    public void onAddViewListener(ViewType viewType, int numberOfAddedViews) {
        Log.d(TAG, "onAddViewListener() called with: viewType = [" + viewType + "], numberOfAddedViews = [" + numberOfAddedViews + "]");
    }

    @Override
    public void onRemoveViewListener(ViewType viewType, int numberOfAddedViews) {
        Log.d(TAG, "onRemoveViewListener() called with: viewType = [" + viewType + "], numberOfAddedViews = [" + numberOfAddedViews + "]");
    }

    @Override
    public void onStartViewChangeListener(ViewType viewType) {
        Log.d(TAG, "onStartViewChangeListener() called with: viewType = [" + viewType + "]");
    }

    @Override
    public void onStopViewChangeListener(ViewType viewType) {
        Log.d(TAG, "onStopViewChangeListener() called with: viewType = [" + viewType + "]");
    }


    private void showSaveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you want to exit without saving image ?");
        builder.setPositiveButton("Save", (dialog, which) -> saveImage());
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.setNeutralButton("Discard", (dialog, which) -> closeFragment());
        builder.create().show();
    }

    @Override
    public void onFilterSelected(PhotoFilter photoFilter) {
        mPhotoEditor.setFilterEffect(photoFilter);
    }

    @Override
    public void onToolSelected(ToolType toolType) {
        switch (toolType) {
            case BRUSH:
                mPhotoEditor.setBrushDrawingMode(true);

                if (getFragmentManager() != null) {

                    PropertiesBSFragment propFragment = new PropertiesBSFragment();
                    propFragment.setPropertiesChangeListener(new PropertiesBSFragment.Properties() {
                        @Override public void onColorChanged(int colorCode) {
                            mPhotoEditor.setBrushColor(colorCode);
                        }

                        @Override public void onOpacityChanged(int opacity) {
                            mPhotoEditor.setOpacity(opacity);
                        }

                        @Override public void onBrushSizeChanged(int brushSize) {
                            mPhotoEditor.setBrushSize(brushSize);
                        }
                    });

                    propFragment.show(getFragmentManager(), "properties");
                }

                break;
            case TEXT:
                TextEditorDialogFragment textEditorDialogFragment = TextEditorDialogFragment.show((AppCompatActivity) getActivity());
                textEditorDialogFragment.setOnTextEditorListener((inputText, colorCode) -> {
                    final TextStyleBuilder styleBuilder = new TextStyleBuilder();
                    styleBuilder.withTextColor(colorCode);

                    mPhotoEditor.addText(inputText, styleBuilder);
                });
                break;
            case ERASER:
                mPhotoEditor.brushEraser();
                break;
            case FILTER:
                showFilter(true);
                break;
            case EMOJI:
                if (getFragmentManager() != null) {
                    EmojiBSFragment emojiBSFragment = new EmojiBSFragment();
                    emojiBSFragment.setEmojiListener(emojiUnicode -> mPhotoEditor.addEmoji(emojiUnicode));
                    emojiBSFragment.show(getFragmentManager(), "emoji fragment");
                }
                break;
            case STICKER:
                if (getFragmentManager() != null) {
                    StickerBSFragment mStickerBSFragment = new StickerBSFragment();
                    mStickerBSFragment.setStickerListener(bitmap -> mPhotoEditor.addImage(bitmap));
                    mStickerBSFragment.show(getFragmentManager(), mStickerBSFragment.getTag());
                }
                break;
            case CROP:
                errorMessage("crop image", null, false);
                break;
        }
    }


    void showFilter(boolean isVisible) {
        mIsFilterVisible = isVisible;
        mConstraintSet.clone(mRootView);

        if (isVisible) {
            mConstraintSet.clear(mRvFilters.getId(), ConstraintSet.START);
            mConstraintSet.connect(mRvFilters.getId(), ConstraintSet.START,
                    ConstraintSet.PARENT_ID, ConstraintSet.START);
            mConstraintSet.connect(mRvFilters.getId(), ConstraintSet.END,
                    ConstraintSet.PARENT_ID, ConstraintSet.END);
        } else {
            mConstraintSet.connect(mRvFilters.getId(), ConstraintSet.START,
                    ConstraintSet.PARENT_ID, ConstraintSet.END);
            mConstraintSet.clear(mRvFilters.getId(), ConstraintSet.END);
        }

        ChangeBounds changeBounds = new ChangeBounds();
        changeBounds.setDuration(350);
        changeBounds.setInterpolator(new AnticipateOvershootInterpolator(1.0f));
        TransitionManager.beginDelayedTransition(mRootView, changeBounds);

        mConstraintSet.applyTo(mRootView);
    }

    @SuppressLint("MissingPermission")
    private void saveImage() {
        if (permission.isPermissionGrantedForExtStorage()) {
            showProgress();
            File file = new File(Environment.getExternalStorageDirectory()
                    + File.separator + ""
                    + System.currentTimeMillis() + ".png");
            try {
                file.createNewFile();

                SaveSettings saveSettings = new SaveSettings.Builder()
                        .setClearViewsEnabled(true)
                        .setTransparencyEnabled(true)
                        .build();

                mPhotoEditor.saveAsFile(file.getAbsolutePath(), saveSettings, new PhotoEditor.OnSaveListener() {
                    @Override
                    public void onSuccess(@NonNull String imagePath) {
                        dismissProgress();
                        errorMessage("Image Saved Successfully", null, false);
                        mPhotoEditorView.getSource().setImageURI(Uri.fromFile(new File(imagePath)));
                        callback.filePath(imagePath);
                        closeFragment();
                    }

                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        dismissProgress();
                        errorMessage("Failed to save Image", null, false);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                dismissProgress();
                errorMessage(e.getMessage(), null, false);
            }
        } else {
            permission.requestFragmentPermissionForExtStorage();
        }
    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionUtils.STORAGE_REQUEST) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    saveImage();
                }
            }
        }
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void filePath(String fromFile);
    }


}
