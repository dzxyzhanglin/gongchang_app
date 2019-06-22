package com.changdu.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.changdu.R;
import com.changdu.util.StringUtil;

import java.util.List;
import java.util.Map;

public class JinduAdapter extends CommonAdapter {

    private ViewHolder holder;
    private LayoutInflater inflater;

    public JinduAdapter(Context mContext, List<Map<String, Object>> dataList) {
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
            view = inflater.inflate(R.layout.item_list_jd, null);
            holder.mSCD_DJBH = view.findViewById(R.id.jd_SCD_DJBH);
            holder.mCPNO = view.findViewById(R.id.jd_CPNO);
            holder.mCPZT = view.findViewById(R.id.jd_CPZT);
            holder.mCPSL = view.findViewById(R.id.jd_CPSL);
            holder.mSPRQ = view.findViewById(R.id.jd_SPRQ);
            holder.mGYB_GYMC = view.findViewById(R.id.jd_GYB_GYMC);
            holder.mSPK_SPBH = view.findViewById(R.id.jd_SPK_SPBH);
            holder.mSPK_SPMC = view.findViewById(R.id.jd_SPK_SPMC);
            holder.mSPK_SPSX = view.findViewById(R.id.jd_SPK_SPSX);
            holder.mMBMC = view.findViewById(R.id.jd_MBMC);
            holder.mDQSL = view.findViewById(R.id.jd_DQSL);
            holder.mWGRQ = view.findViewById(R.id.jd_WGRQ);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Map<String, Object> data = mDataList.get(i);
        holder.mSCD_DJBH.setText(StringUtil.convertStr(data.get("SCD_DJBH")));
        holder.mCPNO.setText(StringUtil.convertStr(data.get("CPNO")));
        holder.mCPZT.setText(StringUtil.convertStr(data.get("CPZT")));
        holder.mCPSL.setText(StringUtil.convertStr(data.get("CPSL")));
        holder.mSPRQ.setText(StringUtil.convertStr(data.get("SPRQ")));
        holder.mGYB_GYMC.setText(StringUtil.convertStr(data.get("GYB_GYMC")));
        holder.mSPK_SPBH.setText(StringUtil.convertStr(data.get("SPK_SPBH")));
        holder.mSPK_SPMC.setText(StringUtil.convertStr(data.get("SPK_SPMC")));
        holder.mSPK_SPSX.setText(StringUtil.convertStr(data.get("SPK_SPSX")));
        holder.mMBMC.setText(StringUtil.convertStr(data.get("MBMC")));
        holder.mDQSL.setText(StringUtil.convertStr(data.get("DQSL")));
        holder.mWGRQ.setText(StringUtil.convertStr(data.get("WGRQ")));


        return view;
    }

    private class ViewHolder {
        private TextView mSCD_DJBH;
        private TextView mCPNO;
        private TextView mCPZT;
        private TextView mCPSL;
        private TextView mSPRQ;
        private TextView mGYB_GYMC;
        private TextView mSPK_SPBH;
        private TextView mSPK_SPMC;
        private TextView mSPK_SPSX;
        private TextView mMBMC;
        private TextView mDQSL;
        private TextView mWGRQ;

    }
}
