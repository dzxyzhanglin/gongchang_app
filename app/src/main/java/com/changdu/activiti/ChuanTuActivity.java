package com.changdu.activiti;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.changdu.R;
import com.changdu.activiti.base.BaseActivity;
import com.changdu.adapter.ChuanTuAdapter;
import com.changdu.constant.Constant;
import com.changdu.network.RequestCenter;
import com.changdu.util.ImageUtil;
import com.changdu.util.StringUtil;
import com.changdu.util.WebServiceUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 手机传图
 */
public class ChuanTuActivity extends BaseActivity implements View.OnClickListener {

    private GridView mGridView;
    private ChuanTuAdapter adapter;
    private List<Map<String, Object>> dataList;
    private Button mSave;

    private String SPBH;
    private String SPID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chuantu_layout);
        mContext = this;
        setTitle(getString(R.string.title_kucun_shoujichuantu), true);

        Intent intent = getIntent();
        SPBH = intent.getStringExtra(Constant.CAPTURE_RESULT_CODE);

        initView();
        getWpDetail();
    }

    private void initView() {
        mGridView = findViewById(R.id.gd_chuantu);
        mSave = findViewById(R.id.btn_chuantu_save);
        mSave.setOnClickListener(this);
    }

    private void getWpDetail() {
        // 先查询物品信息，
        // 再通过物品信息ID查询图片
        HashMap<String, String> properties = new HashMap<>();
        properties.put("SPBH", SPBH);
        showLoading();
        RequestCenter.GETSpzlDetail(properties, new WebServiceUtils.WebServiceCallBack() {

            @Override
            public void callBack(String resultStr) {
                Map<String, Object> map = toMap(resultStr);
                if (map == null) {
                    cancleLoading();
                    return;
                }

                // 修改标题
                String SPK_SPMC = StringUtil.convertStr(map.get("SPK_SPMC"));
                setTitle(SPK_SPMC + " - 上传图片", true);

                SPID = StringUtil.convertStr(map.get("ID"));
                getImgList();
            }
        });
    }

    private void getImgList() {
        HashMap<String, String> properties = new HashMap<>();
        properties.put("SPID", SPID);
        RequestCenter.GETImageList(properties, new WebServiceUtils.WebServiceCallBack() {

            @Override
            public void callBack(String resultStr) {
                cancleLoading();
                Map<String, Object> map = toMap(resultStr);
                if (map == null) {
                    return;
                }

                if ("True".equals(StringUtil.convertStr(map.get("Sucecss")))) {
                    dataList = (List<Map<String, Object>>) map.get("DATA");
                    adapter = new ChuanTuAdapter(mContext, dataList);
                    mGridView.setAdapter(adapter);

                } else {
                    showToast("物品图片加载失败");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_image_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_add_image) {
            PictureSelector.create(mContext)
                .openGallery(PictureMimeType.ofImage())
                .imageSpanCount(4)// 每行显示个数 int
                .selectionMode(PictureConfig.MULTIPLE)
                .forResult(PictureConfig.CHOOSE_REQUEST)
            ;
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片、视频、音频选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    if (selectList.size() > 0) {
                        List<Map<String, Object>> lists = new ArrayList<>(selectList.size());
                        for (LocalMedia localMedia : selectList) {
                            Map<String, Object> d = new HashMap<>();
                            String path = localMedia.getPath();
                            d.put("ID", "");
                            d.put("IMAGE_BYTE", ImageUtil.getByteFromLocalImagePath(path));
                            d.put("TYPE", "PATH");
                            lists.add(d);
                        }
                        adapter.loadMore(lists);
                    }
                    break;
                case Constant.ACTIVITI_FOR_RESULT_PLUG_IMG:
                    Log.e("img", "Constant.ACTIVITI_FOR_RESULT_PLUG_IMG");
                    break;
            }
        }
    }

    /**
     * 保存图片
     */
    private void saveImgs() {
        List<Map<String, Object>> lists = adapter.getmDataList();
        for (Map<String, Object> data : lists) {
            HashMap<String, Object> properties = new HashMap<>();
            properties.put("SPID", SPID);
            properties.put("fid", StringUtil.convertStr(data.get("ID")));
            properties.put("TPMC", StringUtil.convertStr(data.get("TITLE")));
            byte[] bt = (byte[]) data.get("IMAGE_BYTE");
            properties.put("imageBytes", bt);

            showLoading();
            RequestCenter.SaveImg(properties, new WebServiceUtils.WebServiceCallBack() {
                @Override
                public void callBack(String resultStr) {
                    cancleLoading();
                    Map<String, Object> map = toMap(resultStr);
                    if (map == null) {
                        return;
                    }
                    if ("True".equals(StringUtil.convertStr(map.get("Sucecss")))) {
                        showToast("保存成功");
                    } else {
                        showToast(StringUtil.convertStr(map.get("Mesg")));
                    }
                }
            });
        }

        // TODO 判断是否有图片删除
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_chuantu_save:
                saveImgs();
                break;
        }
    }
}
