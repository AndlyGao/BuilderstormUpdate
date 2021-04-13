package com.builderstrom.user.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.repository.retrofit.modals.ToDoClassicFilter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ToDoFilterAdapter extends RecyclerView.Adapter<ToDoFilterAdapter.MyViewHolder> {

    private List<ToDoClassicFilter> filterList;
    private Context context;
    private CallbackFilter callbackFilter;

    public ToDoFilterAdapter(Context context, List<ToDoClassicFilter> filterList, CallbackFilter callbackFilter) {
        this.context = context;
        this.filterList = filterList;
        this.callbackFilter = callbackFilter;
    }

    @NonNull
    @Override
    public ToDoFilterAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_todo_filter, parent, false);
        return new ToDoFilterAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoFilterAdapter.MyViewHolder holder, final int position) {
        ToDoClassicFilter model = filterList.get(position);
        holder.tvFilterName.setText(model.getName());
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    public interface CallbackFilter {
        void callRequest(String categoryId);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvFilterName) TextView tvFilterName;
        @BindView(R.id.ll_Container) LinearLayout ll_Container;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.ll_Container)
        public void onClick() {
            if (!filterList.isEmpty()) {
                callbackFilter.callRequest(filterList.get(getAdapterPosition()).getCategory());
            }
        }
    }
}
