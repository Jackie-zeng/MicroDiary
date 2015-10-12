package com.microdiary.dao;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.microdiary.entity.Diary;
import com.microdiary.entity.Money;

public class MoneyDB {

	public static String DATABASE_NAME = "MONEY_DATABASE"; //数据库名
	public static int DATABASE_VERSION = 1;     //版本号
	
	public final static String TABLE_INCOME = "income_table";   //收入表
	public final static String TABLE_EXTEND = "extend_table"; //支出表


	public final static String MID = "mid";   
	public final static String DATE = "date"; 
	public final static String VALUE = "value";
	public final static String MONEY = "money";
	public final static String REMARK = "remark";

	
	private String CREATE_TABLE_INCOME = "create table " + TABLE_INCOME + "(" 
	        + MID + " integer primary key, " 
			+ DATE + " date, "
			+ VALUE + " text, "
			+ MONEY + " int, "
			+ REMARK + " text)";
	
	private String CREATE_TABLE_EXTEND = "create table " + TABLE_EXTEND + "(" 
	        + MID + " integer primary key, " 
			+ DATE + " date, "
			+ VALUE + " text, "
			+ MONEY + " int, "
			+ REMARK + " text)";
	
	private String UPDATE_TABLE_INCOME = "DROP TABLE IF EXISTS " + TABLE_INCOME;
	private String UPDATE_TABLE_EXTEND = "DROP TABLE IF EXISTS " + TABLE_EXTEND;
	
	private DBHelper dbHelper = null;
	private SQLiteDatabase db = null;
	
	
	/**
	 * 构造函数
	 * @param context
	 */
	public MoneyDB(Context context) {
		dbHelper = new DBHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	
	/**
	 * 关闭数据库
	 */
	public void close() {
	    if(dbHelper != null) {
	    	dbHelper.close();
	    }
	    if(db != null) {
	    	db.close();
	    }
	}
	
	
	/**
	 * 获取收入列表
	 * @return
	 */
	public ArrayList<Money> selectIncome() {
		ArrayList<Money> list = new ArrayList<Money>();
		db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query(TABLE_INCOME, null, null, null, null, null, MID+ " desc");
		
		while(cursor.moveToNext()) {
			Money item = new Money();
			item.setMid(cursor.getInt(cursor.getColumnIndexOrThrow(MID)));
			item.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DATE)));
			item.setValue(cursor.getString(cursor.getColumnIndexOrThrow(VALUE)));
			item.setMoney(cursor.getInt(cursor.getColumnIndexOrThrow(MONEY)));
			item.setRemark(cursor.getString(cursor.getColumnIndexOrThrow(REMARK)));
			list.add(item);
		}
		close();
		return list;
	}
	
	
	
	
	
	/**
	 * 获取第一条收入数据
	 * @return
	 */
	public ArrayList<Money> selectIncomeFirst() {
		ArrayList<Money> list = new ArrayList<Money>();
		db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query(TABLE_INCOME, null, null, null, null, null, MID + " desc");
		
		while(cursor.moveToNext()) {
			Money item = new Money();
			item.setMid(cursor.getInt(cursor.getColumnIndexOrThrow(MID)));
			item.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DATE)));
			item.setValue(cursor.getString(cursor.getColumnIndexOrThrow(VALUE)));
			item.setMoney(cursor.getInt(cursor.getColumnIndexOrThrow(MONEY)));
			item.setRemark(cursor.getString(cursor.getColumnIndexOrThrow(REMARK)));
			list.add(item);
			break;
		}
		close();
		return list;
	}
	
	
	
	/**
	 * 插入一条收入数据
	 * @param date
	 * @param value
	 * @param money
	 * @param remark
	 * @return
	 */
	public long insertIncome(String date, String value, int money, String remark) {
		db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DATE, date);
		values.put(VALUE, value);
		values.put(MONEY, money);
		values.put(REMARK, remark);
		long i = db.insert(TABLE_INCOME, null, values);
		close();
		return i;
	}
	
	/**
	 * 更新一条收入数据
	 * @param date
	 * @param value
	 * @param money
	 * @param remark
	 * @return
	 */
	public long updateIncome(int mid, String date, String value, int money, String remark) {
		db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DATE, date);
		values.put(VALUE, value);
		values.put(MONEY, money);
		values.put(REMARK, remark);
		String whereClause = MID + " = " + mid;
		long i = db.update(TABLE_INCOME, values, whereClause, null);
		close();
		return i;
	}
	
	/**
	 * 删除id为mid的收入数据
	 * @param mid
	 * @return
	 */
	public long deleteIncome(int mid) {
		db = dbHelper.getWritableDatabase();
		String whereClause = MID + " = " + mid;
		long i = db.delete(TABLE_INCOME, whereClause, null);
		close();
		return i;
	}
	
	
	/**
	 * 获取支出列表
	 * @return
	 */
	public ArrayList<Money> selectExtend() {
		ArrayList<Money> list = new ArrayList<Money>();
		db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query(TABLE_EXTEND, null, null, null, null, null, MID + " desc");
		
		while(cursor.moveToNext()) {
			Money item = new Money();
			item.setMid(cursor.getInt(cursor.getColumnIndexOrThrow(MID)));
			item.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DATE)));
			item.setValue(cursor.getString(cursor.getColumnIndexOrThrow(VALUE)));
			item.setMoney(cursor.getInt(cursor.getColumnIndexOrThrow(MONEY)));
			item.setRemark(cursor.getString(cursor.getColumnIndexOrThrow(REMARK)));
			list.add(item);
		}
		close();
		return list;
	}
	
	
	
	
	
	/**
	 * 获取第一条支出数据
	 * @return
	 */
	public ArrayList<Money> selectExtendFirst() {
		ArrayList<Money> list = new ArrayList<Money>();
		db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query(TABLE_EXTEND, null, null, null, null, null, MID + " desc");
		
		while(cursor.moveToNext()) {
			Money item = new Money();
			item.setMid(cursor.getInt(cursor.getColumnIndexOrThrow(MID)));
			item.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DATE)));
			item.setValue(cursor.getString(cursor.getColumnIndexOrThrow(VALUE)));
			item.setMoney(cursor.getInt(cursor.getColumnIndexOrThrow(MONEY)));
			item.setRemark(cursor.getString(cursor.getColumnIndexOrThrow(REMARK)));
			list.add(item);
			break;
		}
		close();
		return list;
	}
	
	
	
	/**
	 * 插入一条支出数据
	 * @param date
	 * @param value
	 * @param money
	 * @param remark
	 * @return
	 */
	public long insertExtend(String date, String value, int money, String remark) {
		db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DATE, date);
		values.put(VALUE, value);
		values.put(MONEY, money);
		values.put(REMARK, remark);
		long i = db.insert(TABLE_EXTEND, null, values);
		close();
		return i;
	}
	
	/**
	 * 更新一条支出数据
	 * @param date
	 * @param value
	 * @param money
	 * @param remark
	 * @return
	 */
	public long updateExtend(int mid, String date, String value, int money, String remark) {
		db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DATE, date);
		values.put(VALUE, value);
		values.put(MONEY, money);
		values.put(REMARK, remark);
		String whereClause = MID + " = " + mid;
		long i = db.update(TABLE_EXTEND, values, whereClause, null);
		close();
		return i;
	}
	
	/**
	 * 删除id为mid的支出数据
	 * @param mid
	 * @return
	 */
	public long deleteExtend(int mid) {
		db = dbHelper.getWritableDatabase();
		String whereClause = MID + " = " + mid;
		long i = db.delete(TABLE_EXTEND, whereClause, null);
		close();
		return i;
	}

	
	private class DBHelper extends SQLiteOpenHelper {

		public DBHelper(Context context, String name, CursorFactory factory,
				int version) {
			super(context, name, factory, version);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL(CREATE_TABLE_INCOME);
			db.execSQL(CREATE_TABLE_EXTEND);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL(UPDATE_TABLE_INCOME);
			db.execSQL(UPDATE_TABLE_EXTEND);
			onCreate(db);
		}
	}	
}
