package com.changdu.activiti.basicdata;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.changdu.R;
import com.changdu.activiti.base.BaseActivity;
import com.changdu.adapter.CkAdapter;
import com.changdu.adapter.KhAdapter;
import com.changdu.constant.Constant;
import com.changdu.manager.UserManager;
import com.changdu.network.RequestCenter;
import com.changdu.util.StringUtil;
import com.changdu.util.WebServiceUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客户
 */
public class KhActivity extends BaseActivity {

    private ListView mListView;
    private EditText mKHMC;
    private Button mSearch;
    private KhAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kh_layout);
        mContext = this;
        // 设置标题
        setTitle(getString(R.string.title_kh), true);

        mListView = findViewById(R.id.lv_ck);
        mKHMC = findViewById(R.id.et_kh_KHMC);
        mSearch = findViewById(R.id.btn_kh);
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataList();
            }
        });
    }

    private void getDataList() {
        HashMap<String, String> properties = new HashMap<>();
        properties.put("KHMC", mKHMC.getText().toString());

        RequestCenter.GETKHList(properties, new WebServiceUtils.WebServiceCallBack() {

            @Override
            public void callBack(String resultStr) {
                Map<String, Object> map = toMap(resultStr);
                if (map == null) {
                    return;
                }

                String Sucecss = StringUtil.convertStr(map.get("Sucecss"));
                Log.e("Sucecss", Sucecss);
                if ("True".equals(Sucecss)) {
                    final List<Map<String, Object>> dataList = (List<Map<String, Object>>) map.get("DATA");
                    adapter = new KhAdapter(mContext, dataList);
                    mListView.setAdapter(adapter);
                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                            Map<String, Object> data = dataList.get(position);
                            String ID = StringUtil.convertStr(data.get("ID"));
                            String NAME = StringUtil.convertStr(data.get("DWZ_DWMC"));

                            Intent intent = new Intent();
                            intent.putExtra("ID", ID);
                            intent.putExtra("NAME", NAME);
                            setResult(Constant.ACTIVITI_FOR_RESULT_KH, intent);
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
