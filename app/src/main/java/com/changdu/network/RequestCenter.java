package com.changdu.network;

import com.changdu.util.WebServiceUtils;

import java.util.HashMap;

/**
 * 请求
 */
public class RequestCenter {

    /**
     * 登录
     * @param properties
     * @param webServiceCallBack
     */
    public static void CheckUserByCode(HashMap<String, String> properties, final WebServiceUtils.WebServiceCallBack webServiceCallBack) {
        WebServiceUtils.callWebService(HttpConstants.ROOT_URL, HttpConstants.CHECK_USER_BU_CODE, properties, webServiceCallBack);
    }

    /**
     * 仓库列表
     * @param properties
     * @param webServiceCallBack
     */
    public static void GETCKItems(HashMap<String, String> properties, final WebServiceUtils.WebServiceCallBack webServiceCallBack) {
        WebServiceUtils.callWebService(HttpConstants.ROOT_URL, HttpConstants.GET_CK_ITEMS, properties, webServiceCallBack);
    }

    /**
     * 物品库存总记录数
     * @param properties
     * @param webServiceCallBack
     */
    public static void GETSpzlCount(HashMap<String, String> properties, final WebServiceUtils.WebServiceCallBack webServiceCallBack) {
        WebServiceUtils.callWebService(HttpConstants.ROOT_URL, HttpConstants.GET_SPZL_COUNT, properties, webServiceCallBack);
    }

    /**
     * 物品库存列表
     * @param properties
     * @param webServiceCallBack
     */
    public static void GETSpzlInfo(HashMap<String, String> properties, final WebServiceUtils.WebServiceCallBack webServiceCallBack) {
        WebServiceUtils.callWebService(HttpConstants.ROOT_URL, HttpConstants.GET_SPZL_INTO, properties, webServiceCallBack);
    }

    /**
     * 销售历史总数
     * @param properties
     * @param webServiceCallBack
     */
    public static void GETBillCount(HashMap<String, String> properties, final WebServiceUtils.WebServiceCallBack webServiceCallBack) {
        WebServiceUtils.callWebService(HttpConstants.ROOT_URL, HttpConstants.GET_BILL_COUNT, properties, webServiceCallBack);
    }

    /**
     * 销售历史列表
     * @param properties
     * @param webServiceCallBack
     */
    public static void GETBillInfo(HashMap<String, String> properties, final WebServiceUtils.WebServiceCallBack webServiceCallBack) {
        WebServiceUtils.callWebService(HttpConstants.ROOT_URL, HttpConstants.GET_BILL_INFO, properties, webServiceCallBack);
    }


}
