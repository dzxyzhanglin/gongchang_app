package com.changdu.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.changdu.R;
import com.changdu.util.StringUtil;

import org.apache.commons.codec.binary.StringUtils;

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
            holder.mSPK_SPMC = view.findViewById(R.id.tv_kaidan_wp_SPK_SPMC);
            holder.mSPK_SPSX = view.findViewById(R.id.tv_kaidan_wp_SPK_SPSX);
            holder.mPC = view.findViewById(R.id.tv_kaidan_wp_PC);
            holder.mHW_NAME = view.findViewById(R.id.tv_kaidan_wp_HW_NAME);
            holder.mSL = view.findViewById(R.id.tv_kaidan_wp_SL);
            holder.mJLB_DWMC = view.findViewById(R.id.tv_kaidan_wp_JLB_DWMC);
            holder.mPRICE = view.findViewById(R.id.tv_kaidan_wp_PRICE);
            holder.mTOTAL_PRICE = view.findViewById(R.id.tv_kaidan_wp_TOTAL_PRICE);
            holder.mDelete = view.findViewById(R.id.iv_kaidan_wp_DELETE);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Map<String, Object> data = mDataList.get(i);
        holder.mSPK_SPBH.setText(StringUtil.convertStr(data.get("SPK_SPBH")));
        holder.mSPK_SPMC.setText(StringUtil.convertStr(data.get("SPK_SPMC")));
        holder.mSPK_SPSX.setText(StringUtil.convertStr(data.get("SPK_SPSX")));
        holder.mPC.setText(StringUtil.convertStr(data.get("PC")));
        holder.mHW_NAME.setText(StringUtil.convertStr(data.get("HW_NAME")));
        holder.mSL.setText(StringUtil.convertStr(data.get("SL")));
        holder.mJLB_DWMC.setText(StringUtil.convertStr(data.get("JLB_DWMC")));
        holder.mPRICE.setText(StringUtil.convertStr(data.get("PRICE")));

        // 总金额
        holder.mTOTAL_PRICE.setText(calcTotalPrice(
                StringUtil.convertStr(data.get("SL")),
                StringUtil.convertStr(data.get("PRICE"))
        ));

        holder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataList.remove(i);
                KaidanWpAdapter.this.notifyDataSetChanged();
            }
        });

        return view;
    }

    private String calcTotalPrice(String sl, String price) {
        if (StringUtil.isBlank(sl) || StringUtil.isBlank(price)) {
            return "0";
        }

        try {
            Integer slInt = Integer.valueOf(sl);
            double priceDouble = Double.valueOf(price);
            String res = String.valueOf(slInt * priceDouble);
            if (res.indexOf(".") != -1) {
                int endIdx = res.indexOf(".") + 3;
                return res.substring(0, endIdx);
            } else {
                return res;
            }
        } catch (Exception ex) {
            Log.e("price", "计算总价出错：" + ex);
            return "";
        }
    }

    private class ViewHolder {
        private TextView mSPK_SPBH;
        private TextView mSPK_SPMC;
        private TextView mSPK_SPSX;
        private TextView mPC;
        private TextView mHW_NAME;
        private TextView mSL;
        private TextView mJLB_DWMC;
        private TextView mPRICE;
        private TextView mTOTAL_PRICE;
        private ImageView mDelete;
    }
}
