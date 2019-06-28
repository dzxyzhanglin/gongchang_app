package com.changdu.activiti;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.changdu.R;
import com.changdu.activiti.base.BaseActivity;
import com.changdu.adapter.CheckItemAdapter;
import com.changdu.constant.Constant;
import com.changdu.manager.UserManager;
import com.changdu.network.RequestCenter;
import com.changdu.util.JsonHandler;
import com.changdu.util.StringUtil;
import com.changdu.util.WebServiceUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.dom4j.DocumentException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 巡检
 */
public class XunjianActivity extends BaseActivity implements View.OnClickListener {

    private TextView CPNO, CPMC, GXMC;
    private TextView JGRY, CPSL, RKSL, BBFSL, PBFSL, BQSL, PQSL, JSSL, SBMC, KGRQ;
    private LinearLayout bar1, bar2;
    private TextView jg_bar, xj_bar;
    private TextView confirm;
    private TextView cancel;
    private ListView listView;

    private String CPNO_CODE; // 传票号码(二维码扫描值)
    private Map<String, Object> resultMap = null;
    private Map<String, String> map = null;
    private Map<String, Object> resultMap1 = null;
    private List<Map<String, String>> list = null;
    private CheckItemAdapter checkItemAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xunjian_layout);
        mContext = this;
        setTitle(getString(R.string.title_xunjian), true);

        Intent intent = getIntent();
        CPNO_CODE = intent.getStringExtra(Constant.CAPTURE_RESULT_CODE);
        Log.e("CPNO_CODE->", CPNO_CODE);

        initView();
        getCheckInfo();
    }

    private void initView() {
        CPNO = (TextView) findViewById(R.id.CPNO);
        CPMC = (TextView) findViewById(R.id.CPMC);
        GXMC = (TextView) findViewById(R.id.GXMC);

        jg_bar = (TextView) findViewById(R.id.jg_bar);
        jg_bar.setOnClickListener(this);
        xj_bar = (TextView) findViewById(R.id.xj_bar);
        xj_bar.setOnClickListener(this);

        BBFSL = (TextView) findViewById(R.id.BBFSL);
        PBFSL = (TextView) findViewById(R.id.PBFSL);
        BQSL = (TextView) findViewById(R.id.BQSL);
        PQSL = (TextView) findViewById(R.id.PQSL);

        JGRY = (TextView) findViewById(R.id.JGRY);
        CPSL = (TextView) findViewById(R.id.CPSL);
        RKSL = (TextView) findViewById(R.id.RKSL);
        SBMC = (TextView) findViewById(R.id.SBMC);
        KGRQ = (TextView) findViewById(R.id.KGRQ);
        JSSL = (TextView) findViewById(R.id.JSSL);

        confirm = (TextView) findViewById(R.id.confirm);
        cancel = (TextView) findViewById(R.id.cancel);
        confirm.setOnClickListener(this);
        cancel.setOnClickListener(this);

        listView = (ListView) findViewById(R.id.checkList);

        bar1 = (LinearLayout) findViewById(R.id.bar1);
        bar2 = (LinearLayout) findViewById(R.id.bar2);
    }

    /**
     * 获取传票信息
     */
    private void getCheckInfo() {
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("CPNO", CPNO_CODE);

        showLoading();
        RequestCenter.GETCheckInfo(properties, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                cancleLoading();
                if (result != null) {
                    try {
                        resultMap = JsonHandler.Dom2Map(result);
                        if (resultMap != null) {
                            if (resultMap.get("Code").equals("0")) {
                                map = (Map<String, String>) resultMap.get("data");
                                CPNO.setText(map.get("CPNO"));
                                CPMC.setText(map.get("CPMC"));
                                GXMC.setText(map.get("GXMC"));

                                BBFSL.setText(map.get("BBFSL"));
                                PBFSL.setText(map.get("PBFSL"));
                                BQSL.setText(map.get("BQSL"));
                                PQSL.setText(map.get("PQSL"));

                                JGRY.setText(map.get("JGRY"));
                                CPSL.setText(map.get("CPSL"));
                                JSSL.setText(map.get("JSSL"));
                                RKSL.setText(map.get("RKSL"));
                                SBMC.setText(map.get("SBMC"));
                                KGRQ.setText(map.get("KGRQ"));

                                GETCheckItems(map.get("CPID"), map.get("DQGX"));
                            } else {
                                showToast("没有相关信息");
                            }
                        }
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                } else {
                    showToast(getString(R.string.data_error));
                }
            }
        });
    }

    private void GETCheckItems(String CPID, String DQGX) {
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("CPID", CPID);
        properties.put("DQGX", DQGX);

        showLoading();
        RequestCenter.GETCheckItems(properties, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                cancleLoading();
                Log.e("GETCheckItems result", result);
                if (result != null) {
                    try {
                        resultMap1 = toMap(result);
                        if (resultMap1 != null) {
                            list = (List<Map<String, String>>) resultMap1.get("DATA");
                            checkItemAdapter = new CheckItemAdapter(mContext, list);
                            listView.setAdapter(checkItemAdapter);
                            GETCheckYYItems();
                            Log.e("result", list.size() + "");
                        } else {
                            showToast("没有相关信息");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    showToast(getString(R.string.data_error));
                }
            }
        });
    }

    private void GETCheckYYItems() {
        HashMap<String, String> properties = new HashMap<String, String>();
        showLoading();
        RequestCenter.GETCheckYYItems(properties, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                cancleLoading();
                Log.e("GETCheckYYItems result", result);
                if (result != null) {
                    try {
                        Map<String, Object> resultMap = toMap(result);
                        if (resultMap == null) {
                            showToast(getString(R.string.data_parse_error));
                            return;
                        }
                        List<Map<String, Object>> listMap = (List<Map<String, Object>>) resultMap.get("DATA");
                        List<String> dataList = new ArrayList<String>();
                        for (Map<String, Object> stringObjectMap : listMap) {
                            dataList.add(StringUtil.convertStr(stringObjectMap.get("ZDM_XMMC")));
                        }
                        checkItemAdapter.setArr(dataList);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    showToast(getString(R.string.data_error));
                }
            }
        });
    }

    private void changeBar(int bar) {
        if (bar == 0) {
            jg_bar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            xj_bar.setBackgroundColor(getResources().getColor(R.color.color_gray));
            bar1.setVisibility(View.VISIBLE);
            bar2.setVisibility(View.GONE);
        } else if (bar == 1) {
            jg_bar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            xj_bar.setBackgroundColor(getResources().getColor(R.color.color_gray));
            bar1.setVisibility(View.GONE);
            bar2.setVisibility(View.VISIBLE);
        }

    }

    private void MadeCheckProc(String JYXM) {
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("CPID", map.get("CPID"));
        properties.put("GXID", map.get("DQGX"));
        properties.put("UID", UserManager.getInstance().getUID());
        properties.put("JYXM", JYXM);

        showLoading();
        RequestCenter.MadeCheckProc(properties, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                cancleLoading();
                if (result != null) {
                    try {
                        Map<String, String> map = JsonHandler.readStringXmlOut(result);
                        if (!"0".equals(map.get("Code"))) {
                            showToast(StringUtil.convertStr(map.get("Msg")));
                        } else {
                            showToast("提交成功");
                            finish();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        showToast("提交失败");
                    }
                } else {
                    showToast("提交失败");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                finish();
                break;
            case R.id.jg_bar:
                changeBar(0);
                break;
            case R.id.xj_bar:
                changeBar(1);
                break;
            case R.id.confirm:
                if (checkItemAdapter != null) {
                    StringBuilder stringBuilder = new StringBuilder();
                    Map<String, String> check = checkItemAdapter.getStringBuilder();
                    int size = list.size();
                    boolean hasSpe = false;
                    for (int i = 0; i < size; i++) {
                        String item = check.get(i + "");
                        if (!StringUtil.isBlank(item)) {
                            if (hasSpe) {
                                stringBuilder.append("^");
                            }
                            stringBuilder.append(item);
                            hasSpe = true;
                        }
                    }
                    Log.e("JYXM", stringBuilder.toString() + "");
                    if (resultMap == null || map == null || resultMap1 == null) {
                        showToast("没有巡检信息");
                    } else {
                        MadeCheckProc(stringBuilder.toString());
                    }

                    Log.e("getStringBuilder", stringBuilder + "");
                }
                break;
        }
    }
}
