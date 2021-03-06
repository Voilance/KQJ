package com.biketomotor.kqj.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.biketomotor.kqj.Class.ActivityItem;
import com.biketomotor.kqj.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ActivityItemAdapter
        extends RecyclerView.Adapter<ActivityItemAdapter.ViewHolder> {

    class ViewHolder
            extends RecyclerView.ViewHolder {

        private TextView tvName;
        private TextView tvTime;

        ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_activity_name);
            tvTime = itemView.findViewById(R.id.tv_activity_time);
        }
    }

    public interface onItemClickListener {
        void onItemClick(int position);
    }

    private List<ActivityItem> activityItemList;

    private onItemClickListener clickListener;

    public ActivityItemAdapter(List<ActivityItem> list) {
        this.activityItemList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onItemClick((Integer)v.getTag());
                }
            }
        });
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.tvName.setText(activityItemList.get(position).getName());
        holder.tvTime.setText(getTime(activityItemList.get(position).getStartTime()));
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return activityItemList.size();
    }

    public void setItemClickListener(onItemClickListener listener) {
        this.clickListener = listener;
    }

    private String getTime(String timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String time = sdf.format(new Date(Long.valueOf(timestamp)));
        return time;
    }
}
