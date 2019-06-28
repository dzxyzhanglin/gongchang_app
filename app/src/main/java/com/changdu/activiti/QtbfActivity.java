package com.changdu.activiti;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.changdu.R;
import com.changdu.activiti.base.BaseActivity;
import com.changdu.adapter.QtbfAdapter;
import com.changdu.constant.Constant;
import com.changdu.network.RequestCenter;
import com.changdu.util.StringUtil;
import com.changdu.util.WebServiceUtils;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.TieBean;
import com.dou361.dialogui.listener.DialogUIItemListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 其他报废
 */
public class QtbfActivity extends BaseActivity implements View.OnClickListener {

    private ListView mListView;
    private QtbfAdapter adapter;
    private Button mAdd;
    private TextView mSave;
    private TextView mCancel;

    private TextView mTitle;
    private EditText mbf;
    private EditText mqj;
    private Button mDialogSave;
    private Dialog mButtonDialog;

    private String CPID;
    private List<Map<String, Object>> bfItemMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qtbf_layout);
        mContext = this;
        setTitle(getString(R.string.title_qtbf), true);

        savedInstanceState = getIntent().getExtras();
        CPID = savedInstanceState.getString("CPID");
        bfItemMap = ((List)savedInstanceState.getSerializable("bfItemMap"));

        initView();
    }

    private void initView() {
        mListView = findViewById(R.id.lv_qtbf);
        adapter = new QtbfAdapter(mContext, bfItemMap);
        mListView.setAdapter(adapter);

        mAdd = findViewById(R.id.btn_qtbf_add);
        mAdd.setOnClickListener(this);
        mSave = findViewById(R.id.tv_qtbf_confirm);
        mSave.setOnClickListener(this);
        mCancel = findViewById(R.id.tv_qtbf_cancel);
        mCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_qtbf_add:
                getGxList();
                break;
            case R.id.tv_qtbf_confirm:
                Intent intent = new Intent();
                Bundle localBundle = new Bundle();
                localBundle.putSerializable("bfItemMap", (Serializable)this.bfItemMap);
                intent.putExtras(localBundle);
                setResult(Constant.ACTIVITI_FOR_RESULT_ADD_QTBF, intent);

                if (mButtonDialog != null) {
                    mButtonDialog.dismiss();
                }
                finish();
                break;
            case R.id.tv_qtbf_cancel:
                finish();
                break;
        }
    }

    /**
     * 获取工序列表
     */
    private void getGxList() {
        HashMap<String, String> properties = new HashMap<>();
        properties.put("CPID", CPID);
        showLoading();
        RequestCenter.GETGXMX(properties, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String resultStr) {
                cancleLoading();
                Map<String, Object> map = toMap(resultStr);
                if (map == null) {
                    return;
                }
                if ("True".equals(StringUtil.convertStr(map.get("Sucecss")))) {
                    final List<Map<String, Object>> gxList = (List<Map<String, Object>>) map.get("DATA");
                    List<TieBean> beanList = new ArrayList<>(gxList.size());
                    for (Map<String, Object> list : gxList) {
                        beanList.add(new TieBean(StringUtil.convertStr(list.get("GYB_GYMC"))));
                    }
                    DialogUIUtils.showSheet(mContext, beanList, "取消", Gravity.BOTTOM, true, true, new DialogUIItemListener() {
                        @Override
                        public void onItemClick(CharSequence text, int position) {
                            final Map<String, Object> data = gxList.get(position);

                            View rootViewB = View.inflate(mContext, R.layout.item_qtbf_dialog, null);
                            if (mButtonDialog == null) {
                                mButtonDialog = DialogUIUtils.showCustomBottomAlert(mContext, rootViewB).show();
                            } else {
                                mButtonDialog.show();
                            }
                            mTitle = mButtonDialog.findViewById(R.id.tv_qtbf_title);
                            mTitle.setText(StringUtil.convertStr(data.get("GYB_GYMC")));
                            mbf = mButtonDialog.findViewById(R.id.et_qtbf_bf);
                            mbf.setText("");
                            mqj = mButtonDialog.findViewById(R.id.et_qtbf_qj);
                            mqj.setText("");
                            mDialogSave = mButtonDialog.findViewById(R.id.btn_qtbf_dialog_save);
                            mDialogSave.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    List<Map<String, Object>> itemList = new ArrayList<>();
                                    Map<String, Object> item = new HashMap<>();
                                    item.put("name", StringUtil.convertStr(data.get("GYB_GYMC")));
                                    item.put("bf", mbf.getText().toString());
                                    item.put("qj", mqj.getText().toString());
                                    itemList.add(item);
                                    adapter.loadMore(itemList);

                                    mButtonDialog.dismiss();
                                }
                            });
                        }
                        @Override
                        public void onBottomBtnClick() {
                        }
                    }).show();
                } else {
                    showToast("加载工序数据失败");
                }
            }
        });
    }
}
