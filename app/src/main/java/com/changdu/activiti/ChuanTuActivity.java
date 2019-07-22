package com.changdu.activiti;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    private static int saveNum = 0;

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
                    if (dataList != null && dataList.size() > 0) {
                        for (Map<String, Object> d : dataList) {
                            d.put("TYPE", "REMOTE");
                        }
                    }
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
                            d.put("IMAGE_BYTE", path);
                            d.put("TPMC", ImageUtil.getImageNameFromLocalPath(path));
                            // 选择的时候不能转为二进制，太耗费资源，容易NR
                            //d.put("IMAGE_BYTE", ImageUtil.getByteFromLocalImagePath(path));
                            d.put("TYPE", "LOCAL");
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
        final int saveSize = lists.size();
        if (saveSize > 0) {
            showLoading();
            saveNum = 0;
            for (Map<String, Object> data : lists) {
                String type = StringUtil.convertStr(data.get("TYPE"));
                if (Objects.equals("LOCAL", type)) {
                    HashMap<String, String> properties = new LinkedHashMap<>();
                    properties.put("SPID", SPID);
                    properties.put("FID", "新增");
                    properties.put("TPMC", StringUtil.convertStr(data.get("TPMC")));

                    String path = StringUtil.convertStr(data.get("IMAGE_BYTE"));
                    properties.put("TPType", ImageUtil.getImageSuffixFromLocalPath(path));
                    String imageBytes = ImageUtil.getByteFromLocalImagePath(path);
                    properties.put("imageBytes", imageBytes);

                    RequestCenter.SaveImg(properties, new WebServiceUtils.WebServiceCallBack() {
                        @Override
                        public void callBack(String resultStr) {
                            saveNum++;
                            if (saveSize == saveNum) {
                                cancleLoading();
                            }
                            if (Objects.equals(resultStr, "保存成功")) {
                                showToast("保存成功");
                            } else {
                                showToast("保存失败");
                            }
                        }
                    });
                } else if (Objects.equals("REMOTE", type)) {
                    HashMap<String, String> properties = new HashMap<>();
                    properties.put("SPID", SPID);
                    properties.put("FID", StringUtil.convertStr(data.get("ID")));
                    properties.put("TPMC", StringUtil.convertStr(data.get("TPMC")));

                    String path = StringUtil.convertStr(data.get("IMAGE_BYTE"));
                    properties.put("imageBytes", path);
                    properties.put("TPType", StringUtil.convertStr(data.get("KIND")));

                    showLoading();
                    RequestCenter.SaveImg(properties, new WebServiceUtils.WebServiceCallBack() {
                        @Override
                        public void callBack(String resultStr) {
                            saveNum++;
                            if (saveSize == saveNum) {
                                cancleLoading();
                            }
                            if (Objects.equals(resultStr, "保存成功")) {
                                showToast("保存成功");
                            } else {
                                showToast("保存失败");
                            }
                        }
                    });
                }
            }
        }

        // 判断是否有图片删除
        List<Map<String, Object>> deletedList = adapter.getDeletedList();
        if (deletedList!= null && deletedList.size() > 0) {
            for (Map<String, Object> map : deletedList) {
                String ImageID = StringUtil.convertStr(map.get("ID"));
                if (!StringUtil.isBlank(ImageID)) {
                    HashMap<String, String> properties = new HashMap<>();
                    properties.put("ImageID", ImageID);
                    showLoading();
                    RequestCenter.DeleteImg(properties, new WebServiceUtils.WebServiceCallBack() {
                        @Override
                        public void callBack(String resultStr) {
                            cancleLoading();
                            // TODO判断返回值
                            showToast("删除成功");
                        }
                    });
                }
            }
        }
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
