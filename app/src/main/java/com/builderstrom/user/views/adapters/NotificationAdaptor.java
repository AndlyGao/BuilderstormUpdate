package com.builderstrom.user.views.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.builderstrom.user.R;
import com.builderstrom.user.repository.retrofit.modals.NotificationIconModel;
import com.builderstrom.user.repository.retrofit.modals.NotificationListModel;
import com.builderstrom.user.utils.CommonMethods;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationAdaptor extends RecyclerView.Adapter<NotificationAdaptor.MyNotificationAdapter> {

    public NotificationCallback notificationCallback;
    private Context mContext;
    private List<NotificationListModel> notificationList;

    public NotificationAdaptor(Context mContext, List<NotificationListModel> notificationList, NotificationCallback notificationCallback) {
        this.mContext = mContext;
        this.notificationList = notificationList;
        this.notificationCallback = notificationCallback;
    }

    @NonNull
    @Override
    public MyNotificationAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyNotificationAdapter(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyNotificationAdapter holder, int position) {
        NotificationListModel model = notificationList.get(position);
        holder.tvTitle.setText(model.getType());
        holder.tvDescription.setText(model.getTitle());
        holder.tvDate.setText(model.getCreatedOn());
        for (NotificationIconModel iconModel : CommonMethods.getNotificationIcon()) {
            if (iconModel.getTitle().equalsIgnoreCase(model.getType())) {
                holder.ivType.setImageResource(iconModel.getImage_id());
                break;
            }
        }
        holder.rlNotification.setBackgroundColor(model.getViewStatus() == 0 ? mContext.getResources().getColor(R.color.notification_color) : mContext.getResources().getColor(R.color.noti_color));

        holder.rlNotification.setOnClickListener(v -> {
            if (model.getViewStatus().equals(0))
                notificationCallback.callNotification(model.getId());
            model.setViewStatus(1);
            holder.rlNotification.setBackgroundColor(mContext.getResources().getColor(R.color.noti_color));
            new AlertDialog.Builder(mContext)
                    .setTitle(model.getType())
                    .setMessage(model.getTitle())
                    .setPositiveButton("Ok", (dialog, which) -> {

                        dialog.dismiss();
                    })
                    .create().show();
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public interface NotificationCallback {
        void callNotification(int notificationId);
    }

    public class MyNotificationAdapter extends RecyclerView.ViewHolder {
        @BindView(R.id.rlNotification)
        RelativeLayout rlNotification;
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvDescription)
        TextView tvDescription;
        @BindView(R.id.ivType)
        ImageView ivType;
        @BindView(R.id.tvDate)
        TextView tvDate;

        public MyNotificationAdapter(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
