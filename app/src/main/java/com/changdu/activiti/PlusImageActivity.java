package com.changdu.activiti;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.changdu.R;
import com.changdu.activiti.base.BaseActivity;
import com.changdu.adapter.ViewImagePagerAdapter;
import com.changdu.constant.Constant;

import java.util.List;
import java.util.Map;

/**
 * 查看图片大图
 */
public class PlusImageActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private ViewPager viewPager; //展示图片的ViewPager
    private List<Map<String, Object>> imgList; //图片的数据源
    private int mPosition; //第几张图片
    private ViewImagePagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plus_image_layout);

        Bundle extras = getIntent().getExtras();
        imgList = (List<Map<String, Object>>) extras.getSerializable(Constant.PLUS_IMG_LIST);
        mPosition = extras.getInt(Constant.PLUS_IMG_POSITION);

        initView();
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.addOnPageChangeListener(this);

        setTitle(mPosition + 1 + "/" + imgList.size(), true);

        adapter = new ViewImagePagerAdapter(this, imgList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(mPosition);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*case R.id.delete_iv:
                //删除图片
                deletePic();
                break;*/
        }
    }

    private void deletePic() {
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int position) {
        mPosition = position;
        setTitle(position + 1 + "/" + imgList.size(), true);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
