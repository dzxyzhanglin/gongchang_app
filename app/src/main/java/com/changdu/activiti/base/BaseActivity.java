package com.changdu.activiti.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.changdu.R;
import com.changdu.constant.Constant;
import com.changdu.util.DateUtil;
import com.changdu.util.JsonToMap;
import com.changdu.util.StringUtil;
import com.dou361.dialogui.DialogUIUtils;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseActivity extends AppCompatActivity {

    protected String TAG;
    protected Activity mContext;
    protected Dialog dialog;

    protected DrawerLayout drawerLayout;
    protected LinearLayout mSearchLayout;

    protected int INIT_DATA = 1; // 初始化数据
    protected int REFRESH_DATA = 2; // 刷新数据
    protected int LOAD_MORE_DATA = 3; // 加载更多数据

    static {
        ClassicsHeader.REFRESH_HEADER_PULLING = "下拉可以刷新";
        ClassicsHeader.REFRESH_HEADER_REFRESHING = "正在刷新...";
        ClassicsHeader.REFRESH_HEADER_LOADING = "正在加载...";
        ClassicsHeader.REFRESH_HEADER_RELEASE = "释放立即刷新";
        ClassicsHeader.REFRESH_HEADER_FINISH = "刷新完成";
        ClassicsHeader.REFRESH_HEADER_FAILED = "刷新失败";
        ClassicsHeader.REFRESH_HEADER_SECONDARY = "释放进入二楼";
        ClassicsHeader.REFRESH_HEADER_UPDATE = "上次更新 M-d HH:mm";

        ClassicsFooter.REFRESH_FOOTER_PULLING = "上拉加载更多";
        ClassicsFooter.REFRESH_FOOTER_RELEASE = "释放立即加载";
        ClassicsFooter.REFRESH_FOOTER_LOADING = "正在加载...";
        ClassicsFooter.REFRESH_FOOTER_REFRESHING = "正在刷新...";
        ClassicsFooter.REFRESH_FOOTER_FINISH = "加载完成";
        ClassicsFooter.REFRESH_FOOTER_FAILED = "加载失败";
        ClassicsFooter.REFRESH_FOOTER_NOTHING = "没有更多数据了";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TAG = getComponentName().getShortClassName();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 设置标题
     * @param title
     * @param showBack
     */
    protected void setTitle(String title, boolean showBack) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);
        actionBar.setDisplayHomeAsUpEnabled(showBack);
    }

    /**
     * 监听左上角返回键
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 申请指定的权限.
     */
    public void requestPermission(int code, String... permissions) {
        ActivityCompat.requestPermissions(this, permissions, code);
    }

    /**
     * 判断是否有指定的权限
     */
    public boolean hasPermission(String... permissions) {

        for (String permisson : permissions) {
            if (ContextCompat.checkSelfPermission(this, permisson)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Constant.WRITE_READ_EXTERNAL_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doSDCardPermission();
                }
                break;
        }
    }

    /**
     * 处理整个应用用中的SDCard业务
     */
    public void doSDCardPermission() {
    }

    /**
     * 隐藏状态栏
     */
    public void hiddenStatusBar() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 显示toast
     * @param msg
     */
    public void showToast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示加载中界面
     */
    public void showLoading() {
        if (dialog != null) {
            dialog.show();
        } else {
            dialog = DialogUIUtils.showLoading(mContext, "加载中...", false, true, true, true).show();
        }
    }

    /**
     * 隐藏加载中界面
     */
    public void hidLoading() {
        if (dialog != null && dialog.isShowing()) {
            dialog.hide();
        }
    }

    public void cancleLoading() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    /**
     * 打开/关闭搜索抽屉
     */
    public void openOrCloseDrawer() {
        if (drawerLayout.isDrawerOpen(mSearchLayout)) {
            Log.e("TAG", "关闭");
            drawerLayout.closeDrawer(mSearchLayout);
        } else {
            Log.e("TAG", "打开");
            drawerLayout.openDrawer(mSearchLayout);
        }
    }

    /**
     * 打开日期控件时，获取日期控件的默认值
     * @param mDate
     * @return
     */
    public long getDatePickDefaultDate(EditText mDate) {
        Date selectedDate = DateUtil.parseDate(mDate.getText().toString());
        Log.e("DATE1", mDate.getText().toString());
        long date = System.currentTimeMillis() + 60000;
        if (selectedDate != null) {
            date = selectedDate.getTime();
        }
        Log.e("DATE", String.valueOf(date));
        return date;
    }

    /**
     * 结果解析为map对象，
     * 解析失败返回null
     * @param resultStr
     * @return
     */
    public Map<String, Object> toMap(String resultStr) {
        if (resultStr != null) {
            if (!StringUtil.checkDataEmpty(resultStr)) {
                try {
                    resultStr = StringUtil.formart(resultStr);
                    Map<String, Object> map = JsonToMap.toMap(resultStr);
                    return map;
                } catch (Exception ex) {
                    showToast(getString(R.string.data_parse_error));
                }
            } else {
                showToast(getString(R.string.data_empty));
            }
        } else {
            showToast(getString(R.string.data_error));
        }
        return null;
    }

    /**
     * 解析为list map对象，
     * 解析失败返回null
     * @param resultStr
     * @return
     */
    public List<Map<String, Object>> toListMap(String resultStr) {
        if (resultStr != null) {
            if (!StringUtil.checkDataEmpty(resultStr)) {
                try {
                    List<Map<String, Object>> lists = JsonToMap.toListMap(resultStr);
                    return lists;
                } catch (Exception ex) {
                    showToast(getString(R.string.data_parse_error));
                }
            } else {
                return new ArrayList<Map<String, Object>>();
            }
        } else {
            showToast(getString(R.string.data_error));
        }
        return null;
    }
}
