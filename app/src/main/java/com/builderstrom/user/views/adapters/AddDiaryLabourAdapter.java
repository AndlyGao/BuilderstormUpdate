package com.builderstrom.user.views.adapters;

import android.app.TimePickerDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.data.retrofit.api.DataNames;
import com.builderstrom.user.data.retrofit.modals.DiaryLabourModel;
import com.builderstrom.user.data.retrofit.modals.ErrorModel;
import com.builderstrom.user.data.retrofit.modals.PojoCostCode;
import com.builderstrom.user.data.retrofit.modals.ProjectHoursModel;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.viewmodels.DiaryViewModel;
import com.builderstrom.user.views.activities.AddDiary;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AddDiaryLabourAdapter extends RecyclerView.Adapter<AddDiaryLabourAdapter.MyViewHolder> {

    private Context context;
    private List<DiaryLabourModel> dataList;
    private List<String> labourTypeList;
    private List<String> payTypeList;
    private List<PojoCostCode> codeCostList;
    private boolean isLabourEnable;
    private boolean isPayEnable;
    private boolean isCostEnable;
    private boolean isLabourManual;
    private boolean isPayManual;
    private boolean isStartFinish;
    private TimePickerDialog timePickerDialog = null;
    private DiaryViewModel viewModel;


    public AddDiaryLabourAdapter(Context ctx, List<DiaryLabourModel> dataList,
                                 List<String> labourTypeList, List<String> payTypeList,
                                 List<PojoCostCode> codeCostList, boolean isLabourEnable, boolean isPayEnable,
                                 boolean isCostEnable, boolean isLabourManual, boolean isPayManual, boolean isStartFinish,
                                 DiaryViewModel viewModel) {
        this.context = ctx;
        this.dataList = dataList;
        this.labourTypeList = labourTypeList;
        this.payTypeList = payTypeList;
        this.codeCostList = codeCostList;
        this.isLabourEnable = isLabourEnable;
        this.isLabourManual = isLabourManual;
        this.isPayEnable = isPayEnable;
        this.isCostEnable = isCostEnable;
        this.isPayManual = isPayManual;
        this.isStartFinish = isStartFinish;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_diary_labour, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DiaryLabourModel model = dataList.get(position);
        if (model.getLabourType() != null && !model.getLabourType().isEmpty())
            holder.etSpinnerLT.setText(model.getLabourType());
        if (model.getPayType() != null && !model.getPayType().isEmpty())
            holder.etSpinnerPT.setText(model.getPayType());
        if (model.getNoSite() != null && !model.getNoSite().isEmpty())
            holder.etNoSite.setText(model.getNoSite());
        if (model.getStartTime() != null && !model.getStartTime().isEmpty())
            holder.tvStart.setText(model.getStartTime());
        if (model.getFinishTime() != null && !model.getFinishTime().isEmpty())
            holder.tvFinish.setText(model.getFinishTime());

        calculateTotalHours(position, holder.etTotalHours);

        if (model.getCostCode() != null && !model.getCostCode().isEmpty()) {
            holder.etSpinnerCC.setText(getCostCode(model.getCostCode()));
        }

        if (isLabourManual) {
            holder.etSpinnerLT.setFocusable(true);
            holder.etSpinnerLT.setClickable(true);
            holder.etSpinnerLT.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    dataList.get(position).setLabourType(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        } else {
            holder.etSpinnerLT.setFocusable(false);
            holder.etSpinnerLT.setClickable(false);
            holder.etSpinnerLT.setOnClickListener(v -> spinnerViewDialog(position, holder.etSpinnerLT, labourTypeList, 0));
        }

        if (isPayManual) {
            holder.etSpinnerPT.setFocusable(true);
            holder.etSpinnerPT.setClickable(true);
            holder.etSpinnerPT.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    dataList.get(position).setPayType(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        } else {
            holder.etSpinnerPT.setFocusable(false);
            holder.etSpinnerPT.setClickable(false);
            holder.etSpinnerPT.setOnClickListener(v -> spinnerViewDialog(position, holder.etSpinnerPT, payTypeList, 1));
        }

        holder.etSpinnerLT.setVisibility(isLabourEnable ? View.VISIBLE : View.GONE);
        holder.etSpinnerPT.setVisibility(isPayEnable ? View.VISIBLE : View.GONE);
        holder.tvStart.setVisibility(isStartFinish ? View.VISIBLE : View.GONE);
        holder.tvFinish.setVisibility(isStartFinish ? View.VISIBLE : View.GONE);
        holder.etSpinnerCC.setVisibility(isCostEnable ? View.VISIBLE : View.GONE);
    }

    private String getCostCode(String costCode) {
        String costName = "";
        for (PojoCostCode cc : codeCostList) {
            if (costCode.equalsIgnoreCase(cc.getId())) {
                costName = cc.getCost_code();
            }
        }
        return costName;

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private void openTimeDialog(TextView textView, int position, boolean isStart, EditText etTotalHours) {
        timePickerDialog = new TimePickerDialog(context, R.style.DatePickerTheme, (view, hourOfDay, minute) -> {
            Calendar calInstance = Calendar.getInstance();
            calInstance.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calInstance.set(Calendar.MINUTE, minute);
            DateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.US);
            if (isStart) {
                if (dataList.get(position).getFinishTime() != null && !dataList.get(position).getFinishTime().isEmpty()) {
                    if (CommonMethods.isStartBeforeEndTime(timeFormat.format(calInstance.getTime()).trim(), dataList.get(position).getFinishTime().trim())) {
                        dataList.get(position).setStartTime((timeFormat.format(calInstance.getTime())));
                        textView.setText(timeFormat.format(calInstance.getTime()));
                        timePickerDialog.dismiss();
                    } else {
                        CommonMethods.displayToast(context, "Start time should be less than the finish time");
                    }
                } else {
                    dataList.get(position).setStartTime((timeFormat.format(calInstance.getTime())));
                    textView.setText(timeFormat.format(calInstance.getTime()));
                    timePickerDialog.dismiss();
                }
            } else {
                if (dataList.get(position).getStartTime() != null && !dataList.get(position).getStartTime().isEmpty()) {
                    if (CommonMethods.isStartBeforeEndTime(dataList.get(position).getStartTime().trim(), timeFormat.format(calInstance.getTime()).trim())) {
                        dataList.get(position).setFinishTime(timeFormat.format(calInstance.getTime()));
                        textView.setText(timeFormat.format(calInstance.getTime()));
                    } else {
                        CommonMethods.displayToast(context, "Finish time should be greater than the start time");
                    }
                } else {
                    dataList.get(position).setFinishTime(timeFormat.format(calInstance.getTime()));
                    textView.setText(timeFormat.format(calInstance.getTime()));
                }
            }
            calculateTotalHours(position, etTotalHours);
        },
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE), true);
        timePickerDialog.setCancelable(true);
        timePickerDialog.show();
    }

    private void spinnerViewDialog(int adapterPosition, EditText spinnerView, List<String> optionsList, int type) {
        if (optionsList != null && !optionsList.isEmpty()) {
            ListPopupWindow popupWindow = new ListPopupWindow(context);
            popupWindow.setWidth(ListPopupWindow.MATCH_PARENT);
            popupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
            popupWindow.setAnchorView(spinnerView);
            popupWindow.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, optionsList));
            popupWindow.setOnItemClickListener((parent, view, position, id) -> {
                spinnerView.setText(optionsList.get(position));
                if (type == 0) {
                    dataList.get(adapterPosition).setLabourType(optionsList.get(position));
                } else if (type == 1) {
                    dataList.get(adapterPosition).setPayType(optionsList.get(position));
                } else if (type == 2) {
                    dataList.get(adapterPosition).setCostCode(optionsList.get(position));
                }
                popupWindow.dismiss();
            });
            popupWindow.show();
        }
    }

    private void spinnerCCViewDialog(int adapterPosition, EditText spinnerView, List<PojoCostCode> optionsList, int type) {
        if (optionsList != null && !optionsList.isEmpty()) {
            ListPopupWindow popupWindow = new ListPopupWindow(context);
            popupWindow.setWidth(ListPopupWindow.MATCH_PARENT);
            popupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
            popupWindow.setAnchorView(spinnerView);
            popupWindow.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, optionsList));
            popupWindow.setOnItemClickListener((parent, view, position, id) -> {
                spinnerView.setText(optionsList.get(position).getCost_code());
                dataList.get(adapterPosition).setCostCode(optionsList.get(position).getId());
                popupWindow.dismiss();
            });
            popupWindow.show();
        } else {
            viewModel.errorModelLD.postValue(new ErrorModel(DataNames.TYPE_SYNCED_SUCCESS, "No Cost code available"));
        }
    }


    private void calculateTotalHours(int position, EditText et) {
        if (dataList != null && !dataList.isEmpty()) {
            DiaryLabourModel dModel = dataList.get(position);
            if (dModel.getNoSite() != null && !dModel.getNoSite().isEmpty()) {
                if (isStartFinish) {
                    if (dModel.getStartTime() != null && !dModel.getStartTime().isEmpty()
                            && dModel.getFinishTime() != null && !dModel.getFinishTime().isEmpty()
                    ) {
                        long td2 = CommonMethods.timeDifferenceMins(dModel.getStartTime(), dModel.getFinishTime());
                        dModel.setTotalHours(String.valueOf(td2));
                        dModel.setSwh(CommonMethods.getSWHours(td2));
                        int siteNo = Integer.parseInt(dModel.getNoSite());
                        et.setText(CommonMethods.convertMinToHours(siteNo * td2));
                    } else {
                        et.setText(null);
                    }
                } else {
                    Float swh = ((AddDiary) context).getsWorkHours();
                    String[] time = null;
                    if (null != viewModel.mPrefs.getSelectedProject()
                            && null != viewModel.mPrefs.getSelectedProject().getProjectHours()) {
                        ProjectHoursModel projectHoursModel = viewModel.mPrefs.getSelectedProject().getProjectHours();
                        time = new String[]{projectHoursModel.getStart_time(), projectHoursModel.getEnd_time()};
                    } else {
                        time = ((AddDiary) context).getTimeArr();
                    }
                    int siteNo = Integer.parseInt(dModel.getNoSite());
                    if (null != swh && 0 != swh) {
                        et.setText(String.valueOf(siteNo * swh));
                    } else if (time != null) {
                        long td2 = CommonMethods.timeDifferenceMins(time[0], time[1]);
                        dModel.setTotalHours(String.valueOf(td2));
                        dModel.setSwh(CommonMethods.getSWHours(td2));
                        et.setText(CommonMethods.convertMinToHours(siteNo * td2));
                    } else {
                        et.setText(null);
                    }
                }
            } else {
                et.setText(null);
            }
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.etSpinnerLT)
        EditText etSpinnerLT;
        @BindView(R.id.etSpinnerPT)
        EditText etSpinnerPT;
        @BindView(R.id.etNoSite)
        EditText etNoSite;
        @BindView(R.id.tvStart)
        TextView tvStart;
        @BindView(R.id.tvFinish)
        TextView tvFinish;
        @BindView(R.id.etTotalHours)
        EditText etTotalHours;
        @BindView(R.id.etSpinnerCC)
        EditText etSpinnerCC;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            etNoSite.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    dataList.get(getAdapterPosition()).setNoSite(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (isStartFinish) {
                        if (!tvStart.getText().toString().trim().isEmpty()
                                && !tvFinish.getText().toString().trim().isEmpty()) {
                            calculateTotalHours(getAdapterPosition(), etTotalHours);
                        }
                    } else {
                        calculateTotalHours(getAdapterPosition(), etTotalHours);

                    }
                }
            });

        }


        @OnClick({R.id.etSpinnerCC, R.id.tvStart, R.id.tvFinish, R.id.ivDelete})
        public void onClick(View viewType) {
            /* change id to null whenever an existing data is changes so that it will be upload to server*/
            switch (viewType.getId()) {
                case R.id.etSpinnerCC:
                    spinnerCCViewDialog(getAdapterPosition(), etSpinnerCC, codeCostList, 2);
//                    spinnerViewDialog(getAdapterPosition(), etSpinnerCC, codeCostList.toString(), 2);
                    break;
                case R.id.tvStart:
                    openTimeDialog(tvStart, getAdapterPosition(), true, etTotalHours);
                    break;
                case R.id.tvFinish:
                    openTimeDialog(tvFinish, getAdapterPosition(), false, etTotalHours);
                    break;
                case R.id.ivDelete:
                    removeRow(getAdapterPosition());
                    break;
            }
        }

    }

    private void removeRow(int adapterPosition) {
        if (adapterPosition < dataList.size()) {
            dataList.remove(adapterPosition);
            notifyItemRemoved(adapterPosition);
            notifyItemRangeChanged(adapterPosition, dataList.size());
        }
    }

}


