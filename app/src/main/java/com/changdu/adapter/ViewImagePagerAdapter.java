package com.changdu.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.changdu.R;
import com.changdu.util.ImageUtil;
import com.changdu.util.StringUtil;
import com.luck.picture.lib.photoview.PhotoView;

import java.util.List;
import java.util.Map;

public class ViewImagePagerAdapter extends PagerAdapter {
    private Activity mActivity;
    protected Context mContext;

    protected List<Map<String, Object>> mDataList; // 图片的数据来源

    public ViewImagePagerAdapter(Activity mContext, List<Map<String, Object>> mDataList) {
        this.mActivity = mContext;
        this.mContext = mContext;
        this.mDataList = mDataList;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.viewimage_pager, null);
        PhotoView imageView = view.findViewById(R.id.img_iv);

        // 显示图片
        Map<String, Object> data = mDataList.get(position);
        String TYPE = StringUtil.convertStr(data.get("TYPE"));
        String path = StringUtil.convertStr(data.get("IMAGE_BYTE"));
        if ("LOCAL".equals(TYPE)) { // 用户选择的图片
            Glide.with(mActivity).load(path).into(imageView);
        } else {
            Bitmap bitmap = ImageUtil.getBitmapFromRemoteImagePath(path);
            Glide.with(mActivity).load(bitmap).into(imageView);
        }

        container.addView(view);
        return view;
    }
}
