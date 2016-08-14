package com.cloudhome.mobilesafer.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudhome.mobilesafer.R;
import com.cloudhome.mobilesafer.activity.AppManagerActivity;
import com.cloudhome.mobilesafer.domain.AppInfo;
import java.util.List;

/**
 * Created by xionghu on 2016/8/8.
 * Email：965705418@qq.com
 */
public class AppManageAdapter  extends BaseAdapter {


   // private List<AppInfo> appInfos;

    private List<AppInfo> systemappInfos;
    private List<AppInfo> userappInfos;


    private Context context;
    private LayoutInflater layoutInflater;



    public AppManageAdapter(Context context) {
        super();
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }


    /**
     * @param userappInfos 用户程序
     * @param systemappInfos 系统程序
     */
    public void setData(List<AppInfo>  userappInfos,List<AppInfo>  systemappInfos) {

        this.userappInfos = userappInfos;
        this.systemappInfos = systemappInfos;


    }

    @Override
    public int getCount() {
        return userappInfos.size()+systemappInfos.size()+2;
    }

    @Override
    public Object getItem(int position) {
        //得到应用程序的信息
        AppInfo appInfo;

        //
        if(position == 0)
        {

            return null;


        }else if(position == (userappInfos.size()+1))
        {

            return null;

        }else if(position<=userappInfos.size())
        {
            int newposition = position-1;
            appInfo=userappInfos.get(newposition);
        }else{

            int newposition = position-userappInfos.size()-2;
            appInfo=systemappInfos.get(newposition);
        }
        return appInfo;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //得到应用程序的信息
        AppInfo appInfo;

        //
        if(position == 0)
        {
            TextView tv = new TextView(context);
            tv.setBackgroundColor(Color.GRAY);
            tv.setText("用户程序("+userappInfos.size()+")");
            tv.setTextColor(Color.WHITE);
            return tv;


        }else if(position == (userappInfos.size()+1))
        {
            TextView tv = new TextView(context);
            tv.setBackgroundColor(Color.GRAY);
            tv.setText("系统程序("+userappInfos.size()+")");
            tv.setTextColor(Color.WHITE);
            return tv;

        }else if(position<=userappInfos.size())
        {
            int newposition = position-1;
            appInfo=userappInfos.get(newposition);
        }else{

            int newposition = position-userappInfos.size()-2;
            appInfo=systemappInfos.get(newposition);
        }
        ViewHolder holder ;
        View view;
        if (convertView != null&&convertView instanceof RelativeLayout) {

            view = convertView;
            holder = (ViewHolder) convertView.getTag();


        } else {


            holder = new ViewHolder();
            view = layoutInflater.inflate(R.layout.appmanager_item, parent, false);
            holder.iv_icon   = (ImageView)  view.findViewById(R.id.iv_icon);
            holder.tv_name     = (TextView)  view.findViewById(R.id.tv_name);
            holder.tv_location   = (TextView) view.findViewById(R.id.tv_location);

            view.setTag(holder);
        }


//        if(position <userappInfos.size())
//        {
//            //用户程序
//            appInfo = userappInfos.get(position);
//
//        }else{
//            //系统程序
//            appInfo = systemappInfos.get(position-userappInfos.size());
//
//        }
        holder.tv_name.setText(appInfo.getName());

        if(appInfo.isRom())
        {
            //安装到手机内部
            holder.tv_location.setText("手机内部");
        }else{

            //外部存储
            holder.tv_location.setText("外部存储");

        }
        holder.iv_icon.setImageDrawable(appInfo.getIcon());

        return view;
    }

    static class ViewHolder {


        ImageView iv_icon;
        TextView tv_name;
        TextView  tv_location;


    }


}
