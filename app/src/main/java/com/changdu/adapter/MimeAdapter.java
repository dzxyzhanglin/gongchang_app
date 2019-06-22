package com.changdu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.changdu.R;
import com.changdu.model.HomeModel;
import com.changdu.util.ImageLoaderManager;

import java.util.List;

public class MimeAdapter extends BaseAdapter {

    private Context mContext;
    private List<HomeModel> dataList;
    private ViewHolder holder;
    private LayoutInflater inflater;
    private ImageLoaderManager mImageLoader;

    public MimeAdapter(Context mContext, List<HomeModel> dataList) {
        this.mContext = mContext;
        this.dataList = dataList;
        this.inflater = LayoutInflater.from(mContext);
        this.mImageLoader = ImageLoaderManager.getInstance(mContext);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_list_mime, parent, false);
            holder.icon = convertView.findViewById(R.id.iv_mime_icon);
            holder.text = convertView.findViewById(R.id.tv_mime_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final HomeModel data = dataList.get(position);
        holder.icon.setImageResource(data.getIcon());
        holder.text.setText(data.getText());

        return convertView;
    }

    private static class ViewHolder {
        private ImageView icon;
        private TextView text;
    }
}
