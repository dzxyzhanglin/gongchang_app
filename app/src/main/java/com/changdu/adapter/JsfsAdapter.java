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

public class JsfsAdapter extends CommonAdapter {

    private ViewHolder holder;
    private LayoutInflater inflater;

    public JsfsAdapter(Context mContext, List<Map<String, Object>> mDataList) {
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.item_list_ck, null);
            holder.mCkName = view.findViewById(R.id.tv_ckname);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Map<String, Object> data = mDataList.get(i);
        holder.mCkName.setText(StringUtil.convertStr(data.get("GXF_MC")));

        return view;
    }

    private class ViewHolder {
        private TextView mCkName;
    }
}
