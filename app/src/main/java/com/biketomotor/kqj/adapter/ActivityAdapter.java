package com.biketomotor.kqj.adapter;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.biketomotor.kqj.R;
import com.biketomotor.kqj.object.Activity;
import com.biketomotor.kqj.ui.*;

import java.util.List;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> {

    private List<Activity> activityList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View activityView;
        ImageView activityImage;
        TextView activityName;
        TextView activityID;

        public ViewHolder (View view) {
            super(view);
            activityView = view;
            activityImage = (ImageView)view.findViewById(R.id.item_activity_image);
            activityName = (TextView)view.findViewById(R.id.item_activity_name);
            activityID = (TextView)view.findViewById(R.id.item_activity_id);
        }
    }

    public ActivityAdapter(List<Activity> list) {
        activityList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.activityView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Activity activity = activityList.get(position);

                Intent to_activity_info = new Intent(view.getContext(), activity_activity_info.class);
//                to_activity_info.putExtra("name", activity.getName());
//                to_activity_info.putExtra("info", activity.getInfo());
//                to_activity_info.putExtra("image", activity.getImage());
                to_activity_info.putExtra("id", activity.getId());
                to_activity_info.putExtra("creater", activity.getCreater());
                view.getContext().startActivity(to_activity_info);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Activity activity = activityList.get(position);
        holder.activityImage.setImageResource(activity.getImage());
        holder.activityName.setText(activity.getName());
        holder.activityID.setText(activity.getId());
    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }

}
