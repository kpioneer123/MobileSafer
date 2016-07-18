package com.cloudhome.mobilesafer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudhome.mobilesafer.R;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * Created by xionghu on 2016/7/14.
 * Email：965705418@qq.com
 */
public class HomeAdapter extends BaseAdapter {


    private Context context;
    private LayoutInflater layoutInflater;

    public ArrayList<HashMap<String, Object>> list =new ArrayList<HashMap<String, Object>>();

    public HomeAdapter(Context context) {
        super();
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setData(ArrayList<HashMap<String, Object>> list) {
        this.list = list;

    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.home_item, parent, false);
            holder.tv_name=(TextView) convertView.findViewById(R.id.tv_name);
            holder.iv_icon=(ImageView) convertView.findViewById(R.id.iv_icon);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        HashMap<String, Object> map = list.get(position);

        holder.tv_name.setText(map.get("name").toString());
        holder.iv_icon.setImageResource((Integer) map.get("img"));

        return convertView;
    }

    class ViewHolder {

        TextView   tv_name;
        ImageView iv_icon;
    }

}