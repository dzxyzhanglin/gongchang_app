package com.changdu.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ImageUtil {

    private static List<Map<String, Object>> mImageList;

    public static List<Map<String, Object>> getmImageList() {
        return mImageList;
    }

    public static void setmImageList(List<Map<String, Object>> mImageList) {
        ImageUtil.mImageList = mImageList;
    }

    /**
     * 从byte获取Bitmap
     * @param data
     * @return
     */
    public static Bitmap getBitmapFromByte(byte[] data) {
        int reqWidth = 100;
        int reqHeight = 100;
        return getBitmapFromByte(data, reqWidth, reqHeight);
    }

    public static Bitmap getBitmapFromByte(byte[] data, int reqWidth, int reqHeight) {

        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);

        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data, 0, data.length, options);
    }


    /**
     * 从远程获取Bitmap
     * @param path
     * @return
     */
    public static Bitmap getBitmapFromRemoteImagePath(String path) {
        byte[] bts = Base64.decode(path, Base64.DEFAULT);
        return getBitmapFromByte(bts);
    }

    /**
     * 从手机本地图片地址获取Bitmap
     * @param path
     * @return
     */
    public static Bitmap getBitmapFromLocalImagePath(String path) {
        int reqWidth = 100;
        int reqHeight = 100;
        return getBitmapFromLocalImagePath(path, reqWidth, reqHeight);
    }
    /**
     * 从手机本地图片地址获取Bitmap
     * @param path
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap getBitmapFromLocalImagePath(String path, int reqWidth, int reqHeight) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(path);
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(fis, null, options);

            // 调用上面定义的方法计算inSampleSize值
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            // 使用获取到的inSampleSize值再次解析图片
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(fis, null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static String getByteFromLocalImagePath(String path) {
        FileInputStream fis = null;
        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
        try {
            fis = new FileInputStream(path);
            int line;
            while ((line = fis.read()) != -1) {
                bytestream.write(line);
            }
            byte imgdata[] = bytestream.toByteArray();
            return Base64.encodeToString(imgdata, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bytestream != null) {
                try {
                    bytestream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static String getImageSuffixFromLocalPath(String path) {
        if (StringUtil.isBlank(path)) {
            return "";
        }
        path = path.toUpperCase();
        return "." + path.substring(path.indexOf(".") + 1);
    }

    // /storage/emulated/0/DCIM/Camera/IMG_20190628_233959.jpg
    public static String getImageNameFromLocalPath(String path) {
        if (StringUtil.isBlank(path)) {
            return "";
        }
        String[] strings = path.split("/");
        String s = strings[strings.length - 1];
        return s.substring(0, s.indexOf("."));
    }

    public static String getImageSuffixFromRemotePath(byte[] photoByte) {
        String strFileExtendName = ".jpg";
        if ((photoByte[0] == 71) && (photoByte[1] == 73) && (photoByte[2] == 70)
                && (photoByte[3] == 56) && ((photoByte[4] == 55) || (photoByte[4] == 57))
                && (photoByte[5] == 97)) {
            strFileExtendName = ".gif";
        } else if ((photoByte[6] == 74) && (photoByte[7] == 70) && (photoByte[8] == 73)
                && (photoByte[9] == 70)) {
            strFileExtendName = ".jpg";
        } else if ((photoByte[0] == 66) && (photoByte[1] == 77)) {
            strFileExtendName = ".bmp";
        } else if ((photoByte[1] == 80) && (photoByte[2] == 78) && (photoByte[3] == 71)) {
            strFileExtendName = ".png";
        }
        return strFileExtendName;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }
}
