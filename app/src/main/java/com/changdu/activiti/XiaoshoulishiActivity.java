package com.changdu.activiti;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.changdu.R;
import com.changdu.activiti.base.BaseActivity;
import com.changdu.adapter.XisoshoulishiAdapter;
import com.changdu.manager.UserManager;
import com.changdu.network.RequestCenter;
import com.changdu.util.CollUtil;
import com.changdu.util.DateUtil;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 销售历史
 */
public class XiaoshoulishiActivity extends BaseActivity implements View.OnClickListener {

    private ListView mListView;
    private List<Map<String, Object>> dataList;
    private XisoshoulishiAdapter adapter;
    private RefreshLayout refreshLayout;

    private EditText mBDate;
    private EditText mEDate;
    private EditText mUSR_NAME;
    private EditText mDWZ_DWMC;
    private Button mSearch;
    private TextView mTotal;

    private Integer SumRecord = 0; // 总记录数
    private Integer PNum = 1; // 页码
    private String DJE = ""; // 总金额

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiaoshoulishi_layout);
        mContext = this;
        // 设置标题
        setTitle(getString(R.string.title_xiaoshoulishi_chaxun), true);

        initView();
        getDataCount();
    }

    private void initView() {
        mListView = findViewById(R.id.lv_xiaoshoulishi);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String, Object> data = dataList.get(i);
                Intent intent = new Intent(mContext, XiaoshoulishiDetailActivity.class);
                intent.putExtra("BillID", StringUtil.convertStr(data.get("ID")));
                startActivity(intent);
            }
        });

        mBDate = findViewById(R.id.et_xsls_BDate);
        mBDate.setText(DateUtil.getPreMonthDate()); // 默认显示一个月数据
        mBDate.setOnClickListener(this);
        mEDate = findViewById(R.id.et_xsls_EDate);
        mEDate.setOnClickListener(this);
        mUSR_NAME = findViewById(R.id.et_xsls_USR_NAME);
        mDWZ_DWMC = findViewById(R.id.et_xsls_DWZ_DWMC);
        mSearch = findViewById(R.id.btn_xiaoshoulishi);
        mSearch.setOnClickListener(this);
        mTotal = findViewById(R.id.tv_total);

        refreshLayout = findViewById(R.id.rl_xiaoshoulishi);
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
        drawerLayout = findViewById(R.id.drawer_xiaoshoulishi_layout);
        mSearchLayout = findViewById(R.id.xiaoshoulishi_search_layout);
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
        RequestCenter.GETBillCount(properties, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String resultStr) {
                Map<String, Object> map = toMap(resultStr);
                if (map == null) {
                    cancleLoading();
                    return;
                }

                String total = StringUtil.convertStr(map.get("DCount"));
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

        RequestCenter.GETBillInfo(properties, new WebServiceUtils.WebServiceCallBack() {
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
                    adapter = new XisoshoulishiAdapter(mContext, pageDataList);
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
        properties.put("UID", UID);
        properties.put("DJZT", ""); // 单据状态
        properties.put("DJBH", ""); // 单据单号
        properties.put("USR_NAME", mUSR_NAME.getText().toString()); // 业务员
        properties.put("KHMC", mDWZ_DWMC.getText().toString()); // 客户名称
        properties.put("BDate", mBDate.getText().toString()); // 开始日期
        properties.put("EDate", mEDate.getText().toString()); // 截至日期
        return properties;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_xsls_BDate: // 开始日期
                long startDate = getDatePickDefaultDate(mBDate);
                DialogUIUtils.showDatePick(mContext, Gravity.BOTTOM, "开始日期", startDate, DateSelectorWheelView.TYPE_YYYYMMDD, 0, new DialogUIDateTimeSaveListener() {
                    @Override
                    public void onSaveSelectedDate(int tag, String selectedDate) {
                        Log.e("selectedDate", selectedDate);
                        mBDate.setText(selectedDate);
                    }
                }).show();
                break;
            case R.id.et_xsls_EDate: // 结束日期
                long endDate = getDatePickDefaultDate(mEDate);
                DialogUIUtils.showDatePick(mContext, Gravity.BOTTOM, "结束日期", endDate, DateSelectorWheelView.TYPE_YYYYMMDD, 1, new DialogUIDateTimeSaveListener() {
                    @Override
                    public void onSaveSelectedDate(int tag, String selectedDate) {
                        Log.e("selectedDate", selectedDate);
                        mEDate.setText(selectedDate);
                    }
                }).show();
                break;
            case R.id.btn_xiaoshoulishi: // 搜索
                PNum = 1;
                openOrCloseDrawer();
                getDataCount();
                break;
        }
    }
}
