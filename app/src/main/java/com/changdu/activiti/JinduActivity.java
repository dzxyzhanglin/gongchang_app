package com.changdu.activiti;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.changdu.R;
import com.changdu.activiti.base.BaseActivity;
import com.changdu.adapter.JinduAdapter;
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
 * 生产进度查询
 */
public class JinduActivity extends BaseActivity implements View.OnClickListener {

    private ListView mListView;
    private List<Map<String, Object>> dataList;
    private JinduAdapter adapter;
    private RefreshLayout refreshLayout;

    private EditText mSCD_DJBH;
    private EditText mCPNO;
    private EditText mBDate;
    private EditText mEDate;
    private EditText mCPMC;
    private TextView mTotal;
    private Button mSearch;

    private Integer SumRecord = 0; // 总记录数
    private Integer PNum = 1; // 页码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jindu_layout);
        mContext = this;
        // 设置标题
        setTitle(getString(R.string.title_jindu_chaxun), true);

        initView();
        getDataCount();

        refreshLayout = findViewById(R.id.rl_jindu);
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
    }

    private void initView() {
        mListView = findViewById(R.id.lv_jindu);
        mSCD_DJBH = findViewById(R.id.et_jd_SCD_DJBH);
        mCPNO = findViewById(R.id.et_jd_CPNO);
        mBDate = findViewById(R.id.et_jd_BDate);
        mBDate.setOnClickListener(this);
        mEDate = findViewById(R.id.et_jd_EDate);
        mCPMC = findViewById(R.id.et_jd_CPMC);
        mEDate.setOnClickListener(this);
        mTotal = findViewById(R.id.tv_total);
        mSearch = findViewById(R.id.btn_jd);
        mSearch.setOnClickListener(this);
    }

    private void getDataCount() {
        HashMap<String, String> properties = getProperties();

        showLoading();

        RequestCenter.GetCPDInfoCount(properties, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String resultStr) {
                if (!StringUtil.checkDataEmpty(resultStr)) {
                    Map<String, Object> map = JsonToMap.toMap(resultStr);
                    String total = StringUtil.convertStr(map.get("DCOUNT"));
                    SumRecord = Integer.valueOf(total);
                    mTotal.setText(total);
                    if (SumRecord > 0) {
                        dataList = new ArrayList<>();
                        getDataList(INIT_DATA);
                    } else {
                        showToast(getString(R.string.data_empty));
                        cancleLoading();
                    }
                } else {
                    showToast(getString(R.string.data_error));
                    cancleLoading();
                }
            }
        });
    }

    private void getDataList(final int type) {
        HashMap<String, String> properties = getProperties();
        properties.put("PNum", String.valueOf(PNum));
        properties.put("SumRecord", String.valueOf(SumRecord));

        RequestCenter.GetCPDInfo(properties, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String resultStr) {
                if (resultStr != null) {
                    PNum++;
                    List<Map<String, Object>> pageDataList = new ArrayList<>();
                    if (!StringUtil.checkDataEmpty(resultStr)) {
                        pageDataList = JsonToMap.toListMap(resultStr);
                        dataList.addAll(pageDataList);
                    }
                    Log.e("pageDataList", pageDataList.toString());
                    if (type == INIT_DATA) { // 初始数据
                        adapter = new JinduAdapter(mContext, pageDataList);
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
                } else {
                    showToast(getString(R.string.data_error));
                }
                cancleLoading();
            }
        });

    }

    private HashMap<String, String> getProperties() {
        HashMap<String, String> properties = new HashMap<>();
        String UID = UserManager.getInstance().getUID();
        properties.put("UID", UID);
        properties.put("SCDH", mSCD_DJBH.getText().toString());
        properties.put("CPNO", mCPNO.getText().toString());
        properties.put("BDate", mBDate.getText().toString());
        properties.put("EDate", mEDate.getText().toString());
        properties.put("CPMC", mCPMC.getText().toString());

        return properties;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_jd_BDate: // 开始日期
                DialogUIUtils.showDatePick(mContext, Gravity.BOTTOM, "开始日期", System.currentTimeMillis() + 60000, DateSelectorWheelView.TYPE_YYYYMMDD, 0, new DialogUIDateTimeSaveListener() {
                    @Override
                    public void onSaveSelectedDate(int tag, String selectedDate) {
                        Log.e("selectedDate", selectedDate);
                        mBDate.setText(selectedDate);
                    }
                }).show();
                break;
            case R.id.et_jd_EDate: // 结束日期
                DialogUIUtils.showDatePick(mContext, Gravity.BOTTOM, "结束日期", System.currentTimeMillis() + 60000, DateSelectorWheelView.TYPE_YYYYMMDD, 0, new DialogUIDateTimeSaveListener() {
                    @Override
                    public void onSaveSelectedDate(int tag, String selectedDate) {
                        Log.e("selectedDate", selectedDate);
                        mEDate.setText(selectedDate);
                    }
                }).show();
                break;
            case R.id.btn_jd: // 搜索
                PNum = 1;
                getDataCount();
                break;
        }
    }
}
