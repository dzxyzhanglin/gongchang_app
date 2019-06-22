package com.changdu.model;

import java.util.List;

/**
 * 用户登录信息
 */
public class UserModel extends BaseModel {

    private String Sucecss;

    private String Msg;

    private String UTYPE; // QC-巡检

    private String UID;

    private String Code;

    private String UName;

    private List<UserPermissionModel> DATA;

    public String getSucecss() {
        return Sucecss;
    }

    public void setSucecss(String sucecss) {
        Sucecss = sucecss;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    public String getUTYPE() {
        return UTYPE;
    }

    public void setUTYPE(String UTYPE) {
        this.UTYPE = UTYPE;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getUName() {
        return UName;
    }

    public void setUName(String UName) {
        this.UName = UName;
    }

    public List<UserPermissionModel> getDATA() {
        return DATA;
    }

    public void setDATA(List<UserPermissionModel> DATA) {
        this.DATA = DATA;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "Sucecss='" + Sucecss + '\'' +
                ", UTYPE='" + UTYPE + '\'' +
                ", UID='" + UID + '\'' +
                ", UName='" + UName + '\'' +
                ", DATA=" + DATA +
                '}';
    }
}
