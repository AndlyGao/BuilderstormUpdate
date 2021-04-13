package com.builderstrom.user.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.utils.CommonMethods;
import com.builderstrom.user.repository.retrofit.modals.ProjectNote;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder> {

    private List<ProjectNote> noteList;
    private Context context;

    public NotesAdapter(Context context, List<ProjectNote> noteList) {
        this.context = context;
        this.noteList = noteList;
    }

    @NonNull
    @Override
    public NotesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);
        return new NotesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.MyViewHolder holder, final int position) {
        holder.ll_Container.setBackgroundColor(ContextCompat.getColor(context, R.color.status_bar));
        ProjectNote model = noteList.get(position);
        holder.tvComment.setText(model.getNote());
        if (model.getCreateDate() != null && !model.getCreateDate().isEmpty()) {
            holder.tvCommentDate.setText(CommonMethods.convertDate(CommonMethods.DF_1, model.getCreateDate(), CommonMethods.DF_6));
        }
        holder.tvUserName.setText(model.getUsername());
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvComment) TextView tvComment;
        @BindView(R.id.tvTime) TextView tvCommentDate;
        @BindView(R.id.tvUserName) TextView tvUserName;
        @BindView(R.id.llContainer) ConstraintLayout ll_Container;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
