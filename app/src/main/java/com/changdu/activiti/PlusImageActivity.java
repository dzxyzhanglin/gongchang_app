package com.changdu.activiti;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.changdu.R;
import com.changdu.activiti.base.BaseActivity;
import com.changdu.adapter.ViewImagePagerAdapter;
import com.changdu.constant.Constant;
import com.changdu.util.ImageUtil;

import java.util.List;
import java.util.Map;

/**
 * 查看图片大图
 */
public class PlusImageActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    private ViewPager viewPager; //展示图片的ViewPager
    private List<Map<String, Object>> imgList; //图片的数据源
    private int mPosition; //第几张图片
    private ViewImagePagerAdapter adapter;
    private static String TITLE = "查看大图";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plus_image_layout);
        mContext = this;

        Bundle extras = getIntent().getExtras();
        imgList = ImageUtil.getmImageList();
        mPosition = extras.getInt(Constant.PLUS_IMG_POSITION);

        initView();

        // 沉浸式状态栏
        //StatusBarUtil.immersive(mContext, R.color.color_transparent, 0);
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.addOnPageChangeListener(this);

        setTitle(TITLE + "(" + (mPosition + 1 + "/" + imgList.size()) + ")", true);

        adapter = new ViewImagePagerAdapter(this, imgList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(mPosition);
    }


    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int position) {
        mPosition = position;
        setTitle(TITLE + "(" + (position + 1 + "/" + imgList.size()) + ")", true);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
