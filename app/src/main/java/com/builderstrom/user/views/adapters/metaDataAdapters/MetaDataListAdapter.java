package com.builderstrom.user.views.adapters.metaDataAdapters;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.repository.retrofit.api.RestClient;
import com.builderstrom.user.repository.retrofit.modals.LocalMetaValues;
import com.builderstrom.user.repository.retrofit.modals.MetaDataField;
import com.builderstrom.user.repository.retrofit.modals.MetaOptions;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.utils.GlideApp;
import com.builderstrom.user.views.activities.BaseActivity;
import com.builderstrom.user.views.adapters.ChildAttachOfflineAdapter;
import com.builderstrom.user.views.adapters.ChildAttachOnlineAdapter;
import com.builderstrom.user.views.dialogFragments.SignatureDialogFragment;
import com.builderstrom.user.views.viewInterfaces.MetaInterfaceCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MetaDataListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int EDIT_TEXT = 0;
    private static final int DATE_PICKER = 1;
    private static final int SPINNER = 2;
    private static final int CHECK_BOX_EDIT_TEXT = 3;
    private static final int MULTI_CHECK_BOXES = 4;
    private static final int SPINNER_EDIT_TEXT = 5;
    private static final int MULTILINE_EDIT_TEXT = 6;
    private static final int RADIO = 7;
    private static final int SIGNATURE = 8;
    private static final int ATTACH_FILES = 9;
    private static final int TEXT_HEADER = 10;

    private List<MetaDataField> dataList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private MetaInterfaceCallback callback;

    public MetaDataListAdapter(Context mContext, List<MetaDataField> dataList) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case EDIT_TEXT:
                return new EditTextViewHolder(mLayoutInflater.inflate(R.layout.row_meta_edit_text, viewGroup, false));
            case MULTILINE_EDIT_TEXT:
                return new MultilineEditTextViewHolder(mLayoutInflater.inflate(R.layout.row_meta_edit_text_multi_line, viewGroup, false));
            case DATE_PICKER:
                return new DateViewHolder(mLayoutInflater.inflate(R.layout.row_meta_date_picker, viewGroup, false));
            case SPINNER:
                return new SpinnerViewHolder(mLayoutInflater.inflate(R.layout.row_meta_spinner, viewGroup, false));
            case MULTI_CHECK_BOXES:
                return new MultiCheckViewHolder(mLayoutInflater.inflate(R.layout.row_meta_multi_check, viewGroup, false));
            case CHECK_BOX_EDIT_TEXT:
                return new CheckEditViewHolder(mLayoutInflater.inflate(R.layout.row_meta_check_edit_text, viewGroup, false));
            case SPINNER_EDIT_TEXT:
                return new SpinnerEditViewHolder(mLayoutInflater.inflate(R.layout.row_inner_spinner_edit, viewGroup, false));
            case SIGNATURE:
                return new SignatureEditBoxViewHolder(mLayoutInflater.inflate(R.layout.row_meta_signature, viewGroup, false));
            case RADIO:
                return new RadioHolder(mLayoutInflater.inflate(R.layout.row_meta_radio, viewGroup, false));
            case ATTACH_FILES:
                return new FilesHolder(mLayoutInflater.inflate(R.layout.row_meta_files, viewGroup, false));
            case TEXT_HEADER:
                return new TextHeaderHolder(mLayoutInflater.inflate(R.layout.row_meta_header, viewGroup, false));
            default:
                return new EditTextViewHolder(mLayoutInflater.inflate(R.layout.row_meta_edit_text, viewGroup, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        MetaDataField metaModel = dataList.get(i);
        switch (getItemViewType(i)) {
            case EDIT_TEXT:
                if (!metaModel.getAnswerString().isEmpty()) {
                    ((EditTextViewHolder) viewHolder).etText.setText(metaModel.getAnswerString());
                } else {
                    ((EditTextViewHolder) viewHolder).etText.setText(null);
                    ((EditTextViewHolder) viewHolder).etText.setHint(isRequired(metaModel.getIsRequired(), metaModel.getFieldLabel()));
                }
                break;
            case MULTILINE_EDIT_TEXT:
                if (!metaModel.getAnswerString().isEmpty()) {
                    ((MultilineEditTextViewHolder) viewHolder).etMultiline.setText(metaModel.getAnswerString());
                } else {
                    ((MultilineEditTextViewHolder) viewHolder).etMultiline.setText(null);
                    ((MultilineEditTextViewHolder) viewHolder).etMultiline.setHint(isRequired(metaModel.getIsRequired(), metaModel.getFieldLabel()));
                }
                break;
            case DATE_PICKER:
                if (!metaModel.getAnswerString().isEmpty()) {
                    ((DateViewHolder) viewHolder).etDatePicker.setText(metaModel.getAnswerString());
                } else {
                    ((DateViewHolder) viewHolder).etDatePicker.setText(null);
                    ((DateViewHolder) viewHolder).etDatePicker.setHint(isRequired(metaModel.getIsRequired(), metaModel.getFieldLabel()));
                }
                break;
            case SPINNER:
                if (metaModel.getOptions() == null || metaModel.getOptions().isEmpty()) {
                    ((SpinnerViewHolder) viewHolder).spView.setVisibility(View.GONE);
                    ((SpinnerViewHolder) viewHolder).tvSvLabel.setVisibility(View.GONE);
                } else {

                    ((SpinnerViewHolder) viewHolder).tvSvLabel.setVisibility(View.VISIBLE);
                    ((SpinnerViewHolder) viewHolder).tvSvLabel.setHint(isRequired(metaModel.getIsRequired(),
                            metaModel.getFieldLabel()));
                    ((SpinnerViewHolder) viewHolder).spView.setVisibility(View.VISIBLE);
                    ArrayAdapter spinnerAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_dropdown_item, metaModel.getOptions());
                    ((SpinnerViewHolder) viewHolder).spView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            for (MetaOptions option : metaModel.getOptions()) {
                                option.setSelected(position == metaModel.getOptions().indexOf(option));
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    ((SpinnerViewHolder) viewHolder).spView.setAdapter(spinnerAdapter);
                }
                break;
            case MULTI_CHECK_BOXES:
                ((MultiCheckViewHolder) viewHolder).tvCbLabel.setHint(isRequired(metaModel.getIsRequired(), metaModel.getFieldLabel()));
                ((MultiCheckViewHolder) viewHolder).rvCheckBox.setAdapter(new MultiCheckAdapter(mContext, metaModel.getOptions(), metaModel::setOptions));
                break;
            case CHECK_BOX_EDIT_TEXT:
                ((CheckEditViewHolder) viewHolder).tvLabel.setHint(isRequired(metaModel.getIsRequired(), metaModel.getFieldLabel()));
                ((CheckEditViewHolder) viewHolder).rvCheckEdit.setAdapter(new CheckEditListAdapter(mContext, metaModel.getOptions(), optionsList -> dataList.get(viewHolder.getAdapterPosition()).setOptions(optionsList)));
                break;
            case SPINNER_EDIT_TEXT:
                ArrayAdapter spinnerAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_dropdown_item, metaModel.getOptions());
                ((SpinnerEditViewHolder) viewHolder).spinner.setAdapter(spinnerAdapter);
                ((SpinnerEditViewHolder) viewHolder).spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        for (MetaOptions option : metaModel.getOptions()) {
                            option.setSelected(position == metaModel.getOptions().indexOf(option));
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                for (MetaOptions option : metaModel.getOptions()) {
                    if (option.isSelected()) {
                        ((SpinnerEditViewHolder) viewHolder).spinner.setSelection(metaModel.getOptions().indexOf(option));
                    }
                }
                ((SpinnerEditViewHolder) viewHolder).editText.setText(metaModel.getAnswerString() == null || metaModel.getAnswerString().isEmpty() ? null : metaModel.getAnswerString());
                break;
            case SIGNATURE:
                ((SignatureEditBoxViewHolder) viewHolder).tvLabel.setHint(isRequired(metaModel.getIsRequired(), metaModel.getFieldLabel()));
                if (!metaModel.getAnswerString().isEmpty() && CommonMethods.isNetworkAvailable(mContext)) {
                    GlideApp.with(mContext).load(RestClient.getBaseImageUrl() + metaModel.getAnswerString()).into(((SignatureEditBoxViewHolder) viewHolder).ivSignature);
                }

                if (metaModel.getSignFile() != null) {
                    ((SignatureEditBoxViewHolder) viewHolder).ivSignature.setImageURI(Uri.fromFile(metaModel.getSignFile()));
                }
                break;
            case RADIO:
                ((RadioHolder) viewHolder).tvLabel.setHint(isRequired(metaModel.getIsRequired(), metaModel.getFieldLabel()));
                ((RadioHolder) viewHolder).rvRadio.setAdapter(new RadioListAdapter(mContext, metaModel.getOptions()));
                break;
            case ATTACH_FILES:
                ((FilesHolder) viewHolder).btnAttachments.setText(metaModel.getFieldLabel());
                if (metaModel.getMetaServerFiles() != null && !metaModel.getMetaServerFiles().isEmpty()) {
                    ((FilesHolder) viewHolder).rvAttachments.setAdapter(new ChildAttachOnlineAdapter(mContext, metaModel.getMetaServerFiles(), true));
                }

                if (null != metaModel.getMetaUploadFiles() && !metaModel.getMetaUploadFiles().isEmpty()) {
                    ((FilesHolder) viewHolder).rvAttachments.setAdapter(new ChildAttachOfflineAdapter(mContext, metaModel.getMetaUploadFiles(), true));
                }
                break;
            case TEXT_HEADER:
                ((TextHeaderHolder) viewHolder).tvHeader.setText(metaModel.getFieldLabel());
                break;

        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (dataList.get(position).getFieldType()) {
            case "textarea":
            case "blankfiller":
                return MULTILINE_EDIT_TEXT;
            case "text":
                return EDIT_TEXT;
            case "date":
                return DATE_PICKER;
            case "select":
                return SPINNER;
            case "checkbox":
                return MULTI_CHECK_BOXES;
            case "checkbox+input":
                return CHECK_BOX_EDIT_TEXT;
            case "select+input":
                return SPINNER_EDIT_TEXT;
            case "signature":
                return SIGNATURE;
            case "radio":
                return RADIO;
            case "file":
                return ATTACH_FILES;
            case "textheader":
                return TEXT_HEADER;
            default:
                return EDIT_TEXT;
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private String isRequired(String isRequired, String fieldLabel) {
        return isRequired != null && isRequired.equalsIgnoreCase("1")
                ? fieldLabel + "*" : fieldLabel;
    }

    public void setCallback(MetaInterfaceCallback callback) {
        this.callback = callback;
    }

    public List<LocalMetaValues> getMetaValuesView() {
        List<LocalMetaValues> valuesList = new ArrayList<>();
        for (MetaDataField model : dataList) {
            switch (model.getFieldType()) {
                case "textarea":
                case "text":
                case "blankfiller":
                case "date":
//                case "textheader":
                    valuesList.add(new LocalMetaValues(model.getFieldLabel(), model.getAnswerString()));
                    break;
                case "select":
                case "checkbox":
                case "checkbox+input":
                case "select+input":
                case "radio":
                    if (!model.getOptions().isEmpty()) {
                        valuesList.add(new LocalMetaValues(model.getFieldLabel(), getOptionsString(model.getOptions())));
                    }
                    break;
                case "file":
                    if (model.getMetaUploadFiles() != null && !model.getMetaUploadFiles().isEmpty()) {
                        LocalMetaValues values = new LocalMetaValues(model.getFieldLabel(), model.getMetaUploadFiles().get(0));
                        values.setAttachment(true);
                        valuesList.add(values);
                    }
                    break;
                case "signature":
                    if (model.getSignFile() != null) {
                        LocalMetaValues values = new LocalMetaValues(model.getFieldLabel(), model.getSignFile().getAbsolutePath());
                        values.setAttachment(true);
                        valuesList.add(values);
                    }
                    break;
            }
        }
        return valuesList;
    }

    public List<LocalMetaValues> getMetaValuesList() {
        List<LocalMetaValues> valuesList = new ArrayList<>();
        for (MetaDataField model : dataList) {
            switch (model.getFieldType()) {
                case "textarea":
                case "text":
                case "blankfiller":
                case "date":
                    valuesList.add(new LocalMetaValues(model.getId(), model.getAnswerString()));
                    break;
                case "select":
                    if (!model.getOptions().isEmpty()) {
                        for (MetaOptions option : model.getOptions()) {
                            if (option.isSelected()) {
//                                valuesList.add(new LocalMetaValues(option.getCustomFieldId(), option.getId()/*option.getOptionName()*/, model.getAnswerString()));
                                valuesList.add(new LocalMetaValues(option.getCustomFieldId(), option.getId(), model.getAnswerString()));
                            }
                        }
                    }
                    break;
                case "checkbox":
                case "radio":
                    if (!model.getOptions().isEmpty()) {
                        for (MetaOptions option : model.getOptions()) {
                            if (option.isSelected()) {
                                valuesList.add(new LocalMetaValues(option.getCustomFieldId(), option.getOptionName()));
                            }
                        }
                    }
                    break;
                case "checkbox+input":
                    if (!model.getOptions().isEmpty()) {
                        for (MetaOptions option : model.getOptions()) {
                            if (option.isSelected()) {
//                                valuesList.add(new LocalMetaValues(option.getCustomFieldId(), option.getOptionName(), option.getAnswerString()));
                                valuesList.add(new LocalMetaValues(option.getCustomFieldId(), option.getOptionName(), option.getAnswerString()));
                            }
                        }
                    }
                    break;
                case "select+input":
                    if (!model.getOptions().isEmpty()) {
                        for (MetaOptions option : model.getOptions()) {
                            if (option.isSelected()) {
                                valuesList.add(new LocalMetaValues(option.getCustomFieldId(), option.getOptionName(), model.getAnswerString()));
                            }
                        }
                    }
                    break;
                case "file":
                    if (model.getMetaUploadFiles() != null && !model.getMetaUploadFiles().isEmpty()) {
                        LocalMetaValues values = new LocalMetaValues(model.getId(), model.getMetaUploadFiles().get(0));
                        values.setAttachment(true);
                        valuesList.add(values);
                    }
                    break;
                case "signature":
                    if (model.getSignFile() != null) {
                        LocalMetaValues values = new LocalMetaValues(model.getId(), model.getSignFile().getAbsolutePath());
                        values.setAttachment(true);
                        valuesList.add(values);
                    }
                    break;
            }
        }
        return valuesList;
    }

    private String getOptionsString(List<MetaOptions> options) {
        StringBuilder value = new StringBuilder();
        for (MetaOptions option : options) {
            if (option.isSelected()) {
                value.append(option.getOptionName()).append(option.getAnswerString().isEmpty() ? "" : " - " + option.getAnswerString());
            }
        }

        return value.toString();

    }

    public Boolean isProperFilled() {
        for (MetaDataField model : dataList) {
            if (model.getIsRequired() != null && model.getIsRequired().equals("1")) {
                switch (model.getFieldType()) {
                    case "textarea":
                    case "text":
                    case "date":
                    case "blankfiller":
                        if (model.getAnswerString().isEmpty()) {
                            ((BaseActivity) mContext).errorMessage(model.getFieldLabel() + " must not be empty.", null, false);
                            return false;
                        }
                        break;
                    case "select":
                    case "checkbox":
                    case "checkbox+input":
                    case "select+input":
                    case "radio":
                        if (isOptionUnSelected(model.getOptions())) {
                            ((BaseActivity) mContext).errorMessage(model.getFieldLabel() + " must not be empty.", null, false);
                            return false;
                        }
                        break;
                }
            }
        }
        return true;
    }

    private boolean isOptionUnSelected(List<MetaOptions> options) {
        for (MetaOptions option : options) {
            if (option.isSelected()) {
                return false;
            }
        }
        return true;
    }

    private class EditTextViewHolder extends RecyclerView.ViewHolder {
        private EditText etText;

        private EditTextViewHolder(@NonNull View itemView) {
            super(itemView);
            etText = itemView.findViewById(R.id.etText);
            etText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    dataList.get(getAdapterPosition()).setAnswerString(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    private class MultilineEditTextViewHolder extends RecyclerView.ViewHolder {
        private EditText etMultiline;

        private MultilineEditTextViewHolder(@NonNull View itemView) {
            super(itemView);
            etMultiline = itemView.findViewById(R.id.etText);
            etMultiline.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    dataList.get(getAdapterPosition()).setAnswerString(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    private class DateViewHolder extends RecyclerView.ViewHolder {
        private EditText etDatePicker;

        private DateViewHolder(@NonNull View itemView) {
            super(itemView);
            etDatePicker = itemView.findViewById(R.id.etText);
            etDatePicker.setOnClickListener(v -> {
                CommonMethods.hideKeyboard(mContext, etDatePicker);
                DatePickerDialog pickerDialog = new DatePickerDialog(mContext, R.style.DatePickerTheme, (view, year, month, dayOfMonth) -> {
                    Calendar calendarInstance = Calendar.getInstance();
                    calendarInstance.set(Calendar.YEAR, year);
                    calendarInstance.set(Calendar.MONTH, month);
                    calendarInstance.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                    dataList.get(getAdapterPosition()).setAnswerString(sdf.format(calendarInstance.getTime()));
                    etDatePicker.setText(sdf.format(calendarInstance.getTime()));
                }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                pickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                pickerDialog.show();
            });

        }
    }

    class SpinnerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.svOptions)
        Spinner spView;
        @BindView(R.id.tvSvLabel)
        TextView tvSvLabel;

        private SpinnerViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private class SpinnerEditViewHolder extends RecyclerView.ViewHolder {
        private Spinner spinner;
        private EditText editText;

        private SpinnerEditViewHolder(@NonNull View itemView) {
            super(itemView);
            spinner = itemView.findViewById(R.id.spinner);
            editText = itemView.findViewById(R.id.etSpinner);
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    dataList.get(getAdapterPosition()).setAnswerString(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }
    }

    class MultiCheckViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rvMultiCheck)
        RecyclerView rvCheckBox;
        @BindView(R.id.tvCbLabel)
        TextView tvCbLabel;

        private MultiCheckViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    /* Used in viewing diary data listing in diary fragment */

    private class CheckEditViewHolder extends RecyclerView.ViewHolder {
        private TextView tvLabel;
        private RecyclerView rvCheckEdit;

        private CheckEditViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLabel = itemView.findViewById(R.id.tvLabel);
            rvCheckEdit = itemView.findViewById(R.id.rvCheckEdit);
        }
    }

    private class SignatureEditBoxViewHolder extends RecyclerView.ViewHolder {
        private FrameLayout ll_Signature;
        private ImageView ivSignature;
        private TextView tvLabel;

        private SignatureEditBoxViewHolder(@NonNull View itemView) {
            super(itemView);
            ll_Signature = itemView.findViewById(R.id.fl_Signature);
            ivSignature = itemView.findViewById(R.id.ivSignature);
            tvLabel = itemView.findViewById(R.id.tvSignLabel);

            itemView.setOnClickListener(v -> {
                SignatureDialogFragment fragment = new SignatureDialogFragment();
                fragment.setmCallback(imageFile -> {
                    if (imageFile != null) {
                        dataList.get(getAdapterPosition()).setAnswerString("");
                        dataList.get(getAdapterPosition()).setSignFile(imageFile);
                        ivSignature.setImageURI(Uri.fromFile(imageFile));
                    }

                });
                fragment.show(((BaseActivity) mContext).getSupportFragmentManager(), "signature");
            });
        }
    }

    private class RadioHolder extends RecyclerView.ViewHolder {
        private TextView tvLabel;
        private RecyclerView rvRadio;

        private RadioHolder(@NonNull View itemView) {
            super(itemView);
            tvLabel = itemView.findViewById(R.id.tvLabel);
            rvRadio = itemView.findViewById(R.id.rvRadio);
        }

    }

    private class FilesHolder extends RecyclerView.ViewHolder {
        private AppCompatButton btnAttachments;
        private RecyclerView rvAttachments;

        private FilesHolder(@NonNull View itemView) {
            super(itemView);
            btnAttachments = itemView.findViewById(R.id.btnAttach);
            rvAttachments = itemView.findViewById(R.id.rvAttachments);
            btnAttachments.setOnClickListener(v -> {
                if (callback != null) callback.onAttachmentClick(getAdapterPosition());
            });

        }
    }

    private class TextHeaderHolder extends RecyclerView.ViewHolder {

        private TextView tvHeader;

        private TextHeaderHolder(@NonNull View itemView) {
            super(itemView);
            tvHeader = itemView.findViewById(R.id.tvHeader);

        }
    }

}
