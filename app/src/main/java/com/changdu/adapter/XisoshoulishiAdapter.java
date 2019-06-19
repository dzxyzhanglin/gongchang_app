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

public class XisoshoulishiAdapter extends CommonAdapter {

    private ViewHolder holder;
    private LayoutInflater inflater;

    public XisoshoulishiAdapter(Context mContext, List<Map<String, Object>> dataList) {
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
            view = inflater.inflate(R.layout.item_list_xiaoshoulishi, null);
            holder.mPFD_DJBH = view.findViewById(R.id.xsls_PFD_DJBH);
            holder.mDWZ_DWMC = view.findViewById(R.id.xsls_DWZ_DWMC);
            holder.mUSR_NAME = view.findViewById(R.id.xsls_USR_NAME);
            holder.mPFD_ZDJE = view.findViewById(R.id.xsls_PFD_ZDJE);
            holder.mPFD_DJZT = view.findViewById(R.id.xsls_PFD_DJZT);
            holder.mPFD_DJRQ = view.findViewById(R.id.xsls_PFD_DJRQ);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Map<String, Object> data = mDataList.get(i);
        holder.mPFD_DJBH.setText(StringUtil.convertStr(data.get("PFD_DJBH")));
        holder.mDWZ_DWMC.setText(StringUtil.convertStr(data.get("DWZ_DWMC")));
        holder.mUSR_NAME.setText(StringUtil.convertStr(data.get("USR_NAME")));
        holder.mPFD_ZDJE.setText(StringUtil.convertStr(data.get("PFD_ZDJE")));
        holder.mPFD_DJZT.setText(StringUtil.convertStr(data.get("PFD_DJZT")));
        holder.mPFD_DJRQ.setText(StringUtil.convertStr(data.get("PFD_DJRQ")));

        return view;
    }

    private class ViewHolder {
        private TextView mPFD_DJBH;
        private TextView mDWZ_DWMC;
        private TextView mUSR_NAME;
        private TextView mPFD_ZDJE;
        private TextView mPFD_DJZT;
        private TextView mPFD_DJRQ;
    }
}
