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

public class KucunAdapter extends CommonAdapter {

    private ViewHolder holder;
    private LayoutInflater inflater;

    public KucunAdapter(Context mContext, List<Map<String, Object>> dataList) {
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
            view = inflater.inflate(R.layout.item_list_kucun, null);
            holder.mSPK_SPBH = view.findViewById(R.id.kucun_SPK_SPBH);
            holder.mSPK_SPMC = view.findViewById(R.id.kucun_SPK_SPMC);
            holder.mSPK_SPSX = view.findViewById(R.id.kucun_SPK_SPSX);
            holder.mJGZ_JGMC = view.findViewById(R.id.kucun_JGZ_JGMC);
            holder.mZSL = view.findViewById(R.id.kucun_ZSL);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Map<String, Object> data = mDataList.get(i);
        holder.mSPK_SPBH.setText(StringUtil.convertStr(data.get("SPK_SPBH")));
        holder.mSPK_SPMC.setText(StringUtil.convertStr(data.get("SPK_SPMC")));
        holder.mSPK_SPSX.setText(StringUtil.convertStr(data.get("SPK_SPSX")));
        holder.mJGZ_JGMC.setText(StringUtil.convertStr(data.get("JGZ_JGMC")));
        holder.mZSL.setText(StringUtil.convertStr(data.get("ZSL")));

        return view;
    }

    private class ViewHolder {
        private TextView mSPK_SPBH;
        private TextView mSPK_SPMC;
        private TextView mSPK_SPSX;
        private TextView mJGZ_JGMC;
        private TextView mZSL;
    }
}
