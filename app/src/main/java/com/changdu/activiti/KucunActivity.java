package com.changdu.activiti;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.changdu.R;
import com.changdu.activiti.base.BaseActivity;
import com.changdu.adapter.KucunAdapter;
import com.changdu.constant.Constant;
import com.changdu.manager.UserManager;
import com.changdu.network.RequestCenter;
import com.changdu.util.CollUtil;
import com.changdu.util.JsonToMap;
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
 * 库存查询
 */
public class KucunActivity extends BaseActivity implements View.OnClickListener {

    private ListView mListView;
    private List<Map<String, Object>> dataList;
    private KucunAdapter adapter;
    private RefreshLayout refreshLayout;

    private EditText mSPBH;
    private EditText mSPMC;
    private EditText mSPSX;
    private EditText mZJM;
    private EditText mCKID;
    private TextView mTotal;
    private Button mSearch;

    private Integer SumRecord = 0; // 总记录数
    private Integer PNum = 1; // 页码
    private String CKID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kucun_layout);
        mContext = this;
        // 设置标题
        setTitle(getString(R.string.title_kucun_chaxun), true);

        initView();
        getDataCount();

        refreshLayout = findViewById(R.id.kucun_refreshLayout);
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
        mListView = findViewById(R.id.kucun_listview);
        mSPBH = findViewById(R.id.et_kucun_SPBH);
        mSPMC = findViewById(R.id.et_kucun_SPMC);
        mSPSX = findViewById(R.id.et_kucun_SPSX);
        mZJM = findViewById(R.id.et_kucun_ZJM);

        mCKID = findViewById(R.id.et_kucun_CKID);
        mCKID.setOnClickListener(this);

        mTotal = findViewById(R.id.tv_total);

        mSearch = findViewById(R.id.btn_kucun);
        mSearch.setOnClickListener(this);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String, Object> data = dataList.get(i);
                Intent intent = new Intent(mContext, KucunDetailActivity.class);
                intent.putExtra("SPID", StringUtil.convertStr(data.get("ID")));
                intent.putExtra("SPK_SPBH", StringUtil.convertStr(data.get("SPK_SPBH")));
                intent.putExtra("SPK_SPMC", StringUtil.convertStr(data.get("SPK_SPMC")));
                intent.putExtra("SPK_SPSX", StringUtil.convertStr(data.get("SPK_SPSX")));
                startActivity(intent);
            }
        });
    }

    private void getDataCount() {

        HashMap<String, String> properties = getProperties();

        showLoading();

        // 获取记录总数
        RequestCenter.GETSpzlCount(properties, new WebServiceUtils.WebServiceCallBack() {
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

        RequestCenter.GETSpzlInfo(properties, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String resultStr) {
                if (resultStr != null) {
                    PNum++;
                    List<Map<String, Object>> pageDataList = new ArrayList<>();
                    if (!StringUtil.checkDataEmpty(resultStr)) {
                        pageDataList = JsonToMap.toListMap(resultStr);
                        dataList.addAll(pageDataList);
                    }
                    if (type == INIT_DATA) { // 初始数据
                        adapter = new KucunAdapter(mContext, pageDataList);
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

    /**
     * 获取查询条件
     * @return
     */
    private HashMap<String, String> getProperties() {
        HashMap<String, String> properties = new HashMap<>();
        String UID = UserManager.getInstance().getUID();
        String SPBH = mSPBH.getText().toString();
        String SPMC = mSPMC.getText().toString();
        String SPSX = mSPSX.getText().toString();
        String ZJM = mZJM.getText().toString();

        properties.put("UID", UID);
        properties.put("SPBH", SPBH);
        properties.put("SPMC", SPMC);
        properties.put("SPSX", SPSX);
        properties.put("ZJM", ZJM);
        properties.put("CKID", CKID);
        return properties;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_kucun_CKID:
                Intent intent = new Intent(mContext, CkActivity.class);
                startActivityForResult(intent, Constant.ACTIVITI_FOR_RESULT_CK);
                break;
            case R.id.btn_kucun:
                PNum = 1;
                getDataCount();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.ACTIVITI_FOR_RESULT_CK) {
            if (data != null) {
                String ckName = data.getStringExtra("CKNAME");
                CKID = data.getStringExtra("CKID");
                mCKID.setText(ckName);
            }
        }
    }
}
