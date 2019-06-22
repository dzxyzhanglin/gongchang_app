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
     * 获取单个物品库存详情
     * @param properties
     * @param webServiceCallBack
     */
    public static void GETKCDetail(HashMap<String, String> properties, final WebServiceUtils.WebServiceCallBack webServiceCallBack) {
        WebServiceUtils.callWebService(HttpConstants.ROOT_URL, HttpConstants.GET_KC_DETAIL, properties, webServiceCallBack);
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

    /**
     * 获取生产统计记录数
     * @param properties
     * @param webServiceCallBack
     */
    public static void GETMadeCount(HashMap<String, String> properties, final WebServiceUtils.WebServiceCallBack webServiceCallBack) {
        WebServiceUtils.callWebService(HttpConstants.ROOT_URL, HttpConstants.GET_MADE_COUNT, properties, webServiceCallBack);
    }

    /**
     * 获取生产统计明细
     * @param properties
     * @param webServiceCallBack
     */
    public static void GETMadeDetail(HashMap<String, String> properties, final WebServiceUtils.WebServiceCallBack webServiceCallBack) {
        WebServiceUtils.callWebService(HttpConstants.ROOT_URL, HttpConstants.GET_MADE_DETAIL, properties, webServiceCallBack);
    }

    /**
     * 获取企业详情
     * @param properties
     * @param webServiceCallBack
     */
    public static void GETQYInfo(HashMap<String, String> properties, final WebServiceUtils.WebServiceCallBack webServiceCallBack) {
        WebServiceUtils.callWebService(HttpConstants.ROOT_URL, HttpConstants.GET_QY_INFO, properties, webServiceCallBack);
    }

    /**
     * 修改密码
     * @param properties
     * @param webServiceCallBack
     */
    public static void MODIFYPWD(HashMap<String, String> properties, final WebServiceUtils.WebServiceCallBack webServiceCallBack) {
        WebServiceUtils.callWebService(HttpConstants.ROOT_URL, HttpConstants.MODIFY_PWD, properties, webServiceCallBack);
    }


}
