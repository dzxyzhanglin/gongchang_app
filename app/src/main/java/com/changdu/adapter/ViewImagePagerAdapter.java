package com.changdu.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.changdu.R;
import com.changdu.util.ImageUtil;
import com.luck.picture.lib.photoview.PhotoView;

import java.util.List;
import java.util.Map;

public class ViewImagePagerAdapter extends PagerAdapter {
    protected Context mContext;

    protected List<Map<String, Object>> mDataList; // 图片的数据来源

    public ViewImagePagerAdapter(Context mContext, List<Map<String, Object>> mDataList) {
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
        byte[] bt = (byte[]) data.get("IMAGE_BYTE");
        Bitmap bitmap = ImageUtil.getBitmapFromByte(bt);
        imageView.setImageBitmap(bitmap);

        container.addView(view);
        return view;
    }
}
