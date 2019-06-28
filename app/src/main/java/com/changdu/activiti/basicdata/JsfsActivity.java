package com.changdu.activiti.basicdata;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.changdu.R;
import com.changdu.activiti.base.BaseActivity;
import com.changdu.adapter.FplxAdapter;
import com.changdu.adapter.JsfsAdapter;
import com.changdu.constant.Constant;
import com.changdu.network.RequestCenter;
import com.changdu.util.JsonToMap;
import com.changdu.util.StringUtil;
import com.changdu.util.WebServiceUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 结算方式列表
 */
public class JsfsActivity extends BaseActivity {

    private ListView mListView;
    private JsfsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ck_layout);
        mContext = this;
        // 设置标题
        setTitle(getString(R.string.title_jsfs), true);

        mListView = findViewById(R.id.lv_ck);

        initData();
    }

    private void initData() {
        HashMap<String, String> properties = new HashMap<>();

        RequestCenter.GETJSFSItems(properties, new WebServiceUtils.WebServiceCallBack() {

            @Override
            public void callBack(String resultStr) {
                Map<String, Object> map = toMap(resultStr);
                if (map == null) {
                    return;
                }

                String Sucecss = StringUtil.convertStr(map.get("Sucecss"));
                if ("True".equals(Sucecss)) {
                    final List<Map<String, Object>> dataList = (List<Map<String, Object>>) map.get("DATA");
                    adapter = new JsfsAdapter(mContext, dataList);
                    mListView.setAdapter(adapter);
                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                            Map<String, Object> data = dataList.get(position);
                            String ID = StringUtil.convertStr(data.get("ID"));
                            String NAME = StringUtil.convertStr(data.get("GXF_MC"));

                            Intent intent = new Intent();
                            intent.putExtra("ID", ID);
                            intent.putExtra("NAME", NAME);
                            setResult(Constant.ACTIVITI_FOR_RESULT_JSFS, intent);
                            finish();
                        }
                    });
                } else {
                    showToast(getString(R.string.data_error));
                }
            }
        });
    }
}
