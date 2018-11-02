package com.biketomotor.kqj.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.biketomotor.kqj.Adapter.BottomNavigationViewHelper;
import com.biketomotor.kqj.Adapter.ViewPagerAdapter;
import com.biketomotor.kqj.Class.HttpsUtil;
import com.biketomotor.kqj.Class.Sys;
import com.biketomotor.kqj.Class.User;
import com.biketomotor.kqj.Fragment.ActivityFragment;
import com.biketomotor.kqj.Fragment.CurriculumFragment;
import com.biketomotor.kqj.Fragment.FriendFragment;
import com.biketomotor.kqj.Fragment.MineFragment;
import com.biketomotor.kqj.Fragment.SigninFragment;
import com.biketomotor.kqj.Interface.HttpsListener;
import com.biketomotor.kqj.R;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity
        extends BaseActivity {
    private static final String TAG = "TagMain";

    private ViewPager viewPager;
    private MenuItem menuItem;
    private BottomNavigationView navigationView;
    FriendFragment friendFragment;
    CurriculumFragment curriculumFragment;
    SigninFragment signinFragment;
    ActivityFragment activityFragment;
    MineFragment mineFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        // 判断用户是否已经登陆，避免该活动重复自动登陆
        if (!User.isOnline()) {
            Sys.readSP(getSharedPreferences("user", Context.MODE_PRIVATE));
            // 判断本地是否存有最近登陆的用户信息，
            // 如果有且上次应用退出时用户处于登陆状态，则自动登陆
            // 否则跳到登陆活动
            if (!Sys.getAccount().equals("") && Sys.isLogin()) {
                User.readSP(getSharedPreferences(Sys.getAccount(), Context.MODE_PRIVATE));
                HttpsUtil.sendPostRequest(HttpsUtil.loginAddress, getJsonData(), new HttpsListener() {
                    @Override
                    public void onSuccess(final String response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject data = new JSONObject(response);
                                    String result = data.getString("result");
                                    String reason = data.getString("reason");
                                    if (result.equals("true")) {
//                                        User.readJSON(data); // 利用后端返回的信息（包含用户所有基本信息）初始化User
                                        // 设置登陆状态为true并初始化页面
                                        User.setOnline(true);
                                    } else {
                                        User.setOnline(false);
                                        LoginActivity.actionActivity(MainActivity.this);
                                    }
                                } catch (JSONException e) {
                                    Log.e(TAG, "autoLogin/onSuccess:" + e.toString());
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailure(Exception exception) {
                        Log.e(TAG, "autoLogin/onFailure:" + exception.toString());
                    }
                });
            } else {
                LoginActivity.actionActivity(MainActivity.this);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.view_pager);
        navigationView = findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.disableShiftMode(navigationView);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_friends:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.item_schedule:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.item_signin:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.item_activities:
                        viewPager.setCurrentItem(3);
                        break;
                    case R.id.item_mine:
                        viewPager.setCurrentItem(4);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    navigationView.getMenu().getItem(2).setChecked(false);
                }
                menuItem = navigationView.getMenu().getItem(position);
                menuItem.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
//        // 禁止ViewPager滑动
//        viewPager.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return true;
//            }
//        });
        initViewPager(viewPager);
    }

    private void initViewPager(ViewPager pager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        if (adapter.getCount() > 0) {
            adapter.removeAllFragments();
        }
        friendFragment = new FriendFragment();
        curriculumFragment = new CurriculumFragment();
        signinFragment = new SigninFragment();
        activityFragment = new ActivityFragment();
        mineFragment = new MineFragment();
        adapter.addFragment(friendFragment);
        adapter.addFragment(curriculumFragment);
        adapter.addFragment(signinFragment);
        adapter.addFragment(activityFragment);
        adapter.addFragment(mineFragment);
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(5);
    }

    private JSONObject getJsonData() {
        JSONObject data = new JSONObject();
        try {
            data.put("account", User.getAccount());
            data.put("pwd", User.getPassword());
        } catch (JSONException e) {
            Log.e(TAG, "getJsonData:" + e.toString());
        }
        return data;
    }

}
