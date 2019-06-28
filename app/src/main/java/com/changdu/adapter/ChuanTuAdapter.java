package com.changdu.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;

import com.changdu.R;
import com.changdu.activiti.ChuanTuActivity;
import com.changdu.activiti.PlusImageActivity;
import com.changdu.constant.Constant;
import com.changdu.network.RequestCenter;
import com.changdu.util.ImageLoaderManager;
import com.changdu.util.ImageUtil;
import com.changdu.util.StringUtil;
import com.changdu.util.WebServiceUtils;
import com.luck.picture.lib.PictureSelector;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChuanTuAdapter extends CommonAdapter {

    private Activity mActivity;
    private ViewHolder holder;
    private LayoutInflater inflater;
    private ImageLoaderManager mImageLoader;

    public ChuanTuAdapter(Activity mActivity, List<Map<String, Object>> dataList) {
        this.mActivity = mActivity;
        this.mDataList = dataList;
        this.inflater = LayoutInflater.from(mActivity);
        this.mImageLoader = ImageLoaderManager.getInstance(mActivity);
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_list_chuantu, parent, false);
            holder.mImage = convertView.findViewById(R.id.iv_chuantu_img);
            holder.mTitle = convertView.findViewById(R.id.et_chuantu_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Map<String, Object> data = mDataList.get(position);
        String TYPE = StringUtil.convertStr(data.get("TYPE"));
        if ("PATH".equals(TYPE)) { // 用户选择的图片
            byte[] bts = (byte[]) data.get("IMAGE_BYTE");
            Bitmap bitmap = ImageUtil.getBitmapFromByte(bts);
            holder.mImage.setImageBitmap(bitmap);
        } else { // 已经保存的图片
            // 动态加载图片
            HashMap<String, String> properties = new HashMap<>();
            properties.put("ID", StringUtil.convertStr(data.get("ID")));
            RequestCenter.GetImg(properties, new WebServiceUtils.WebServiceCallBack() {
                @Override
                public void callBack(String resultStr) {
                    Log.e("img", resultStr);
                    mDataList.get(position).put("TYPE", "REMOTE");
                    //mDataList.get(position).put("IMAGE_BYTE", null);
                }
            });
        }

        holder.mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 预览图片
                //PictureSelector.create(mActivity).externalPicturePreview();
                Log.e("D", "出发了点击事件");
                Intent intent = new Intent(mActivity, PlusImageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.PLUS_IMG_LIST, (Serializable) mDataList);
                bundle.putInt(Constant.PLUS_IMG_POSITION, position);
                intent.putExtras(bundle);
                mActivity.startActivityForResult(intent, Constant.ACTIVITI_FOR_RESULT_PLUG_IMG);
            }
        });
        holder.mTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mDataList.get(position).put("TITLE", s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return convertView;
    }

    private static class ViewHolder {
        private ImageView mImage;
        private EditText mTitle;
    }
}
