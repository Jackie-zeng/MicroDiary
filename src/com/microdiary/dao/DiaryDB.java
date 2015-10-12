package com.microdiary.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.microdiary.entity.Diary;

public class DiaryDB {

	public static String DATABASE_NAME = "DIARY_DATABASE"; //���ݿ���
	public static int DATABASE_VERSION = 1;     //�汾��
	
	public final static String TABLE_NAME = "diary_table";   //΢�ռǱ���
   	

	public final static String DID = "did";   
	public final static String DATE = "date"; 
	public final static String CONTENT = "content";
	public final static String PICTURE = "picture";
	public final static String AUDIO = "audio";
	public final static String VIDEO = "video";
	public final static String AUDIOADDRESS = "audioaddress";
	public final static String PHOTOADDRESS = "photoaddress";
	public final static String VIDEOADDRESS = "videoaddress";
	
	private String CREATE_TABLE = "create table " + TABLE_NAME + "(" 
	        + DID + " integer primary key, " 
			+ DATE + " date, "
			+ CONTENT + " text, "
			+ PICTURE + " int, "
			+ AUDIO + " int, "
			+ VIDEO + " int, "
			+ AUDIOADDRESS + " varchar(100), "
			+ VIDEOADDRESS + " varchar(100), "
			+ PHOTOADDRESS + " varchar(100))";
	
	private String UPDATE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
	
	private DBHelper dbHelper = null;
	private SQLiteDatabase db = null;
	
	
	/**
	 * ���캯��
	 * @param context
	 */
	public DiaryDB(Context context) {
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
	 * ��ȡ�ռ��б�
	 * @return
	 */
	public ArrayList<Diary> select() {
		ArrayList<Diary> list = new ArrayList<Diary>();
		db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, DATE + " desc");
		while(cursor.moveToNext()) {
			Diary diary = new Diary();
			diary.setDid(cursor.getInt(cursor.getColumnIndexOrThrow(DID)));
			diary.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DATE)));
			diary.setContent(cursor.getString(cursor.getColumnIndexOrThrow(CONTENT)));
			diary.setPicture(cursor.getInt(cursor.getColumnIndexOrThrow(PICTURE)));
			diary.setAudio(cursor.getInt(cursor.getColumnIndexOrThrow(AUDIO)));
			diary.setVideo(cursor.getInt(cursor.getColumnIndexOrThrow(VIDEO)));
			diary.setAudioAddress(cursor.getString(cursor.getColumnIndexOrThrow(AUDIOADDRESS)));
			diary.setPhotoAddress(cursor.getString(cursor.getColumnIndexOrThrow(PHOTOADDRESS)));
			diary.setVideoAddress(cursor.getString(cursor.getColumnIndexOrThrow(VIDEOADDRESS)));
			list.add(diary);
		}
		close();
		return list;
	}
	
	
	/**
	 * �����ռ�
	 * @return
	 */
	public ArrayList<Diary> search(String key) {
		ArrayList<Diary> list = new ArrayList<Diary>();
		db = dbHelper.getReadableDatabase();
		String selection = "content like '%" + key + "%'";
		Cursor cursor = db.query(TABLE_NAME, new String[]{DATE}, selection, null, null, null, DATE + " desc", "10");
		while(cursor.moveToNext()) {
			Diary diary = new Diary();
			diary.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DATE)));
			list.add(diary);
		}
		close();
		return list;
	}
	
	
	/**
	 * ��ȡ��һ���ռ�
	 * @return
	 */
	public ArrayList<Diary> selectFirst() {
		ArrayList<Diary> list = new ArrayList<Diary>();
		db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, DATE + " desc");
		while(cursor.moveToNext()) {
			Diary diary = new Diary();
			diary.setDid(cursor.getInt(cursor.getColumnIndexOrThrow(DID)));
			diary.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DATE)));
			diary.setContent(cursor.getString(cursor.getColumnIndexOrThrow(CONTENT)));
			diary.setPicture(cursor.getInt(cursor.getColumnIndexOrThrow(PICTURE)));
			diary.setAudio(cursor.getInt(cursor.getColumnIndexOrThrow(AUDIO)));
			diary.setVideo(cursor.getInt(cursor.getColumnIndexOrThrow(VIDEO)));
			diary.setAudioAddress(cursor.getString(cursor.getColumnIndexOrThrow(AUDIOADDRESS)));
			diary.setPhotoAddress(cursor.getString(cursor.getColumnIndexOrThrow(PHOTOADDRESS)));
			diary.setVideoAddress(cursor.getString(cursor.getColumnIndexOrThrow(VIDEOADDRESS)));
			list.add(diary);
			break;
		}
		close();
		return list;
	}
	
	/**
	 * ��ȡָ�����ڵ��ռ��б�
	 * @param from
	 * @param to
	 * @return
	 */
	public ArrayList<Diary> select(String from, String to) {
		ArrayList<Diary> list = new ArrayList<Diary>();
		db = dbHelper.getReadableDatabase();
		String selection = DATE + " between ? and ?";
    	String[] selectionArgs = new String[] {from, to};
    	Cursor cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, DATE + " desc");
    	while(cursor.moveToNext()) {
			Diary diary = new Diary();
			diary.setDid(cursor.getInt(cursor.getColumnIndexOrThrow(DID)));
			diary.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DATE)));
			diary.setContent(cursor.getString(cursor.getColumnIndexOrThrow(CONTENT)));
			diary.setPicture(cursor.getInt(cursor.getColumnIndexOrThrow(PICTURE)));
			diary.setAudio(cursor.getInt(cursor.getColumnIndexOrThrow(AUDIO)));
			diary.setVideo(cursor.getInt(cursor.getColumnIndexOrThrow(VIDEO)));
			diary.setAudioAddress(cursor.getString(cursor.getColumnIndexOrThrow(AUDIOADDRESS)));
			diary.setPhotoAddress(cursor.getString(cursor.getColumnIndexOrThrow(PHOTOADDRESS)));
			diary.setVideoAddress(cursor.getString(cursor.getColumnIndexOrThrow(VIDEOADDRESS)));
			list.add(diary);
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
			int picture, int audio, int video, String audioAddress, String photoAddress, String videoAddress) {
		db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DATE, date);
		values.put(CONTENT, content);
		values.put(PICTURE, picture);
		values.put(AUDIO, audio);
		values.put(VIDEO, video);
		values.put(AUDIOADDRESS, audioAddress);
		values.put(PHOTOADDRESS, photoAddress);
		values.put(VIDEOADDRESS, videoAddress);
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
	public long update(int did, String date, String content, 
			int picture, int audio, int video, String audioAddress, String photoAddress, String videoAddress) {
		db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DATE, date);
		values.put(CONTENT, content);
		values.put(PICTURE, picture);
		values.put(AUDIO, audio);
		values.put(VIDEO, video);
		values.put(AUDIOADDRESS, audioAddress);
		values.put(PHOTOADDRESS, photoAddress);
		values.put(VIDEOADDRESS, videoAddress);
		String whereClause = DID + " = " + did;
		long i = db.update(TABLE_NAME, values, whereClause, null);
		close();
		return i;
	}
	
	/**
	 * ɾ��idΪdid���ռ�
	 * @param did
	 * @return
	 */
	public long delete(int did) {
		db = dbHelper.getWritableDatabase();
		String whereClause = DID + " = " + did;
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
		String sql = "delete from " + TABLE_NAME + " where did in(select did from diary_table limit " + n +
				")";
		db.execSQL(sql);
		//db.delete(table, whereClause, whereArgs)
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
