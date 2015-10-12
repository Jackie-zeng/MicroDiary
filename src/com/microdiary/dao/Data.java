package com.microdiary.dao;

import java.util.ArrayList;
import java.util.List;

import org.ksoap2.serialization.SoapObject;

import com.microdiary.entity.Money;

public class Data {

	public static WeatherData data;
	public static List<String> provinces;
	public static List<String> cities;
	public static SoapObject detail;
	public static boolean isOnline;
	
	
	public static MoneyDB db;
	public static ArrayList<Money> incomeMoneys;
	public static ArrayList<Money> extendMoneys;

}
