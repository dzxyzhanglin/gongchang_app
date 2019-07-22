package com.changdu.util;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    /**
     * 字符串转日期
     * @param str
     * @return
     */
    public static Date parseDate(String str, String pattern) {
        if (StringUtil.isBlank(str)) {
            return null;
        }
        try {
            if (StringUtil.isBlank(pattern)) {
                pattern = "yyyy-MM-dd";
            }
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.parse(str);
        } catch (Exception ex) {
            Log.e("D", ex.getMessage());
            return null;
        }
    }

    public static String formatDate(Date date, String pattern) {
        if (date == null) {
            return "";
        }
        if (StringUtil.isBlank(pattern)) {
            pattern = "yyyy-MM-dd";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * 获取一个月前的日期
     * @return
     */
    public static String getPreMonthDate() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, -1);
        Date m = c.getTime();
        return formatDate(m, null);
    }
}
