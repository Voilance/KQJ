package com.biketomotor.kqj.Adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.biketomotor.kqj.Class.UserItem;
import com.biketomotor.kqj.R;

import java.util.List;

public class UserItemAdapter
        extends RecyclerView.Adapter<UserItemAdapter.ViewHolder> {
    private static final String TAG = "TagUserItemAdapter";

    private int colorBlue;
    private int colorGreen;
    private int colorYellow;
    private int colorRed;

    class ViewHolder
            extends RecyclerView.ViewHolder {

        private TextView tvName;
        private TextView tvStatus;

        ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_user_name);
            tvStatus = itemView.findViewById(R.id.tv_user_status);
            colorBlue = itemView.getResources().getColor(R.color.colorLightBlue);
            colorGreen = itemView.getResources().getColor(R.color.colorGreen);
            colorYellow = itemView.getResources().getColor(R.color.colorYellow);
            colorRed = itemView.getResources().getColor(R.color.colorRed);
        }
    }

    public interface onItemClickListener {
        void onItemClick(int position);
        void onItemLongClick(int position);
    }

    private List<UserItem> userItemList;
    private onItemClickListener clickListener;

    public UserItemAdapter(List<UserItem> list) {
        this.userItemList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent,false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onItemClick((Integer)v.getTag());
                }
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (clickListener != null) {
                    clickListener.onItemLongClick((Integer)v.getTag());
                }
                return true;
            }
        });
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.tvName.setText(userItemList.get(position).getRealname() + "(" + userItemList.get(position).getNickname() + ")");
        String status = userItemList.get(position).getStatus();
        if (status.equals("1")) {
            holder.tvStatus.setBackgroundColor(colorGreen);
        } else if (status.equals("2")) {
            holder.tvStatus.setBackgroundColor(colorYellow);
        } else if (status.equals("3")) {
            holder.tvStatus.setBackgroundColor(colorRed);
        } else {
            holder.tvStatus.setBackgroundColor(colorBlue);
        }
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return userItemList.size();
    }

    public void setItemClickListener(onItemClickListener listener) {
        this.clickListener = listener;
    }
}
