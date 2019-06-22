package com.changdu.activiti;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.changdu.R;
import com.changdu.activiti.base.BaseActivity;
import com.changdu.activiti.basicdata.CkActivity;
import com.changdu.activiti.basicdata.FplxActivity;
import com.changdu.activiti.basicdata.JsfsActivity;
import com.changdu.constant.Constant;

/**
 * 销售开单
 */
public class XiaoshouKaidanActivity extends BaseActivity implements View.OnClickListener {

    private EditText mKHID;
    private EditText mCKID;
    private EditText mFPLX;
    private EditText mJSFS;
    private ImageView mAddWp;

    private String KHID;
    private String CKID; //仓库
    private String CK_NAME;
    private String FPLX; // 发票类型
    private String FPLX_NAME;
    private String JSFS; // 结算方式
    private String JSFS_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiaoshou_kaidan_layout);

        mContext = this;
        // 设置标题
        setTitle(getString(R.string.title_xiaoshou_kaidan), true);

        initView();
    }

    private void initView() {
        mKHID = findViewById(R.id.et_kaidan_KHID);
        mCKID = findViewById(R.id.et_kaidan_CKID);
        mCKID.setOnClickListener(this);
        mFPLX = findViewById(R.id.et_kaidan_FPLX);
        mFPLX.setOnClickListener(this);
        mJSFS = findViewById(R.id.et_kaidan_JSFS);
        mJSFS.setOnClickListener(this);

        mAddWp = findViewById(R.id.btn_kaidan_addwp);
        mAddWp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
                startActivityForResult(
                        new Intent(mContext, XiaoshouKaidanAddWpActivity.class), Constant.ACTIVITI_FOR_RESULT_ADD_WP);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.ACTIVITI_FOR_RESULT_CK) {
            if (data != null) {
                CK_NAME = data.getStringExtra("CKNAME");
                CKID = data.getStringExtra("CKID");
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

            }
        }
    }
}
