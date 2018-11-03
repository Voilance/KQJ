package com.biketomotor.kqj.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biketomotor.kqj.Activity.SettingActivity;
import com.biketomotor.kqj.Class.User;
import com.biketomotor.kqj.R;

public class MineFragment
        extends Fragment
        implements View.OnClickListener {
    private static final String TAG = "TagMine";

    private RelativeLayout rlAvatar;
    private TextView tvNickname;
    private TextView tvAccount;
    private RelativeLayout rlSetting;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);

        rlAvatar = view.findViewById(R.id.rl_avatar);
        rlAvatar.setOnClickListener(this);
        tvNickname = view.findViewById(R.id.tv_nickname);
        tvAccount = view.findViewById(R.id.tv_account);
        rlSetting = view.findViewById(R.id.rl_setting);
        rlSetting.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        tvNickname.setText(User.getNickname());
        tvAccount.setText(User.getAccount());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_avatar:
                Toast.makeText(getContext(), "avatar", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rl_setting:
                SettingActivity.actionActivity(getContext());
                break;
            default:
                break;
        }
    }
}
