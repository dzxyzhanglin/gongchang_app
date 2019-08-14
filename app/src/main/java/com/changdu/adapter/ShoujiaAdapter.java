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

public class ShoujiaAdapter extends CommonAdapter {

    private ViewHolder holder;
    private LayoutInflater inflater;

    public ShoujiaAdapter(Context mContext, List<Map<String, Object>> dataList) {
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
            view = inflater.inflate(R.layout.item_list_shoujia, null);
            holder.mDWZ_DWMC = view.findViewById(R.id.sj_DWZ_DWMC);
            holder.mXSRQ = view.findViewById(R.id.sj_XSRQ);
            holder.mJLB_JLDW = view.findViewById(R.id.sj_JLB_JLDW);
            holder.mSPK_SPBH = view.findViewById(R.id.sj_SPK_SPBH);
            holder.mSPK_SPMC = view.findViewById(R.id.sj_SPK_SPMC);
            holder.mSPK_SPSX = view.findViewById(R.id.sj_SPK_SPSX);
            holder.mJLB_XSJG = view.findViewById(R.id.sj_JLB_XSJG);
            holder.mZJJG = view.findViewById(R.id.sj_ZJJG);
            holder.mZGJG = view.findViewById(R.id.sj_ZGJG);
            holder.mZDJG = view.findViewById(R.id.sj_ZDJG);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Map<String, Object> data = mDataList.get(i);
        holder.mDWZ_DWMC.setText(StringUtil.convertStr(data.get("DWZ_DWMC")));
        holder.mXSRQ.setText(StringUtil.convertStr(data.get("XSRQ")));
        holder.mJLB_JLDW.setText(StringUtil.convertStr(data.get("JLB_JLDW")));
        holder.mSPK_SPBH.setText(StringUtil.convertStr(data.get("SPK_SPBH")));
        holder.mSPK_SPMC.setText(StringUtil.convertStr(data.get("SPK_SPMC")));
        holder.mSPK_SPSX.setText(StringUtil.convertStr(data.get("SPK_SPSX")));
        holder.mJLB_XSJG.setText(StringUtil.convertStr(data.get("JLB_XSJG")));
        holder.mZJJG.setText(StringUtil.convertStr(data.get("ZJJG")));
        holder.mZGJG.setText(StringUtil.convertStr(data.get("ZGJG")));
        holder.mZDJG.setText(StringUtil.convertStr(data.get("ZDJG")));

        return view;
    }

    private class ViewHolder {
        private TextView mDWZ_DWMC;
        private TextView mXSRQ;
        private TextView mJLB_JLDW;
        private TextView mSPK_SPBH;
        private TextView mSPK_SPMC;
        private TextView mSPK_SPSX;
        private TextView mJLB_XSJG;
        private TextView mZJJG;
        private TextView mZGJG;
        private TextView mZDJG;
    }
}
