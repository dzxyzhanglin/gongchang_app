package com.changdu.network;

/**
 * 所有请求相关地址
 */
public class HttpConstants {

    public static final String ROOT_URL = "http://183.230.28.135:8081/ERPWebService.asmx";

    /**
     * 登录
     */
    public static String CHECK_USER_BU_CODE = "CheckUserByCode";

    /**
     * 仓库列表
     */
    public static String GET_CK_ITEMS = "GETCKItems";

    /**
     * 货位列表
     */
    public static String GET_HW_ITEMS = "GETHWItems";

    /**
     * 计量单位列表
     */
    public static String GET_JLDW_ITEM = "GETJLDItem";

    /**
     * 发票类型
     */
    public static String GET_FPLX_ITEM = "GETFPLXItems";

    /**
     * 结算方式
     */
    public static String GET_JSFS_ITEM = "GETJSFSItems";

    /**
     * 客户列表
     */
    public static String GET_KH_LIST = "GETKHList";

    /**
     * 设备列表
     */
    public static String GET_DEVICE_MX = "GETDeviceMX";

    /**
     * 物品库存总记录数
     */
    public static String GET_SPZL_COUNT = "GETSpzlCount";

    /**
     * 物品库存列表
     */
    public static String GET_SPZL_INTO = "GETSpzlInfo";

    /**
     * 物品明细
     */
    public static String GET_SPZL_DETAIL = "GETSpzlDetail";

    /**
     * 十八、获取单个物品库存详情
     */
    public static String GET_KC_DETAIL = "GETKCDetail";

    /**
     * 销售历史总数（二十一、获取销售发货单记录数）
     */
    public static String GET_BILL_COUNT = "GETBillCount";

    /**
     * 销售历史列表（二十二、获取销售发货单明细）
     */
    public static String GET_BILL_INFO = "GETBillInfo";

    /**
     * 销售历史详情
     */
    public static String GET_BILL_DETAIL = "GETBillDetail";


    /**
     * 十四、获取生产统计记录数
     */
    public static String GET_MADE_COUNT = "GETMadeCount";

    /**
     * 十五、获取生产统计明细
     */
    public static String GET_MADE_DETAIL = "GETMadeDetail";

    /**
     * 获取企业详情
     */
    public static String GET_QY_INFO = "GETQYInfo";

    /**
     * 修改密码
     */
    public static String MODIFY_PWD = "MODIFYPWD";

    /**
     * 物品信息新增、修改
     */
    public static String INVENT_SAVE = "InventSave";

    /**
     * 生产进度总量
     */
    public static String GET_CPD_INFO_COUNT = "GetCPDInfoCount";

    /**
     * 生产进度列表
     */
    public static String GET_CPD_INFO = "GETCPInfo";

    /**
     * 销售开单保存
     */
    public static String SELL_SAVE = "SellSave";

    /**
     * 获取物品图片列表
     */
    public static String GET_IMAGE_LIST = "GETImageList";

    /**
     * 获取单个图片
     */
    public static String GET_IMG = "GetImg";

    /**
     * 保存图片
     */
    public static String SAVE_IMG = "SaveImg";

    /**
     * 删除图片
     */
    public static String DELETE_IMG = "DeleteImg";

    /**
     * 获取传票信息
     */
    public static String GET_CP_INFO = "GETCPInfo";

    /**
     * 加工保存
     */
    public static String MADE_OVER_PROC = "MadeOverProc";

    /**
     * 获取生产工序列表
     */
    public static String GET_GXMX = "GETGXMX";

    /**
     * 获取传票完工登记(检验接口)
     */
    public static String GET_CHECK_INFO = "GETCheckInfo";

    /**
     * 获取检验项目列表
     */
    public static String GET_CHECK_ITEMS = "GETCheckItems";

    /**
     * 获取问题原因列表
     */
    public static String GET_CHECK_YY_ITEMS = "GETCheckYYItems";

    /**
     * 检验登记
     */
    public static String MADE_CHECK_PROC = "MadeCheckProc";

}
