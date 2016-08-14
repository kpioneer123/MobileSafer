package com.cloudhome.mobilesafer;

import android.test.AndroidTestCase;

import com.cloudhome.mobilesafer.domain.TaskInfo;
import com.cloudhome.mobilesafer.engine.TaskInfoProvider;

import java.util.List;

/**
 * Created by xionghu on 2016/8/11.
 * Email：965705418@qq.com
 */
public class TestGetTaskInfo extends AndroidTestCase{

    public void testgetTaskInfo()
    {
       List<TaskInfo> taskInfo =  TaskInfoProvider.getAllTaskInfos(getContext());
        for(TaskInfo  task : taskInfo)
        {
            System.out.println(task.toString());
        }
    }
}
