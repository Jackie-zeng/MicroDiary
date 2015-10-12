package com.microdiary;

/**
 * 微闹钟页面
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

import com.microdiary.dao.ClockDB;
import com.microdiary.dao.WeatherData;
import com.microdiary.entity.Clock;
import com.microdiary.util.MicroDiaryUtil;

public class MicroNaozhong {
	
	
	public MainMicroDiary context;
	public View view;              
	
	TextView title;
	
	public ImageButton write;    //右上角按扭

	public ListView moneyList;  //收入(支出)列表
	
	
	EditText dateEdit;    //收入(支出)对话框中的日期编辑框
	EditText timeEdit;    //收入(支出)对话框中的日期编辑框
	EditText remarkEdit;    //备注
	Button opButton;   //右下角的操作按扭
	
	
	SelectDialog moneyDialog = null;   //收入(支出)对话框
	
	ClockDB db;
	
	/*
	 * 弹窗类型
	 */
	public final int WRITE_DIALOG = 1;    //新建 
	public final int EDIT_DIALOG = 2;     //编辑
	public final int REMOVE_DIALOG = 3;   //删除
	public final int READ_DIALOG = 4;     //查看

	public ArrayList<Clock> clocks;   //收入列表
//	public ArrayList<Money> extendMoneys;  //支出列表
	
//	public int currList = 1; //1收入列表 2支出列表
	public Clock currentClock;  //当前数据
	public int currentIndex;   //当前数据在列表中的id
	
	/**
	 * 构造函数
	 * @param context
	 * @param view
	 */
	public MicroNaozhong(MainMicroDiary context, View view) {
		this.context = context;
		this.view = view;
		title = (TextView) view.findViewById(R.id.title);
		title.setText("微闹钟");
		
		db = new ClockDB(context);
		
		write = (ImageButton) view.findViewById(R.id.write);
		moneyList = (ListView) view.findViewById(R.id.moneyList);
		

		
		write.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showMoneyDialog(WRITE_DIALOG);
			}
		});
		
		
		
		moneyList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				currentClock = clocks.get(arg2);
				showMoneyDialog(READ_DIALOG);
			}
		});
		

		moneyList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				currentClock = clocks.get(arg2);
				showEditOrRemoveDialog();
				
				return false;
			}
		});

		clocks = db.select();
		refresh1();
		
	}
	
	/**
	 * 刷新收入列表
	 */
	public void refresh1() {
		
		ClockAdapter adapter = new ClockAdapter(context, clocks);
		moneyList.setAdapter(adapter);
		moneyList.setSelection(currentIndex);
	}
	
	

	
	/**
	 * 编辑或删除的对话框
	 */
	public void showEditOrRemoveDialog() {
		final SelectDialog dialog = new SelectDialog(context, R.style.dialog, R.layout.edit_remove_diary_dialog);
		dialog.show();
		Button editDiary = (Button) dialog.findViewById(R.id.editDiary);
		Button removeDiary = (Button) dialog.findViewById(R.id.removeDiary);
		editDiary.setText("修改数据");
		removeDiary.setText("删除数据");
		editDiary.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				showMoneyDialog(EDIT_DIALOG);
			}
		});
		removeDiary.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				showMoneyDialog(REMOVE_DIALOG);
			}
		});
	}
	
	/**
	 * 收入(支出)对话框
	 * @param tag
	 */
	public void showMoneyDialog(final int tag) {
		moneyDialog = new SelectDialog(context, R.style.dialog, R.layout.clock_dialog);
		moneyDialog.show();
		
		dateEdit = (EditText) moneyDialog.findViewById(R.id.date);
		timeEdit = (EditText) moneyDialog.findViewById(R.id.time);
		remarkEdit = (EditText) moneyDialog.findViewById(R.id.remark_edit);
		opButton = (Button) moneyDialog.findViewById(R.id.ok);
		
//		dateEdit.setText(MicroDiaryUtil.getCurrentDate());
		dateEdit.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction() == MotionEvent.ACTION_DOWN) {
					
					SelectDialog dateDialog = new SelectDialog(context, R.style.dialog,
							R.layout.datepicker_dialog);
					dateDialog.show();
					Calendar c = Calendar.getInstance();
					int year = c.get(Calendar.YEAR);
					int month = c.get(Calendar.MONTH);
					int day = c.get(Calendar.DAY_OF_MONTH);
					String[] dates = dateEdit.getText().toString().split("-");
					year = Integer.parseInt(dates[0]);
					month = Integer.parseInt(dates[1]);
					day = Integer.parseInt(dates[2]);
					month--;
					
					DatePicker datePicker = (DatePicker) dateDialog.findViewById(R.id.datePicker);
					
					datePicker.init(year, month, day, new OnDateChangedListener() {
						
						@Override
						public void onDateChanged(DatePicker view, int year, int monthOfYear,
								int dayOfMonth) {
							// TODO Auto-generated method stub
							monthOfYear++;
							dateEdit.setText(year + "-" + monthOfYear / 10 + monthOfYear % 10 +
									"-" + dayOfMonth / 10 + dayOfMonth % 10);
						}
					});
				}
				return true;
			}
		});
		
		

		timeEdit.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction() == MotionEvent.ACTION_DOWN) {
					
					SelectDialog timeDialog = new SelectDialog(context, R.style.dialog,
							R.layout.timepicker_dialog);
					timeDialog.show();
					Calendar c = Calendar.getInstance();
					int hour = c.get(Calendar.HOUR_OF_DAY);
					int minute = c.get(Calendar.MINUTE);
//					int day = c.get(Calendar.DAY_OF_MONTH);
					String[] dates = timeEdit.getText().toString().split(":");
					hour = Integer.parseInt(dates[0]);
					minute = Integer.parseInt(dates[1]);
//					day = Integer.parseInt(dates[2]);
//					month--;
					
					TimePicker timePicker  = (TimePicker) timeDialog.findViewById(R.id.timePicker);
					timePicker.setIs24HourView(true);
					
					timePicker.setCurrentHour(hour);
					timePicker.setCurrentMinute(minute);
					
					timePicker.setOnTimeChangedListener(new OnTimeChangedListener() {
						
						@Override
						public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
							// TODO Auto-generated method stub
							timeEdit.setText( hourOfDay / 10 + "" + hourOfDay % 10 +
									":" + minute / 10 + minute % 10);
						}
					});
				}
				return true;
			}
		});
		
		
		if(tag == WRITE_DIALOG) {
			writeDialog();
		}
		else if(tag == EDIT_DIALOG) {
			editDialog();
		}
		else if(tag == REMOVE_DIALOG) {
			removeDialog();
		}
		else if(tag == READ_DIALOG) {
			readDialog();
		}
	}

	/**
	 * 查看收入(支出)
	 */
	private void readDialog() {
		// TODO Auto-generated method stub
		dateEdit.setText(currentClock.getDate());
	    timeEdit.setText(currentClock.getTime());
	    remarkEdit.setText(currentClock.getContent());
	    dateEdit.setEnabled(false);
	    timeEdit.setEnabled(false);
	    remarkEdit.setEnabled(false);
	    
	    opButton.setText("确定");
	    
	    opButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				moneyDialog.dismiss();
			}
		});
	}

	
	/**
	 * 删除收入(支出)
	 */
	private void removeDialog() {
		// TODO Auto-generated method stub
		dateEdit.setText(currentClock.getDate());
	    timeEdit.setText(currentClock.getTime());
	    remarkEdit.setText(currentClock.getContent());
	    dateEdit.setEnabled(false);
	    timeEdit.setEnabled(false);
	    remarkEdit.setEnabled(false);
	    
	    opButton.setText("删除");
	    
	    opButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				db.delete(currentClock.getCid());
				Toast.makeText(context, "删除成功", 3000).show();
				clocks.remove(currentIndex);
				if(currentIndex != 0)
					currentIndex--;
				refresh1();
				moneyDialog.dismiss();
			}
		});
	}

	/**
	 * 修改收入(支出)
	 */
	private void editDialog() {
		// TODO Auto-generated method stub
		dateEdit.setText(currentClock.getDate());
	    timeEdit.setText(currentClock.getTime());
	    remarkEdit.setText(currentClock.getContent());
	    opButton.setText("修改");
	    
	    opButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String date = dateEdit.getText().toString();
			    String time = timeEdit.getText().toString();
			    String content = remarkEdit.getText().toString();
			    
			    if(isPast(date + " " + time)) {
			    	Toast.makeText(context, "不能计划过去的事情", Toast.LENGTH_SHORT).show();
			    	return;
			    }
			    
			    if(content.equals("")) {
			    	Toast.makeText(context, "内容不能为空", Toast.LENGTH_SHORT).show();
			    	return;
			    }
			    
			    db.update(currentClock.getCid(), date, content, time);
			    Toast.makeText(context, "修改成功，系统将会在 " + date + " " + time + " 提醒您", Toast.LENGTH_LONG).show();
			    currentClock.setDate(date);
			    currentClock.setTime(time);
			    currentClock.setContent(content);
			    openPushPassage(context, content, date + " " + time, currentClock.getCid());
			    clocks.set(currentIndex, currentClock);
			    
			    moneyDialog.dismiss();
			    refresh1();
			    
			}
		});
	}

	/**
	 * 新建收入(支出)
	 */
	private void writeDialog() {
		// TODO Auto-generated method stub
	    dateEdit.setText(MicroDiaryUtil.getCurrentDate());
	    timeEdit.setText(MicroDiaryUtil.getCurrentTime());
	    currentClock = new Clock();
	    opButton.setText("完成");
	    
	    opButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String date = dateEdit.getText().toString();
			    String time = timeEdit.getText().toString();
			    String content = remarkEdit.getText().toString();
			    
			    if(isPast(date + " " + time)) {
			    	Toast.makeText(context, "不能计划过去的事情", Toast.LENGTH_SHORT).show();
			    	return;
			    }
			    
			    if(content.equals("")) {
			    	Toast.makeText(context, "内容不能为空", Toast.LENGTH_SHORT).show();
			    	return;
			    }
			    
			    
			    
			    db.insert(date, content, time);
			    
			    currentClock = db.selectFirst().get(0);
			    
			    clocks.add(0, currentClock);
			    
			    Toast.makeText(context, "添加成功，系统将会在 " + date + " " + time + " 提醒您", Toast.LENGTH_LONG).show();
			    openPushPassage(context, content, date + " " + time, currentClock.getCid());
			    
			    moneyDialog.dismiss();
			    refresh1();
			    
			}
		});
	    
	}
	
	public boolean isPast(String time) {
		Date date=null;  
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm");  
        try {
			date = formatter.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			date = new Date();
			e.printStackTrace();
		}
        long during = date.getTime() - System.currentTimeMillis();
//        System.out.println("during:" + during + " start:" + date.getTime() + " end:" + System.currentTimeMillis());
        if(during <= 0)
        	return true;
        else
        	return false;
	}
	
	/**
	 * 打开闹钟
	 * pushtime为推送周期(即每隔多长时间推送一次)
	 * @param context
	 * @param pushTime
	 */
	public static void openPushPassage(Context context, String content, String time, int tag) {
		closePushPassage(context, tag);
		
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTimeInMillis(System.currentTimeMillis());
    	
    	Date date=null;  
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm");  
        try {
			date = formatter.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			date = new Date();
			e.printStackTrace();
		}
        long during = date.getTime() - System.currentTimeMillis();
//        System.out.println("during:" + during + " start:" + date.getTime() + " end:" + System.currentTimeMillis());
		
//		Receiver receiver = new Receiver(content);
		Intent intent = new Intent(context, Receiver.class);
//		Bundle bundle = new Bundle();
//		bundle.putString("nz_content", content);
//		bundle.putInt("nz_tag", tag);
//		intent.putExtras(bundle);
//		intent.putExtra("nz_content", content);
//		intent.putExtra("nz_tag", tag);
    	WeatherData wd = new WeatherData(context);
    	wd.setValue("nz_content", content);
    	wd.setValue("nz_tag", tag+"");
		
		PendingIntent sender = PendingIntent.getBroadcast(context, tag, intent, tag);
    	AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
//    	alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, startTime, repeatTime, sender);
    	alarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime(),sender);
    	
    }
	
	/**
	 * 关闭闹钟
	 * @param context
	 */
	public static void closePushPassage(Context context, int tag) {
		Intent intent = new Intent(context, Receiver.class);
    	PendingIntent sender = PendingIntent.getBroadcast(context, tag, intent, tag);
    	AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
    	alarmManager.cancel(sender);
	}
	
	
}
	

