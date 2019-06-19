package com.changdu.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.changdu.R;
import com.changdu.activiti.KucunActivity;
import com.changdu.activiti.LoginActivity;
import com.changdu.activiti.XiaoshoulishiActivity;
import com.changdu.adapter.HomeAdapter;
import com.changdu.model.HomeModel;

import java.util.ArrayList;
import java.util.List;

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

        dataList = new ArrayList<>();
        HomeModel model = new HomeModel(R.drawable.icon_kucun_chaxun, getString(R.string.title_kucun_chaxun));
        dataList.add(model);

        model = new HomeModel(R.drawable.icon_shoujichuantu, "手机传图");
        dataList.add(model);

        model = new HomeModel(R.drawable.icon_shoujipandian, "手机盘点");
        dataList.add(model);

        model = new HomeModel(R.drawable.icon_xiaoshoukaidan, "销售开单");
        dataList.add(model);

        model = new HomeModel(R.drawable.icon_xisoshoulishi, getString(R.string.title_xiaoshoulishi_chaxun));
        dataList.add(model);

        model = new HomeModel(R.drawable.icon_chejiansaoma, "车间扫描");
        dataList.add(model);

        model = new HomeModel(R.drawable.icon_gongrenchanliangtongji, "产量统计");
        dataList.add(model);

        model = new HomeModel(R.drawable.icon_jinduchaxun, "生产进度查询");
        dataList.add(model);

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
                    /*case R.drawable.icon_shoujichuantu:
                        startActivity(new Intent(mContext, LoginActivity.class));
                        break;*/
                    default:
                        Toast.makeText(mContext, "暂无该功能", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
