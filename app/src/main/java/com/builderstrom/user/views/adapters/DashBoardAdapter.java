package com.builderstrom.user.views.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.repository.retrofit.modals.DashBoardMenuModel;
import com.builderstrom.user.utils.BuilderStormApplication;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashBoardAdapter extends RecyclerView.Adapter<DashBoardAdapter.MyViewHolder> {

    private List<DashBoardMenuModel> menuList;
    private Context mContext;
    private Callback mCallback;

    public DashBoardAdapter(Context mContext, List<DashBoardMenuModel> menuList, Callback mCallback) {
        this.mContext = mContext;
        this.menuList = menuList;
        this.mCallback = mCallback;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_list_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DashBoardMenuModel model = menuList.get(position);
        holder.tvTitle.setText(model.getTitle());
        holder.ivMenuIcon.setImageResource(model.getImage_id());

        if (model.getTitle().equalsIgnoreCase("sign in") || model.getTitle().equalsIgnoreCase("sign out")) {
            if (BuilderStormApplication.mPrefs.isProjectSignIn()) {
                holder.card_view.setBackgroundColor(Color.parseColor("#ff4955"));
                holder.tvTitle.setText(mContext.getString(R.string.sign_out));
            } else {
                holder.card_view.setBackgroundColor(Color.parseColor(model.getColor()));
                holder.tvTitle.setText(model.getTitle());
            }
        } else {
            holder.card_view.setBackgroundColor(Color.parseColor(model.getColor()));
        }

        holder.card_view.setOnClickListener(v -> {
            if (null != mCallback && model != null && model.getAction_index() != -1) {
                mCallback.setItemListener(model.getAction_index(),model.getCatSection(),model.getCategoryId());
//                mCallback.setItemListener(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public interface Callback {
        void setItemListener(int position, String catSection, String categoryId);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvMenuTitle)
        TextView tvTitle;
        @BindView(R.id.ivMenuIcon)
        ImageView ivMenuIcon;
        @BindView(R.id.card_view)
        CardView card_view;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}