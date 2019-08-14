package com.changdu.activiti.basicdata;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.changdu.R;
import com.changdu.activiti.base.BaseActivity;
import com.changdu.adapter.HwAdapter;
import com.changdu.adapter.PcAdapter;
import com.changdu.constant.Constant;
import com.changdu.network.RequestCenter;
import com.changdu.util.StringUtil;
import com.changdu.util.WebServiceUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 批次
 */
public class PcActivity extends BaseActivity {

    private ListView mListView;
    private PcAdapter adapter;

    private String CKID = ""; // 仓库ID
    private String SPID = ""; // 商品ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ck_layout);
        mContext = this;
        // 设置标题
        setTitle(getString(R.string.title_pc), true);

        mListView = findViewById(R.id.lv_ck);

        Intent intent = getIntent();
        CKID = intent.getStringExtra("CKID");
        SPID = intent.getStringExtra("SPID");

        initData();
    }

    // 初始化数据
    private void initData() {
        HashMap<String, String> properties = new HashMap<>();
        properties.put("CKID", CKID);
        properties.put("SPID", SPID);
        RequestCenter.GETPCItem(properties, new WebServiceUtils.WebServiceCallBack() {

            @Override
            public void callBack(String resultStr) {
                Map<String, Object> map = toMap(resultStr);
                if (map == null) {
                    return;
                }

                final List<Map<String, Object>> dataList = (List<Map<String, Object>>) toListMap(resultStr);
                adapter = new PcAdapter(mContext, dataList);
                mListView.setAdapter(adapter);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        Map<String, Object> data = dataList.get(position);
                        String ID = StringUtil.convertStr(data.get("PC"));
                        String NAME = StringUtil.convertStr(data.get("PC"));

                        Intent intent = new Intent();
                        intent.putExtra("ID", ID);
                        intent.putExtra("NAME", NAME);
                        setResult(Constant.ACTIVITI_FOR_RESULT_PC, intent);
                        finish();
                    }
                });
            }
        });
    }
}
