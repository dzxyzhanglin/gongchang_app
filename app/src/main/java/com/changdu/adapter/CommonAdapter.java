package com.changdu.adapter;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.List;
import java.util.Map;

public abstract class CommonAdapter extends BaseAdapter {

    protected Context mContext;

    protected List<Map<String, Object>> mDataList;

    public List<Map<String, Object>> getmDataList() {
        return mDataList;
    }

    /**
     * 刷新
     * @param dataList
     * @return
     */
    public CommonAdapter refresh(List<Map<String, Object>> dataList) {
        mDataList.clear();
        mDataList.addAll(dataList);
        notifyDataSetChanged();
        return this;
    }

    /**
     * 加载更多
     * @param dataList
     * @return
     */
    public CommonAdapter loadMore(List<Map<String, Object>> dataList) {
        mDataList.addAll(dataList);
        notifyDataSetChanged();
        return this;
    }
}
