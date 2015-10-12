package com.microdiary.dao;

import android.content.Context;
import android.content.SharedPreferences;

public class WeatherData {

	private Context context;
	SharedPreferences preferences;
	SharedPreferences.Editor editor;
	
	
	public final static String PROVINCE = "PROVINCE";
	public final static String CITY = "CITY";
	public final static String ICON_TODAY1 = "ICON_TODAY1";
	public final static String ICON_TODAY2 = "ICON_TODAY2";
	public final static String ICON_TOMORROW1 = "ICON_TOMORROW1";
	public final static String ICON_TOMORROW2 = "ICON_TOMORROW2";
	public final static String ICON_AFTERDAY1 = "ICON_AFTERDAY1";
	public final static String ICON_AFTERDAY2 = "ICON_AFTERDAY2";
	public final static String TODAY_TEXT = "TADAY_TEXT";
	public final static String TOMORROW_TEXT = "TOMORROW_TEXT";
	public final static String AFTERDAY_TEXT = "AFTERDAY_TEXT";
	public final static String PROVINCE_LIST = "PROVINCE_LIST";
	public final static String CITY_LIST = "CITY_LIST";


	
	public WeatherData(Context context) {
		this.context = context;
		preferences = context.getSharedPreferences("weather_data", context.MODE_PRIVATE);
		editor = preferences.edit();
	}
	
	public int getProvinceId() {
		
		return preferences.getInt(PROVINCE, 21);
	}
	public void setProvinceId(int id) {
		editor.putInt(PROVINCE, id);
		editor.commit();
	}
	
	public int getCityId() {
		return preferences.getInt(CITY, 19);
	}
	public void setCityId(int id) {
		editor.putInt(CITY, id);
		editor.commit();
	}
	
	
	public void setValue(String key, String value) {
		editor.putString(key, value);
		editor.commit();
	} 
	
	public String getValue(String key) {
		return preferences.getString(key, "");
	}
	
	
	
	
	
}
