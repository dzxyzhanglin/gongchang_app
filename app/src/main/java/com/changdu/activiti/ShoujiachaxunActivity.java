package com.changdu.activiti;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.changdu.R;
import com.changdu.activiti.base.BaseActivity;
import com.changdu.adapter.ShoujiaAdapter;
import com.changdu.manager.UserManager;
import com.changdu.network.RequestCenter;
import com.changdu.util.CollUtil;
import com.changdu.util.StringUtil;
import com.changdu.util.WebServiceUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 售价查询
 */
public class ShoujiachaxunActivity extends BaseActivity implements View.OnClickListener {

    private ListView mListView;
    private List<Map<String, Object>> dataList;
    private ShoujiaAdapter adapter;
    private RefreshLayout refreshLayout;

    private EditText mSPBH;
    private EditText mSPMC;
    private EditText mSPSX;
    private EditText mZJM;
    private EditText mKHMC;
    private TextView mTotal;
    private Button mSearch;

    private Integer SumRecord = 0; // 总记录数
    private Integer PNum = 1; // 页码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoujia_layout);
        mContext = this;
        // 设置标题
        setTitle(getString(R.string.title_shoujiachaxun), true);

        initView();
        getDataCount();
    }

    private void initView() {
        mListView = findViewById(R.id.lv_shoujia);
        mSPBH = findViewById(R.id.et_sj_SPBH);
        mSPMC = findViewById(R.id.et_sj_SPMC);
        mSPSX = findViewById(R.id.et_sj_SPSX);
        mZJM = findViewById(R.id.et_sj_ZJM);
        mKHMC = findViewById(R.id.et_sj_KHMC);
        mTotal = findViewById(R.id.tv_total);
        mSearch = findViewById(R.id.btn_sj);
        mSearch.setOnClickListener(this);

        refreshLayout = findViewById(R.id.rl_shoujia);
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
        drawerLayout = findViewById(R.id.drawer_shoujia_layout);
        mSearchLayout = findViewById(R.id.shoujia_search_layout);
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

    private void getDataCount() {
        HashMap<String, String> properties = getProperties();
        showLoading();
        RequestCenter.GETPriceCount(properties, new WebServiceUtils.WebServiceCallBack() {
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

        RequestCenter.GETPriceDetail(properties, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String resultStr) {
                List<Map<String, Object>> pageDataList = toListMap(resultStr);
                if (pageDataList == null) {
                    cancleLoading();
                    return;
                }
                dataList.addAll(pageDataList);
                PNum++;

                Log.e("pageDataList", pageDataList.toString());
                if (type == INIT_DATA) { // 初始数据
                    adapter = new ShoujiaAdapter(mContext, pageDataList);
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

    private HashMap<String, String> getProperties() {
        HashMap<String, String> properties = new HashMap<>();
        String UID = UserManager.getInstance().getUID();
        properties.put("UID", UID);
        properties.put("SPBH", mSPBH.getText().toString());
        properties.put("SPMC", mSPMC.getText().toString());
        properties.put("SPSX", mSPSX.getText().toString());
        properties.put("ZJM", mZJM.getText().toString());
        properties.put("KHMC", mKHMC.getText().toString());

        return properties;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_jd: // 搜索
                PNum = 1;
                openOrCloseDrawer();
                getDataCount();
                break;
        }
    }
}
