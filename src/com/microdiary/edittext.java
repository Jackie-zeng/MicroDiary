package com.microdiary;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.graphics.Color;  

import android.database.Cursor;

import android.R.drawable;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.AlertDialog.Builder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class edittext extends Activity{
	private Button b1;
	private Button b2;
	private EditText myedittext;
    private TextView textview1;
    private TextView textview2;
    private String str1;
    private String str2;
    private String str3;
    private String str4;
    private String str5;
    private Intent intent;
   

    private Bundle b;
	@Override
    public  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.edittext);
//        addShortCut();
        intent=getIntent();
        b=intent.getExtras();
        str1=b.getString("date");
        str2=b.getString("month");
        str3=b.getString("judge");
        str4=b.getString("state");
        str5=b.getString("text");
        textview1=(TextView)findViewById(R.id.month);
        textview2=(TextView)findViewById(R.id.date);
        textview1.setText(str2+"月");
        textview2.setText(str1+"日");
        b1=(Button)findViewById(R.id.butt);
        b2=(Button)findViewById(R.id.unready);
        myedittext=(EditText)findViewById(R.id.bianji);   
        myedittext.setText(str5);
        Log.i("edittext","bianjikuang");
        
        if(str4.equals("complate")) {
        	b2.setText("已完成");
        	b2.setBackgroundResource(R.drawable.btn_style_alert_dialog_button_pressed);
        }
        else {
        	b2.setText("未完成");
        	 b2.setBackgroundResource(R.drawable.btn_style_alert_dialog_button_normal);
        }
     
        b2.setOnClickListener(new OnClickListener() {
        	
			@Override
			public void onClick(View v) {
				
				if(b2.getText().equals("未完成"))
						{
					         b2.setText("已完成");
					          b2.setBackgroundResource(R.drawable.btn_style_alert_dialog_button_pressed);
					          str4="complate";
						}
				else
				{
			         b2.setText("未完成");
			         b2.setBackgroundResource(R.drawable.btn_style_alert_dialog_button_normal);
			          str4="uncomplate";
				}  
			}
        });
        Log.i(str4,"biaoji");
      
        b1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				  if(myedittext.getText().toString().equals(""))
					 {
						 
					 }
					 else 
						 {
						      str5=myedittext.getText().toString();
						      str3="true";
						 }
				
				 
				 b.putString("month", str2);
				 b.putString("date", str1);
				 b.putString("judge",str3);
				 b.putString("state",str4);
				 b.putString("text", str5);
				 intent.putExtras(b);
			     edittext.this.setResult(RESULT_OK, intent);
			   
			    edittext.this.finish();
			}
		});
          
	}
	   @Override
       public boolean onKeyDown(int keyCode,KeyEvent event)
       {
     	  if(keyCode==KeyEvent.KEYCODE_BACK)
     	  {
     		  if(myedittext.getText().toString().equals(""))
     		 {
     			 
     		 }
     		 else 
     			 {
     			      str5=myedittext.getText().toString();
     			      str3="true";
     			 }
     		 b.putString("month", str2);
			 b.putString("date", str1);
			 b.putString("judge",str3);
			 b.putString("state",str4);
			 b.putString("text", str5);
			 intent.putExtras(b);
			 edittext.this.setResult(0, intent);
		     edittext.this.finish();
     	  }
     	  return super.onKeyDown(keyCode,event);
       }

	   public void addShortCut(){
	        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
	        // 设置属性
	        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, getResources().getString(R.string.app_name));
	        ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(edittext.this, R.drawable.btn_style_alert_dialog_button_pressed);
	        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON,iconRes);
	 
	        // 是否允许重复创建
	        shortcut.putExtra("duplicate", false);
	        Intent intent = new Intent(Intent.ACTION_MAIN);
	        intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
	        intent.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
	        intent.addCategory(Intent.CATEGORY_LAUNCHER);
	        intent.setClass(edittext.this, MainMicroDiary.class);
	        // 设置启动程序
	        System.out.println("createIcon");
	        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
	        edittext.this.sendBroadcast(shortcut);
	    }
	    
  }
