package com.changdu.activiti;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ListView;
import android.widget.TextView;

import com.changdu.R;
import com.changdu.activiti.base.BaseActivity;
import com.changdu.adapter.XisoshoulishiAdapter;
import com.changdu.manager.UserManager;
import com.changdu.network.RequestCenter;
import com.changdu.util.CollUtil;
import com.changdu.util.JsonToMap;
import com.changdu.util.StringUtil;
import com.changdu.util.WebServiceUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 销售历史
 */
public class XiaoshoulishiActivity extends BaseActivity {

    private ListView mListView;
    private List<Map<String, Object>> dataList;
    private XisoshoulishiAdapter adapter;
    private RefreshLayout refreshLayout;

    private TextView mTotal;

    private Integer SumRecord = 0; // 总记录数
    private Integer PNum = 1; // 页码
    private String DJE = ""; // 总金额

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiaoshoulishi_layout);
        mContext = this;
        // 设置标题
        setTitle(getString(R.string.title_xiaoshoulishi_chaxun), true);

        initView();
        getDataCount();

        refreshLayout = findViewById(R.id.rl_xiaoshoulishi);
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
    }

    private void initView() {
        mListView = findViewById(R.id.lv_xiaoshoulishi);
        mTotal = findViewById(R.id.tv_total);
    }

    /**
     * 获取数据总数
     */
    private void getDataCount() {
        HashMap<String, String> properties = getProperties();

        RequestCenter.GETBillCount(properties, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String resultStr) {
                if (resultStr != null) {
                    Map<String, Object> map = JsonToMap.toMap(resultStr);
                    String total = StringUtil.convertStr(map.get("DCount"));
                    SumRecord = Integer.valueOf(total);
                    mTotal.setText(total);
                    if (SumRecord > 0) {
                        dataList = new ArrayList<>();
                        getDataList(INIT_DATA);
                    } else {
                        showToast(getString(R.string.data_empty));
                    }
                } else {
                    showToast(getString(R.string.data_error));
                }
            }
        });
    }

    private void getDataList(final int type) {
        HashMap<String, String> properties = getProperties();
        properties.put("PNum", String.valueOf(PNum));
        properties.put("SumRecord", String.valueOf(SumRecord));

        RequestCenter.GETBillInfo(properties, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String resultStr) {
                if (resultStr != null) {
                    PNum++;
                    List<Map<String, Object>> pageDataList = new ArrayList<>();
                    if (!checkDataEmpty(resultStr)) {
                        pageDataList = JsonToMap.toListMap(resultStr);
                        dataList.addAll(pageDataList);
                    }
                    if (type == INIT_DATA) { // 初始数据
                        adapter = new XisoshoulishiAdapter(mContext, pageDataList);
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
                } else {
                    showToast(getString(R.string.data_error));
                }
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
        properties.put("UID", UID);
        properties.put("DJZT", ""); // 单据状态
        properties.put("DJBH", ""); // 单据单号
        properties.put("KHMC", ""); // 客户名称
        properties.put("BDate", ""); // 开始日期
        properties.put("EDate", ""); // 截至日期
        return properties;
    }
}
