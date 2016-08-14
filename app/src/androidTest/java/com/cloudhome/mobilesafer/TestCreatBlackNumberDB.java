package com.cloudhome.mobilesafer;

import android.test.AndroidTestCase;

import com.cloudhome.mobilesafer.db.BlackNumberDBOpenHelper;
import com.cloudhome.mobilesafer.db.BlackNumberDao;

import java.util.Random;


public class TestCreatBlackNumberDB extends AndroidTestCase {

    public void testCreatBlackNumberDB(){
        BlackNumberDBOpenHelper helper = new BlackNumberDBOpenHelper(getContext());
        helper.getWritableDatabase();
    }


    public void testadd(){
                BlackNumberDao dao = new BlackNumberDao(getContext());
        //13512345600 - 13512345699
        Random random = new Random();
        for(int i=0;i<100;i++){
           // random.nextInt(3)  range[0,3)
            dao.add("1351234560"+i, String.valueOf(random.nextInt(3)));
        }
//        BlackNumberDao dao = new BlackNumberDao(getContext());
//        dao.add("199","1");

    }


    public void testdelete(){
        BlackNumberDBOpenHelper helper = new BlackNumberDBOpenHelper(getContext());
        helper.getWritableDatabase();
        BlackNumberDao dao = new BlackNumberDao(getContext());
        dao.delete("199");
    }

    public void testdeleteAll(){
        BlackNumberDBOpenHelper helper = new BlackNumberDBOpenHelper(getContext());
        helper.getWritableDatabase();
        BlackNumberDao dao = new BlackNumberDao(getContext());
        dao.deleteAll();
    }
//
//
//    public void update(){
//        BlackNumberDao dao = new BlackNumberDao(getContext());
//        dao.update("119", "0");
//    }
//
//    public void query(){
//        BlackNumberDao dao = new BlackNumberDao(getContext());
//        boolean result = dao.query("119");
//        assertEquals(true, result);
//    }
//
//    public void queryMode(){
//        BlackNumberDao dao = new BlackNumberDao(getContext());
//        String mode = dao.queryMode("119");
//        System.out.println("mode=="+mode);
//    }
//


}
