package com.cloudhome.mobilesafer.db;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;
import android.util.Log;

import com.cloudhome.mobilesafer.domain.BlackNumberInfo;

import java.util.ArrayList;
import java.util.List;


public class BlackNumberDao {

	private   BlackNumberDBOpenHelper helper;

	public BlackNumberDao(Context context){
		helper = new BlackNumberDBOpenHelper(context);
	}

	/**
	 * 添加一条黑名单数据
	 * @param number 黑名单号码
	 * @param mode 拦截模式，0电话拦截，1短信拦截，2全部拦截
	 */
	public void add(String number,String mode){
		SQLiteDatabase db = helper.getWritableDatabase();

		//ContentValues 相当于数据集合
		ContentValues values = new ContentValues();
		values.put("number", number);
		values.put("mode", mode);

		//第二个参数为null，底层插入就会失败 ，values 不为null  ，则第二个参数不会进行判断
		db.insert("blacknumber", null, values );
		db.close();
	}

	/**
	 * 删除一条黑名单数据
	 * @param number 要删除的黑名单号码
	 */
	public void delete(String number){
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("blacknumber", "number=?", new String[]{number});
		db.close();
	}

	/**
	 * 删除黑名单所有数据
	 *
	 */
	public void deleteAll(){
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("blacknumber", null, null);
		db.close();
	}


	/**
	 * 修改一条数据
	 * @param number 要修改的电话号码
	 * @param newmode 新的拦截模式
	 */
	public void update(String number,String newmode){
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("mode", newmode);
		db.update("blacknumber", values , "number=?", new String[]{number});
		db.close();
	}

	/**
	 * 查询黑名单是否存在
	 * @param number 要查询的电话号码
	 * @return
	 */
	public boolean query(String number){
		boolean result = false;
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.query("blacknumber", null, "number=?", new String[]{number}, null, null, null);
		if(cursor.moveToNext()){
			result = true;
		}
		db.close();

		return result;
	}

	/**
	 * 查询黑名单的拦截模式
	 * @param number 要查询的电话号码
	 * @return
	 */
	public String queryMode(String number){
		String result = "2";
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.query("blacknumber", new String[]{"mode"}, "number=?", new String[]{number}, null, null, null);
		if(cursor.moveToNext()){
			result = cursor.getString(0);
		}
		db.close();
		return result;
	}


	/**
	 * 得到所有的黑名单信息
	 * @return
	 */
	public  List<BlackNumberInfo> queryAll(){
		SystemClock.sleep(3000);
		List<BlackNumberInfo> result =  new ArrayList<BlackNumberInfo>();
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.query("blacknumber", new String[]{"number","mode"}, null, null, null, null, null);
		while(cursor.moveToNext()){
			BlackNumberInfo info = new BlackNumberInfo();
			String number = cursor.getString(0);
			info.setNumber(number);
			String mode = cursor.getString(1);
			info.setMode(mode);
        Log.d("888888",number);
			result.add(info);

		}
		db.close();
		return result;
	}


	/**
	 *  order by _id desc 倒序排序
	 * 加载部分（每次请求返回20条）数据
	 * @param index 从那个地方开始加载20条
	 * @return
	 */
	public List<BlackNumberInfo> queryPart(int index){
		SystemClock.sleep(600);
		List<BlackNumberInfo> result =  new ArrayList<BlackNumberInfo>();
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select number,mode from blacknumber order by _id desc limit 20 offset ?;", new String[]{index+""});
		while(cursor.moveToNext()){
			BlackNumberInfo info = new BlackNumberInfo();
			String number = cursor.getString(0);
			info.setNumber(number);
			String mode = cursor.getString(1);
			info.setMode(mode);

			result.add(info);

		}
		db.close();
		return result;
	}

	/**
	 * 得到数据库的总条数
	 * @return
	 */
	public int queryCount(){
		int result = 0;
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select count(*) from blacknumber;", null);
		while(cursor.moveToNext()){
			result = cursor.getInt(0);
		}
		db.close();
		return result;
	}






}
