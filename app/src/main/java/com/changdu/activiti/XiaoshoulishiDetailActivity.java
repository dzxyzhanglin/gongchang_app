package com.changdu.activiti;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.changdu.R;
import com.changdu.activiti.base.BaseActivity;

/**
 * 销售历史详情
 */
public class XiaoshoulishiDetailActivity extends BaseActivity {

    private String BillID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiaoshoulishi_detail_layout);
        mContext = this;
        // 设置标题
        setTitle(getString(R.string.title_xiaoshoulishi_detail), true);

        // 获取前一个页面传递过来的数据
        Intent intent = getIntent();
        BillID = intent.getStringExtra("BillID");
        Log.e("BillID", BillID);
    }
}
