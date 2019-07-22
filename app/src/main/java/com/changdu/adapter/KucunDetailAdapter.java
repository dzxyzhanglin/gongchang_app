package com.changdu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.changdu.R;
import com.changdu.util.StringUtil;

import java.util.List;
import java.util.Map;

public class KucunDetailAdapter extends CommonAdapter {

    private ViewHolder holder;
    private LayoutInflater inflater;

    public KucunDetailAdapter(Context mContext, List<Map<String, Object>> dataList) {
        this.mContext = mContext;
        this.mDataList = dataList;
        this.inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int i) {
        return mDataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.item_list_kucun_detail, null);
            holder.mNO = view.findViewById(R.id.kucun_detail_NO);
            holder.mJGZ_JGMC = view.findViewById(R.id.kucun_detail_JGZ_JGMC);
            holder.mZPD_PC = view.findViewById(R.id.kucun_detail_ZPD_PC);
            holder.mHWB_HWMC = view.findViewById(R.id.kucun_detail_HWB_HWMC);
            holder.mZPD_SL = view.findViewById(R.id.kucun_detail_ZPD_SL);
            holder.mZPD_JLDW = view.findViewById(R.id.kucun_detail_ZPD_JLDW);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Map<String, Object> data = mDataList.get(i);
        holder.mNO.setText((i + 1) + "");
        holder.mJGZ_JGMC.setText(StringUtil.convertStr(data.get("JGZ_JGMC")));
        holder.mZPD_PC.setText(StringUtil.convertStr(data.get("ZPD_PC")));
        holder.mHWB_HWMC.setText(StringUtil.convertStr(data.get("HWB_HWMC")));
        holder.mZPD_SL.setText(StringUtil.convertStr(data.get("ZPD_SL")));
        holder.mZPD_JLDW.setText(StringUtil.convertStr(data.get("JLB_JLDW")));

        return view;
    }

    private class ViewHolder {
        private TextView mNO;
        private TextView mJGZ_JGMC;
        private TextView mZPD_PC;
        private TextView mHWB_HWMC;
        private TextView mZPD_SL;
        private TextView mZPD_JLDW;
    }
}
