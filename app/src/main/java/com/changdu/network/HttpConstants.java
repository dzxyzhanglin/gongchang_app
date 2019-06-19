package com.changdu.network;

/**
 * 所有请求相关地址
 */
public class HttpConstants {
 
    /**
     * 登录
     */
    public static String CHECK_USER_BU_CODE = "CheckUserByCode";

    /**
     * 仓库列表
     */
    public static String GET_CK_ITEMS = "GETCKItems";

    /**
     * 物品库存总记录数
     */
    public static String GET_SPZL_COUNT = "GETSpzlCount";

    /**
     * 物品库存列表
     */
    public static String GET_SPZL_INTO = "GETSpzlInfo";

    /**
     * 销售历史总数（二十一、获取销售发货单记录数）
     */
    public static String GET_BILL_COUNT = "GETBillCount";

    /**
     * 销售历史列表（二十二、获取销售发货单明细）
     */
    public static String GET_BILL_INFO = "GETBillInfo";
}
