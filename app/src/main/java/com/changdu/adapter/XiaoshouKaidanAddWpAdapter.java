package com.changdu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.changdu.R;
import com.changdu.util.StringUtil;

import java.util.List;
import java.util.Map;

public class XiaoshouKaidanAddWpAdapter extends CommonAdapter {

    private ViewHolder holder;
    private LayoutInflater inflater;
    private int selectPosition = -1;

    public XiaoshouKaidanAddWpAdapter(Context mContext, List<Map<String, Object>> dataList) {
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


    public void setSelectPosition(int position) {
        this.selectPosition = position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.item_list_xiaoshou_kaidan_addwp, null);
            holder.mRb = view.findViewById(R.id.xskd_idx);
            holder.mSPK_SPBH = view.findViewById(R.id.xskd_SPK_SPBH);
            holder.mSPK_SPMC = view.findViewById(R.id.xskd_SPK_SPMC);
            holder.mSPK_SPSX = view.findViewById(R.id.xskd_SPK_SPSX);
            holder.mJLB_JLDW = view.findViewById(R.id.xskd_JLB_JLDW);
            holder.mJGZ_JGMC = view.findViewById(R.id.xskd_JGZ_JGMC);
            holder.mZSL = view.findViewById(R.id.xskd_ZSL);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Map<String, Object> data = mDataList.get(i);
        holder.mSPK_SPBH.setText(StringUtil.convertStr(data.get("SPK_SPBH")));
        holder.mSPK_SPMC.setText(StringUtil.convertStr(data.get("SPK_SPMC")));
        holder.mSPK_SPSX.setText(StringUtil.convertStr(data.get("SPK_SPSX")));
        holder.mJLB_JLDW.setText(StringUtil.convertStr(data.get("JLB_DWMC")));
        holder.mJGZ_JGMC.setText(StringUtil.convertStr(data.get("JGZ_JGMC")));
        holder.mZSL.setText(StringUtil.convertStr(data.get("ZSL")));

        if (selectPosition  == i) {
            holder.mRb.setChecked(true);
        } else {
            holder.mRb.setChecked(false);
        }

        return view;
    }

    private class ViewHolder {
        private RadioButton mRb;
        private TextView mSPK_SPBH;
        private TextView mSPK_SPMC;
        private TextView mSPK_SPSX;
        private TextView mJLB_JLDW;
        private TextView mJGZ_JGMC;
        private TextView mZSL;
    }
}
