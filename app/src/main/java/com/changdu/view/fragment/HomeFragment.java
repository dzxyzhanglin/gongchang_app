package com.changdu.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.changdu.R;
import com.changdu.activiti.ChanliangtongjiActivity;
import com.changdu.activiti.JinduActivity;
import com.changdu.activiti.KucunActivity;
import com.changdu.activiti.XiaoshouKaidanActivity;
import com.changdu.activiti.XiaoshoulishiActivity;
import com.changdu.adapter.HomeAdapter;
import com.changdu.constant.Constant;
import com.changdu.manager.UserManager;
import com.changdu.model.HomeModel;
import com.changdu.util.StringUtil;
import com.changdu.zxing.app.CaptureActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 首页
 */
public class HomeFragment extends BaseFragment {

    private static final int REQUEST_QRCODE = 0x01;

    private View mContentView;
    private GridView mGridView;
    private List<HomeModel> dataList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mContentView = inflater.inflate(R.layout.fragment_home_layout, container, false);
        initView();
        initData();
        return mContentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void initView() {
        mGridView = mContentView.findViewById(R.id.home_gridlist);
    }

    private void initData() {

        String permissionData = UserManager.getInstance().getPermissionData();
        dataList = new ArrayList<>();
        if (!StringUtil.isBlank(permissionData)) {
            HomeModel model = null;
            String[] lists = permissionData.split(",");
            for (String title : lists) {
                if (Objects.equals(title, getString(R.string.title_kucun_chaxun))) {
                    model = new HomeModel(R.drawable.icon_kucun_chaxun, title);
                    dataList.add(model);
                } else if (Objects.equals(title, getString(R.string.title_kucun_shoujichuantu))) {
                    model = new HomeModel(R.drawable.icon_shoujichuantu, title);
                    dataList.add(model);
                } else if (Objects.equals(title, getString(R.string.title_kucun_shoujipandian))) {
                    model = new HomeModel(R.drawable.icon_shoujipandian, title);
                    dataList.add(model);
                } else if (Objects.equals(title, getString(R.string.title_xiaoshou_kaidan))) {
                    model = new HomeModel(R.drawable.icon_xiaoshoukaidan, title);
                    dataList.add(model);
                } else if (Objects.equals(title, getString(R.string.title_xiaoshoulishi_chaxun))) {
                    model = new HomeModel(R.drawable.icon_xisoshoulishi, title);
                    dataList.add(model);
                } else if (Objects.equals(title, getString(R.string.title_kucun_chejiansaoma))) {
                    model = new HomeModel(R.drawable.icon_chejiansaoma, title);
                    dataList.add(model);
                } else if (Objects.equals(title, getString(R.string.title_chanliangtongji))) {
                    model = new HomeModel(R.drawable.icon_gongrenchanliangtongji, title);
                    dataList.add(model);
                } else if (Objects.equals(title, getString(R.string.title_jindu_chaxun))) {
                    model = new HomeModel(R.drawable.icon_jinduchaxun, title);
                    dataList.add(model);
                }
            }

            /*String kucunTitle = getString(R.string.title_kucun_chaxun);
            if (permissionData.indexOf(kucunTitle) != -1) {
                model = new HomeModel(R.drawable.icon_kucun_chaxun, kucunTitle);
                dataList.add(model);
            }*/

            /*String shoujichuantuTitle = getString(R.string.title_kucun_shoujichuantu);
            if (permissionData.indexOf(shoujichuantuTitle) != -1) {
                model = new HomeModel(R.drawable.icon_shoujichuantu, shoujichuantuTitle);
                dataList.add(model);
            }*/

            /*String shoujipandianTitle = getString(R.string.title_kucun_shoujipandian);
            if (permissionData.indexOf(shoujipandianTitle) != -1) {
                model = new HomeModel(R.drawable.icon_shoujipandian, shoujipandianTitle);
                dataList.add(model);
            }*/

            /*String xiaoshoukaidanTitle = getString(R.string.title_xiaoshou_kaidan);
            if (permissionData.indexOf(xiaoshoukaidanTitle) != -1) {
                model = new HomeModel(R.drawable.icon_xiaoshoukaidan, xiaoshoukaidanTitle);
                dataList.add(model);
            }*/

           /* String xisoshoulishiTitle = getString(R.string.title_xiaoshoulishi_chaxun);
            if (permissionData.indexOf(xisoshoulishiTitle) != -1) {
                model = new HomeModel(R.drawable.icon_xisoshoulishi, xisoshoulishiTitle);
                dataList.add(model);
            }*/

            /*String chejiansaomaTitle = getString(R.string.title_kucun_chejiansaoma);
            if (permissionData.indexOf(chejiansaomaTitle) != -1) {
                model = new HomeModel(R.drawable.icon_chejiansaoma, chejiansaomaTitle);
                dataList.add(model);
            }*/

            /*String gongrenchanliangtongjiTitle = getString(R.string.title_chanliangtongji);
            if (permissionData.indexOf(gongrenchanliangtongjiTitle) != -1) {
                model = new HomeModel(R.drawable.icon_gongrenchanliangtongji, gongrenchanliangtongjiTitle);
                dataList.add(model);
            }*/

            /*String jinduchaxunTitle = getString(R.string.title_jindu_chaxun);
            if (permissionData.indexOf(jinduchaxunTitle) != -1) {
                model = new HomeModel(R.drawable.icon_jinduchaxun, jinduchaxunTitle);
                dataList.add(model);
            }*/
        }

        // 显示数据
        showData();
    }

    private void showData() {
        HomeAdapter adapter = new HomeAdapter(mContext, dataList);
        mGridView.setAdapter(adapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HomeModel data = dataList.get(position);
                int icon = data.getIcon();
                switch (icon) {
                    case R.drawable.icon_kucun_chaxun: // 库存查询
                        startActivity(new Intent(mContext, KucunActivity.class));
                        break;
                    case R.drawable.icon_xisoshoulishi: // 销售历史
                        startActivity(new Intent(mContext, XiaoshoulishiActivity.class));
                        break;
                    case R.drawable.icon_gongrenchanliangtongji: // 工人产量统计
                        startActivity(new Intent(mContext, ChanliangtongjiActivity.class));
                        break;
                    case R.drawable.icon_jinduchaxun:
                        startActivity(new Intent(mContext, JinduActivity.class));
                        break;
                    case R.drawable.icon_xiaoshoukaidan:
                        startActivity(new Intent(mContext, XiaoshouKaidanActivity.class));
                        break;
                    case R.drawable.icon_shoujichuantu: // 手机传图
                        CAPTURE_TYPE = Constant.CAPTURE_CHUANTU;
                        if (hasPermission(Constant.HARDWEAR_CAMERA_PERMISSION)) {
                            doOpenCamera(CAPTURE_TYPE);
                        } else {
                            requestPermission(Constant.HARDWEAR_CAMERA_CODE, Constant.HARDWEAR_CAMERA_PERMISSION);
                        }
                        break;
                    case R.drawable.icon_shoujipandian: // 手机盘点
                        CAPTURE_TYPE = Constant.CAPTURE_PANDIAN;
                        if (hasPermission(Constant.HARDWEAR_CAMERA_PERMISSION)) {
                            doOpenCamera(CAPTURE_TYPE);
                        } else {
                            requestPermission(Constant.HARDWEAR_CAMERA_CODE, Constant.HARDWEAR_CAMERA_PERMISSION);
                        }
                        break;
                    case R.drawable.icon_chejiansaoma: // 车间扫码
                        CAPTURE_TYPE = Constant.CAPTURE_CHEJIAN_SAOMIAO;
                        if (hasPermission(Constant.HARDWEAR_CAMERA_PERMISSION)) {
                            doOpenCamera(CAPTURE_TYPE);
                        } else {
                            requestPermission(Constant.HARDWEAR_CAMERA_CODE, Constant.HARDWEAR_CAMERA_PERMISSION);
                        }
                        break;
                    default:
                        Toast.makeText(mContext, "暂无该功能", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void doOpenCamera(String captureType) {
        Log.e("doOpenCamera", "子 doOpenCamera");

        Intent intent = new Intent(mContext, CaptureActivity.class);
        intent.putExtra("CAPTURE_TYPE", captureType);
        startActivity(intent);
    }
}
