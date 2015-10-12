package com.microdiary.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.microdiary.entity.Clock;
import com.microdiary.entity.Diary;

public class ClockDB {

	public static String DATABASE_NAME = "CLOCK_DATABASE"; //���ݿ���
	public static int DATABASE_VERSION = 1;     //�汾��
	
	public final static String TABLE_NAME = "clock_table";   //΢�ռǱ���
   	

	public final static String CID = "cid";   
	public final static String DATE = "date"; 
	public final static String TIME = "time";
	public final static String CONTENT = "content";
	
	private String CREATE_TABLE = "create table " + TABLE_NAME + "(" 
	        + CID + " integer primary key, " 
			+ DATE + " date, "
			+ CONTENT + " text, "
			+ TIME + " date)";
	
	private String UPDATE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
	
	private DBHelper dbHelper = null;
	private SQLiteDatabase db = null;
	
	
	/**
	 * ���캯��
	 * @param context
	 */
	public ClockDB(Context context) {
		dbHelper = new DBHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	
	/**
	 * �ر����ݿ�
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
	 * ��ȡnaozhong�б�
	 * @return
	 */
	public ArrayList<Clock> select() {
		ArrayList<Clock> list = new ArrayList<Clock>();
		db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, CID + " desc");
		while(cursor.moveToNext()) {
			Clock clock = new Clock();
			clock.setCid(cursor.getInt(cursor.getColumnIndexOrThrow(CID)));
			clock.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DATE)));
			clock.setTime(cursor.getString(cursor.getColumnIndexOrThrow(TIME)));
			clock.setContent(cursor.getString(cursor.getColumnIndexOrThrow(CONTENT)));
			list.add(clock);
		}
		close();
		return list;
	}
	
	
	
	/**
	 * ��ȡ��һ���ռ�
	 * @return
	 */
	public ArrayList<Clock> selectFirst() {
		ArrayList<Clock> list = new ArrayList<Clock>();
		db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, CID + " desc");
		while(cursor.moveToNext()) {
			Clock clock = new Clock();
			clock.setCid(cursor.getInt(cursor.getColumnIndexOrThrow(CID)));
			clock.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DATE)));
			clock.setTime(cursor.getString(cursor.getColumnIndexOrThrow(TIME)));
			clock.setContent(cursor.getString(cursor.getColumnIndexOrThrow(CONTENT)));
			list.add(clock);
			break;
		}
		close();
		return list;
	}
	
	
	
	/**
	 * ����һ���ռ�
	 * @param date
	 * @param content
	 * @param picture
	 * @param audio
	 * @param video
	 * @param audioAddress
	 * @param photoAddress
	 * @param videoAddress
	 * @return
	 */
	public long insert(String date, String content, 
			String time) {
		db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DATE, date);
		values.put(TIME, time);
		values.put(CONTENT, content);
		long i = db.insert(TABLE_NAME, null, values);
		close();
		return i;
	}
	
	/**
	 * ����һ���ռ�
	 * @param did
	 * @param date
	 * @param content
	 * @param picture
	 * @param audio
	 * @param video
	 * @param audioAddress
	 * @param photoAddress
	 * @param videoAddress
	 * @return
	 */
	public long update(int cid, String date, String content, 
			String time) {
		db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DATE, date);
		values.put(CONTENT, content);
		values.put(TIME, time);
		String whereClause = CID + " = " + cid;
		long i = db.update(TABLE_NAME, values, whereClause, null);
		close();
		return i;
	}
	
	/**
	 * ɾ��idΪdid���ռ�
	 * @param did
	 * @return
	 */
	public long delete(int cid) {
		db = dbHelper.getWritableDatabase();
		String whereClause = CID + " = " + cid;
		long i = db.delete(TABLE_NAME, whereClause, null);
		close();
		return i;
	}
	
	/**
	 * 
	 * ɾ��n������
	 * @param n
	 */
	public void deleteN(int n) {
		db = dbHelper.getWritableDatabase();//table where Num in�� Num table limit 8��
		String sql = "delete from " + TABLE_NAME + " where cid in(select did from "  + TABLE_NAME + " limit " + n +
				")";
		db.execSQL(sql);
		close();
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
			db.execSQL(CREATE_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL(UPDATE_TABLE);
			onCreate(db);
		}
	}	
}
