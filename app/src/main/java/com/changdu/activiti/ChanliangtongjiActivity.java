package com.changdu.activiti;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.changdu.R;
import com.changdu.activiti.base.BaseActivity;
import com.changdu.adapter.ChanliangtongjiAdapter;
import com.changdu.manager.UserManager;
import com.changdu.network.RequestCenter;
import com.changdu.util.CollUtil;
import com.changdu.util.JsonToMap;
import com.changdu.util.StringUtil;
import com.changdu.util.WebServiceUtils;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIDateTimeSaveListener;
import com.dou361.dialogui.widget.DateSelectorWheelView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工人产量统计
 */
public class ChanliangtongjiActivity extends BaseActivity implements View.OnClickListener {

    private ListView mListView;
    private List<Map<String, Object>> dataList;
    private ChanliangtongjiAdapter adapter;
    private RefreshLayout refreshLayout;

    private EditText mBDate;
    private EditText mEDate;
    private TextView mTotal;
    private Button mSearch;

    private Integer SumRecord = 0; // 总记录数
    private Integer PNum = 1; // 页码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chanliangtongji_layout);
        mContext = this;
        // 设置标题
        setTitle(getString(R.string.title_chanliangtongji), true);

        initView();
        getDataCount();
    }

    private void initView() {
        mListView = findViewById(R.id.lv_chanliangtongji);
        mBDate = findViewById(R.id.et_cltj_BDate);
        mBDate.setOnClickListener(this);
        mEDate = findViewById(R.id.et_cltj_EDate);
        mEDate.setOnClickListener(this);
        mSearch = findViewById(R.id.btn_chanliangtongji);
        mSearch.setOnClickListener(this);
        mTotal = findViewById(R.id.tv_total);

        refreshLayout = findViewById(R.id.rl_chanliangtongji);
        refreshLayout.setEnableAutoLoadMore(true);//开启自动加载功能（非必须）
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout rl) {
                rl.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        PNum = 1;
                        getDataList(REFRESH_DATA);
                    }
                }, 500);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout rl) {
                rl.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getDataList(LOAD_MORE_DATA);
                    }
                }, 500);
            }
        });

        // 搜索抽屉
        drawerLayout = findViewById(R.id.drawer_chanliangtongji_layout);
        mSearchLayout = findViewById(R.id.chanliangtongji_search_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_search_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_add_search) {
            openOrCloseDrawer();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * 获取数据总数
     */
    private void getDataCount() {
        HashMap<String, String> properties = getProperties();

        showLoading();

        RequestCenter.GETMadeCount(properties, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String resultStr) {
                Map<String, Object> map = toMap(resultStr);
                if (map == null) {
                    cancleLoading();
                    return;
                }

                String total = StringUtil.convertStr(map.get("DCOUNT"));
                SumRecord = Integer.valueOf(total);
                mTotal.setText(total);
                if (SumRecord > 0) {
                    dataList = new ArrayList<>();
                    getDataList(INIT_DATA);
                } else {
                    showToast(getString(R.string.data_empty));
                    if (dataList != null && dataList.size() > 0) {
                        dataList = new ArrayList<>();
                        adapter.setmDataList(dataList);
                        adapter.notifyDataSetChanged();
                    }
                    cancleLoading();
                }
            }
        });
    }

    private void getDataList(final int type) {
        HashMap<String, String> properties = getProperties();
        properties.put("PNum", String.valueOf(PNum));
        properties.put("SumRecord", String.valueOf(SumRecord));

        RequestCenter.GETMadeDetail(properties, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String resultStr) {
                List<Map<String, Object>> pageDataList = toListMap(resultStr);
                if (pageDataList == null) {
                    cancleLoading();
                    return;
                }
                dataList.addAll(pageDataList);
                PNum++;

                if (type == INIT_DATA) { // 初始数据
                    adapter = new ChanliangtongjiAdapter(mContext, pageDataList);
                    mListView.setAdapter(adapter);
                    if (CollUtil.isEmpty(pageDataList)) {
                        showToast(getString(R.string.data_empty));
                    }
                } else if (type == REFRESH_DATA) { // 刷新数据
                    adapter.refresh(pageDataList);
                    refreshLayout.finishRefresh();
                    refreshLayout.resetNoMoreData();
                } else if (type == LOAD_MORE_DATA) { // 加载更多数据
                    if (CollUtil.isEmpty(pageDataList)) {
                        refreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
                    } else {
                        adapter.loadMore(pageDataList);
                        refreshLayout.finishLoadMore();
                    }
                }

                cancleLoading();
            }
        });
    }

    /**
     * 获取查询条件
     * @return
     */
    private HashMap<String, String> getProperties() {
        HashMap<String, String> properties = new HashMap<>();
        String UID = UserManager.getInstance().getUID();
        properties.put("RYID", UID);
        properties.put("BDate", mBDate.getText().toString()); // 开始日期
        properties.put("EDate", mEDate.getText().toString()); // 截至日期
        return properties;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_cltj_BDate:
                long startDate = getDatePickDefaultDate(mBDate);
                DialogUIUtils.showDatePick(mContext, Gravity.BOTTOM, "开始日期", startDate, DateSelectorWheelView.TYPE_YYYYMMDD, 0, new DialogUIDateTimeSaveListener() {
                    @Override
                    public void onSaveSelectedDate(int tag, String selectedDate) {
                        Log.e("selectedDate", selectedDate);
                        mBDate.setText(selectedDate);
                    }
                }).show();
                break;
            case R.id.et_cltj_EDate:
                long endDate = getDatePickDefaultDate(mEDate);
                DialogUIUtils.showDatePick(mContext, Gravity.BOTTOM, "结束日期", endDate, DateSelectorWheelView.TYPE_YYYYMMDD, 0, new DialogUIDateTimeSaveListener() {
                    @Override
                    public void onSaveSelectedDate(int tag, String selectedDate) {
                        Log.e("selectedDate", selectedDate);
                        mEDate.setText(selectedDate);
                    }
                }).show();
                break;
            case R.id.btn_chanliangtongji:
                PNum = 1;
                openOrCloseDrawer();
                getDataCount();
                break;
        }
    }
}
