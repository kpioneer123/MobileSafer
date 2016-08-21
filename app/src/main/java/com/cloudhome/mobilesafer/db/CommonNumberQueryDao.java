package com.cloudhome.mobilesafer.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by xionhgu on 2016/8/21.
 * Email：965705418@qq.com
 */
public class CommonNumberQueryDao {



    /**
     * 得到分组的总数
     */

    public static int getGroupCount(SQLiteDatabase db){
        int result = 0;
//		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);

        Cursor cursor = db.rawQuery("select count(*) from classlist;", null);
        cursor.moveToFirst();
        result = cursor.getInt(0);
        cursor.close();
//		db.close();
        return result;

    }


    /**
     * 得到某组的孩子的总个数
     * @param groupPosition 某组
     * @return
     */
    public static int getChildCount(int groupPosition,SQLiteDatabase db){
        int result = 0;
        int newgroupPosition = groupPosition + 1;
//		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);

        Cursor cursor = db.rawQuery("select count(*) from table"+newgroupPosition,null);
        cursor.moveToFirst();
        result = cursor.getInt(0);
        cursor.close();
//		db.close();
        return result;

    }

    /**
     * 得到分组的名称
     */
    public static String getGroupName(int groupPosition,SQLiteDatabase db){
        String result = "";
        int newgroupPosition = groupPosition + 1;
//		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);

        Cursor cursor = db.rawQuery("select name from classlist where idx = ?", new String[]{newgroupPosition+""});
        cursor.moveToFirst();
        result = cursor.getString(0);
        cursor.close();
//		db.close();
        return result;

    }

    /**
     * 得到某组的某个孩子的名称
     */
    public static String getChildName(int groupPosition,int childPosition,SQLiteDatabase db){
        String result = "";
        int newgroupPosition = groupPosition + 1;
        int newchildPosition = childPosition + 1;
//		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);

        Cursor cursor = db.rawQuery("select name , number from table"+newgroupPosition+ "  where _id = ?", new String[]{newchildPosition+""});
        cursor.moveToFirst();
        String name = cursor.getString(0);
        String number = cursor.getString(1);
        result = name+"\n  "+number;
        cursor.close();
//		db.close();
        return result;

    }
}
