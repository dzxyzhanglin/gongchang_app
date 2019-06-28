package com.changdu.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    /**
     * 字符串转日期
     * @param str
     * @return
     */
    public static Date parseDate(String str) {
        if (StringUtil.isBlank(str)) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.parse(str);
        } catch (Exception ex) {
            return null;
        }
    }
}
