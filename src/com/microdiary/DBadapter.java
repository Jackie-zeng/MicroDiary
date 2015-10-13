package com.microdiary;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.Menu;


public class DBadapter extends Activity {
	public static final String KEY_ROWID = "_id";
	public static final String KEY_Date="date";
	public static final String KEY_Year = "year";
	public static final String KEY_Text= "text";
	public static final String KEY_Month = "month";
	private static final String DATABASE_NAME = "diary";
	public static final String KEY_State="ifstate";
	private static final String TAG = "DBadapter";
	private static final String  DATABASE_TABLE = "titles";
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_CREATE ="create table titles (_id integer primary key autoincrement, "+ "ifstate text not null,date text not null, year text not null,month text not null, "+ "text text not null);";
	private final Context context;
	public  DatabaseHelper DBHelper;
	public SQLiteDatabase db;
	public SQLiteDatabase db1;
	public DBadapter(Context ctx)
	{
		this.context = ctx;  
		DBHelper = new DatabaseHelper(context);
	}
	private static class DatabaseHelper extends SQLiteOpenHelper
	{
		DatabaseHelper(Context context)
		{
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
	
	
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(DATABASE_CREATE);
	}
	public void onUpgrade(SQLiteDatabase db, int oldVersion,int newVersion)
	{
		Log.w(TAG, "Upgrading database from version " + oldVersion+ " to "+ newVersion + ", which will destroy all old data");
	}
    }
	
	public DBadapter open() throws SQLException
	{
		db = DBHelper.getWritableDatabase();
		return this;
	}
	
	public void close()
	{
		DBHelper.close();
	}
	public long insertTitle(String riqi, String nianfeng,String yuefen,String zhengwen,String zhuantai)
	{
	
	ContentValues initialValues = new ContentValues();
	initialValues.put(KEY_Date, riqi);
	initialValues.put( KEY_Year, nianfeng);
	initialValues.put( KEY_Month,yuefen);
	initialValues.put(KEY_Text, zhengwen);
	initialValues.put(KEY_State,zhuantai);
    long lg;
	 lg=db.insert(DATABASE_TABLE, null, initialValues);
	
	 return lg;
	}
	public Cursor getAllTitles()
	{  
		Cursor c= db.query(DATABASE_TABLE, new String[] {KEY_ROWID,KEY_State,KEY_Date,KEY_Year,KEY_Month,KEY_Text},null,null,null,null,null);
		return c;
	}
	public Cursor getDateTitle(String date)throws SQLException
	{   
		Cursor c =db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,KEY_State,KEY_Date,KEY_Year,KEY_Month,KEY_Text},KEY_Date + "=?",new String[]{date},null,null,null,null);

		if (c != null) 
		{
			c.moveToFirst();
		}
		
		return c;
	
	}
	public Cursor getYearTitle(String year)throws SQLException
	{  
	
		Cursor c =db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,KEY_State,KEY_Date,KEY_Year,KEY_Month,KEY_Text},KEY_Year + "="+year,null,null,null,null,null);
		if (c != null) 
		{
			c.moveToFirst();
		}
		
		return c;
	
	}
	public Cursor getMonthTitle(String month)throws SQLException
	{
		
		Cursor c =db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,KEY_State,KEY_Date,KEY_Year,KEY_Month,KEY_Text},KEY_Month + "=?",new String[]{month},null,null,null,null);
		if (c != null) 
		{
			c.moveToFirst();
		}
		
		return c;
	
	}
	   public boolean updateTitle(long rowId, String date,

			    String year,String month, String text,String state)

			    {

			    ContentValues args = new ContentValues();
                 args.put(KEY_State,state );
			    args.put(KEY_Date, date);

			    args.put(KEY_Year, year);
			    args.put(KEY_Month, month);

			    args.put(KEY_Text, text);
			    db = DBHelper.getWritableDatabase();
			    boolean bush;
			     bush=db.update(DATABASE_TABLE, args,

			    KEY_ROWID + "=" + rowId, null) > 0;
			  
			    return bush;

			    }
	   
	   public Cursor getConcretTitle(String year,String month,String date)throws SQLException
		{
		   String selection = KEY_Year + "=" + year + " and " + KEY_Month + "=" + month + " and " + KEY_Date + "=" + date ;
		   Cursor cursor = db.query(DATABASE_TABLE , null, selection, null, null, null,  null);
		   return cursor;
		}
	   public Cursor getmyConcretTitle(String year,String month)throws SQLException
		{
		   String selection = KEY_Year + "=" + year + " and " + KEY_Month + "=" + month ;
		  Cursor cursor = db.query(DATABASE_TABLE , null, selection, null, null, null,  null);
		   return cursor;
		}
}
