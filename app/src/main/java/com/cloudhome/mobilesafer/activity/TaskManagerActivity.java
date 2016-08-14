package com.cloudhome.mobilesafer.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudhome.mobilesafer.R;
import com.cloudhome.mobilesafer.adapter.TaskInfoAdapter;
import com.cloudhome.mobilesafer.domain.TaskInfo;
import com.cloudhome.mobilesafer.engine.TaskInfoProvider;
import com.cloudhome.mobilesafer.utils.SystemInfoUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by xionghu on 2016/8/10.
 * Email：965705418@qq.com
 */
public class TaskManagerActivity extends Activity{

    private TextView tv_run_process_count;
    private TextView tv_avail_ram;
    private TextView tv_status;
    private ListView lv_taskmanager;
    private LinearLayout ll_loading;
    private ActivityManager am;
    private TaskInfoAdapter madapter;
    /**
     *   运行中进程个数
     */
    private int runningProcessCount;
    /**
     * 可用内存
     */
    private long availRam;
    /**
     * 总内存
     */
    private long totalRam;

    private List<TaskInfo> taskInfos;//所有在运行的进程列表this


    private List<TaskInfo> systemtaskInfos;

    private List<TaskInfo> usertaskInfos;


    private Handler handler  =new Handler(){

        @Override
        public void handleMessage(Message msg) {

            madapter.setData(usertaskInfos,systemtaskInfos);
            lv_taskmanager.setAdapter(madapter);

            ll_loading.setVisibility(View.GONE);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_manager);

        initView();
        initEvent();
    }

    private void initView() {
        am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        tv_run_process_count  =  (TextView) findViewById(R.id.tv_run_process_count);
        tv_avail_ram          =  (TextView) findViewById(R.id.tv_avail_ram);
        lv_taskmanager        =  (ListView) findViewById(R.id.lv_taskmanager);
        ll_loading            =  (LinearLayout) findViewById(R.id.ll_loading);
        tv_status             =  (TextView) findViewById(R.id.tv_status);


    }

    private void initEvent() {
        runningProcessCount = SystemInfoUtils.getRunningProcessCount(this);
        availRam = SystemInfoUtils.getAvailRam(this);
        totalRam = SystemInfoUtils.getTotalRam(this);

        tv_run_process_count.setText("运行中进程:"+runningProcessCount+"个");

        tv_avail_ram.setText("剩余/总内存："+ Formatter.formatFileSize(this, availRam)+"/"+Formatter.formatFileSize(this, totalRam));

        madapter = new TaskInfoAdapter(this);



        lv_taskmanager.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if(systemtaskInfos ==null||usertaskInfos==null)
                {
                    return;
                }
                if(firstVisibleItem > systemtaskInfos.size())
                {
                    //显示系统程序
                    tv_status.setText("系统程序("+systemtaskInfos.size()+")");

                }else{
                    //用户程序
                    tv_status.setText("用户程序("+usertaskInfos.size()+")");
                }
            }
        });


        lv_taskmanager.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Object obj = lv_taskmanager.getItemAtPosition(position);
                if(obj != null)
                {
                    TaskInfo taskinfo = (TaskInfo)obj;
                    CheckBox cb_status = (CheckBox) findViewById(R.id.cb_status);
                    if(taskinfo.isChecked())
                    {
                        taskinfo.setChecked(false);
                     //   cb_status.setChecked(false);
                    }else{
                        taskinfo.setChecked(true);
                  //      cb_status.setChecked(true);
                    }

                    madapter.notifyDataSetChanged();
                }


            }
        });
        fillData();
    }

    private void fillData() {

        ll_loading.setVisibility(View.VISIBLE);
        new Thread()
        {
            public void run()
            {
              taskInfos = TaskInfoProvider.getAllTaskInfos(TaskManagerActivity.this);

                systemtaskInfos = new ArrayList<TaskInfo>();
                usertaskInfos = new ArrayList<TaskInfo>();
                for(TaskInfo taskInfo : taskInfos){
                    if(taskInfo.isUser()){
                        usertaskInfos.add(taskInfo);
                    }else{
                        systemtaskInfos.add(taskInfo);
                    }
                }

                handler.sendEmptyMessage(0);

            }
        }.start();

    }


    public void selectAll(View view){

        for(TaskInfo taskInfo : usertaskInfos)
        {
            if(getPackageName().equals(taskInfo.getPackName())){
                continue;
            }

            taskInfo.setChecked(true);
        }

        for(TaskInfo taskInfo : systemtaskInfos)
        {
            taskInfo.setChecked(true);
        }

        //刷新页面
        madapter.notifyDataSetChanged();    //getCount --getView()



    }

    public void unSelect(View view){

        for(TaskInfo taskInfo : usertaskInfos)
        {
            if(getPackageName().equals(taskInfo.getPackName())){
                continue;
            }


            taskInfo.setChecked(!taskInfo.isChecked());
        }

        for(TaskInfo taskInfo : systemtaskInfos)
        {
            taskInfo.setChecked(!taskInfo.isChecked());
        }

        //刷新页面
        madapter.notifyDataSetChanged();    //getCount --getView()

    }



    /**
     * 一键清理
     */
    public void killAll(View view){

        int killedCount = 0;
        List<TaskInfo> killedTaskInfo = new ArrayList<TaskInfo>();
       long ramAdd = 0;
        for(TaskInfo taskInfo : usertaskInfos)
        {

            //把进程杀死-自杀
            if(taskInfo.isChecked())
            {
                am.killBackgroundProcesses(taskInfo.getPackName());
               // usertaskInfos.remove(taskInfo);
                killedTaskInfo.add(taskInfo);
                killedCount++;
                ramAdd += taskInfo.getMeminfosize();
            }
        }

        for(TaskInfo taskInfo : systemtaskInfos)
        {


            if(taskInfo.isChecked())
            {
                am.killBackgroundProcesses(taskInfo.getPackName());
               // systemtaskInfos.remove(taskInfo);
                killedTaskInfo.add(taskInfo);
                killedCount++;
                ramAdd += taskInfo.getMeminfosize();
            }
        }

        for(TaskInfo taskInfo : killedTaskInfo)
        {
            if(taskInfo.isUser()){
                usertaskInfos.remove(taskInfo);
            }else{
                systemtaskInfos.remove(taskInfo);
            }

        }
        runningProcessCount -= killedCount;
        availRam += ramAdd;

        tv_run_process_count.setText("运行中进程:"+runningProcessCount+"个");

        tv_avail_ram.setText("剩余/总内存："+ Formatter.formatFileSize(this, availRam)+"/"+Formatter.formatFileSize(this, totalRam));


        Toast.makeText(getApplicationContext(),"杀死了：" + killedCount+"个进程,释放了："+ Formatter.formatFileSize(getApplicationContext(),availRam),Toast.LENGTH_SHORT).show();
       // fillData();
//        //刷新页面
        madapter.notifyDataSetChanged();    //getCount --getView() list没有改变 但这里 remove 忽悠用户

    }

    public void reEnterSetting(View view){

        Intent intent = new Intent(this,TaskManagerSettingActivity.class);

        startActivityForResult(intent,0);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //需要重新加载吗？不需要
        madapter.notifyDataSetChanged(); //getcount - getview
    }
}
