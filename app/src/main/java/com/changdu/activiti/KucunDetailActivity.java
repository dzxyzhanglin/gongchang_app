package com.changdu.activiti;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.changdu.R;
import com.changdu.activiti.base.BaseActivity;
import com.changdu.adapter.KucunDetailAdapter;
import com.changdu.constant.Constant;
import com.changdu.network.RequestCenter;
import com.changdu.util.JsonToMap;
import com.changdu.util.StringUtil;
import com.changdu.util.WebServiceUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 物品库存详情及盘点
 */
public class KucunDetailActivity extends BaseActivity implements View.OnClickListener {

    private TextView mSPK_SPBH; // 编号
    private TextView mSPK_SPMC; // 名称
    private TextView mSPK_SPSX; // 规格
    private ListView mListView;
    private List<Map<String, Object>> dataList = new ArrayList<>();
    private String mData; // 原始数据
    private KucunDetailAdapter adapter;
    private RefreshLayout refreshLayout;
    private Button mAdd;

    private String SPID = ""; // 物品ID
    private String SPK_SPBH = "";
    private String SPK_SPMC = "";
    private String SPK_SPSX = "";

    private boolean LOAD_DATA_STATUS = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kucun_detail_layout);
        mContext = this;
        // 设置标题
        setTitle(getString(R.string.title_kucun_detail), true);
        LOAD_DATA_STATUS = false;

        Intent intent = getIntent();
        SPID = intent.getStringExtra("SPID");
        SPK_SPBH = intent.getStringExtra("SPK_SPBH");
        SPK_SPMC = intent.getStringExtra("SPK_SPMC");
        SPK_SPSX = intent.getStringExtra("SPK_SPSX");

        initView();
        getDataList();

        refreshLayout = findViewById(R.id.rl_kucun_detail);
        refreshLayout.setEnableAutoLoadMore(true);//开启自动加载功能（非必须）
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout rl) {
                rl.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getDataList();
                    }
                }, 500);
            }
        });
    }

    private void initView() {
        mSPK_SPBH = findViewById(R.id.kucun_detail_SPK_SPBH);
        mSPK_SPMC = findViewById(R.id.kucun_detail_SPK_SPMC);
        mSPK_SPSX = findViewById(R.id.kucun_detail_SPK_SPSX);

        mListView = findViewById(R.id.lv_kucun_detail);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String, Object> data = dataList.get(i);
                Intent intent = new Intent(mContext, KucunAddUpdateActivity.class);
                intent.putExtra("SPID", SPID);
                intent.putExtra("ZPD_JGID", StringUtil.convertStr(data.get("ZPD_JGID")));
                intent.putExtra("JGZ_JGMC", StringUtil.convertStr(data.get("JGZ_JGMC")));
                intent.putExtra("ZPD_PC", StringUtil.convertStr(data.get("ZPD_PC")));
                intent.putExtra("ZPD_HW", StringUtil.convertStr(data.get("ZPD_HW")));
                intent.putExtra("HWB_HWMC", StringUtil.convertStr(data.get("HWB_HWMC")));
                intent.putExtra("ZPD_SL", StringUtil.convertStr(data.get("ZPD_SL")));
                intent.putExtra("ZPD_JLDW", StringUtil.convertStr(data.get("ZPD_JLDW")));
                intent.putExtra("JLB_JLDW", StringUtil.convertStr(data.get("JLB_JLDW")));
                intent.putExtra("OPERATE", Constant.OPERATE_UPDATE);
                intent.putExtra("DATA_LIST", mData);
                intent.putExtra("DATA_INDEX", i);
                startActivity(intent);
            }
        });

        mAdd = findViewById(R.id.btn_kucun_detal_wtadd);
        mAdd.setOnClickListener(this);
    }

    private void getDataList() {
        HashMap<String, String> properties = new HashMap<>();
        properties.put("SPID", SPID);

        RequestCenter.GETKCDetail(properties, new WebServiceUtils.WebServiceCallBack() {

            @Override
            public void callBack(String resultStr) {
                Map<String, Object> map = toMap(resultStr);
                if (map == null) {
                    return;
                }

                LOAD_DATA_STATUS = true;
                mAdd.setBackgroundResource(R.color.colorPrimary);

                // 标题赋值
                mSPK_SPBH.setText(StringUtil.convertStr(map.get("SPK_SPBH")));
                mSPK_SPMC.setText(StringUtil.convertStr(map.get("SPK_SPMC")));
                mSPK_SPSX.setText(StringUtil.convertStr(map.get("SPK_SPSX")));

                // 列表
                dataList = (List<Map<String, Object>>) map.get("DList");
                adapter = new KucunDetailAdapter(mContext, dataList);
                mListView.setAdapter(adapter);
                refreshLayout.finishRefresh();
                refreshLayout.resetNoMoreData();
                mData = resultStr;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_kucun_detal_wtadd:
                if (!LOAD_DATA_STATUS) {
                    showToast("没有查询到物品详细信息，不能进行次操作");
                    return;
                }
                Intent intent = new Intent(mContext, KucunAddUpdateActivity.class);
                intent.putExtra("SPID", SPID);
                intent.putExtra("ZPD_JGID", "");
                intent.putExtra("JGZ_JGMC", "");
                intent.putExtra("ZPD_PC", "");
                intent.putExtra("ZPD_HW", "");
                intent.putExtra("HWB_HWMC", "");
                intent.putExtra("ZPD_SL", "");
                intent.putExtra("OPERATE", Constant.OPERATE_ADD);
                intent.putExtra("DATA_LIST", mData);
                intent.putExtra("DATA_INDEX", -1);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        // 重新刷新数据
        getDataList();
    }
}
