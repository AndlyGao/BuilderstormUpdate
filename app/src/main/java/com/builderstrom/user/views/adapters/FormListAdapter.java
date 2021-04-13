package com.builderstrom.user.views.adapters;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.data.retrofit.api.RestClient;
import com.builderstrom.user.data.retrofit.modals.MetaOptions;
import com.builderstrom.user.data.retrofit.modals.RowFormModel;
import com.builderstrom.user.utils.BuilderStormApplication;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.utils.GlideApp;
import com.builderstrom.user.views.activities.BaseActivity;
import com.builderstrom.user.views.activities.DigitalFormActivity;
import com.builderstrom.user.views.activities.VideoPlayerActivity;
import com.builderstrom.user.views.adapters.metaDataAdapters.MultiCheckAdapter;
import com.builderstrom.user.views.adapters.metaDataAdapters.RadioListAdapter;
import com.builderstrom.user.views.dialogFragments.SignatureDialogFragment;
import com.builderstrom.user.views.fragments.ImageMarkUpFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FormListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int NOT_YET_DEFINED = -1;
    private static final int EDIT_TEXT = 0;
    private static final int DATE_PICKER = 1;
    private static final int SPINNER = 2;
    private static final int MULTI_CHECK_BOXES = 3;
    private static final int MULTILINE_EDIT_TEXT = 4;
    private static final int TEXT_VIEW_USER = 5;
    private static final int TEXT_VIEW_HEADING = 6;
    private static final int TEXT_VIEW_DATE = 7;
    private static final int TEXT_VIEW_PROJECT = 8;
    private static final int TEXT_VIEW_TIME = 9;
    private static final int TEXT_VIEW_FILLER = 10;
    private static final int EDIT_SIGNATURE = 11;
    private static final int UPLOAD_FILE = 12;
    private static final int INPUT_FILE = 13;
    private static final int RADIO = 14;
    private static final int INPUT_MARKUP_FILE = 15;
    private static final int VIEW_LINE_BREAK = 16;
    private static final int VIDEO = 17;
    private static final int TEXT_VIEW_PROJECT_NAME = 18;
    private static final int TEXT_VIEW_CLIENT = 19;
    private static final int TEXT_VIEW_INCREMENT = 20;
    private static final int TEXT_VIEW_PROJECT_ADDRESS = 21;
    private static final int MULTI_DROP_TEXT = 22;
    int screenWidth;
    private List<RowFormModel> dataList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private Integer rowPosition;
    private boolean isTabular;

    public FormListAdapter(Context mContext, Integer rowPosition, List<RowFormModel> dataList, boolean isTabular, int screenWidth) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.dataList = dataList;
        this.rowPosition = rowPosition;
        this.isTabular = isTabular;
        this.screenWidth = screenWidth;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        switch (i) {
            case EDIT_TEXT:
                return new EditTextViewHolder(setItemsWidth(mLayoutInflater.inflate(R.layout.row_meta_edit_text, viewGroup, false)));
            case MULTI_DROP_TEXT:
                return new MultiDropTextViewHolder(setItemsWidth(mLayoutInflater.inflate(R.layout.row_meta_edit_text, viewGroup, false)));
            case MULTILINE_EDIT_TEXT:
                return new MultilineEditTextViewHolder(setItemsWidth(mLayoutInflater.inflate(R.layout.row_meta_edit_text_multi_line, viewGroup, false)));
            case DATE_PICKER:
                return new DateViewHolder(setItemsWidth(mLayoutInflater.inflate(R.layout.row_meta_date_picker, viewGroup, false)));
            case SPINNER:
                return new SpinnerViewHolder(setItemsWidth(mLayoutInflater.inflate(R.layout.row_meta_spinner, viewGroup, false)));
            case MULTI_CHECK_BOXES:
                return new MultiCheckViewHolder(setItemsWidth(mLayoutInflater.inflate(R.layout.row_meta_multi_check, viewGroup, false)));
            case TEXT_VIEW_HEADING:
            case TEXT_VIEW_FILLER:
                return new TextViewHolder(setItemsWidth(mLayoutInflater.inflate(R.layout.row_meta_text_view, viewGroup, false)));
            case TEXT_VIEW_DATE:
                return new TextDateHolder(setItemsWidth(mLayoutInflater.inflate(R.layout.row_meta_text_view, viewGroup, false)));
            case TEXT_VIEW_PROJECT:
            case TEXT_VIEW_CLIENT:
            case TEXT_VIEW_INCREMENT:
            case TEXT_VIEW_PROJECT_NAME:
            case TEXT_VIEW_PROJECT_ADDRESS:
                return new TextProjectHolder(setItemsWidth(mLayoutInflater.inflate(R.layout.row_meta_text_view, viewGroup, false)));
            case TEXT_VIEW_USER:
                return new TextUserHolder(setItemsWidth(mLayoutInflater.inflate(R.layout.row_meta_text_view, viewGroup, false)));
            case TEXT_VIEW_TIME:
                return new TextTimeHolder(setItemsWidth(mLayoutInflater.inflate(R.layout.row_meta_text_view, viewGroup, false)));
            case EDIT_SIGNATURE:
                return new SignatureEditBoxViewHolder(setItemsWidth(mLayoutInflater.inflate(R.layout.row_meta_signature, viewGroup, false)));
            case UPLOAD_FILE:
                return new FilesHolder(setItemsWidth(mLayoutInflater.inflate(R.layout.row_meta_files, viewGroup, false)));
            case RADIO:
                return new RadioHolder(setItemsWidth(mLayoutInflater.inflate(R.layout.row_meta_radio, viewGroup, false)));
            case VIEW_LINE_BREAK:
                return new LineBreakHolder(setItemsWidth(mLayoutInflater.inflate(R.layout.row_line_break, viewGroup, false)));
            case INPUT_FILE:
                return new ImageViewHolder(setItemsWidth(mLayoutInflater.inflate(R.layout.row_meta_image, viewGroup, false)));
            case INPUT_MARKUP_FILE:
                return new InputMarkupHolder(setItemsWidth(mLayoutInflater.inflate(R.layout.row_meta_image, viewGroup, false)));
            case VIDEO:
                return new VideoHolder(setItemsWidth(mLayoutInflater.inflate(R.layout.row_form_video, viewGroup, false)));
            default:
                return new NotSpecifiedHolder(setItemsWidth(mLayoutInflater.inflate(R.layout.row_form_input_markup, viewGroup, false)));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        RowFormModel metaModel = dataList.get(i);
        String labelText = getWebFilteredString(metaModel.getLabel());
        metaModel.setPositionTag(String.valueOf(i));
        switch (getItemViewType(i)) {

            case EDIT_TEXT:
                if (!metaModel.getAnswerString().isEmpty()) {
                    ((EditTextViewHolder) viewHolder).etText.setText(metaModel.getAnswerString());
                } else {
                    ((EditTextViewHolder) viewHolder).etText.setText(null);
                    ((EditTextViewHolder) viewHolder).etText.setHint(mContext.getResources().getString(R.string.enter_value_here));
                }

                ((EditTextViewHolder) viewHolder).tvTextLabel.setText(labelText);
                ((EditTextViewHolder) viewHolder).etText.setClickable(metaModel.isEditableColumn());
                ((EditTextViewHolder) viewHolder).etText.setBackground(metaModel.isEditableColumn() ? mContext.getResources().getDrawable(R.drawable.edit_text_background) : mContext.getResources().getDrawable(R.drawable.edit_text_unselected));
                break;

            case MULTI_DROP_TEXT:
                try {
                    if (!dataList.get(i - 2).getAnswerString().isEmpty() && !dataList.get(i - 1).getAnswerString().isEmpty()) {
                        int totalValue = Integer.parseInt(dataList.get(i - 2).getAnswerString()) * Integer.parseInt(dataList.get(i - 1).getAnswerString());
                        metaModel.setAnswerString(String.valueOf(totalValue));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (!metaModel.getAnswerString().isEmpty()) {
                    ((MultiDropTextViewHolder) viewHolder).etText.setText(metaModel.getAnswerString());
                    if (metaModel.getColorValue() != null && !metaModel.getColorValue().isEmpty()) {
                        if (metaModel.getColor() != null && !metaModel.getColor().isEmpty()) {
                            for (int index = 0; index < metaModel.getColorValue().size(); index++) {
                                if (Integer.parseInt(metaModel.getAnswerString()) < Integer.parseInt(metaModel.getColorValue().get(index))) {
                                    metaModel.setColorCode(metaModel.getColor().get(index));
                                    ((MultiDropTextViewHolder) viewHolder).etText.setBackgroundColor(Color.parseColor(metaModel.getColor().get(index)));
                                    return;
                                }
                            }
                        }
                    }
                } else {
                    ((MultiDropTextViewHolder) viewHolder).etText.setText(null);
                    ((MultiDropTextViewHolder) viewHolder).etText.setHint(mContext.getResources().getString(R.string.enter_value_here));
                }

                ((MultiDropTextViewHolder) viewHolder).tvTextLabel.setText(labelText);
                ((MultiDropTextViewHolder) viewHolder).etText.setBackground(metaModel.isEditableColumn() ? mContext.getResources().getDrawable(R.drawable.edit_text_background) : mContext.getResources().getDrawable(R.drawable.edit_text_unselected));
                break;

            case MULTILINE_EDIT_TEXT:
                if (!metaModel.getAnswerString().isEmpty()) {
                    ((MultilineEditTextViewHolder) viewHolder).etMultiline.setText(metaModel.getAnswerString());
                } else {
                    ((MultilineEditTextViewHolder) viewHolder).etMultiline.setText(null);
                    ((MultilineEditTextViewHolder) viewHolder).etMultiline.setHint(mContext.getResources().getString(R.string.enter_value_here));
                }

                ((MultilineEditTextViewHolder) viewHolder).tvTextLabel.setText(labelText);
                ((MultilineEditTextViewHolder) viewHolder).etMultiline.setBackground(metaModel.isEditableColumn() ? mContext.getResources().getDrawable(R.drawable.edit_text_background) : mContext.getResources().getDrawable(R.drawable.edit_text_unselected));
                ((MultilineEditTextViewHolder) viewHolder).etMultiline.setClickable(metaModel.isEditableColumn());
                break;

            case DATE_PICKER:
                if (!metaModel.getAnswerString().isEmpty()) {
                    ((DateViewHolder) viewHolder).etDatePicker.setText(metaModel.getAnswerString());
                } else {
                    ((DateViewHolder) viewHolder).etDatePicker.setText(null);
                    ((DateViewHolder) viewHolder).etDatePicker.setHint(mContext.getResources().getString(R.string.enter_value_here));
                }

                ((DateViewHolder) viewHolder).tvTextLabel.setText(labelText);
                ((DateViewHolder) viewHolder).etDatePicker.setClickable(metaModel.isEditableColumn());
                ((DateViewHolder) viewHolder).etDatePicker.setBackground(metaModel.isEditableColumn() ? mContext.getResources().getDrawable(R.drawable.edit_text_background) : mContext.getResources().getDrawable(R.drawable.edit_text_unselected));
                break;

            case TEXT_VIEW_HEADING:
                if (metaModel.getHeading_type() != null && !metaModel.getHeading_type().isEmpty()) {
                    CommonMethods.setTextAppearance(mContext, updateTextAppearance(metaModel.getHeading_type()), ((TextViewHolder) viewHolder).tvText);
                }

                ((TextViewHolder) viewHolder).tvText.setText(Html.fromHtml(metaModel.getValue()));
                break;

            case TEXT_VIEW_FILLER:
                ((TextViewHolder) viewHolder).tvText.setText(Html.fromHtml(metaModel.getValue()));
                break;

            case TEXT_VIEW_USER:
                if (metaModel.getValue().isEmpty()) {
                    metaModel.setAnswerString(BuilderStormApplication.mPrefs.getUserName());
                }

                ((TextUserHolder) viewHolder).tvText.setText(combineStrings(labelText, !metaModel.getValue().isEmpty() ? metaModel.getValue() : metaModel.getAnswerString()));
                break;

            case TEXT_VIEW_TIME:
                if (metaModel.getValue().isEmpty()) {
                    metaModel.setAnswerString(CommonMethods.getCurrentDate(CommonMethods.DF_16));
                }

                ((TextTimeHolder) viewHolder).tvText.setText(combineStrings(labelText, !metaModel.getValue().isEmpty() ? metaModel.getValue() : metaModel.getAnswerString()));
                break;

            case TEXT_VIEW_PROJECT:
                if (metaModel.getValue().isEmpty()) {
                    metaModel.setAnswerString(BuilderStormApplication.mPrefs.getProjectId());
                }

                ((TextProjectHolder) viewHolder).tvText.setText(combineStrings(labelText, !metaModel.getValue().isEmpty() ? metaModel.getValue() : metaModel.getAnswerString()));
                break;

            case TEXT_VIEW_CLIENT:
                if (metaModel.getValue().isEmpty()) {
                    metaModel.setAnswerString("BuilderStorm");
                }

                ((TextProjectHolder) viewHolder).tvText.setText(combineStrings(labelText, !metaModel.getValue().isEmpty() ? metaModel.getValue() : metaModel.getAnswerString()));
                break;

            case TEXT_VIEW_INCREMENT:
            case TEXT_VIEW_PROJECT_ADDRESS:
                if (metaModel.getValue().isEmpty()) {
                    metaModel.setAnswerString("");
                }

                ((TextProjectHolder) viewHolder).tvText.setText(combineStrings(labelText, !metaModel.getValue().isEmpty() ? metaModel.getValue() : metaModel.getAnswerString()));
                break;

            case TEXT_VIEW_PROJECT_NAME:
                if (metaModel.getValue().isEmpty()) {
                    metaModel.setAnswerString(BuilderStormApplication.mPrefs.getProjectName());
                }

                ((TextProjectHolder) viewHolder).tvText.setText(combineStrings(labelText, !metaModel.getValue().isEmpty() ? metaModel.getValue() : metaModel.getAnswerString()));
                break;

            case TEXT_VIEW_DATE:
                if (metaModel.getAnswerString().isEmpty()) {
                    metaModel.setAnswerString(CommonMethods.getCurrentDate(CommonMethods.DF_2));
                }

                ((TextDateHolder) viewHolder).tvText.setText(combineStrings(labelText, (metaModel.getValue().isEmpty() ? CommonMethods.convertDate(CommonMethods.DF_2, metaModel.getAnswerString(), CommonMethods.DF_5) : metaModel.getValue())));
                break;

            case EDIT_SIGNATURE:
                if (metaModel.getAnswerString() != null && !metaModel.getAnswerString().isEmpty()) {
                    Glide.with(mContext).load(RestClient.getBaseImageUrl() + URLDecoder.decode(metaModel.getValue())).apply(RequestOptions.placeholderOf(R.drawable.ic_app_logo)).into(((SignatureEditBoxViewHolder) viewHolder).ivSignature);
                } else if (metaModel.getImageFile() != null) {
                    ((SignatureEditBoxViewHolder) viewHolder).ivSignature.setImageURI(Uri.fromFile(metaModel.getImageFile()));
                }

                ((SignatureEditBoxViewHolder) viewHolder).layoutSignature.setBackground(metaModel.isEditableColumn() ? mContext.getResources().getDrawable(R.drawable.edit_text_background) : mContext.getResources().getDrawable(R.drawable.edit_text_unselected));
                ((SignatureEditBoxViewHolder) viewHolder).layoutSignature.setClickable(metaModel.isEditableColumn());
                break;

            case UPLOAD_FILE:
                if (metaModel.getValue() != null && !metaModel.getValue().isEmpty() && null == metaModel.getMetaUploadFiles()) {
                    GlideApp.with(mContext).load(CommonMethods.decodeUrl(metaModel.getValue()))
                            .apply(new RequestOptions().error(R.mipmap.txt).placeholder(R.mipmap.txt).fitCenter())
                            .into(((FilesHolder) viewHolder).ivImage);
                } else {
                    ((FilesHolder) viewHolder).rlContainer.setVisibility(View.GONE);
                }

                if (null != metaModel.getMetaUploadFiles()) {
                    ((FilesHolder) viewHolder).rvAttachments.setAdapter(new ChildAttachOfflineAdapter(mContext, metaModel.getMetaUploadFiles(), true));
                }

                ((FilesHolder) viewHolder).rlContainer.setVisibility(View.VISIBLE);
                ((FilesHolder) viewHolder).tvFileName.setText(CommonMethods.getFileNameFromPath(metaModel.getValue()));
                ((FilesHolder) viewHolder).mConstraintLayout.setBackground(metaModel.isEditableColumn() ? mContext.getResources().getDrawable(R.drawable.edit_text_selected) : mContext.getResources().getDrawable(R.drawable.edit_text_unselected));
                ((FilesHolder) viewHolder).btnAttachments.setBackground(metaModel.isEditableColumn() ? mContext.getResources().getDrawable(R.drawable.uploadbg) : mContext.getResources().getDrawable(R.drawable.edit_text_unselected));
                ((FilesHolder) viewHolder).btnAttachments.setClickable(metaModel.isEditableColumn());
                ((FilesHolder) viewHolder).btnAttachments.setText(labelText.isEmpty() ? "Upload" : labelText);
                break;

            case INPUT_FILE:
                if (null != metaModel.getValue()) {
                    Glide.with(mContext).load(URLDecoder.decode(metaModel.getValue())).apply(RequestOptions.placeholderOf(R.drawable.ic_app_logo)).into(((ImageViewHolder) viewHolder).ivPhoto);
                }
                break;

            case SPINNER:
                if (metaModel.getOptions() != null && !metaModel.getOptions().isEmpty()) {
                    for (MetaOptions option : metaModel.getOptions()) {
                        if (option.isSelected()) {
                            metaModel.setAnswerString(option.getOptionName());
                            return;
                        }
                    }
                }

                if (metaModel.getAnswerString() != null && !metaModel.getAnswerString().isEmpty()) {
                    ((SpinnerViewHolder) viewHolder).etSpinner.setText(metaModel.getAnswerString());
                }

                ((SpinnerViewHolder) viewHolder).tvTextLabel.setText(labelText);
                ((SpinnerViewHolder) viewHolder).etSpinner.setVisibility(metaModel.getOptions() == null || metaModel.getOptions().isEmpty() ? View.GONE : View.VISIBLE);
                ((SpinnerViewHolder) viewHolder).etSpinner.setHint(selectedDropDownValue(metaModel));
                ((SpinnerViewHolder) viewHolder).etSpinner.setClickable(metaModel.isEditableColumn());
                ((SpinnerViewHolder) viewHolder).ll_container.setBackground(metaModel.isEditableColumn() ? mContext.getResources().getDrawable(R.drawable.spineer_selected) : mContext.getResources().getDrawable(R.drawable.edit_text_unselected));
                break;

            case MULTI_CHECK_BOXES:
                List<String> selectedCheckList = new ArrayList<>();
                if (metaModel.getValue() != null && !metaModel.getValue().isEmpty()) {
                    selectedCheckList.addAll(CommonMethods.getListFromCommaString(metaModel.getValue()));
                    if (selectedCheckList.size() > 0 && metaModel.getOptions().size() > 0) {
                        for (int m = 0; m < metaModel.getOptions().size(); m++) {
                            if (selectedCheckList.contains(metaModel.getOptions().get(m).getOptionName())) {
                                metaModel.getOptions().get(m).setSelected(true);
                            }
                        }
                    }
                }

                if (metaModel.getOptions() != null) {
                    ((MultiCheckViewHolder) viewHolder).rvCheckBox.setAdapter(new MultiCheckAdapter(mContext, metaModel.getOptions(), metaModel::setOptions, metaModel.isEditableColumn()));
                }

                ((MultiCheckViewHolder) viewHolder).tvCbLabel.setText(labelText);
                ((MultiCheckViewHolder) viewHolder).tvCbLabel.setVisibility(metaModel.getOptions() == null || metaModel.getOptions().isEmpty() ? View.GONE : View.VISIBLE);
                ((MultiCheckViewHolder) viewHolder).rvCheckBox.setBackground(metaModel.isEditableColumn() ? mContext.getResources().getDrawable(R.drawable.edit_text_selected) : mContext.getResources().getDrawable(R.drawable.edit_text_unselected));
                ((MultiCheckViewHolder) viewHolder).rvCheckBox.setClickable(metaModel.isEditableColumn());
                break;

            case RADIO:
                if (metaModel.getValue() != null && !metaModel.getValue().isEmpty()) {
                    for (int m = 0; m < metaModel.getOptions().size(); m++) {
                        if (metaModel.getOptions().get(m).getOptionName().equalsIgnoreCase(metaModel.getValue())) {
                            metaModel.getOptions().get(m).setSelected(true);
                        }
                    }
                }

                if (metaModel.getOptions() != null) {
                    ((RadioHolder) viewHolder).rvRadio.setAdapter(new RadioListAdapter(mContext, metaModel.getOptions(), metaModel.isEditableColumn()));
                }

                ((RadioHolder) viewHolder).tvLabel.setText(isRequired(metaModel.getIsRequired(), labelText));
                ((RadioHolder) viewHolder).rvRadio.setBackground(metaModel.isEditableColumn() ? mContext.getResources().getDrawable(R.drawable.edit_text_selected) : mContext.getResources().getDrawable(R.drawable.edit_text_unselected));
                ((RadioHolder) viewHolder).rvRadio.setClickable(metaModel.isEditableColumn());
                break;

            case INPUT_MARKUP_FILE:
                GlideApp.with(mContext).load(metaModel.getAnswerString().isEmpty() ? URLDecoder.decode(metaModel.getValue()) : metaModel.getAnswerString())
                        .apply(new RequestOptions().error(R.drawable.ic_app_logo).placeholder(R.drawable.ic_app_logo).fitCenter())
                        .into(((InputMarkupHolder) viewHolder).ivPhoto);
//                    Glide.with(mContext).load(URLDecoder.decode(metaModel.getValue())).apply(RequestOptions.placeholderOf(R.drawable.ic_app_logo)).into(((InputMarkupHolder) viewHolder).ivPhoto);
//                ((InputMarkupHolder) viewHolder).tvInputMarkup.setText(mContext.getString(R.string.input_markup_file));
                break;
            case VIDEO:
                GlideApp.with(mContext).load(CommonMethods.getVideoThumbNail(CommonMethods.decodeUrl(metaModel.getValue())))
                        .apply(new RequestOptions().error(R.drawable.ic_app_logo).placeholder(R.drawable.ic_app_logo).centerCrop())
                        .into(((VideoHolder) viewHolder).ivVideo);
                break;
            case NOT_YET_DEFINED:
                ((NotSpecifiedHolder) viewHolder).tvNotSpecified.setText(String.format("Not Specified yet---->%s", metaModel.getType()));
                break;
        }
    }

    private void showFile(TextView tvFileName, ImageView ivImage, String value) {
        GlideApp.with(mContext).load(RestClient.getBaseImageUrl() + CommonMethods.decodeUrl(value))
                .apply(new RequestOptions().error(R.mipmap.txt).placeholder(R.mipmap.txt).fitCenter())
                .into(ivImage);
        tvFileName.setText(CommonMethods.getFileNameFromPath(value));
    }

    private int updateTextAppearance(String headingType) {
        switch (headingType) {
            case "h1":
                return R.style.textAppH1;
            case "h2":
                return R.style.textAppH2;
            case "h3":
                return R.style.textAppH3;
            case "h4":
                return R.style.textAppH4;
            case "h5":
                return R.style.textAppH5;
            case "h6":
                return R.style.textAppH6;
            default:
                return R.style.textAppDefault;
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public int getItemViewType(int position) {

        switch (dataList.get(position).getType()) {
            case "textarea":
                return MULTILINE_EDIT_TEXT;
            case "text":
                return EDIT_TEXT;
            case "date_picker":
                return DATE_PICKER;
            case "heading":
                return TEXT_VIEW_HEADING;
            case "Line_Break":
            case "Blank_Filler":
                return VIEW_LINE_BREAK;
            case "text_filler":
//            case "Blank_Filler":
                return TEXT_VIEW_FILLER;
            case "today_date":
                return TEXT_VIEW_DATE;
            case "radio":
                return RADIO;
            case "project_id":
                return TEXT_VIEW_PROJECT;
            case "client":
                return TEXT_VIEW_CLIENT;
            case "project_name":
                return TEXT_VIEW_PROJECT_NAME;
            case "today_date_time":
                return TEXT_VIEW_TIME;
            case "autoincrement":
                return TEXT_VIEW_INCREMENT;
            case "loggedin_user":
                return TEXT_VIEW_USER;
            case "select":
            case "dropdown":
            case "multiplication-dropdown":
                return SPINNER;
            case "checkbox":
                return MULTI_CHECK_BOXES;
            case "signature":
                return EDIT_SIGNATURE;
            case "input_file":
            case "input":
                return INPUT_FILE;
            case "file":
            case "photo_gallery":
                return UPLOAD_FILE;
            case "video":
                return VIDEO;
            case "input_markup_file":
                return INPUT_MARKUP_FILE;
            case "project_address":
                return TEXT_VIEW_PROJECT_ADDRESS;
            case "multi_drop_text":
                return MULTI_DROP_TEXT;
            default:
                return NOT_YET_DEFINED;
        }
    }

    /* View Holders */
    private String getWebFilteredString(String value) {
        return value.replace("&nbsp;", "").trim();
    }

    private SpannableStringBuilder combineStrings(String label, String value) {
        return CommonMethods.spannedText((label.isEmpty() ? "" : label + " : ") + value, 0, label.isEmpty() ? 0 : label.length() + 2);
    }

    private String isRequired(String isRequired, String fieldLabel) {
        return isRequired != null && isRequired.equalsIgnoreCase("1")
                ? fieldLabel + "*" : fieldLabel;
    }

    public List<RowFormModel> getDataList() {
        return dataList;
    }

    public String selectedDropDownValue(RowFormModel metaModel) {
        if (metaModel.getAnswerString() != null && !metaModel.getAnswerString().isEmpty()) {
            String selected_Id = metaModel.getAnswerString();
            for (MetaOptions option : metaModel.getOptions()) {
//                if (option.getId().equalsIgnoreCase(selected_Id)) {
                if (option.getOptionName().equalsIgnoreCase(selected_Id)) {
                    option.setSelected(true);
                    return option.getOptionName();
                }
            }
        }
//        return metaModel.getLabel();
        return mContext.getResources().getString(R.string.select_value_here);
    }

    public View setItemsWidth(View itemView) {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        // Set the ViewHolder width to be a third of the screen size, and height to wrap content
        if (isTabular)
            itemView.setLayoutParams(new RecyclerView.LayoutParams(screenWidth / dataList.size(), RecyclerView.LayoutParams.WRAP_CONTENT));
//            itemView.setLayoutParams(new RecyclerView.LayoutParams((int) (1900 / dataList.size()), RecyclerView.LayoutParams.WRAP_CONTENT));
        return itemView;
    }

    class EditTextViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.etText)
        EditText etText;
        @BindView(R.id.tvTextLabel)
        TextView tvTextLabel;

        EditTextViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
//            etText.setClickable(false);
            etText.setFocusable(false);
            etText.setLongClickable(false);
//            etText.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    if (dataList.get(getAdapterPosition()).getPositionTag().equalsIgnoreCase(String.valueOf(getAdapterPosition())))
//                        dataList.get(getAdapterPosition()).setAnswerString(s.toString());
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//
//                }
//            });
        }

        @OnClick(R.id.etText)
        public void onClick() {
            final Dialog dialog = new Dialog(mContext, R.style.DialogTheme);
            dialog.setContentView(R.layout.dialog_digital_text);
            TextView tvTitle = dialog.findViewById(R.id.tvTitle);
            EditText etCustomText = dialog.findViewById(R.id.etCustomText);
            tvTitle.setText(dataList.get(getAdapterPosition()).getLabel().isEmpty() ? "Please enter value" : dataList.get(getAdapterPosition()).getLabel());
            etCustomText.setText(dataList.get(getAdapterPosition()).getAnswerString().isEmpty() ? "" : dataList.get(getAdapterPosition()).getAnswerString());
            Button btnOk = dialog.findViewById(R.id.btnSave);
            Button btnCancel = dialog.findViewById(R.id.btnCancel);
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dataList.get(getAdapterPosition()).getPositionTag().equalsIgnoreCase(String.valueOf(getAdapterPosition())))
                        dataList.get(getAdapterPosition()).setAnswerString(etCustomText.getText().toString());
                    dialog.dismiss();
                    notifyDataSetChanged();
                }
            });
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }
    }

    class MultiDropTextViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.etText)
        EditText etText;
        @BindView(R.id.tvTextLabel)
        TextView tvTextLabel;

        MultiDropTextViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            etText.setClickable(false);
            etText.setFocusable(false);
            etText.setLongClickable(false);
        }
    }

    public class TextViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvText)
        TextView tvText;

        TextViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    public class TextDateHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvText)
        TextView tvText;

        TextDateHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    public class TextUserHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvText)
        TextView tvText;

        TextUserHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    public class TextTimeHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvText)
        TextView tvText;

        TextTimeHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class TextProjectHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvText)
        TextView tvText;

        TextProjectHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class InputMarkupHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivPhoto)
        ImageView ivPhoto;

        InputMarkupHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.ivPhoto)
        public void onClick() {
            String imageUrl = CommonMethods.decodeUrl(dataList.get(getAdapterPosition()).getValue());
//            String imageUrl = CommonMethods.decodeUrl("https%3A%2F%2Fdev.builderstorm.com%2F%2Fuploads%2Fdev%2Fcustom_documents%2Ffooter_5db281308f7f3.jpeg");
            if (CommonMethods.isNetworkAvailable(mContext)) {
                if (!imageUrl.isEmpty()) {
                    ImageMarkUpFragment markUpFragment = ImageMarkUpFragment.newInstance(imageUrl);
                    markUpFragment.setCallback(new ImageMarkUpFragment.Callback() {
                        @Override
                        public void filePath(String fromFile) {
                            dataList.get(InputMarkupHolder.this.getAdapterPosition()).setAnswerString(fromFile);
                            notifyDataSetChanged();
                        }
                    });
                    ((BaseActivity) mContext).getSupportFragmentManager().beginTransaction()
                            .add(R.id.flContainer, markUpFragment, "markup fragment")
                            .addToBackStack("markup fragment").commit();

                } else {
                    CommonMethods.displayToast(mContext, "Invalid url");
                }
            } else {
                CommonMethods.displayToast(mContext, "Can't Markup in offline mode");
            }
        }
    }

    public class VideoHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivVideo)
        ImageView ivVideo;

        VideoHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.ivPlay)
        public void onClick() {
            String videoUrl = CommonMethods.decodeUrl(dataList.get(getAdapterPosition()).getValue());
            if (CommonMethods.isNetworkAvailable(mContext)) {
                if (!videoUrl.isEmpty()) {
                    mContext.startActivity(new Intent(mContext, VideoPlayerActivity.class)
                            .putExtra("videoUrl", videoUrl));
//                    ((BaseActivity) mContext).getSupportFragmentManager().beginTransaction()
//                            .add(R.id.flContainer, VideoPlayerFragment.newInstance(videoUrl), "video fragment")
//                            .addToBackStack("video fragment").commit();
                } else {
                    CommonMethods.displayToast(mContext, "Invalid url");
                }
            } else {
                CommonMethods.displayToast(mContext, "Can't play video in offline mode");
            }


        }
    }

    public class MultilineEditTextViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.etText)
        EditText etMultiline;
        @BindView(R.id.tvTextLabel)
        TextView tvTextLabel;

        MultilineEditTextViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            etMultiline.setFocusable(false);
            etMultiline.setLongClickable(false);
//            etMultiline.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    dataList.get(getAdapterPosition()).setAnswerString(s.toString());
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//
//                }
//            });


        }

        @OnClick(R.id.etText)
        public void onClick() {
            final Dialog dialog = new Dialog(mContext, R.style.DialogTheme);
            dialog.setContentView(R.layout.dialog_digital_text);
            TextView tvTitle = dialog.findViewById(R.id.tvTitle);
            EditText etCustomText = dialog.findViewById(R.id.etCustomText);
            tvTitle.setText(dataList.get(getAdapterPosition()).getLabel().isEmpty() ? "Please enter value" : dataList.get(getAdapterPosition()).getLabel());
            etCustomText.setText(dataList.get(getAdapterPosition()).getAnswerString().isEmpty() ? "" : dataList.get(getAdapterPosition()).getAnswerString());
            Button btnOk = dialog.findViewById(R.id.btnSave);
            Button btnCancel = dialog.findViewById(R.id.btnCancel);
            btnOk.setOnClickListener(v -> {
                if (dataList.get(getAdapterPosition()).getPositionTag().equalsIgnoreCase(String.valueOf(getAdapterPosition())))
                    dataList.get(getAdapterPosition()).setAnswerString(etCustomText.getText().toString());
                dialog.dismiss();
                notifyDataSetChanged();
            });
            btnCancel.setOnClickListener(v -> dialog.dismiss());
            dialog.show();
        }

    }

    public class DateViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.etText)
        EditText etDatePicker;
        @BindView(R.id.tvTextLabel)
        TextView tvTextLabel;

        DateViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.etText)
        public void onClick() {
            CommonMethods.hideKeyboard(mContext, etDatePicker);
            DatePickerDialog pickerDialog = new DatePickerDialog(mContext, R.style.DatePickerTheme, (view, year, month, dayOfMonth) -> {
                Calendar calendarInstance = Calendar.getInstance();
                calendarInstance.set(Calendar.YEAR, year);
                calendarInstance.set(Calendar.MONTH, month);
                calendarInstance.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String selectedDate = CommonMethods.getCalenderDate(CommonMethods.DF_8, calendarInstance.getTime());
                dataList.get(getAdapterPosition()).setAnswerString(selectedDate);
                etDatePicker.setText(selectedDate);
            }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
            pickerDialog.show();
        }
    }

    public class SpinnerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.etSpinner)
        EditText etSpinner;
        @BindView(R.id.ll_container)
        LinearLayout ll_container;
        @BindView(R.id.tvTextLabel)
        TextView tvTextLabel;

        SpinnerViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.etSpinner)
        public void onClick() {
            RowFormModel metaModel = dataList.get(getAdapterPosition());
            if (metaModel.getOptions() != null && !metaModel.getOptions().isEmpty()) {
                ArrayAdapter spinnerAdapter = new ArrayAdapter<>(mContext,
                        android.R.layout.simple_spinner_dropdown_item, metaModel.getOptions());
                ListPopupWindow popupWindow = new ListPopupWindow(mContext);
                popupWindow.setWidth(ListPopupWindow.MATCH_PARENT);
                popupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
                popupWindow.setAnchorView(etSpinner);
                popupWindow.setAdapter(spinnerAdapter);
                popupWindow.setOnItemClickListener((parent, view, position, id) -> {
                    for (MetaOptions option : metaModel.getOptions()) {
                        option.setSelected(position == metaModel.getOptions().indexOf(option));
                    }
                    etSpinner.setText(metaModel.getOptions().get(position).getOptionName());
                    dataList.get(getAdapterPosition()).setAnswerString(metaModel.getOptions().get(position).getOptionName());
                    notifyDataSetChanged();
                    popupWindow.dismiss();
                });
                popupWindow.show();
            }
        }
    }

    public class MultiCheckViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rvMultiCheck)
        RecyclerView rvCheckBox;
        @BindView(R.id.tvCbLabel)
        TextView tvCbLabel;

        MultiCheckViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            if (isTabular) {
                rvCheckBox.setLayoutManager(new GridLayoutManager(mContext, 2));
            } else {
                rvCheckBox.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            }
        }
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivPhoto)
        ImageView ivPhoto;

        ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class FilesHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.btnAttach)
        AppCompatButton btnAttachments;
        @BindView(R.id.rvAttachments)
        RecyclerView rvAttachments;
        @BindView(R.id.constraintLayout)
        ConstraintLayout mConstraintLayout;
        @BindView(R.id.rlContainer)
        RelativeLayout rlContainer;
        @BindView(R.id.ivImage)
        ImageView ivImage;
        @BindView(R.id.tvFileName)
        TextView tvFileName;

        FilesHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.btnAttach)
        public void onClick() {
            ((DigitalFormActivity) mContext).openExplorerOnclick(rowPosition, getAdapterPosition());
        }
    }

    /* Update values to all places */
    public class RadioHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvLabel)
        TextView tvLabel;
        @BindView(R.id.rvRadio)
        RecyclerView rvRadio;

        RadioHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class SignatureEditBoxViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivSignature)
        ImageView ivSignature;
        @BindView(R.id.fl_Signature)
        FrameLayout layoutSignature;

        SignatureEditBoxViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.fl_Signature)
        public void onClick() {
            SignatureDialogFragment fragment = new SignatureDialogFragment();
            fragment.setmCallback(imageFile -> {
                if (imageFile != null) {
                    dataList.get(getAdapterPosition()).setImageFile(imageFile);
                    ivSignature.setImageURI(Uri.fromFile(imageFile));
                }
            });
            fragment.show(((BaseActivity) mContext).getSupportFragmentManager(), "signature");
        }
    }

    public class LineBreakHolder extends RecyclerView.ViewHolder {

        LineBreakHolder(@NonNull View itemView) {
            super(itemView);
        }

    }

    public class NotSpecifiedHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvType)
        TextView tvNotSpecified;

        NotSpecifiedHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}
