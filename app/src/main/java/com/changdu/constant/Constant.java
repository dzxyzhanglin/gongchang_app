package com.changdu.constant;

import android.Manifest;
import android.os.Environment;

/**
 * @author: vision
 * @function:
 * @date: 16/6/16
 */
public class Constant {

    /**
     * 权限常量相关
     */
    public static final int WRITE_READ_EXTERNAL_CODE = 0x01;
    public static final String[] WRITE_READ_EXTERNAL_PERMISSION = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    public static final int HARDWEAR_CAMERA_CODE = 0x02;
    public static final String[] HARDWEAR_CAMERA_PERMISSION = new String[]{Manifest.permission.CAMERA};

    //整个应用文件下载保存路径
    public static String APP_PHOTO_DIR = Environment.
            getExternalStorageDirectory().getAbsolutePath().
            concat("/gongchang_business/photo/");

    /**
     * ACTIVITI_FOR_RESULT CODE
     */
    public static final int ACTIVITI_FOR_RESULT_CK = 1000;

    public static final int ACTIVITI_FOR_RESULT_HW = 1001;

    public static final int ACTIVITI_FOR_RESULT_JLDW = 1002;

    public static final int ACTIVITI_FOR_RESULT_FPLX = 1002;

    public static final int ACTIVITI_FOR_RESULT_JSFS = 1003;

    public static final int ACTIVITI_FOR_RESULT_KH = 1004;

    public static final int ACTIVITI_FOR_RESULT_DEVICE = 1005;

    public static final int ACTIVITI_FOR_RESULT_ADD_WP = 1100;

    public static final int ACTIVITI_FOR_RESULT_ADD_QTBF = 1101;

    public static final int ACTIVITI_FOR_RESULT_PLUG_IMG = 1150;

    /**
     * 扫码类型
     */
    public static final String CAPTURE_CHUANTU = "CHUANTU"; // 手机传图

    public static final String CAPTURE_PANDIAN = "PANDIAN"; // 手机盘点

    public static final String CAPTURE_CHEJIAN_SAOMIAO = "CHEJIAN_SAOMIAO"; // 车间扫码

    public static final String CAPTURE_RESULT_CODE = "CAPTURE_RESULT_CODE"; // 车间扫码

    /**
     * 操作类型
     */
    public static final String OPERATE_UPDATE = "UPDATE";

    public static final String OPERATE_ADD = "ADD";

    /**
     * 查看图片
     */
    public static final String PLUS_IMG_LIST = "PLUS_IMG_LIST";
    public static final String PLUS_IMG_POSITION = "PLUS_IMG_POSITION";

}
