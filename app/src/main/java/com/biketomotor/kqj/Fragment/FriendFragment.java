package com.biketomotor.kqj.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.biketomotor.kqj.Activity.MainActivity;
import com.biketomotor.kqj.Activity.UserInfo;
import com.biketomotor.kqj.Adapter.FriendItemAdapter;
import com.biketomotor.kqj.Class.FriendItem;
import com.biketomotor.kqj.Class.HttpsUtil;
import com.biketomotor.kqj.Class.User;
import com.biketomotor.kqj.Interface.HttpsListener;
import com.biketomotor.kqj.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FriendFragment
        extends Fragment {
    private static final String TAG = "TagFriend";

    private static MainActivity mainActivity;
    private static List<FriendItem> friendItemList;
    private static FriendItemAdapter friendItemAdapter;
    private static RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend, container, false);

        mainActivity = (MainActivity)getActivity();
        recyclerView = view.findViewById(R.id.rv_friend_list);
        friendItemList = new ArrayList<>();
        friendItemAdapter = new FriendItemAdapter(friendItemList);
        friendItemAdapter.setItemClickListener(new FriendItemAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                UserInfo.actionActivity(mainActivity, friendItemList.get(position).getAccount(), "", 0);
            }
        });
        recyclerView.setAdapter(friendItemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));

        return view;
    }

    public static void editView() {
        getFriendList();
    }

    private static void getFriendList() {
        HttpsUtil.sendPostRequest(HttpsUtil.getFriendListAddr, getJsonData(), new HttpsListener() {
            @Override
            public void onSuccess(final String response) {
                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject data = new JSONObject(response);
                            String result = data.getString("result");
                            String reason = data.getString("reason");
                            friendItemList.clear();
                            if (result.equals("true")) {
                                JSONArray array = new JSONArray(data.getString("friend"));
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    String account = object.getString("id");
                                    String nickname = object.getString("name");
                                    String realname = object.getString("realname");
                                    friendItemList.add(new FriendItem(nickname, realname, account, "msg"));
                                }
                            }
                            friendItemAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            Log.e(TAG, "getFriendList/onSuccess:" + e.toString());
                        }
                    }
                });
            }

            @Override
            public void onFailure(Exception exception) {
                Log.e(TAG, "getFriendList/onFailure: " + exception.toString());
            }
        });
    }

    private static JSONObject getJsonData() {
        JSONObject data = new JSONObject();
        try {
            data.put("user_id", User.getAccount());
        } catch (JSONException e) {
            Log.e(TAG, "getJsonData:" + e.toString());
        }
        return data;
    }
}
