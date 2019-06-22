package com.changdu.activiti;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.changdu.R;
import com.changdu.activiti.base.BaseActivity;
import com.changdu.network.RequestCenter;
import com.changdu.util.CollUtil;
import com.changdu.util.JsonToMap;
import com.changdu.util.StringUtil;
import com.changdu.util.WebServiceUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 公司基本信息
 */
public class CompanyActivity extends BaseActivity {

    private TextView mEPI_CNAME;
    private TextView mEPI_NAME;
    private TextView mEPI_ABBR;
    private TextView mEPI_TEL;
    private TextView mEPI_ADDR;
    private TextView mEPI_FAX;
    private TextView mEPI_ZIP;
    private TextView mEPI_WEB;
    private TextView mEPI_EMAIL;
    private TextView mEPI_BANK;
    private TextView mEPI_BANKNO;
    private TextView mEPI_TAXNO;
    private TextView mEPI_LEGAL;
    private TextView mEPI_CONTACT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_layout);
        mContext = this;
        // 设置标题
        setTitle(getString(R.string.title_company), true);

        initView();
        initData();
    }

    private void initView() {
        mEPI_CNAME = findViewById(R.id.tv_company_EPI_CNAME);
        mEPI_NAME = findViewById(R.id.tv_company_EPI_NAME);
        mEPI_ABBR = findViewById(R.id.tv_company_EPI_ABBR);
        mEPI_TEL = findViewById(R.id.tv_company_EPI_TEL);
        mEPI_ADDR = findViewById(R.id.tv_company_EPI_ADDR);
        mEPI_FAX = findViewById(R.id.tv_company_EPI_FAX);
        mEPI_ZIP = findViewById(R.id.tv_company_EPI_ZIP);
        mEPI_WEB = findViewById(R.id.tv_company_EPI_WEB);
        mEPI_EMAIL = findViewById(R.id.tv_company_EPI_EMAIL);
        mEPI_BANK = findViewById(R.id.tv_company_EPI_BANK);
        mEPI_BANKNO = findViewById(R.id.tv_company_EPI_BANKNO);
        mEPI_TAXNO = findViewById(R.id.tv_company_EPI_TAXNO);
        mEPI_LEGAL = findViewById(R.id.tv_company_EPI_LEGAL);
        mEPI_CONTACT = findViewById(R.id.tv_company_EPI_CONTACT);
    }

    private void initData() {
        HashMap<String, String> properties = new HashMap<>();
        RequestCenter.GETQYInfo(properties, new WebServiceUtils.WebServiceCallBack() {

            @Override
            public void callBack(String resultStr) {
                if (!StringUtil.checkDataEmpty(resultStr)) {
                    Map<String, Object> map = JsonToMap.toMap(resultStr);
                    if ("True".equals(StringUtil.convertStr(map.get("Sucecss")))) {
                        List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("DATA");
                        if (!CollUtil.isEmpty(list)) {
                            Map<String, Object> data = list.get(0);
                            mEPI_CNAME.setText(StringUtil.convertStr(data.get("EPI_CNAME")));
                            mEPI_NAME.setText(StringUtil.convertStr(data.get("EPI_NAME")));
                            mEPI_ABBR.setText(StringUtil.convertStr(data.get("EPI_ABBR")));
                            mEPI_TEL.setText(StringUtil.convertStr(data.get("EPI_TEL")));
                            mEPI_ADDR.setText(StringUtil.convertStr(data.get("EPI_ADDR")));
                            mEPI_FAX.setText(StringUtil.convertStr(data.get("EPI_FAX")));
                            mEPI_ZIP.setText(StringUtil.convertStr(data.get("EPI_ZIP")));
                            mEPI_WEB.setText(StringUtil.convertStr(data.get("EPI_WEB")));
                            mEPI_EMAIL.setText(StringUtil.convertStr(data.get("EPI_EMAIL")));
                            mEPI_BANK.setText(StringUtil.convertStr(data.get("EPI_BANK")));
                            mEPI_BANKNO.setText(StringUtil.convertStr(data.get("EPI_BANKNO")));
                            mEPI_TAXNO.setText(StringUtil.convertStr(data.get("EPI_TAXNO")));
                            mEPI_LEGAL.setText(StringUtil.convertStr(data.get("EPI_LEGAL")));
                            mEPI_CONTACT.setText(StringUtil.convertStr(data.get("EPI_CONTACT")));
                        }
                    } else {
                        showToast(getString(R.string.data_error));
                    }
                } else {
                    showToast(getString(R.string.data_error));
                }
            }
        });
    }
}
