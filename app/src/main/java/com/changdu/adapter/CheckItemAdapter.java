package com.changdu.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.changdu.R;
import com.changdu.util.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckItemAdapter extends BaseAdapter {
    List<Map<String, String>> list;
    Context context;
    private Map<String, String> map = new HashMap<>();
    private ArrayAdapter<String> arr_adapter;
    //定义hashMap 用来存放之前创建的每一项item
    HashMap<Integer, View> lmap = new HashMap<Integer, View>();

    public CheckItemAdapter(Context context, List<Map<String, String>> list) {
        this.list = list;
        this.context = context;
    }

    public void setArr(List<String> listS) {
        //适配器
        arr_adapter = new ArrayAdapter<String>(context, R.layout.sp_item, listS);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    public Map<String, String> getStringBuilder() {
        return map;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final viewHolder holder;
        //创建每一个滑动出来的item项，将创建出来的项，放入数组中，为下次复用使用
        if (lmap.get(position) == null) {
            holder = new viewHolder();
            convertView = View.inflate(context, R.layout.item_list_checkitem, null);
            holder.XMMC = (TextView) convertView.findViewById(R.id.XMMC);
            holder.sp = (Spinner) convertView.findViewById(R.id.sp);
            convertView.setTag(holder);
            lmap.put(position, convertView);
        } else {
            convertView = lmap.get(position);
            holder = (viewHolder) convertView.getTag();
        }

        final Map<String, String> deviceInfo = list.get(position);
        holder.XMMC.setText(StringUtil.convertStr(deviceInfo.get("XMMC")));

        //加载适配器
        holder.sp.setAdapter(arr_adapter);
        holder.sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int p, long id) {
                Log.e("选中：", arr_adapter.getItem(p) + p);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(StringUtil.convertStr(deviceInfo.get("ID")));
                stringBuilder.append("|");
                stringBuilder.append(arr_adapter.getItem(p));
                stringBuilder.append("|");
                stringBuilder.append(StringUtil.convertStr(deviceInfo.get("BFSL")));
                map.put(position + "", stringBuilder.toString());

                Log.e("mapsize", map.size() + "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return convertView;
    }

    private class viewHolder {
        private TextView XMMC;
        private EditText yy;
        private Spinner sp;
    }
}
