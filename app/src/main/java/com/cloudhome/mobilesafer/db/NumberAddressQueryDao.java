package com.cloudhome.mobilesafer.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by xionghu on 2016/7/26.
 * Email：965705418@qq.com
 */
public class NumberAddressQueryDao {


    private  static String path = "/data/data/com.cloudhome.mobilesafer/files/address.db";


    /**
     * 号码归属地的查询
     * @param number 要查询的电话号码
     * @return 号码的归属地
     */
    public static  String  getAddress(String number){

        String address = number;

        //assets webview 加载图片
        SQLiteDatabase db = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READONLY);


        //^1[3456789]\d{9}$

        if(number.matches("^1[3456789]\\d{9}$"))
        { //手机电话号码
            Cursor cursor = db.rawQuery("select location from data2 where id = (select outkey from data1 where id = ?)",new String[]{number.substring(0,7)});


            while (cursor.moveToNext()){
                String location = cursor.getString(0);
                address = location;
            }

            cursor.close();
            db.close();
        }else {
            switch (number.length())
            {
                case 3://110,119,120
                address = "政府部门以及公共事业号码" ;

                        break;

                case 4:
                    address = "模拟器号码" ;

                    break;
                case 5:
                    address = "客服号码" ;

                    break;

                case 6:
                    address = "本地号码" ;

                    break;
                case 7:
                    address = "本地号码" ;

                    break;
                case 8:
                    address = "本地号码" ;

                    break;
                default:
                    if(number!=null&&number.startsWith("0")&&number.length()>=10)
                    {
                        //手机电话号码
                        Cursor cursor = db.rawQuery("select location from data2 where area = ? ",new String[]{number.substring(1,3)});


                        while (cursor.moveToNext()){
                            String location = cursor.getString(0);
                            address = location.substring(0,location.length()-2);
                        }


                         cursor = db.rawQuery("select location from data2 where area = ? ",new String[]{number.substring(1,4)});


                        while (cursor.moveToNext()){
                            String location = cursor.getString(0);
                            address = location.substring(0,location.length()-2);
                        }



                        cursor.close();
                        db.close();
                    }
                    break;
            }
        }


        return address;
    }
}
