package com.changdu.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.print.PrinterId;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.changdu.R;
import com.changdu.activiti.PlusImageActivity;
import com.changdu.constant.Constant;
import com.changdu.network.RequestCenter;
import com.changdu.util.CollUtil;
import com.changdu.util.ImageLoaderManager;
import com.changdu.util.ImageUtil;
import com.changdu.util.StringUtil;
import com.changdu.util.WebServiceUtils;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ChuanTuAdapter extends CommonAdapter {

    private Activity mActivity;
    private ViewHolder holder;
    private LayoutInflater inflater;
    private List<Map<String, Object>> deletedList = new ArrayList<>();

    public ChuanTuAdapter(Activity mActivity, List<Map<String, Object>> dataList) {
        this.mActivity = mActivity;
        this.mDataList = dataList;
        this.inflater = LayoutInflater.from(mActivity);
    }

    public List<Map<String, Object>> getDeletedList() {
        return deletedList;
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
            holder.mDelete = convertView.findViewById(R.id.iv_chuantu_img_delete);
            holder.mTitle = convertView.findViewById(R.id.et_chuantu_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Map<String, Object> data = mDataList.get(position);
        final String TPMC = StringUtil.convertStr(data.get("TPMC"));
        holder.mTitle.setText(TPMC);
        String TYPE = StringUtil.convertStr(data.get("TYPE"));
        if ("LOCAL".equals(TYPE)) { // 用户选择的图片
            String path = StringUtil.convertStr(data.get("IMAGE_BYTE"));
            Log.e("path", path);
            Glide.with(mActivity).load(path).into(holder.mImage);
        } else { // 已经保存的图片
            // 动态加载图片
            String IMAGE_BYTE = StringUtil.convertStr(data.get("IMAGE_BYTE"));
            if (StringUtil.isBlank(IMAGE_BYTE)) {
                HashMap<String, String> properties = new HashMap<>();
                properties.put("ID", StringUtil.convertStr(data.get("ID")));
                final Dialog dialog = DialogUIUtils.showLoading(mActivity, "图片加载中...", false, false, false, true).show();
                RequestCenter.GetImg(properties, new WebServiceUtils.WebServiceCallBack() {
                    @Override
                    public void callBack(String resultStr) {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        mDataList.get(position).put("IMAGE_BYTE", resultStr);

                        Bitmap bitmap = ImageUtil.getBitmapFromRemoteImagePath(resultStr);
                        Glide.with(mActivity).load(bitmap).into(holder.mImage);
                    }
                });
            } else {
                Bitmap bitmap = ImageUtil.getBitmapFromRemoteImagePath(IMAGE_BYTE);
                Glide.with(mActivity).load(bitmap).into(holder.mImage);
            }
        }

        holder.mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageUtil.setmImageList(mDataList);
                // 预览图片
                Intent intent = new Intent(mActivity, PlusImageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(Constant.PLUS_IMG_POSITION, position);
                intent.putExtras(bundle);
                mActivity.startActivity(intent);
            }
        });
        holder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUIUtils.showMdAlert(mActivity, "标题", "确认删除该图片", new DialogUIListener() {
                    @Override
                    public void onPositive() {
                        Map<String, Object> d = mDataList.get(position);
                        String TYPE = StringUtil.convertStr(d.get("TYPE"));
                        if (Objects.equals(TYPE, "REMOTE")) {
                            deletedList.add(d);
                        }

                        mDataList.remove(position);
                        ChuanTuAdapter.this.notifyDataSetChanged();
                    }

                    @Override
                    public void onNegative() {
                    }

                }).show();
            }
        });
        holder.mTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (position < mDataList.size()) {
                    mDataList.get(position).put("TPMC", s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return convertView;
    }

    private static class ViewHolder {
        private ImageView mImage;
        private ImageView mDelete;
        private EditText mTitle;
    }
}
