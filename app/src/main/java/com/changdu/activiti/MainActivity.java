package com.changdu.activiti;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.changdu.R;
import com.changdu.activiti.base.BaseActivity;
import com.changdu.view.fragment.HomeFragment;
import com.changdu.view.fragment.MimeFragment;
import com.changdu.view.fragment.ProductFragment;
import com.changdu.view.fragment.StatisticsFragment;
import com.pgyersdk.update.PgyUpdateManager;

public class MainActivity extends BaseActivity {

    private final int STATISTICS_MENU_ID = Menu.FIRST + 1;
    private final int PRODUCT_MENU_ID = Menu.FIRST + 2;
    private final int MIME_MENU_ID = Menu.FIRST + 3;

    private long mExitTime;

    private TextView mTvTitle;

    private FragmentManager fm;
    private HomeFragment mHomeFragment;
    private StatisticsFragment mStatisticsFragment;
    private ProductFragment mProductFragment;
    private MimeFragment mMimeFragment;
    private Fragment mCurrent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);
        mContext = this;

        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        View mActionBarView = LayoutInflater.from(this).inflate(R.layout.actionbar_layout, null);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(mActionBarView, lp);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        mTvTitle = findViewById(R.id.actionbar_title);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        addMenuItems(navView);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // 默认显示首页
        mHomeFragment  = new HomeFragment();
        fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.content_layout, mHomeFragment);
        fragmentTransaction.commit();

        // 检查更新
        checkVersion();
    }

    /**
     * 动态变化底部导航栏
     * @param navView
     */
    private void addMenuItems(BottomNavigationView navView) {
        Menu menu = navView.getMenu();
        int groupId = menu.getItem(0).getGroupId();

        //MenuItem tongjiItem = menu.add(groupId, STATISTICS_MENU_ID, 2, "统计报表");
        //tongjiItem.setIcon(R.drawable.ic_dashboard_black_24dp);

        //MenuItem wupinItem = menu.add(groupId, PRODUCT_MENU_ID, 3, "物品信息");
        //wupinItem.setIcon(R.drawable.ic_notifications_black_24dp);

        MenuItem mimeItem = menu.add(groupId, MIME_MENU_ID, 4, "个人中心");
        mimeItem.setIcon(R.drawable.ic_person_outline_black_24dp);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            hideAllFragment(fragmentTransaction);

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if (mHomeFragment == null) {
                        mHomeFragment = new HomeFragment();
                        fragmentTransaction.add(R.id.content_layout, mHomeFragment);
                    } else {
                        mCurrent = mHomeFragment;
                        fragmentTransaction.show(mHomeFragment);
                    }
                    fragmentTransaction.commit();
                    mTvTitle.setText("掌佳科技");
                    return true;
                case STATISTICS_MENU_ID:
                    if (mStatisticsFragment == null) {
                        mStatisticsFragment = new StatisticsFragment();
                        fragmentTransaction.add(R.id.content_layout, mStatisticsFragment);
                    } else {
                        mCurrent = mStatisticsFragment;
                        fragmentTransaction.show(mStatisticsFragment);
                    }
                    fragmentTransaction.commit();
                    mTvTitle.setText(getString(R.string.title_statistics));
                    return true;
                case PRODUCT_MENU_ID:
                    if (mProductFragment == null) {
                        mProductFragment = new ProductFragment();
                        fragmentTransaction.add(R.id.content_layout, mProductFragment);
                    } else {
                        mCurrent = mProductFragment;
                        fragmentTransaction.show(mProductFragment);
                    }
                    fragmentTransaction.commit();
                    mTvTitle.setText(getString(R.string.title_product));
                    return true;
                case MIME_MENU_ID:
                    if (mMimeFragment == null) {
                        mMimeFragment = new MimeFragment();
                        fragmentTransaction.add(R.id.content_layout, mMimeFragment);
                    } else {
                        mCurrent = mMimeFragment;
                        fragmentTransaction.show(mMimeFragment);
                    }
                    fragmentTransaction.commit();
                    mTvTitle.setText(getString(R.string.title_mime));
                    return true;
            }

            return false;
        }
    };

    private void hideAllFragment(FragmentTransaction ft) {
        hideFragment(mHomeFragment, ft);
        hideFragment(mStatisticsFragment, ft);
        hideFragment(mProductFragment, ft);
        hideFragment(mMimeFragment, ft);
    }

    private void hideFragment(Fragment fragment, FragmentTransaction ft) {
        if (fragment != null) {
            ft.hide(fragment);
        }
    }

    /**
     * 检查更新
     */
    private void checkVersion() {
        Log.e("TAG", "检查是否有新版本更新...");
        new PgyUpdateManager.Builder()
                .setForced(false)                //设置是否强制提示更新
                // v3.0.4+ 以上同时可以在官网设置强制更新最高低版本；网站设置和代码设置一种情况成立则提示强制更新
                .setUserCanRetry(false)         //失败后是否提示重新下载
                .setDeleteHistroyApk(true)     // 检查更新前是否删除本地历史 Apk， 默认为true
                .register();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 判断两次点击的时间间隔（默认设置为2秒）
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                showToast("再按一次退出程序");
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
                super.onBackPressed();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
