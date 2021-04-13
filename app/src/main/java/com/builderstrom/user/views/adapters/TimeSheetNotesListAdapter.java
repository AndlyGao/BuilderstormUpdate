package com.builderstrom.user.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.repository.retrofit.modals.TSNote;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.viewmodels.TimeSheetViewModel;
import com.builderstrom.user.views.dialogFragments.AddTSOfflineDialogFragment;
import com.builderstrom.user.views.dialogFragments.AddTimeSheetDialogFragment;
import com.builderstrom.user.views.dialogFragments.EditTSActivityFragment;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TimeSheetNotesListAdapter extends RecyclerView.Adapter<TimeSheetNotesListAdapter.NotesHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<TSNote> list;
    private TimeSheetViewModel viewModel;
    private DialogFragment dialogFragment;

    public TimeSheetNotesListAdapter(DialogFragment dialogFragment, List<TSNote> list, TimeSheetViewModel viewModel) {
        this.mContext = dialogFragment.getActivity();
        this.dialogFragment = dialogFragment;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.list = list;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public NotesHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new NotesHolder(
                mLayoutInflater.inflate(R.layout.row_time_sheet_note, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotesHolder notesHolder, int position) {
        TSNote note = list.get(position);
        notesHolder.tvName.setText(CommonMethods.coloredSpannedText(note.getFullName() + "   "
                + CommonMethods.convertDate(CommonMethods.DF_1, note.getAddedOn(),
                CommonMethods.DF_6), 0, note.getFullName().length() + 1));
        notesHolder.tvDescription.setText(note.getNoteDescription());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void deleteNoteAt(int position) {
        if (position < list.size()) {
            list.remove(position);
            notifyItemRemoved(position);
        }
    }

    class NotesHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvDescription)
        TextView tvDescription;

        NotesHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.ivEdit, R.id.ivDelete})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ivEdit:
                    if (null != dialogFragment.getFragmentManager()) {
                        if (dialogFragment instanceof AddTimeSheetDialogFragment) {
                            AddTimeSheetDialogFragment onlineDialog = (AddTimeSheetDialogFragment) dialogFragment;
                            EditTSActivityFragment editFragment = EditTSActivityFragment.newInstance(
                                    2, onlineDialog.getGlobalDate(), list.get(getAdapterPosition()));
                            editFragment.setEditSuccessCallback(onlineDialog);
                            editFragment.show(onlineDialog.getFragmentManager(), "edit View");
                        } else {
                            AddTSOfflineDialogFragment offFragment = (AddTSOfflineDialogFragment) dialogFragment;
                            EditTSActivityFragment editFragment = EditTSActivityFragment.newInstance(
                                    2, offFragment.getGlobalDate(), list.get(getAdapterPosition()));
                            editFragment.setEditSuccessCallback(success -> {
                                list.get(NotesHolder.this.getAdapterPosition()).setNoteDescription(success);
                                viewModel.deleteOfflineNote(offFragment.getGlobalDate(),
                                        NotesHolder.this.getAdapterPosition(), new Gson().toJson(list), "0", "");
                            });
                            editFragment.show(offFragment.getFragmentManager(), "edit View");
                        }
                    }
                    break;
                case R.id.ivDelete:
                    if (dialogFragment instanceof AddTimeSheetDialogFragment) {
                        viewModel.deleteNote(list.get(getAdapterPosition()).getId(),
                                getAdapterPosition());
                    } else {
                        AddTSOfflineDialogFragment offFragment = (AddTSOfflineDialogFragment) dialogFragment;
                        list.remove(getAdapterPosition());
                        viewModel.deleteOfflineNote(offFragment.getGlobalDate(),
                                getAdapterPosition(), new Gson().toJson(list), "0", "");
                    }
                    break;
            }
        }
    }
}
