package com.cloudhome.mobilesafer.test;

import java.util.Random;


import android.test.AndroidTestCase;

import com.cloudhome.mobilesafer.db.BlackNumberDBOpenHelper;
import com.cloudhome.mobilesafer.db.BlackNumberDao;

public class TestCreatBlackNumberDB extends AndroidTestCase {

    public void testCreatBlackNumberDB(){
        BlackNumberDBOpenHelper helper = new BlackNumberDBOpenHelper(getContext());
        helper.getWritableDatabase();
    }


    public void testdd(){

        BlackNumberDBOpenHelper helper = new BlackNumberDBOpenHelper(getContext());
        helper.getWritableDatabase();

        BlackNumberDao dao = new BlackNumberDao(getContext());
        dao.add("119", "1");
        assertEquals(4, 2 + 2);
    }


//    public void delete(){
//        BlackNumberDao dao = new BlackNumberDao(getContext());
//        dao.delete("119");
//    }
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



}
