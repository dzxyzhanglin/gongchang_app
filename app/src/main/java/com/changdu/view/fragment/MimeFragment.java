package com.changdu.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.changdu.R;
import com.changdu.activiti.CompanyActivity;
import com.changdu.activiti.UpdatePassActivity;
import com.changdu.adapter.MimeAdapter;
import com.changdu.model.HomeModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 个人中心
 */
public class MimeFragment extends BaseFragment {

    private View mContentView;
    private ListView mListView;
    private List<HomeModel> mDataList;
    private MimeAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mContentView = inflater.inflate(R.layout.fragment_mime_layout, container, false);
        mListView = mContentView.findViewById(R.id.lv_mime);
        initData();
        return mContentView;
    }

    private void initData() {
        mDataList = new ArrayList<>();
        HomeModel model = new HomeModel(R.drawable.icon_company, "企业信息");
        mDataList.add(model);

        model = new HomeModel(R.drawable.icon_update_password, "密码修改");
        mDataList.add(model);

        // 显示数据
        showData();
    }

    private void showData() {
        adapter = new MimeAdapter(mContext, mDataList);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                HomeModel data = mDataList.get(position);
                switch (data.getIcon()) {
                    case R.drawable.icon_company:
                        startActivity(new Intent(mContext, CompanyActivity.class));
                        break;
                    case R.drawable.icon_update_password:
                        startActivity(new Intent(mContext, UpdatePassActivity.class));
                        break;
                }
            }
        });
    }
}
