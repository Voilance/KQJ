package com.biketomotor.kqj.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biketomotor.kqj.Activity.MainActivity;
import com.biketomotor.kqj.Activity.SettingActivity;
import com.biketomotor.kqj.Class.User;
import com.biketomotor.kqj.R;

public class MineFragment
        extends Fragment
        implements View.OnClickListener {
    private static final String TAG = "TagMine";

    private MainActivity mainActivity;
    private RelativeLayout rlAvatar;
    private static TextView tvNickname;
    private static TextView tvAccount;
    private RelativeLayout rlSetting;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);

        mainActivity = (MainActivity)getActivity();
        rlAvatar = view.findViewById(R.id.rl_avatar);
        rlAvatar.setOnClickListener(this);
        tvNickname = view.findViewById(R.id.tv_nickname);
        tvAccount = view.findViewById(R.id.tv_account);
        rlSetting = view.findViewById(R.id.rl_setting);
        rlSetting.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_avatar:
                Toast.makeText(getContext(), "avatar", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rl_setting:
                SettingActivity.actionActivity(mainActivity);
                break;
            default:
                break;
        }
    }

    public static void editView() {
        tvNickname.setText(User.getNickname());
        tvAccount.setText(User.getAccount());
    }
}
