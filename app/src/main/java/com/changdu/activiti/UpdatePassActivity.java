package com.changdu.activiti;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.changdu.R;
import com.changdu.activiti.base.BaseActivity;
import com.changdu.manager.UserManager;
import com.changdu.network.RequestCenter;
import com.changdu.util.JsonToMap;
import com.changdu.util.StringUtil;
import com.changdu.util.WebServiceUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 修改密码
 */
public class UpdatePassActivity extends BaseActivity implements View.OnClickListener {

    private EditText mOLDPWD;
    private EditText mNEWPWD;
    private EditText mCONFIRM_NEWPWD;
    private TextView mUSERNAME;
    private Button mSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatepass_layout);
        mContext = this;
        // 设置标题
        setTitle(getString(R.string.title_updatepass), true);

        initView();
    }

    private void initView() {
        mOLDPWD = findViewById(R.id.et_updatepass_OLDPWD);
        mNEWPWD = findViewById(R.id.et_updatepass_NEWPWD);
        mCONFIRM_NEWPWD = findViewById(R.id.et_updatepass_CONFIRM_NEWPWD);
        mUSERNAME = findViewById(R.id.et_updatepass_USERNAME);
        mUSERNAME.setText(UserManager.getInstance().getUName());
        mSave = findViewById(R.id.btn_updatepass);
        mSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_updatepass:
                String OLDPWD = mOLDPWD.getText().toString();
                String NEWPWD = mNEWPWD.getText().toString();
                String CONFIRM_NEWPWD = mCONFIRM_NEWPWD.getText().toString();
                if (!Objects.equals(NEWPWD, CONFIRM_NEWPWD)) {
                    showToast("两次密码输入不一致");
                    return;
                }

                HashMap<String, String> properties = new HashMap<>();
                properties.put("UID", UserManager.getInstance().getUID());
                properties.put("OLDPWD", OLDPWD);
                properties.put("NEWPWD", NEWPWD);
                properties.put("CONFIRM_NEWPWD", CONFIRM_NEWPWD);
                RequestCenter.MODIFYPWD(properties, new WebServiceUtils.WebServiceCallBack() {

                    @Override
                    public void callBack(String resultStr) {
                        Map<String, Object> map = toMap(resultStr);
                        if (map == null) {
                            return;
                        }
                        showToast(StringUtil.convertStr(map.get("Mesg")));
                    }
                });
                break;
        }
    }
}
