package com.changdu.activiti;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.changdu.R;
import com.changdu.activiti.base.BaseActivity;
import com.changdu.adapter.XisoshoulishiDetailAdapter;
import com.changdu.network.RequestCenter;
import com.changdu.util.StringUtil;
import com.changdu.util.WebServiceUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 销售历史详情
 */
public class XiaoshoulishiDetailActivity extends BaseActivity {

    private TextView mPFD_DJBH;
    private TextView mDWZ_DWMC;
    private TextView mUSR_NAME;
    private TextView mPFD_ZDJE;
    private TextView mPFD_DJZT;
    private TextView mPFD_DJRQ;
    private TextView mGXF_MC;
    private TextView mIV_MC;
    private ListView mListView;
    private XisoshoulishiDetailAdapter adapter;

    private String BillID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiaoshoulishi_detail_layout);
        mContext = this;
        // 设置标题
        setTitle(getString(R.string.title_xiaoshoulishi_detail), true);

        // 获取前一个页面传递过来的数据
        Intent intent = getIntent();
        BillID = intent.getStringExtra("BillID");

        initView();
        initData();
    }

    private void initView() {
        mPFD_DJBH = findViewById(R.id.tv_xsls_detail_PFD_DJBH);
        mDWZ_DWMC = findViewById(R.id.tv_xsls_detail_DWZ_DWMC);
        mUSR_NAME = findViewById(R.id.tv_xsls_detail_USR_NAME);
        mPFD_ZDJE = findViewById(R.id.tv_xsls_detail_PFD_ZDJE);
        mPFD_DJZT = findViewById(R.id.tv_xsls_detail_PFD_DJZT);
        mPFD_DJRQ = findViewById(R.id.tv_xsls_detail_PFD_DJRQ);
        mGXF_MC = findViewById(R.id.tv_xsls_detail_GXF_MC);
        mIV_MC = findViewById(R.id.tv_xsls_detail_IV_MC);

        mListView = findViewById(R.id.lv_xsls_detail);
    }

    private void initData() {
        HashMap<String, String> properties = new HashMap<>();
        properties.put("BillID", BillID);

        RequestCenter.GETBillDetail(properties, new WebServiceUtils.WebServiceCallBack() {

            @Override
            public void callBack(String resultStr) {
                Map<String, Object> map = toMap(resultStr);
                if (map == null) {
                    return;
                }
                // 主信息
                mPFD_DJBH.setText(StringUtil.convertStr(map.get("PFD_DJBH")));
                mDWZ_DWMC.setText(StringUtil.convertStr(map.get("DWZ_DWMC")));
                mUSR_NAME.setText(StringUtil.convertStr(map.get("USR_NAME")));
                mPFD_ZDJE.setText(StringUtil.convertStr(map.get("PFD_ZDJE")));
                mPFD_DJZT.setText(StringUtil.convertStr(map.get("PFD_DJZT")));
                mPFD_DJRQ.setText(StringUtil.convertStr(map.get("PFD_DJRQ")));
                mGXF_MC.setText(StringUtil.convertStr(map.get("GXF_MC")));
                mIV_MC.setText(StringUtil.convertStr(map.get("IV_MC")));

                // 物品信息
                List<Map<String, Object>> dataList = (List<Map<String, Object>>) map.get("DList");
                adapter = new XisoshoulishiDetailAdapter(mContext, dataList);
                mListView.setAdapter(adapter);
            }
        });
    }
}
