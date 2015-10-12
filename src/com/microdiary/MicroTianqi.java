package com.microdiary;

/**
 * ΢����ҳ��
 */

import org.ksoap2.serialization.SoapObject;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.microdiary.dao.Data;
import com.microdiary.dao.WeatherData;
import com.microdiary.util.MicroDiaryUtil;
import com.microdiary.util.NetWorkUtil;
import com.microdiary.util.ThreadUtil;
import com.microdiary.util.WebServiceUtil;

public class MicroTianqi {
	
	
	public MainMicroDiary context;
	public View view;              
	public Spinner provinceSpinner;
	public Spinner citySpinner;
	public ImageView todayWhIcon1;
	public ImageView todayWhIcon2;
	public TextView textWeatherToday;
	public ImageView tomorrowWhIcon1;
	public ImageView tomorrowWhIcon2;
	public TextView textWeatherTomorrow;
	public ImageView afterdayWhIcon1;
	public ImageView afterdayWhIcon2;
	public TextView textWeatherAfterday;
	
	public TextView title;
	
	
	boolean isInit = true;
	
	
//	SharedPreferences preferences;
//	SharedPreferences.Editor editor;
	
//	final String MICRO_TIANQI = "MICRO_TIANQI";
	
//	WeatherData weatherData;
	
	
	public MicroTianqi(MainMicroDiary context, View view) {
		this.context = context;
		this.view = view;
		title = (TextView) view.findViewById(R.id.title);
		title.setText("΢����");
		
//		preferences = context.getSharedPreferences(MICRO_TIANQI, context.MODE_PRIVATE);
//		editor = preferences.edit(); 
		 
//		weatherData = new WeatherData(context);
		
		
		/*
		Data.data.setValue(WeatherData.ICON_TODAY1, "0.gif");
		Data.data.setValue(WeatherData.ICON_TODAY2, "0.gif");

		Data.data.setValue(WeatherData.ICON_TOMORROW1, "0.gif");
		Data.data.setValue(WeatherData.ICON_TOMORROW2, "0.gif");
		
		Data.data.setValue(WeatherData.ICON_AFTERDAY1, "0.gif");
		Data.data.setValue(WeatherData.ICON_AFTERDAY2, "0.gif");

		Data.data.setValue(WeatherData.TODAY_TEXT, "today weather");
		
		Data.data.setValue(WeatherData.TOMORROW_TEXT, "tomorrow weather");
		
		Data.data.setValue(WeatherData.AFTERDAY_TEXT,"afterday weather");*/
		
		if(!Data.isOnline) {
			initOutLineState();
			//initOnLineState();
		}
		else {
			initOnLineState();
		}
	}
	
    
	
	public void initOutLineState() {
		/*
		ImageView offlineImage = (ImageView) view.findViewById(R.id.offlineImage);
		RelativeLayout provinceLayout = (RelativeLayout) view.findViewById(R.id.provinceLayout);
		RelativeLayout cityLayout = (RelativeLayout) view.findViewById(R.id.cityLayout);
		LinearLayout weatherLayout = (LinearLayout) view.findViewById(R.id.weatherLayout);
		provinceLayout.setVisibility(View.GONE);
		cityLayout.setVisibility(View.GONE);
		weatherLayout.setVisibility(View.GONE);
		offlineImage.setVisibility(View.VISIBLE);*/
		
		ImageView offlineImage = (ImageView) view.findViewById(R.id.offlineImage);
		RelativeLayout provinceLayout = (RelativeLayout) view.findViewById(R.id.provinceLayout);
		RelativeLayout cityLayout = (RelativeLayout) view.findViewById(R.id.cityLayout);
		LinearLayout weatherLayout = (LinearLayout) view.findViewById(R.id.weatherLayout);
		provinceLayout.setVisibility(View.VISIBLE);
		cityLayout.setVisibility(View.VISIBLE);
		weatherLayout.setVisibility(View.VISIBLE);
		offlineImage.setVisibility(View.GONE);
		
		
		provinceSpinner = (Spinner) view.findViewById(R.id.province);
		provinceSpinner.setPrompt("ѡ��ʡ��");
		citySpinner = (Spinner) view.findViewById(R.id.city);
		citySpinner.setPrompt("ѡ�����");
	    todayWhIcon1 = (ImageView) view.findViewById(R.id.todayWhIcon1);
		todayWhIcon2 = (ImageView) view.findViewById(R.id.todayWhIcon2);
		textWeatherToday = (TextView) view.findViewById(R.id.weatherToday);
		tomorrowWhIcon1 = (ImageView) view.findViewById(R.id.tomorrowWhIcon1);
		tomorrowWhIcon2 = (ImageView) view.findViewById(R.id.tomorrowWhIcon2);
		textWeatherTomorrow = (TextView) view.findViewById(R.id.weatherTomorrow);
		afterdayWhIcon1 = (ImageView) view.findViewById(R.id.afterdayWhIcon1);
		afterdayWhIcon2 = (ImageView) view.findViewById(R.id.afterdayWhIcon2);
		textWeatherAfterday = (TextView) view.findViewById(R.id.weatherAfterday);

		
		showWeather();
		
		// ��ȡ���������ѡ��ʡ�ݡ����е�Spinner���
		provinceSpinner = (Spinner) view.findViewById(R.id.province);
		citySpinner = (Spinner) view.findViewById(R.id.city);
		// ����Զ��Web Service��ȡʡ���б�
		
//		if(Data.provinces.size() == 0)
//			Data.provinces.add("����ʧ��");
		ArrayAdapter<String> provinceAdapter = new ArrayAdapter<String>(context, 
	    		android.R.layout.simple_dropdown_item_1line, Data.provinces);
		/*
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < Data.provinces.size(); i++) {
		    if(i == 0)
		    	sb.append(Data.provinces.get(i));
		    else
		    	sb.append("," + Data.provinces.get(i));
		}
		weatherData.setValue(WeatherData.PROVINCE_LIST, sb.toString());*/
		
		// ʹ��Spinner��ʾʡ���б�
		provinceSpinner.setAdapter(provinceAdapter);
		if(Data.data.getProvinceId() < Data.provinces.size())
			provinceSpinner.setSelection(Data.data.getProvinceId());
		// ��ʡ��Spinner��ѡ����ı�ʱ
		
		
		
		
		provinceSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> source, View parent,
				int position, long id)
			{
				
				if(isInit) {
//					isInit = false;
					ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(MicroTianqi.this.context, 
				    		android.R.layout.simple_dropdown_item_1line, Data.cities);
					// ʹ��Spinner��ʾ�����б�
					citySpinner.setAdapter(cityAdapter);
//					citySpinner.setSelection(Data.data.getCityId());
					if(Data.data.getCityId() < Data.cities.size())
						citySpinner.setSelection(Data.data.getCityId());
				}
				else {
					if(!NetWorkUtil.isConnect(context)) {
						Toast.makeText(context, "��������ʧ��", Toast.LENGTH_SHORT).show();
						return;
					}
					System.out.println("PRO_POS-->" + position);
					Data.data.setProvinceId(position);
					if(ThreadUtil.thread != null)
						ThreadUtil.thread.interrupt();
					ThreadUtil.showProgress(context);
					ThreadUtil.thread = new Thread(
							new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									Data.cities = WebServiceUtil
											.getCityListByProvince(provinceSpinner.getSelectedItem()
												.toString());
									
									StringBuilder sb = new StringBuilder();
									for(int i = 0; i < Data.cities.size(); i++) {
									    if(i == 0)
									    	sb.append(Data.cities.get(i));
									    else
									    	sb.append("," + Data.cities.get(i));
									}
									Data.data.setValue(WeatherData.CITY_LIST, sb.toString());
									
									Data.detail = WebServiceUtil.getWeatherByCity(Data.cities.get(0));
									ThreadUtil.closeProgress();
									MicroTianqi.this.context.handler.sendEmptyMessage(1);
								}
							});
					ThreadUtil.thread.start();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0)
			{
			}
		});
		/*
		// ������Spinner��ѡ����ı�ʱ
		citySpinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> source, View parent,
				int position, long id)
			{
				if(isInit) {
					isInit = false;
					showWeather();
				}
				else {
					if(!NetWorkUtil.isConnect(context)) {
						Toast.makeText(context, "��������ʧ��", Toast.LENGTH_SHORT).show();
						return;
					}
					
					
					System.out.println("CITY_POS-->" + position);
					Data.data.setCityId(position);
					if(ThreadUtil.thread != null)
						ThreadUtil.thread.interrupt();
					ThreadUtil.showProgress(context);
					ThreadUtil.thread = new Thread(
							new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									Data.detail = WebServiceUtil.getWeatherByCity(citySpinner.getSelectedItem().toString());
									ThreadUtil.closeProgress();
									MicroTianqi.this.context.handler.sendEmptyMessage(2);
								}
							});
					ThreadUtil.thread.start();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0)
			{
			}
		});*/
	}
	
	public void initOnLineState() {
		ImageView offlineImage = (ImageView) view.findViewById(R.id.offlineImage);
		RelativeLayout provinceLayout = (RelativeLayout) view.findViewById(R.id.provinceLayout);
		RelativeLayout cityLayout = (RelativeLayout) view.findViewById(R.id.cityLayout);
		LinearLayout weatherLayout = (LinearLayout) view.findViewById(R.id.weatherLayout);
		provinceLayout.setVisibility(View.VISIBLE);
		cityLayout.setVisibility(View.VISIBLE);
		weatherLayout.setVisibility(View.VISIBLE);
		offlineImage.setVisibility(View.GONE);
		
		
		provinceSpinner = (Spinner) view.findViewById(R.id.province);
		citySpinner = (Spinner) view.findViewById(R.id.city);
	    todayWhIcon1 = (ImageView) view.findViewById(R.id.todayWhIcon1);
		todayWhIcon2 = (ImageView) view.findViewById(R.id.todayWhIcon2);
		textWeatherToday = (TextView) view.findViewById(R.id.weatherToday);
		tomorrowWhIcon1 = (ImageView) view.findViewById(R.id.tomorrowWhIcon1);
		tomorrowWhIcon2 = (ImageView) view.findViewById(R.id.tomorrowWhIcon2);
		textWeatherTomorrow = (TextView) view.findViewById(R.id.weatherTomorrow);
		afterdayWhIcon1 = (ImageView) view.findViewById(R.id.afterdayWhIcon1);
		afterdayWhIcon2 = (ImageView) view.findViewById(R.id.afterdayWhIcon2);
		textWeatherAfterday = (TextView) view.findViewById(R.id.weatherAfterday);

		
		
		
		// ��ȡ���������ѡ��ʡ�ݡ����е�Spinner���
		provinceSpinner = (Spinner) view.findViewById(R.id.province);
		citySpinner = (Spinner) view.findViewById(R.id.city);
		// ����Զ��Web Service��ȡʡ���б�
		ArrayAdapter<String> provinceAdapter = new ArrayAdapter<String>(context, 
	    		android.R.layout.simple_dropdown_item_1line, Data.provinces);
		/*
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < Data.provinces.size(); i++) {
		    if(i == 0)
		    	sb.append(Data.provinces.get(i));
		    else
		    	sb.append("," + Data.provinces.get(i));
		}
		weatherData.setValue(WeatherData.PROVINCE_LIST, sb.toString());*/
		
		// ʹ��Spinner��ʾʡ���б�
		provinceSpinner.setAdapter(provinceAdapter);
		provinceSpinner.setSelection(Data.data.getProvinceId());
		// ��ʡ��Spinner��ѡ����ı�ʱ
		provinceSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> source, View parent,
				int position, long id)
			{
				
				if(isInit) {
//					isInit = false;
					
					ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(MicroTianqi.this.context, 
				    		android.R.layout.simple_dropdown_item_1line, Data.cities);
					// ʹ��Spinner��ʾ�����б�
					citySpinner.setAdapter(cityAdapter);
					citySpinner.setSelection(Data.data.getCityId());
				}
				else {
					if(!NetWorkUtil.isConnect(context)) {
						Toast.makeText(context, "��������ʧ��", Toast.LENGTH_SHORT).show();
						return;
					}
					System.out.println("PRO_POS-->" + position);
					Data.data.setProvinceId(position);
					if(ThreadUtil.thread != null)
						ThreadUtil.thread.interrupt();
					ThreadUtil.showProgress(context);
					ThreadUtil.thread = new Thread(
							new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									Data.cities = WebServiceUtil
											.getCityListByProvince(provinceSpinner.getSelectedItem()
												.toString());
									
									StringBuilder sb = new StringBuilder();
									for(int i = 0; i < Data.cities.size(); i++) {
									    if(i == 0)
									    	sb.append(Data.cities.get(i));
									    else
									    	sb.append("," + Data.cities.get(i));
									}
									Data.data.setValue(WeatherData.CITY_LIST, sb.toString());
									
									Data.detail = WebServiceUtil.getWeatherByCity(Data.cities.get(0));
									ThreadUtil.closeProgress();
									MicroTianqi.this.context.handler.sendEmptyMessage(1);
								}
							});
					ThreadUtil.thread.start();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0)
			{
			}
		});
		// ������Spinner��ѡ����ı�ʱ
		citySpinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> source, View parent,
				int position, long id)
			{
				if(isInit) {
					isInit = false;
					showWeather();
				}
				else {
					if(!NetWorkUtil.isConnect(context)) {
						Toast.makeText(context, "��������ʧ��", Toast.LENGTH_SHORT).show();
						return;
					}
					
					
					System.out.println("CITY_POS-->" + position);
					Data.data.setCityId(position);
					if(ThreadUtil.thread != null)
						ThreadUtil.thread.interrupt();
					ThreadUtil.showProgress(context);
					ThreadUtil.thread = new Thread(
							new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									Data.detail = WebServiceUtil.getWeatherByCity(citySpinner.getSelectedItem().toString());
									ThreadUtil.closeProgress();
									MicroTianqi.this.context.handler.sendEmptyMessage(2);
								}
							});
					ThreadUtil.thread.start();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0)
			{
			}
		});
	}
	
	
	public void fillCity() {
		ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(MicroTianqi.this.context, 
	    		android.R.layout.simple_dropdown_item_1line, Data.cities);
		// ʹ��Spinner��ʾ�����б�
		citySpinner.setAdapter(cityAdapter);
		citySpinner.setSelection(0);
	}

	
	
	public void showWeather()
	{
		
		String weatherToday = null;
		String weatherTomorrow = null;
		String weatherAfterday = null;
		String weatherCurrent = null;
		int iconToday[] = new int[2];
		int iconTomorrow[] = new int[2];
		int iconAfterday[] = new int[2];
		
		System.out.println("show weather");
		
		if(NetWorkUtil.isConnect(context)) {
			
			System.out.println("online weather");
			
			try {
				// ��ȡԶ��Web Service���صĶ���
				// ��ȡ����ʵ��
				weatherCurrent = Data.detail.getProperty(4).toString();
				// ����������������
				
				String date = Data.detail.getProperty(7).toString();
				weatherToday = "���죺" + date.split(" ")[0];
				weatherToday = weatherToday + "\n������" + date.split(" ")[1];
				weatherToday = weatherToday + "\n���£�"
					+ Data.detail.getProperty(8).toString();
				weatherToday = weatherToday + "\n";
				iconToday[0] = WebServiceUtil.parseIcon(Data.detail.getProperty(10).toString());
				iconToday[1] = WebServiceUtil.parseIcon(Data.detail.getProperty(11).toString());
				Data.data.setValue(WeatherData.ICON_TODAY1, Data.detail.getProperty(10).toString());
				Data.data.setValue(WeatherData.ICON_TODAY2, Data.detail.getProperty(11).toString());
				// ����������������
				date = Data.detail.getProperty(12).toString();
				weatherTomorrow = "���죺" + date.split(" ")[0];
				weatherTomorrow = weatherTomorrow + "\n������" + date.split(" ")[1];
				weatherTomorrow = weatherTomorrow + "\n���£�"
					+ Data.detail.getProperty(13).toString();
				weatherTomorrow = weatherTomorrow + "\n";
				iconTomorrow[0] = WebServiceUtil.parseIcon(Data.detail.getProperty(15).toString());
				iconTomorrow[1] = WebServiceUtil.parseIcon(Data.detail.getProperty(16).toString());
				Data.data.setValue(WeatherData.ICON_TOMORROW1, Data.detail.getProperty(15).toString());
				Data.data.setValue(WeatherData.ICON_TOMORROW2, Data.detail.getProperty(16).toString());
				// ����������������
				date = Data.detail.getProperty(17).toString();
				weatherAfterday = "���죺" + date.split(" ")[0];
				weatherAfterday = weatherAfterday + "\n������" + date.split(" ")[1];
				weatherAfterday = weatherAfterday + "\n���£�"
					+ Data.detail.getProperty(18).toString();
				weatherAfterday = weatherAfterday + "\n";
				iconAfterday[0] = WebServiceUtil.parseIcon(Data.detail.getProperty(20).toString());
				iconAfterday[1] = WebServiceUtil.parseIcon(Data.detail.getProperty(21).toString());
				Data.data.setValue(WeatherData.ICON_AFTERDAY1, Data.detail.getProperty(20).toString());
				Data.data.setValue(WeatherData.ICON_AFTERDAY2, Data.detail.getProperty(21).toString());
				// ���µ��������ʵ��
//				textWeatherCurrent.setText(weatherCurrent);
				// ������ʾ����������ͼ����ı���
				textWeatherToday.setText(weatherToday);
				Data.data.setValue(WeatherData.TODAY_TEXT, weatherToday);
				todayWhIcon1.setImageResource(iconToday[0]);
				todayWhIcon2.setImageResource(iconToday[1]);
				// ������ʾ����������ͼ����ı���
				textWeatherTomorrow.setText(weatherTomorrow);
				Data.data.setValue(WeatherData.TOMORROW_TEXT, weatherTomorrow);
				tomorrowWhIcon1.setImageResource(iconTomorrow[0]);
				tomorrowWhIcon2.setImageResource(iconTomorrow[1]);
				// ������ʾ����������ͼ����ı���
				textWeatherAfterday.setText(weatherAfterday);
				Data.data.setValue(WeatherData.AFTERDAY_TEXT, weatherAfterday);
				afterdayWhIcon1.setImageResource(iconAfterday[0]);
				afterdayWhIcon2.setImageResource(iconAfterday[1]);
				
				
				StringBuilder sb = new StringBuilder("");
				for(int i = 7; i <= 11; i++) {
					String str = Data.detail.getProperty(i).toString();
					sb.append(str + "\n");
				}
//				textWeatherCurrent.setText(sb.toString());
			}catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
		}
		else {
			
			System.out.println("outline weather");
			iconToday[0] =  WebServiceUtil.parseIcon(Data.data.getValue(WeatherData.ICON_TODAY1));
			iconToday[1] = WebServiceUtil.parseIcon(Data.data.getValue(WeatherData.ICON_TODAY2));
			
			iconTomorrow[0] = WebServiceUtil.parseIcon(Data.data.getValue(WeatherData.ICON_TOMORROW1));
			iconTomorrow[1] = WebServiceUtil.parseIcon(Data.data.getValue(WeatherData.ICON_TOMORROW2));
			
			iconAfterday[0] = WebServiceUtil.parseIcon(Data.data.getValue(WeatherData.ICON_AFTERDAY1));
			iconAfterday[1] = WebServiceUtil.parseIcon(Data.data.getValue(WeatherData.ICON_AFTERDAY2));
			
			textWeatherToday.setText(Data.data.getValue(WeatherData.TODAY_TEXT));
			todayWhIcon1.setImageResource(iconToday[0]);
			todayWhIcon2.setImageResource(iconToday[1]);
			// ������ʾ����������ͼ����ı���
			textWeatherTomorrow.setText(Data.data.getValue(WeatherData.TOMORROW_TEXT));
			tomorrowWhIcon1.setImageResource(iconTomorrow[0]);
			tomorrowWhIcon2.setImageResource(iconTomorrow[1]);
			// ������ʾ����������ͼ����ı���
			textWeatherAfterday.setText(Data.data.getValue(WeatherData.AFTERDAY_TEXT));
			afterdayWhIcon1.setImageResource(iconAfterday[0]);
			afterdayWhIcon2.setImageResource(iconAfterday[1]);
		}
	}
	
	public void showToday() {
		SoapObject detail = WebServiceUtil.getWeatherByCity("��ͷ");
		StringBuilder sb = new StringBuilder("");
		for(int i = 1; i < 11; i++) {
			String str = detail.getProperty(i).toString();
			sb.append(str + "\n");
		}
//		textWeatherCurrent.setText(sb.toString());
	} 
	
	

	
}
