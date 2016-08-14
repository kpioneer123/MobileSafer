package com.cloudhome.mobilesafer.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudhome.mobilesafer.R;
import com.cloudhome.mobilesafer.activity.TaskManagerActivity;
import com.cloudhome.mobilesafer.domain.AppInfo;
import com.cloudhome.mobilesafer.domain.TaskInfo;

import java.util.List;

/**
 * Created by xionghu on 2016/8/8.
 * Email：965705418@qq.com
 */
public class TaskInfoAdapter extends BaseAdapter {


   // private List<AppInfo> appInfos;

    private List<TaskInfo> systemtaskInfos;
    private List<TaskInfo>  usertaskInfos;


    private Context context;
    private LayoutInflater layoutInflater;



    public TaskInfoAdapter(Context context) {
        super();
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }


    /**
     * @param usertaskInfos   用户进程
     * @param systemtaskInfos 系统进程
     */
    public void setData( List<TaskInfo>  usertaskInfos, List<TaskInfo>  systemtaskInfos) {

        this.usertaskInfos = usertaskInfos;
        this.systemtaskInfos = systemtaskInfos;


    }

    @Override
    public int getCount() {

        SharedPreferences sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        boolean showsystem =sp.getBoolean("showsystem",true);

        if(showsystem)
        {
            return usertaskInfos.size()+systemtaskInfos.size()+2;

        }else{
            return usertaskInfos.size()+1;
        }

       // return usertaskInfos.size()+systemtaskInfos.size()+2;
    }

    @Override
    public Object getItem(int position) {
        //得到应用程序的信息
        TaskInfo taskInfo;

        //
        if(position == 0)
        {

            return null;


        }else if(position == (usertaskInfos.size()+1))
        {

            return null;

        }else if(position<=usertaskInfos.size())
        {
            //用户进程
            taskInfo=usertaskInfos.get(position-1);
        }else{
            //系统进程
            int newposition = position-usertaskInfos.size()-2;
            taskInfo=systemtaskInfos.get(newposition);
        }
        return taskInfo;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //得到应用程序的信息
        TaskInfo taskInfo;


        if(position == 0)
        {
            TextView tv = new TextView(context);
            tv.setBackgroundColor(Color.GRAY);
            tv.setText("用户进程("+usertaskInfos.size()+")");
            tv.setTextColor(Color.WHITE);
            return tv;


        }else if(position == (usertaskInfos.size()+1))
        {
            TextView tv = new TextView(context);
            tv.setBackgroundColor(Color.GRAY);
            tv.setText("系统进程("+usertaskInfos.size()+")");
            tv.setTextColor(Color.WHITE);
            return tv;

        }else if(position<=usertaskInfos.size())
        {

            taskInfo=usertaskInfos.get(position-1);
        }else{

            int newposition = position-usertaskInfos.size()-2;
            taskInfo=systemtaskInfos.get(newposition);
        }
        ViewHolder holder ;
        View view;
        if (convertView != null&&convertView instanceof RelativeLayout) {

            view = convertView;
            holder = (ViewHolder) convertView.getTag();


        } else {


            holder = new ViewHolder();
            view = layoutInflater.inflate(R.layout.taskmanger_item, parent, false);
            holder.iv_icon   = (ImageView)  view.findViewById(R.id.iv_icon);
            holder.tv_name     = (TextView)  view.findViewById(R.id.tv_name);
            holder.tv_location   = (TextView) view.findViewById(R.id.tv_location);
            holder.tv_meminfosize   = (TextView) view.findViewById(R.id.tv_meminfosize);
            holder.cb_status   = (CheckBox) view.findViewById(R.id.cb_status);

            view.setTag(holder);
        }


        holder.tv_name.setText(taskInfo.getName());
        holder.tv_meminfosize.setText(Formatter.formatFileSize(context, taskInfo.getMeminfosize()));
        holder.iv_icon.setImageDrawable(taskInfo.getIcon());


        if(taskInfo.isChecked())
        {
            //被选中-勾选
            holder.cb_status.setChecked(true);

        }else{

            //没有选择-不是勾选
            holder.cb_status.setChecked(false);

        }


        if(context.getPackageName().equals(taskInfo.getPackName())){
            //手机卫士
            holder.cb_status.setVisibility(View.GONE);
        }else{
            //其他应用显示
            holder.cb_status.setVisibility(View.VISIBLE);
        }




        return view;
    }

    static class ViewHolder {


        ImageView iv_icon;
        TextView  tv_name;
        TextView  tv_location;
        TextView  tv_meminfosize;
        CheckBox  cb_status;
    }


}
