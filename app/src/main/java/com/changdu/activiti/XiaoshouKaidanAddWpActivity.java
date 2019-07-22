package com.changdu.activiti;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.changdu.R;
import com.changdu.activiti.base.BaseActivity;
import com.changdu.activiti.basicdata.CkActivity;
import com.changdu.activiti.basicdata.HwActivity;
import com.changdu.activiti.basicdata.JldwActivity;
import com.changdu.adapter.XiaoshouKaidanAddWpAdapter;
import com.changdu.constant.Constant;
import com.changdu.manager.UserManager;
import com.changdu.network.RequestCenter;
import com.changdu.util.CollUtil;
import com.changdu.util.StringUtil;
import com.changdu.util.WebServiceUtils;
import com.dou361.dialogui.DialogUIUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 销售开单时选择物品
 */
public class XiaoshouKaidanAddWpActivity extends BaseActivity implements View.OnClickListener {
    private ListView mListView;
    private List<Map<String, Object>> dataList;
    private XiaoshouKaidanAddWpAdapter adapter;
    private RefreshLayout refreshLayout;

    private EditText mSPBH;
    private EditText mSPMC;
    private EditText mSPSX;
    private EditText mZJM;
    private EditText mCKID;
    private TextView mTotal;
    private Button mSearch;
    private Button mAddConfirm;

    private Integer SumRecord = 0; // 总记录数
    private Integer PNum = 1; // 页码
    private String CKID = "";
    private String CK_NAME;

    // 底部弹窗
    private EditText mDialogPC; // 批次
    private EditText mDialogHWID; // 货位
    private EditText mDialogDW; // 单位
    private EditText mDialogSL; // 数量
    private EditText mDialogPRICE; // 价格
    private Dialog mButtonDialog;
    private Button mButtonDialogSave;

    private String DialogHWID = "";
    private String DialogHW_NAME = "";
    private String DialogDWID = "";
    private String DialogDW_NAME = "";
    private Map<String, Object> selectedData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kucun_layout);
        mContext = this;
        // 设置标题
        setTitle(getString(R.string.title_xiaoshou_kaidan_addwp), true);

        Intent intent = getIntent();
        CKID = intent.getStringExtra("CKID");
        CK_NAME = intent.getStringExtra("CK_NAME");

        initView();
        getDataCount();
    }

    private void initView() {
        mListView = findViewById(R.id.kucun_listview);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedData = dataList.get(position);
                adapter.setSelectPosition(position);
                adapter.notifyDataSetChanged();
            }
        });

        mSPBH = findViewById(R.id.et_kucun_SPBH);
        mSPMC = findViewById(R.id.et_kucun_SPMC);
        mSPSX = findViewById(R.id.et_kucun_SPSX);
        mZJM = findViewById(R.id.et_kucun_ZJM);
        mCKID = findViewById(R.id.et_kucun_CKID);
        mCKID.setText(CK_NAME);
        mTotal = findViewById(R.id.tv_total);
        mSearch = findViewById(R.id.btn_kucun);
        mSearch.setOnClickListener(this);

        mAddConfirm = findViewById(R.id.btn_kaidan_confirm);
        mAddConfirm.setVisibility(View.VISIBLE);
        mAddConfirm.setOnClickListener(this);

        refreshLayout = findViewById(R.id.kucun_refreshLayout);
        refreshLayout.setEnableAutoLoadMore(true);//开启自动加载功能（非必须）
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout rl) {
                rl.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        PNum = 1;
                        getDataList(REFRESH_DATA);
                    }
                }, 500);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout rl) {
                rl.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getDataList(LOAD_MORE_DATA);
                    }
                }, 500);
            }
        });

        // 搜索抽屉
        drawerLayout = findViewById(R.id.drawer_kucun_layout);
        mSearchLayout = findViewById(R.id.kucun_search_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_search_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_add_search) {
            openOrCloseDrawer();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void getDataCount() {

        HashMap<String, String> properties = getProperties();

        showLoading();

        // 获取记录总数
        RequestCenter.GETSpzlCount(properties, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String resultStr) {
                Map<String, Object> map = toMap(resultStr);
                if (map == null) {
                    cancleLoading();
                    return;
                }

                String total = StringUtil.convertStr(map.get("DCOUNT"));
                SumRecord = Integer.valueOf(total);
                mTotal.setText(total);
                if (SumRecord > 0) {
                    dataList = new ArrayList<>();
                    getDataList(INIT_DATA);
                } else {
                    showToast(getString(R.string.data_empty));
                    if (dataList != null && dataList.size() > 0) {
                        dataList = new ArrayList<>();
                        adapter.setmDataList(dataList);
                        adapter.notifyDataSetChanged();
                    }
                    cancleLoading();
                }
            }
        });
    }

    private void getDataList(final int type) {
        HashMap<String, String> properties = getProperties();
        properties.put("PNum", String.valueOf(PNum));
        properties.put("SumRecord", String.valueOf(SumRecord));

        RequestCenter.GETSpzlInfo(properties, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String resultStr) {
                List<Map<String, Object>> pageDataList = toListMap(resultStr);
                if (pageDataList == null) {
                    cancleLoading();
                    return;
                }
                dataList.addAll(pageDataList);
                PNum++;

                if (type == INIT_DATA) { // 初始数据
                    adapter = new XiaoshouKaidanAddWpAdapter(mContext, pageDataList);
                    mListView.setAdapter(adapter);
                    if (CollUtil.isEmpty(pageDataList)) {
                        showToast(getString(R.string.data_empty));
                    }
                } else if (type == REFRESH_DATA) { // 刷新数据
                    adapter.refresh(pageDataList);
                    refreshLayout.finishRefresh();
                    refreshLayout.resetNoMoreData();
                } else if (type == LOAD_MORE_DATA) { // 加载更多数据
                    if (CollUtil.isEmpty(pageDataList)) {
                        refreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
                    } else {
                        adapter.loadMore(pageDataList);
                        refreshLayout.finishLoadMore();
                    }
                }

                cancleLoading();
            }
        });
    }

    /**
     * 获取查询条件
     * @return
     */
    private HashMap<String, String> getProperties() {
        HashMap<String, String> properties = new HashMap<>();
        String UID = UserManager.getInstance().getUID();
        String SPBH = mSPBH.getText().toString();
        String SPMC = mSPMC.getText().toString();
        String SPSX = mSPSX.getText().toString();
        String ZJM = mZJM.getText().toString();

        properties.put("UID", UID);
        properties.put("SPBH", SPBH);
        properties.put("SPMC", SPMC);
        properties.put("SPSX", SPSX);
        properties.put("ZJM", ZJM);
        properties.put("CKID", CKID);
        return properties;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_kucun_CKID:
                Intent intent = new Intent(mContext, CkActivity.class);
                startActivityForResult(intent, Constant.ACTIVITI_FOR_RESULT_CK);
                break;
            case R.id.btn_kucun:
                PNum = 1;
                openOrCloseDrawer();
                getDataCount();
                break;

            // 底部弹窗
            case R.id.btn_kaidan_confirm: // 确认选择物品
                if (selectedData == null) {
                    showToast("请选择开单物品");
                    return;
                }
                View rootViewB = View.inflate(mContext, R.layout.item_xiaoshou_kaidan_addwp_dialog, null);
                if (mButtonDialog == null) {
                    mButtonDialog = DialogUIUtils.showCustomBottomAlert(this, rootViewB).show();
                    Log.e("mButtonDialog", "创建底部弹窗");
                } else {
                    // 清空之前已经填写的内容
                    mDialogPC.setText("");
                    mDialogHWID.setText("");
                    mDialogDW.setText("");
                    mDialogSL.setText("");
                    mDialogPRICE.setText("");
                    mButtonDialog.setTitle("");

                    DialogHWID = "";
                    DialogDWID = "";
                    DialogHW_NAME = "";
                    DialogDW_NAME = "";

                    mButtonDialog.show();
                }
                // 底部弹窗
                mDialogPC = mButtonDialog.findViewById(R.id.et_kaidan_dialog_PC); // 批次
                mDialogHWID = mButtonDialog.findViewById(R.id.et_kaidan_dialog_HWID); // 货位
                mDialogHWID.setOnClickListener(this);
                mDialogDW = mButtonDialog.findViewById(R.id.et_kaidan_dialog_DW); // 单位
                mDialogDW.setOnClickListener(this);
                mDialogSL = mButtonDialog.findViewById(R.id.et_kaidan_dialog_SL); // 数量
                mDialogPRICE = mButtonDialog.findViewById(R.id.et_kaidan_dialog_PRICE); // 价格
                mButtonDialogSave = mButtonDialog.findViewById(R.id.btn_kaidan_dialog_save);
                mButtonDialogSave.setOnClickListener(this);
                break;
            case R.id.et_kaidan_dialog_HWID: // 货位
                Intent hwIntent = new Intent(mContext, HwActivity.class);
                hwIntent.putExtra("CKID", CKID);
                startActivityForResult(hwIntent, Constant.ACTIVITI_FOR_RESULT_HW);
                break;
            case R.id.et_kaidan_dialog_DW: // 单位
                Intent dwIntent = new Intent(mContext, JldwActivity.class);
                String SPID = StringUtil.convertStr(selectedData.get("ID"));
                dwIntent.putExtra("SPID", SPID);
                startActivityForResult(dwIntent, Constant.ACTIVITI_FOR_RESULT_JLDW);
                break;
            case R.id.btn_kaidan_dialog_save: // 保存
                Map<String, Object> m = new HashMap<>();
                m.put("SPID", StringUtil.convertStr(selectedData.get("ID")));
                m.put("SPK_SPBH", StringUtil.convertStr(selectedData.get("SPK_SPBH")));
                m.put("DWID", DialogDWID);
                m.put("DW_NAME", DialogDW_NAME);
                m.put("PC", mDialogPC.getText().toString());
                m.put("HWID", DialogHWID);
                m.put("HW_NAME", DialogHW_NAME);
                m.put("SL", mDialogSL.getText().toString());
                m.put("PRICE", mDialogPRICE.getText().toString());

                Intent i = new Intent();
                Bundle localBundle = new Bundle();
                localBundle.putSerializable("SELECTED_WP", (Serializable) m);
                i.putExtras(localBundle);
                setResult(Constant.ACTIVITI_FOR_RESULT_ADD_WP, i);

                if (mButtonDialog != null) {
                    mButtonDialog.dismiss();
                }
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.ACTIVITI_FOR_RESULT_CK) {
            if (data != null) {
                String ckName = data.getStringExtra("NAME");
                CKID = data.getStringExtra("ID");
                mCKID.setText(ckName);
            }
        } else if (requestCode == Constant.ACTIVITI_FOR_RESULT_HW) { // 货位
            if (data != null) {
                DialogHW_NAME = data.getStringExtra("NAME");
                DialogHWID = data.getStringExtra("ID");
                mDialogHWID.setText(DialogHW_NAME);
            }
        } else if (requestCode == Constant.ACTIVITI_FOR_RESULT_JLDW) {
            if (data != null) {
                DialogDW_NAME = data.getStringExtra("NAME");
                DialogDWID = data.getStringExtra("ID");
                mDialogDW.setText(DialogDW_NAME);
            }
        }
    }

}
