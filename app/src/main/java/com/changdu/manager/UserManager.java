package com.changdu.manager;

import com.changdu.db.SPManager;
import com.changdu.model.UserModel;
import com.changdu.model.UserPermissionModel;

import java.util.ArrayList;
import java.util.List;

public class UserManager {

    private static UserManager userManager = null;

    private static final String APP_PREFERENCES_UID_KEY = "gongchang_profile_uid";
    private static final String APP_PREFERENCES_UNAME_KEY = "gongchang_profile_uname";
    private static final String APP_PREFERENCES_CODE_KEY = "gongchang_profile_code";
    private static final String APP_PREFERENCES_UTYPE_KEY = "gongchang_profile_utype";
    private static final String APP_PREFERENCES_PERMISSION_KEY = "gongchang_profile_permission";

    public static UserManager getInstance() {
        if (userManager == null) {
            synchronized (UserManager.class) {
                if (userManager == null) {
                    userManager = new UserManager();
                }
                return userManager;
            }
        } else {
            return userManager;
        }
    }

    public void setUser(UserModel user) {
        if (user != null) {
            SPManager spManager = SPManager.getInstance();
            spManager.putString(APP_PREFERENCES_UID_KEY, user.getUID());
            spManager.putString(APP_PREFERENCES_UNAME_KEY, user.getUName());
            spManager.putString(APP_PREFERENCES_CODE_KEY, user.getCode());
            spManager.putString(APP_PREFERENCES_UTYPE_KEY, user.getUTYPE());

            // 权限
            List<UserPermissionModel> data = user.getDATA();
            if (data != null && data.size() > 0) {
                StringBuilder sb = new StringBuilder();
                for (UserPermissionModel p : data) {
                    sb.append(p.getRGH_NAME()).append(",");
                }
                spManager.putString(APP_PREFERENCES_PERMISSION_KEY, sb.toString().substring(0, sb.toString().length() - 1));
            } else {
                spManager.putString(APP_PREFERENCES_PERMISSION_KEY, "");
            }
        }
    }

    public void removeUser() {
        SPManager spManager = SPManager.getInstance();
        spManager.remove(APP_PREFERENCES_UID_KEY);
        spManager.remove(APP_PREFERENCES_UNAME_KEY);
        spManager.remove(APP_PREFERENCES_CODE_KEY);
        spManager.remove(APP_PREFERENCES_UTYPE_KEY);
        spManager.remove(APP_PREFERENCES_PERMISSION_KEY);
    }

    public String getUID() {
        return SPManager.getInstance().getString(APP_PREFERENCES_UID_KEY, "");
    }

    public String getUName() {
        return SPManager.getInstance().getString(APP_PREFERENCES_UNAME_KEY, "");
    }

    public String getCode() {
        return SPManager.getInstance().getString(APP_PREFERENCES_CODE_KEY, "");
    }

    public String getUType() {
        return SPManager.getInstance().getString(APP_PREFERENCES_UTYPE_KEY, "");
    }

    public String getPermissionData() {
        return SPManager.getInstance().getString(APP_PREFERENCES_PERMISSION_KEY, "");
    }

    /**
     * 判断用户是否登录
     * @return
     */
    public boolean isLogin() {
        String UID = getUID();
        return !(UID == null || "".equals(UID)) ;
    }
}
