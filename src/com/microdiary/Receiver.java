package com.microdiary;

/**
 * ��Ϣ����
 */

import com.microdiary.dao.WeatherData;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class Receiver extends BroadcastReceiver {
	
	    

	String content = "";
	int tag = 0;
	
	
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			
			/*
			try {
//				Bundle bundle = intent.getExtras();//intent.getExtras();
				content = intent.getStringExtra("nz_content");//bundle.getString("nz_content");
				tag = intent.getIntExtra("nz_tag", 0);//bundle.getInt("nz_tag");
				System.out.println("receiver content:" + content + " tag:" + tag);
			}catch (Exception e) {
				content = "";
				tag = 0;
				// TODO: handle exception
			}*/
			
			WeatherData wd = new WeatherData(context);
//	    	wd.setValue("nz_content", content);
//	    	wd.setValue("nz_tag", tag+"");
			content = wd.getValue("nz_content");
			tag = Integer.parseInt( wd.getValue("nz_tag"));
			push(context);
		}
		
		
		
		/**
		 * ����
		 * ������context,����Ƶ���б�
		 * @param context
		 * @param channels
		 * @param camids
		 */
		public void push(Context context) {
			Intent intent = new Intent(); //������ͺ������ĸ�Activity������������
			NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
		    Notification barMsg = new Notification();
		    barMsg.icon = R.drawable.rj;
		    barMsg.tickerText = "΢��������";
		    barMsg.defaults = Notification.DEFAULT_SOUND;
		    PendingIntent pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		    
		    barMsg.setLatestEventInfo(context, "΢�ռ�", content, pi);
		    barMsg.flags = Notification.FLAG_AUTO_CANCEL;
		    notificationManager.notify(tag, barMsg);
		}
}
