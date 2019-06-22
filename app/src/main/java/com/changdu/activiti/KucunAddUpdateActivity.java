package com.changdu.activiti;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.changdu.R;
import com.changdu.activiti.base.BaseActivity;
import com.changdu.constant.Constant;

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
        mZPD_JGID = findViewById(R.id.et_kucun_addupdate_ZPD_JGID);
        mZPD_PC = findViewById(R.id.et_kucun_addupdate_ZPD_PC);
        mZPD_HW = findViewById(R.id.et_kucun_addupdate_ZPD_HW);
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

        // 设置标题
        String title = (Constant.OPERATE_ADD.equals(OPERATE) ? "新增" : "修改") + "物品";
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

                break;

        }
    }
}
