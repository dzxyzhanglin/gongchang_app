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

public class ChanliangtongjiAdapter extends CommonAdapter {

    private ViewHolder holder;
    private LayoutInflater inflater;

    public ChanliangtongjiAdapter(Context mContext, List<Map<String, Object>> dataList) {
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
            view = inflater.inflate(R.layout.item_list_chanliangtongji, null);
            holder.mCPNO = view.findViewById(R.id.cltj_CPNO);
            holder.mSCRY = view.findViewById(R.id.cltj_SCRY);
            holder.mGYB_GYMC = view.findViewById(R.id.cltj_GYB_GYMC);
            holder.mSPK_SPBH = view.findViewById(R.id.cltj_SPK_SPBH);
            holder.mSPK_SPMC = view.findViewById(R.id.cltj_SPK_SPMC);
            holder.mJSSL = view.findViewById(R.id.cltj_JSSL);
            holder.mRKSL = view.findViewById(R.id.cltj_RKSL);
            holder.mBFSL = view.findViewById(R.id.cltj_BFSL);
            holder.mBDATE = view.findViewById(R.id.cltj_BDATE);
            holder.mEDATE = view.findViewById(R.id.cltj_EDATE);
            holder.mBQSL = view.findViewById(R.id.cltj_BQSL);
            holder.mBHGSL = view.findViewById(R.id.cltj_BHGSL);
            holder.mCYBFSL = view.findViewById(R.id.cltj_CYBFSL);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Map<String, Object> data = mDataList.get(i);
        holder.mCPNO.setText(StringUtil.convertStr(data.get("CPNO")));
        holder.mSCRY.setText(StringUtil.convertStr(data.get("SCRY")));
        holder.mGYB_GYMC.setText(StringUtil.convertStr(data.get("GYB_GYMC")));
        holder.mSPK_SPBH.setText(StringUtil.convertStr(data.get("SPK_SPBH")));
        holder.mSPK_SPMC.setText(StringUtil.convertStr(data.get("SPK_SPMC")));
        holder.mJSSL.setText(StringUtil.convertStr(data.get("JSSL")));
        holder.mRKSL.setText(StringUtil.convertStr(data.get("RKSL")));
        holder.mBFSL.setText(StringUtil.convertStr(data.get("BFSL")));
        holder.mBDATE.setText(StringUtil.convertStr(data.get("BDATE")));
        holder.mEDATE.setText(StringUtil.convertStr(data.get("EDATE")));
        holder.mBQSL.setText(StringUtil.convertStr(data.get("BQSL")));
        holder.mBHGSL.setText(StringUtil.convertStr(data.get("BHGSL")));
        holder.mCYBFSL.setText(StringUtil.convertStr(data.get("CYBFSL")));

        return view;
    }

    private class ViewHolder {
        private TextView mCPNO;
        private TextView mSCRY;
        private TextView mGYB_GYMC;
        private TextView mSPK_SPBH;
        private TextView mSPK_SPMC;
        private TextView mJSSL;
        private TextView mRKSL;
        private TextView mBFSL;
        private TextView mBDATE;
        private TextView mEDATE;
        private TextView mBQSL;
        private TextView mBHGSL;
        private TextView mCYBFSL;

    }
}
