package com.builderstrom.user.views.dialogFragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.builderstrom.user.R;
import com.builderstrom.user.utils.CommonMethods;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;


public class DateFilterDialogFragment extends BaseDialogFragment {

    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.etStartDate) EditText etStartDate;
    @BindView(R.id.etEndDate) EditText etEndDate;
    @BindView(R.id.btnSave) Button btnSave;
    private FilterDateRange filterDateRange;
    private Calendar calendar = Calendar.getInstance();

    public static DateFilterDialogFragment newInstance(String title) {
        DateFilterDialogFragment dialogFragment = new DateFilterDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Override
    protected boolean isFullScreenDialog() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_filtration_date_todo;
    }

    @Override
    protected void bindViews(View view) {
        if (getArguments() != null) {
            tvTitle.setText(getArguments().getString("title"));
        }
    }

    @OnClick({R.id.ivClose, R.id.etStartDate, R.id.etEndDate, R.id.btnSave})
    public void onClick(View view) {
        DatePickerDialog pickerDialog = null;
        switch (view.getId()) {
            case R.id.ivClose:
                dismissWithHideKeyboard();
                break;
            case R.id.etStartDate:
                pickerDialog = new DatePickerDialog(getActivity(), R.style.DatePickerTheme,
                        (pickerView, year, month, dayOfMonth) -> {
                            calendar.set(Calendar.YEAR, year);
                            calendar.set(Calendar.MONTH, month);
                            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            etStartDate.setText(new SimpleDateFormat(CommonMethods.DF_7, Locale.US).format(calendar.getTime()));
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                pickerDialog.show();
                break;

            case R.id.etEndDate:
                pickerDialog = new DatePickerDialog(getActivity(), R.style.DatePickerTheme,
                        (pickerView, year, month, dayOfMonth) -> {
                            calendar.set(Calendar.YEAR, year);
                            calendar.set(Calendar.MONTH, month);
                            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            etEndDate.setText(new SimpleDateFormat(CommonMethods.DF_7, Locale.US).format(calendar.getTime()));
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                pickerDialog.show();
                break;

            case R.id.btnSave:
                if (etStartDate.getText().toString().isEmpty()) {
                    etStartDate.setError("required field");
                } else if (etEndDate.getText().toString().isEmpty()) {
                    etEndDate.setError("required field");
                } else {
                    filterDateRange.filterDates(etStartDate.getText().toString().trim(), etEndDate.getText().toString().trim());
                    dismissWithHideKeyboard();
                }
                break;
        }
    }

    @Override
    protected void init() {

    }

    public void setFilterValues(FilterDateRange filterDateRange) {
        this.filterDateRange = filterDateRange;
    }

    public interface FilterDateRange {
        void filterDates(String id, String name);
    }
}
