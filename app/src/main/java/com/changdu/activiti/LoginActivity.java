package com.changdu.activiti;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.changdu.R;
import com.changdu.activiti.base.BaseActivity;
import com.changdu.manager.UserManager;
import com.changdu.model.UserModel;
import com.changdu.network.RequestCenter;
import com.changdu.util.JsonHandler;
import com.changdu.util.StatusBarUtil;
import com.changdu.util.StringUtil;
import com.changdu.util.WebServiceUtils;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText mUserName;
    private EditText mPassword;
    private Button mBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_layout);
        mContext = this;

        initView();

        StatusBarUtil.immersive(mContext, R.color.color_transparent);
    }

    private void initView() {
        mUserName = findViewById(R.id.login_username);
        mPassword = findViewById(R.id.login_password);
        mBtn = findViewById(R.id.login_btn);
        mBtn.setOnClickListener(this);

        // 默认显示上次登录的用户名
        String uCode = UserManager.getInstance().getCode();
        if (uCode != null) {
            mUserName.setText(uCode);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                final String Code = mUserName.getText().toString();
                String PWD = mPassword.getText().toString();
                if (Code == null || "".equals(Code)) {
                    showToast("请输入用户名");
                    return;
                }

                HashMap<String, String> properties = new HashMap<String, String>();
                properties.put("Code", Code);
                properties.put("PWD", PWD);
                RequestCenter.CheckUserByCode(properties, new WebServiceUtils.WebServiceCallBack() {

                    @Override
                    public void callBack(String resultStr) {
                        if (resultStr != null) {
                            // 去掉首尾的 [  ];
                            resultStr = StringUtil.formart(resultStr);
                            UserModel userModel = (UserModel) JsonHandler.jsonToObject(resultStr, UserModel.class);
                            if ("True".equals(userModel.getSucecss())) {
                                userModel.setCode(Code);
                                UserManager.getInstance().setUser(userModel);
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            } else {
                                showToast(userModel.getMsg());
                            }
                        } else {
                            showToast("请求数据失败");
                        }
                    }
                });
                break;
        }
    }
}
