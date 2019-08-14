package com.changdu.activiti;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.changdu.R;
import com.changdu.activiti.base.BaseActivity;
import com.changdu.activiti.basicdata.CkActivity;
import com.changdu.activiti.basicdata.FplxActivity;
import com.changdu.activiti.basicdata.JsfsActivity;
import com.changdu.activiti.basicdata.KhActivity;
import com.changdu.adapter.KaidanWpAdapter;
import com.changdu.constant.Constant;
import com.changdu.manager.UserManager;
import com.changdu.network.RequestCenter;
import com.changdu.util.StringUtil;
import com.changdu.util.WebServiceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 销售开单
 */
public class XiaoshouKaidanActivity extends BaseActivity implements View.OnClickListener {

    private EditText mKHID;
    private EditText mCKID;
    private EditText mFPLX;
    private EditText mJSFS;
    private ImageView mAddWp;
    private ListView mWpListView;
    private Button mSave;

    private String KHID = ""; // 客户
    private String KH_NAME;
    private String CKID = ""; //仓库
    private String CK_NAME;
    private String FPLX = ""; // 发票类型
    private String FPLX_NAME;
    private String JSFS = ""; // 结算方式
    private String JSFS_NAME;

    private List<Map<String, Object>> WP_LIST;
    private KaidanWpAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiaoshou_kaidan_layout);

        mContext = this;
        WP_LIST = new ArrayList<>();
        // 设置标题
        setTitle(getString(R.string.title_xiaoshou_kaidan), true);

        initView();
    }

    private void initView() {
        mKHID = findViewById(R.id.et_kaidan_KHID);
        mKHID.setOnClickListener(this);
        mCKID = findViewById(R.id.et_kaidan_CKID);
        mCKID.setOnClickListener(this);
        mFPLX = findViewById(R.id.et_kaidan_FPLX);
        mFPLX.setOnClickListener(this);
        mJSFS = findViewById(R.id.et_kaidan_JSFS);
        mJSFS.setOnClickListener(this);
        mSave = findViewById(R.id.btn_kaidan_save);
        mSave.setOnClickListener(this);

        mAddWp = findViewById(R.id.btn_kaidan_addwp);
        mAddWp.setOnClickListener(this);

        mWpListView = findViewById(R.id.lv_kaidan_wp);
        adapter = new KaidanWpAdapter(mContext, WP_LIST);
        mWpListView.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_kaidan_KHID: // 选择客户
                startActivityForResult(
                        new Intent(mContext, KhActivity.class), Constant.ACTIVITI_FOR_RESULT_KH);
                break;
            case R.id.et_kaidan_CKID:
                startActivityForResult(
                        new Intent(mContext, CkActivity.class), Constant.ACTIVITI_FOR_RESULT_CK);
                break;
            case R.id.et_kaidan_FPLX:
                startActivityForResult(
                        new Intent(mContext, FplxActivity.class), Constant.ACTIVITI_FOR_RESULT_FPLX);
                break;
            case R.id.et_kaidan_JSFS:
                startActivityForResult(
                        new Intent(mContext, JsfsActivity.class), Constant.ACTIVITI_FOR_RESULT_JSFS);
                break;
            case R.id.btn_kaidan_addwp: // 添加物品
                if (StringUtil.isBlank(CKID)) {
                    showToast("请先选择仓库");
                    return;
                }
                Intent intent = new Intent(mContext, XiaoshouKaidanAddWpActivity.class);
                intent.putExtra("CKID", CKID);
                intent.putExtra("CK_NAME", CK_NAME);
                startActivityForResult(
                        intent, Constant.ACTIVITI_FOR_RESULT_ADD_WP);
                break;

            case R.id.btn_kaidan_save: // 保存
                HashMap<String, String> properties = new HashMap<>();
                properties.put("UID", UserManager.getInstance().getUID());
                properties.put("KHID", KHID);
                properties.put("CKID", CKID);
                properties.put("FPLX", FPLX);
                properties.put("JSFS", JSFS);
                properties.put("XDJID", "");
                properties.put("List", buildWpList());

                showLoading();
                RequestCenter.SellSave(properties, new WebServiceUtils.WebServiceCallBack() {

                    @Override
                    public void callBack(String resultStr) {
                        cancleLoading();
                        Map<String, Object> map = toMap(resultStr);
                        if (map == null) {
                            return;
                        }

                        if ("True".equals(StringUtil.convertStr(map.get("Sucecss")))) {
                            showToast(StringUtil.convertStr(map.get("Mesg")));
                            finish();
                        } else {
                            showToast(StringUtil.convertStr(map.get("Mesg")));
                        }
                    }
                });

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.ACTIVITI_FOR_RESULT_KH) {
            if (data != null) {
                KH_NAME = data.getStringExtra("NAME");
                KHID = data.getStringExtra("ID");
                mKHID.setText(KH_NAME);
            }
        } else if (requestCode == Constant.ACTIVITI_FOR_RESULT_CK) {
            if (data != null) {
                CK_NAME = data.getStringExtra("NAME");
                CKID = data.getStringExtra("ID");
                mCKID.setText(CK_NAME);
            }
        } else if (requestCode == Constant.ACTIVITI_FOR_RESULT_FPLX) {
            if (data != null) {
                FPLX_NAME = data.getStringExtra("NAME");
                FPLX = data.getStringExtra("ID");
                mFPLX.setText(FPLX_NAME);
            }
        } else if (requestCode == Constant.ACTIVITI_FOR_RESULT_JSFS) {
            if (data != null) {
                JSFS_NAME = data.getStringExtra("NAME");
                JSFS = data.getStringExtra("ID");
                mJSFS.setText(JSFS_NAME);
            }
        } else if (requestCode == Constant.ACTIVITI_FOR_RESULT_ADD_WP) { // 添加物品
            if (data != null) {
                Map<String, Object> d = (Map<String, Object>) data.getExtras().getSerializable("SELECTED_WP");
                WP_LIST.add(d);
                adapter.notifyDataSetChanged();
            }
        }
    }

    // 商品ID|计量单位ID|批次|货位ID|销售数量|销售价格
    private String buildWpList() {
        StringBuilder sb = new StringBuilder("");;
        int idx = 0;
        for (Map<String, Object> map : WP_LIST) {
            sb.append(StringUtil.convertStr(map.get("SPID"))).append("|");
            sb.append(StringUtil.convertStr(map.get("DWID"))).append("|");
            sb.append(StringUtil.convertStr(map.get("PC"))).append("|");
            sb.append(StringUtil.convertStr(map.get("HWID"))).append("|");
            sb.append(StringUtil.convertStr(map.get("SL"))).append("|");
            sb.append(StringUtil.convertStr(map.get("PRICE")));
            if (idx != WP_LIST.size() - 1) {
                sb.append("^");
            }
            idx++;
        }
        return sb.toString();
    }
}
