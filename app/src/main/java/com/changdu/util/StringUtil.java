package com.changdu.util;

public class StringUtil {

    /**
     * 判断字符串是否为空
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        return str == null || "".equals(str);
    }

    /**
     * 格式化json，去除首尾[ ]
     * @param json
     * @return
     */
    public static String formart(String json) {
        if (isBlank(json)) {
            return "";
        }
        if (json.startsWith("[")) {
            json = json.substring(1);
            json = json.substring(0, json.length() - 1);
            return json;
        }
        return json;
    }

    /**
     * 去除双引号
     * @param str
     * @return
     */
    public static String fomart2(String str) {
        if (isBlank(str)) {
            return "";
        }
        return str.replaceAll("\"", "");
    }

    public static String convertStr(Object str) {
        if (str == null) {
            return "";
        }
        return fomart2(str.toString());
    }
}
