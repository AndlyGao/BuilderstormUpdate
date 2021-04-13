package com.builderstrom.user.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.repository.retrofit.modals.DashBoardMenuModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TimeSheetMenuAdapter extends RecyclerView.Adapter<TimeSheetMenuAdapter.MyViewHolder> {

    private int selectedPosition = 0;
    private List<DashBoardMenuModel> menuList;
    private Context mContext;
    private Callback mCallback;

    public TimeSheetMenuAdapter(Context mContext, List<DashBoardMenuModel> menuList, Callback mCallback) {
        this.mContext = mContext;
        this.menuList = menuList;
        this.mCallback = mCallback;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_time_sheet_menu, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DashBoardMenuModel model = menuList.get(position);
        holder.tvTitle.setText(model.getTitle());
        holder.ivMenu.setImageResource(model.getImage_id());
        holder.itemView.setSelected(selectedPosition == position);

    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public interface Callback {
        void setItemListener(int action);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.ivMenu) ImageView ivMenu;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.clItem})
        public void onClick() {
            if (null != mCallback && selectedPosition != getAdapterPosition()) {
                selectedPosition = getAdapterPosition();
                mCallback.setItemListener(menuList.get(getAdapterPosition()).getAction_index());
                notifyDataSetChanged();
            }
        }
    }

    public void setDefaultView() {
        selectedPosition = 0;
        notifyDataSetChanged();
    }

    public void setOfflineDefaultView() {
        selectedPosition = 0;
        notifyDataSetChanged();
        if (mCallback != null)
            mCallback.setItemListener(menuList.get(selectedPosition).getAction_index());
    }

    public void setSpecificView(int position) {
        selectedPosition = position;
        notifyDataSetChanged();

    }


}
