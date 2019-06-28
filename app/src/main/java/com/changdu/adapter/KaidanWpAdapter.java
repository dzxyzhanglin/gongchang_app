package com.changdu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.changdu.R;
import com.changdu.util.StringUtil;

import java.util.List;
import java.util.Map;

public class KaidanWpAdapter extends CommonAdapter {

    private ViewHolder holder;
    private LayoutInflater inflater;

    public KaidanWpAdapter(Context mContext, List<Map<String, Object>> mDataList) {
        this.mContext = mContext;
        this.mDataList = mDataList;
        inflater = LayoutInflater.from(mContext);
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.item_list_xiaoshou_kaidan_wp, null);
            holder.mSPK_SPBH = view.findViewById(R.id.tv_kaidan_wp_SPK_SPBH);
            holder.mPC = view.findViewById(R.id.tv_kaidan_wp_PC);
            holder.mHW_NAME = view.findViewById(R.id.tv_kaidan_wp_HW_NAME);
            holder.mSL = view.findViewById(R.id.tv_kaidan_wp_SL);
            holder.mPRICE = view.findViewById(R.id.tv_kaidan_wp_PRICE);
            holder.mDelete = view.findViewById(R.id.iv_kaidan_wp_DELETE);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Map<String, Object> data = mDataList.get(i);
        holder.mSPK_SPBH.setText(StringUtil.convertStr(data.get("SPK_SPBH")));
        holder.mPC.setText(StringUtil.convertStr(data.get("PC")));
        holder.mHW_NAME.setText(StringUtil.convertStr(data.get("HW_NAME")));
        holder.mSL.setText(StringUtil.convertStr(data.get("SL")));
        holder.mPRICE.setText(StringUtil.convertStr(data.get("PRICE")));

        holder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataList.remove(i);
                KaidanWpAdapter.this.notifyDataSetChanged();
            }
        });

        return view;
    }

    private class ViewHolder {
        private TextView mSPK_SPBH;
        private TextView mPC;
        private TextView mHW_NAME;
        private TextView mSL;
        private TextView mPRICE;
        private ImageView mDelete;
    }
}
