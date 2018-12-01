package com.biketomotor.kqj.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.biketomotor.kqj.Class.FriendItem;
import com.biketomotor.kqj.R;

import java.util.List;

public class FriendItemAdapter extends RecyclerView.Adapter<FriendItemAdapter.ViewHolder> {
    private static final String TAG = "TagFriendItemAdapter";

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private TextView tvMsg;

        ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_friend_name);
            tvMsg = itemView.findViewById(R.id.tv_friend_msg);
        }
    }

    public interface onItemClickListener {
        void onItemClick(int position);
    }

    private List<FriendItem> friendItemList;
    private onItemClickListener clickListener;

    public FriendItemAdapter(List<FriendItem> list) {
        this.friendItemList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvName.setText(friendItemList.get(position).getNickname() + "(" + friendItemList.get(position).getRealname() + ")");
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return friendItemList.size();
    }

    public void setItemClickListener(onItemClickListener listener) {
        this.clickListener = listener;
    }
}
