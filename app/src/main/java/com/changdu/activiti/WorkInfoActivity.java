package com.changdu.activiti;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.changdu.R;
import com.changdu.activiti.base.BaseActivity;
import com.changdu.activiti.basicdata.DeviceActivity;
import com.changdu.constant.Constant;
import com.changdu.manager.UserManager;
import com.changdu.network.RequestCenter;
import com.changdu.util.DateUtil;
import com.changdu.util.JsonHandler;
import com.changdu.util.StringUtil;
import com.changdu.util.WebServiceUtils;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIDateTimeSaveListener;
import com.dou361.dialogui.widget.DateSelectorWheelView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 加工信息
 */
public class WorkInfoActivity extends BaseActivity implements View.OnClickListener {

    private TextView mCPNO;
    private TextView mCPMC;
    private TextView mGXMC;

    private EditText mJSSL;
    private EditText mCurBFSL;
    private EditText mPreBFSL;
    private EditText mCYBFSL;
    private EditText mCurKJSL;
    private EditText mPreKJSL;
    private EditText mMDID;
    private EditText mQTBF;
    private EditText mSBID;
    private EditText mKGRQ_TIME;
    private RadioGroup mSJBZ;
    private RadioButton mSjYes;
    private RadioButton mSjNo;

    private TextView mSave;
    private TextView mCancel;

    private Map<String, Object> cpinfo;
    private String CPID;
    private String CPNO; // 传票号码(二维码扫描值)
    private String SBID; // 设备
    private String SBID_NAME;
    private String SJBZ = "1";
    private List<Map<String, Object>> bfItemMap = new ArrayList<>();
    private boolean LOAD_DATA_STATUS = false; // 加载数据状态

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workinfo_layout);
        mContext = this;
        setTitle(getString(R.string.title_workinfo), true);

        Intent intent = getIntent();
        CPNO = intent.getStringExtra(Constant.CAPTURE_RESULT_CODE);
        Log.e("CPNO->", CPNO);

        initView();
        getCpData();
    }

    private void initView() {
        mCPNO = findViewById(R.id.CPNO);
        mCPMC = findViewById(R.id.CPMC);
        mGXMC = findViewById(R.id.GXMC);

        mJSSL = findViewById(R.id.JSSL);
        mCurBFSL = findViewById(R.id.CurBFSL);
        mPreBFSL = findViewById(R.id.PreBFSL);
        mCYBFSL = findViewById(R.id.CYBFSL);
        mCurKJSL = findViewById(R.id.CurKJSL);
        mPreKJSL = findViewById(R.id.PreKJSL);
        mMDID = findViewById(R.id.MDID);
        mQTBF = findViewById(R.id.QTBF);
        mQTBF.setOnClickListener(this);
        mSBID = findViewById(R.id.SBID);
        mSBID.setOnClickListener(this);
        mKGRQ_TIME = findViewById(R.id.KGRQ_TIME);
        mKGRQ_TIME.setOnClickListener(this);
        mSJBZ = findViewById(R.id.SJBZ);
        mSJBZ.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == mSjYes.getId()) {
                    SJBZ = "1";
                } else if (checkedId == mSjNo.getId()) {
                    SJBZ = "0";
                }
            }
        });

        mSave = findViewById(R.id.tv_workinfo_confirm);
        mSave.setOnClickListener(this);
        mCancel = findViewById(R.id.tv_workinfo_cancel);
        mCancel.setOnClickListener(this);
    }

    /**
     * 获取传票基本信息
     */
    private void getCpData() {
        HashMap<String, String> properties = new HashMap<>();
        properties.put("CPNO", CPNO);
        properties.put("UID", UserManager.getInstance().getUID());

        showLoading();
        RequestCenter.GetCPDInfo(properties, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String resultStr) {
                if (resultStr != null) {
                    try {
                        cancleLoading();
                        Map<String, Object> reslutMap = JsonHandler.Dom2Map(resultStr);
                        if (reslutMap == null) {
                            return;
                        }
                        String Code = StringUtil.convertStr(reslutMap.get("Code"));
                        if (Objects.equals(Code, "0")) {
                            LOAD_DATA_STATUS = true;
                            mSave.setBackgroundResource(R.color.colorPrimary);

                            cpinfo = (Map<String, Object>) reslutMap.get("data");
                            CPID = StringUtil.convertStr(cpinfo.get("CPID"));

                            mCPNO.setText(StringUtil.convertStr(cpinfo.get("CPNO")));
                            mCPMC.setText(StringUtil.convertStr(cpinfo.get("CPMC")));
                            mGXMC.setText(StringUtil.convertStr(cpinfo.get("GXMC")));

                            mJSSL.setText(StringUtil.convertStr(cpinfo.get("DQSL")));
                            SBID = StringUtil.convertStr(cpinfo.get("SBID"));
                            mSBID.setText(StringUtil.convertStr(cpinfo.get("SBMC")));
                            mKGRQ_TIME.setText(StringUtil.convertStr(cpinfo.get("WGRQ")));

                        } else if (Objects.equals(Code, "1")) {
                            showToast(StringUtil.convertStr(reslutMap.get("Msg")));
                        } else {
                            showToast(getString(R.string.data_error));
                        }
                    } catch (Exception ex) {
                        showToast(getString(R.string.data_parse_error));
                    }
                } else {
                    showToast(getString(R.string.data_error));
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.QTBF: // 其他报废
                Intent intent = new Intent(mContext, QtbfActivity.class);
                Bundle localBundle = new Bundle();
                localBundle.putSerializable("bfItemMap", (Serializable)this.bfItemMap);
                localBundle.putString("CPID", CPID);
                intent.putExtras(localBundle);
                startActivityForResult(intent, Constant.ACTIVITI_FOR_RESULT_ADD_QTBF);
                break;
            case R.id.SBID:// 设备
                startActivityForResult(
                        new Intent(mContext, DeviceActivity.class), Constant.ACTIVITI_FOR_RESULT_DEVICE);
                break;
            case R.id.KGRQ_TIME: // 完工时间
                Date selectedDate = DateUtil.parseDate(mKGRQ_TIME.getText().toString());
                long date = System.currentTimeMillis() + 60000;
                if (selectedDate != null) {
                    date = selectedDate.getTime();
                }
                DialogUIUtils.showDatePick(mContext, Gravity.BOTTOM, "选择开工时间", date, DateSelectorWheelView.TYPE_YYYYMMDDHHMMSS, 0, new DialogUIDateTimeSaveListener() {
                    @Override
                    public void onSaveSelectedDate(int tag, String selectedDate) {
                        mKGRQ_TIME.setText(selectedDate);
                    }
                }).show();
                break;
            case R.id.tv_workinfo_confirm: // 保存
                submitWorkInfo();
                break;
            case R.id.tv_workinfo_cancel: // 取消
                finish();
                break;
        }
    }

    private void submitWorkInfo() {
        if (!LOAD_DATA_STATUS) {
            showToast("没有获取到传票数据");
            return;
        }
        if (StringUtil.isBlank(SBID)) {
            showToast("请选择生产设备");
            return;
        }
        String KGRQ = mKGRQ_TIME.getText().toString();
        if (StringUtil.isBlank(KGRQ)) {
            showToast("请选择开工时间");
            return;
        }

        HashMap<String, String> properties = new HashMap<>();
        properties.put("CPID", CPID);
        properties.put("GXID", StringUtil.convertStr(cpinfo.get("DQGX")));
        properties.put("UID", UserManager.getInstance().getUID());
        properties.put("JSSL", StringUtil.convertStr(cpinfo.get("DQSL")));
        properties.put("PreBFSL", formatNum(mPreBFSL.getText().toString()));
        properties.put("CurKJSL", formatNum(mCurKJSL.getText().toString()));
        properties.put("PreKJSL", formatNum(mPreKJSL.getText().toString()));
        properties.put("CYBFSL", formatNum(mCYBFSL.getText().toString()));
        properties.put("MDID", formatNum(mMDID.getText().toString()));
        properties.put("SBID", SBID);
        properties.put("KGRQ", fomatDate(mKGRQ_TIME.getText().toString()));
        properties.put("SJBZ", SJBZ);
        properties.put("QTBF", formatBf());

        showLoading();
        RequestCenter.MadeOverProc(properties, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String resultStr) {
                cancleLoading();
                if (resultStr != null) {
                    try {
                        Map<String, Object> reslutMap = JsonHandler.Dom2Map(resultStr);
                        String Code = StringUtil.convertStr(reslutMap.get("Code"));
                        if (Objects.equals(Code, "0")) { // 成功
                            showToast(StringUtil.convertStr(reslutMap.get("Msg")));
                            finish();
                        } else {
                            showToast(StringUtil.convertStr(reslutMap.get("Msg")));
                        }
                    } catch (Exception ex) {
                        showToast(getString(R.string.data_parse_error));
                    }
                } else {
                    showToast("服务器错误，提交失败");
                }
            }
        });
    }

    private String formatNum(String str) {
        if (StringUtil.isBlank(str)) {
            return "0";
        }
        return str;
    }

    private String fomatDate(String str) {
        return str.replaceAll("-", "")
                .replaceAll(":", "")
                .replace(" ", "");
    }

    private String formatBf() {
        StringBuilder localStringBuilder = new StringBuilder("");
        int i = 0;
        while (i < bfItemMap.size()) {
            Map localMap = (Map)bfItemMap.get(i);
            localStringBuilder.append(localMap.get("name") + "|");
            localStringBuilder.append(localMap.get("bf") + "|");
            localStringBuilder.append(localMap.get("qj"));
            if (i != bfItemMap.size() - 1) {
                localStringBuilder.append("^");
            }
            i += 1;
        }
        return localStringBuilder.toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.ACTIVITI_FOR_RESULT_DEVICE) {
            if (data != null) {
                SBID = data.getStringExtra("ID");
                String SB_NAME = data.getStringExtra("NAME");
                mSBID.setText(SB_NAME);
            }
        } else if (requestCode == Constant.ACTIVITI_FOR_RESULT_ADD_QTBF) {
            bfItemMap = ((List)data.getExtras().getSerializable("bfItemMap"));
            if (this.bfItemMap.size() > 0) {
                mQTBF.setText("工序数量" + this.bfItemMap.size());
            }
        }
    }
}
