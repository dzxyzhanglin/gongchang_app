package com.changdu.activiti;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.changdu.R;
import com.changdu.activiti.base.BaseActivity;
import com.changdu.activiti.basicdata.CkActivity;
import com.changdu.activiti.basicdata.HwActivity;
import com.changdu.constant.Constant;
import com.changdu.manager.UserManager;
import com.changdu.network.RequestCenter;
import com.changdu.util.JsonToMap;
import com.changdu.util.StringUtil;
import com.changdu.util.WebServiceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 新增/修改物品
 */
public class KucunAddUpdateActivity extends BaseActivity implements View.OnClickListener {

    private Button mAddUpdate;
    private EditText mZPD_JGID;
    private EditText mZPD_PC;
    private EditText mZPD_HW;
    private EditText mZPD_SL;

    private String SPID = ""; // 物品ID
    private String ZPD_JGID = ""; // 仓库ID
    private String JGZ_JGMC = ""; // 仓库名称
    private String ZPD_PC = ""; // 批次
    private String ZPD_HW = ""; // 货位ID
    private String HWB_HWMC = ""; // 货位名称
    private String ZPD_SL = ""; // 数量
    private String OPERATE = "";
    private List<Map<String, Object>> dataList;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kucun_addupdate_layout);
        mContext = this;

        initView();
        initData();
    }

    private void initView() {
        mAddUpdate = findViewById(R.id.btn_addupdate);
        mAddUpdate.setOnClickListener(this);
        mZPD_JGID = findViewById(R.id.et_kucun_addupdate_ZPD_JGID);
        mZPD_JGID.setOnClickListener(this);
        mZPD_PC = findViewById(R.id.et_kucun_addupdate_ZPD_PC);
        mZPD_HW = findViewById(R.id.et_kucun_addupdate_ZPD_HW);
        mZPD_HW.setOnClickListener(this);
        mZPD_SL = findViewById(R.id.et_kucun_addupdate_ZPD_SL);
    }

    private void initData() {
        Intent intent = getIntent();
        SPID = intent.getStringExtra("SPID");
        ZPD_JGID = intent.getStringExtra("ZPD_JGID");
        JGZ_JGMC = intent.getStringExtra("JGZ_JGMC");
        ZPD_PC = intent.getStringExtra("ZPD_PC");
        ZPD_HW = intent.getStringExtra("ZPD_HW");
        HWB_HWMC = intent.getStringExtra("HWB_HWMC");
        ZPD_SL = intent.getStringExtra("ZPD_SL");
        OPERATE = intent.getStringExtra("OPERATE");
        index = intent.getIntExtra("DATA_INDEX", -1);

        String mData = intent.getStringExtra("DATA_LIST");
        if (StringUtil.isBlank(mData)) {
            dataList = new ArrayList<>();
        } else {
            Map<String, Object> map = toMap(mData);
            dataList = (List<Map<String, Object>>) map.get("DList");
        }
        Log.e("d", dataList.toString());

        // 设置标题
        String title = (Constant.OPERATE_ADD.equals(OPERATE) ? "新增" : "修改") + "物品库存信息";
        setTitle(title, true);
        mAddUpdate.setText(title);

        // 如果是修改，需要回显数据
        if (Constant.OPERATE_UPDATE.equals(OPERATE)) {
            mZPD_JGID.setText(JGZ_JGMC);
            mZPD_PC.setText(ZPD_PC);
            mZPD_HW.setText(HWB_HWMC);
            mZPD_SL.setText(ZPD_SL);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_addupdate:
                saveData();
                break;
            case R.id.et_kucun_addupdate_ZPD_JGID: // 选择仓库
                startActivityForResult(new Intent(mContext, CkActivity.class), Constant.ACTIVITI_FOR_RESULT_CK);
                break;
            case R.id.et_kucun_addupdate_ZPD_HW: // 选择货位
                if (StringUtil.isBlank(ZPD_JGID)) {
                    showToast("请先选择仓库");
                    return;
                }
                Intent hwIntent = new Intent(mContext, HwActivity.class);
                hwIntent.putExtra("CKID", ZPD_JGID);
                startActivityForResult(hwIntent, Constant.ACTIVITI_FOR_RESULT_HW);
                break;
        }
    }

    private void saveData() {
        if (StringUtil.isBlank(ZPD_JGID)) {
            showToast("请选择仓库");
            return;
        }
        ZPD_PC = mZPD_PC.getText().toString();
        ZPD_SL = mZPD_SL.getText().toString();

        HashMap<String, String> properties = new HashMap<>();
        properties.put("UID", UserManager.getInstance().getUID());
        String WU_LIST = buildDataList();
        properties.put("List", WU_LIST);

        showLoading();
        RequestCenter.InventSave(properties, new WebServiceUtils.WebServiceCallBack() {

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
    }

    // 物品ID|单位ID|批次|仓库ID|货位ID|系统数|实际数
    private String buildDataList() {
        List<String> lists = new ArrayList<>();
        int idx = 0;
        for (Map<String, Object> map : dataList) {
            StringBuilder sb = new StringBuilder();
            if (Constant.OPERATE_UPDATE.equals(OPERATE) && idx == index) {
                sb.append(SPID).append("|");
                sb.append(StringUtil.convertStr(map.get("ZPD_JLDW"))).append("|");
                sb.append(ZPD_PC).append("|");
                sb.append(ZPD_JGID).append("|");
                sb.append(ZPD_HW).append("|");
                sb.append(0).append("|");
                sb.append(ZPD_SL);
            } else {
                sb.append(SPID).append("|");
                sb.append(StringUtil.convertStr(map.get("ZPD_JLDW"))).append("|");
                sb.append(StringUtil.convertStr(map.get("ZPD_PC"))).append("|");
                sb.append(StringUtil.convertStr(map.get("ZPD_JGID"))).append("|");
                sb.append(StringUtil.convertStr(map.get("ZPD_HW"))).append("|");
                sb.append(0).append("|");
                sb.append(StringUtil.convertStr(map.get("ZPD_SL")));
            }
            lists.add(sb.toString());
            idx++;
        }

        if (Constant.OPERATE_ADD.equals(OPERATE)) {
            StringBuilder sb = new StringBuilder();
            sb.append(SPID).append("|");
            sb.append("{1D586C20-D3E8-4833-B9DF-5ECD266F9396}").append("|");
            sb.append(ZPD_PC).append("|");
            sb.append(ZPD_JGID).append("|");
            sb.append(ZPD_HW).append("|");
            sb.append(0).append("|");
            sb.append(ZPD_SL);
            lists.add(sb.toString());
        }

        StringBuilder sb = new StringBuilder();
        int jdx = 0;
        for (String str : lists) {
            sb.append(str);
            if (jdx != lists.size() - 1) {
                sb.append("^");
            }
            jdx++;
        }

        return sb.toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.ACTIVITI_FOR_RESULT_CK) {
            if (data != null) {
                JGZ_JGMC = data.getStringExtra("NAME");
                ZPD_JGID = data.getStringExtra("ID");
                mZPD_JGID.setText(JGZ_JGMC);

                // 清空货位选择
                ZPD_HW = "";
                mZPD_HW.setText("");
            }
        } else if (requestCode == Constant.ACTIVITI_FOR_RESULT_HW) {
            if (data != null) {
                HWB_HWMC = data.getStringExtra("NAME");
                ZPD_HW = data.getStringExtra("ID");
                mZPD_HW.setText(HWB_HWMC);
            }
        }
    }
}
