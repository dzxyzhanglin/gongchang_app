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

public class QtbfAdapter extends CommonAdapter {

    private ViewHolder holder;
    private LayoutInflater inflater;

    public QtbfAdapter(Context mContext, List<Map<String, Object>> mDataList) {
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
            view = inflater.inflate(R.layout.item_list_bf, null);
            holder.mName = view.findViewById(R.id.tv_qtbf_name);
            holder.mBf = view.findViewById(R.id.tv_qtbf_bf);
            holder.mQj = view.findViewById(R.id.tv_qtbf_qj);
            holder.mDelete = view.findViewById(R.id.tv_qtbf_delete);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Map<String, Object> data = mDataList.get(i);
        holder.mName.setText(StringUtil.convertStr(data.get("name")));
        holder.mBf.setText(StringUtil.convertStr(data.get("bf")));
        holder.mQj.setText(StringUtil.convertStr(data.get("qj")));
        holder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataList.remove(i);
                notifyDataSetChanged();
            }
        });

        return view;
    }

    private class ViewHolder {
        private TextView mName;
        private TextView mBf;
        private TextView mQj;
        private TextView mDelete;
    }
}
