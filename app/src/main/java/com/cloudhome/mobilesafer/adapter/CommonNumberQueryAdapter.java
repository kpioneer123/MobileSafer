package com.cloudhome.mobilesafer.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.cloudhome.mobilesafer.db.CommonNumberQueryDao;

/**
 * Created by xionhgu on 2016/8/21.
 * Email：965705418@qq.com
 */
public class CommonNumberQueryAdapter extends BaseExpandableListAdapter {


    private Context context;
    private LayoutInflater layoutInflater;
    private SQLiteDatabase db;


    public CommonNumberQueryAdapter(Context context,SQLiteDatabase db) {
        super();
        this.db = db;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }



    @Override
    public int getGroupCount() {
        return CommonNumberQueryDao.getGroupCount(db);
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return CommonNumberQueryDao.getChildCount(groupPosition,db);
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        TextView view;
        if(convertView !=null){
            view = (TextView) convertView;
        }else{
            view = new TextView(context);
        }
        view.setTextColor(Color.RED);
        view.setTextSize(20);
        view.setText("       "+ CommonNumberQueryDao.getGroupName(groupPosition,db));
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        TextView view;
        if(convertView !=null){
            view = (TextView) convertView;
        }else{
            view = new TextView(context);
        }
        view.setTextColor(Color.BLACK);
        view.setTextSize(18);
        view.setText("  "+CommonNumberQueryDao.getChildName(groupPosition, childPosition,db));
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


}
