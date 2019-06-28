package com.changdu.activiti.basicdata;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.changdu.R;
import com.changdu.activiti.base.BaseActivity;
import com.changdu.adapter.HwAdapter;
import com.changdu.adapter.JldwAdapter;
import com.changdu.constant.Constant;
import com.changdu.network.RequestCenter;
import com.changdu.util.JsonToMap;
import com.changdu.util.StringUtil;
import com.changdu.util.WebServiceUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 计量单位基础数据
 */
public class JldwActivity extends BaseActivity {

    private ListView mListView;
    private JldwAdapter adapter;

    private String SPID = ""; // 物品ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ck_layout);
        mContext = this;
        // 设置标题
        setTitle(getString(R.string.title_jldw), true);

        mListView = findViewById(R.id.lv_ck);

        Intent intent = getIntent();
        SPID = intent.getStringExtra("SPID");

        initData();
    }

    private void initData() {
        HashMap<String, String> properties = new HashMap<>();
        properties.put("SPID", SPID);
        RequestCenter.GETJLDItem(properties, new WebServiceUtils.WebServiceCallBack() {

            @Override
            public void callBack(String resultStr) {
                final List<Map<String, Object>> dataList = (List<Map<String, Object>>) toListMap(resultStr);
                adapter = new JldwAdapter(mContext, dataList);
                mListView.setAdapter(adapter);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        Map<String, Object> data = dataList.get(position);
                        String ID = StringUtil.convertStr(data.get("ID"));
                        String NAME = StringUtil.convertStr(data.get("JLB_JLDW"));

                        Intent intent = new Intent();
                        intent.putExtra("ID", ID);
                        intent.putExtra("NAME", NAME);
                        setResult(Constant.ACTIVITI_FOR_RESULT_JLDW, intent);
                        finish();
                    }
                });
            }
        });
    }
}
