package com.microdiary;

/**
 * 微计划页面
 */

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MicroJihua implements android.widget.AdapterView.OnItemClickListener{
	
	
	public MainMicroDiary context;
	public View view;              
	
	public TextView title;
	
	
	public Cursor b;
	DBadapter db;
    public int year;
    public int month;
    public int[] array;
    public int size;
    private SimpleAdapter adapter;
    public listviewitem item;
    public  List<Map<String,Object>> list;
	private ListView listview;
	public  Intent intent;
	public View layoutView;
    public ImageButton buttonr;
    public ImageButton buttonl;
//	public View view;              
//	public TextView title;
	public ImageView imag;
	public TextView textviewp;
	public TextView textviewm;
	public TextView textviewd;
	public TextView textviewy;
	public int []qrray; 
	public int[]weekarray={0, 3, 2, 5, 0, 3, 5, 1, 4, 6, 2, 4};
	
	public MicroJihua(MainMicroDiary context, View view) {
		this.context = context;
		this.view = view;
		title = (TextView) view.findViewById(R.id.title);
		title.setText("微计划");
		
		qrray =new int[32];
		for(int i=0;i<=30;i++)
		{
			qrray[i]=0;
		}

		  
//		 setContentView(R.layout.database);	
		title = (TextView)view.findViewById(R.id.title);
	     buttonr=(ImageButton)view.findViewById(R.id.right);
	     buttonl=(ImageButton)view.findViewById(R.id.left);
		  textviewd=(TextView)view.findViewById(R.id.date);
		  textviewm=(TextView)view.findViewById(R.id.month);
		  textviewp=(TextView)view.findViewById(R.id.microplan);
	      listview=(ListView)view.findViewById(R.id.myListview);
	      textviewy=(TextView)view.findViewById(R.id.year);
		 db = new DBadapter(context);
		  item=new listviewitem();
	      db.open();
	     
	      Calendar c = Calendar.getInstance();  
		  year = c.get(Calendar.YEAR) ;
			month = c.get(Calendar.MONTH)+1;
		  build(year,month);
	     list=new ArrayList<Map<String,Object>>();
		
	     initialize();
	      getwhat();
	 
	    adapter=new SimpleAdapter(context,list,R.layout.listview,new String[]{"image","Date","day","text"},new int[]{R.id.imageview,R.id.date,R.id.day,R.id.text});
	    layoutView = LayoutInflater.from(context).inflate(R.layout.listview, null);
	    imag=(ImageView)layoutView.findViewById(R.id.imageview);
	    listview.setAdapter(adapter);
	    
	  listview.setOnItemClickListener(this);
	  buttonl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				month=month-1;
				if(month==0)
				{
					month=12;
					year=year-1;
				}
				db.open();
				build(year,month);
				restart();
				 
			}
		});
	  buttonr.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				month=month+1;
				if(month==13)
				{
					month=1;
					year++;
				}
				db.open();
				build(year,month);
				restart();
				Log.i("right","youbian");
				
			}
		});

	   
	 }
		public  void build(int year1,int month1)
		{
			item.year=year1;
			item.month=month1;
			textviewm.setText(String.valueOf(month1)+"月");
			textviewy.setText(String.valueOf(year1)+"年");
			array=item.Compute(item.year, item.month);
			size=item.size;
		}
       public void restart()
       {
    	   SimpleAdapter adapter;
    	    list.clear();
    	    for(int i=0;i<=30;i++)
    	    {
    	    	qrray[i]=0;
    	    }
			initialize();
			getwhat();
			 adapter=new SimpleAdapter(context,list,R.layout.listview,new String[]{"image","Date","day","text"},new int[]{R.id.imageview,R.id.date,R.id.day,R.id.text});
			 listview.setAdapter(adapter);
			 Log.i("chongxing","dianji");
       }


	
		private  void  getwhat()
		{
			
		    Cursor c;
		  c=db.getmyConcretTitle(String.valueOf(item.year), String.valueOf(item.month));
		  if(c.moveToFirst())
		  {
			  do
			  {
				  Map<String,Object> map=new HashMap<String,Object>();
				  int nameColumn=c.getColumnIndex(db.KEY_Date);
				  String str=c.getString(nameColumn);
				  map.put("Date",str);
		    		 map.put("day", str);
				  int nameColumn2=c.getColumnIndex(db.KEY_Text);
		    		 map.put("text",c.getString(nameColumn2));
		    		  int nameColumn3=c.getColumnIndex(db.KEY_State);
		    		 if(c.getString(nameColumn3).toString().equals("complate"))
		    		 {
		    		     map.put("image",R.drawable.ok);
		    		     qrray[Integer.parseInt(str)-1]=1;
		    		 }
		    		 else 
		    			 {
		    			    map.put("image",R.drawable.page);
		    			    qrray[Integer.parseInt(str)-1]=0;
		    			 }
		    		 list.set(Integer.parseInt(str)-1, map);
		    		 
			  }while(c.moveToNext());
		  }
			}
		private  void initialize()
		{
		    String str;
			for(int i=0;i<size;i++)
			{
				Map<String,Object> map=new HashMap<String,Object>();
				map.put("image",R.drawable.page);
				map.put("Date",String.valueOf(array[i]));
				switch(weekday(year,month,array[i]))
				{
				case 0:str="日"; break;
				case 1:str="一"; break;
				case 2:str="二";break;
				case 3:str="三";break;
				case 4:str="四";break;
				case 5:str="五";break;
				case 6:str="六";break;
				default:str="";break;
				}
				map.put("day",str);
				map.put("text", null);
				list.add(map);
			}
			
		}
		 public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id)
		 {
			// addShortCut();
			 intent = new Intent(context, edittext.class); 
			 Bundle b=new Bundle();
			 String str1=String.valueOf(month);
			 String str2=String.valueOf(position+1);
			 String str3="false";
			 Cursor c;
			 c=db.getConcretTitle(String.valueOf(item.year), String.valueOf(item.month),str2);
			 if(c.moveToFirst())
			 {
				
					 int nameColumn=c.getColumnIndex(db.KEY_Text);
					 String str=c.getString(nameColumn); 
					// b.putString("month",str1);
					 b.putString("text",str);
			 }
			 else
			 {
			   b.putString("text","");
			 }
			 b.putString("month",str1);
			 b.putString("date",str2);
			 b.putString("judge",str3);
			 
			 
			 System.out.println("qrray postion1:" + position  + " "+ qrray[position]);
			 if(qrray[position]==0)
			 b.putString("state","uncomplate");
			 else b.putString("state","complate");
			 intent.putExtras(b);
			 context.startActivityForResult(intent, 100);
		 }
	
		 
		
		protected void onActivityResultForJihua(int requestCode,int resultCode,Intent data)
		{
		
			Bundle b=data.getExtras();
			String date=b.getString("date");
			String text=b.getString("text");
			String str3=b.getString("judge");
			String str4=b.getString("state");
			if(str3.equals("false"))
			{
				 Map<String,Object> map=new HashMap<String,Object>();
				 int position=Integer.parseInt(date)-1;
				 map=list.get(position);
				 if(str4.equals("complate"))
				 {
					 map.remove("image");
					 map.put("image",R.drawable.ok);
					 list.set(position,map);
					 qrray[position] = 1;
					 adapter=new SimpleAdapter(context,list,R.layout.listview,new String[]{"image","Date","day","text"},new int[]{R.id.imageview,R.id.date,R.id.day,R.id.text});
					 listview.setAdapter(adapter);   
				 }
				 else
				 {
					 map.remove("image");
					 map.put("image",R.drawable.page);
					 list.set(position,map);
					 qrray[position] = 0;
					 adapter=new SimpleAdapter(context,list,R.layout.listview,new String[]{"image","Date","day","text"},new int[]{R.id.imageview,R.id.date,R.id.day,R.id.text});
					 listview.setAdapter(adapter);   
				 }
				 System.out.println("qrray postion2:" + position  + " "+ qrray[position]);
			}
			if(str3.equals("true"))
			{
			Log.i("nalicuoe","bushuang");
			 boolean judge=false;
			 Map<String,Object> map=new HashMap<String,Object>();
			 int position=Integer.parseInt(date)-1;
		
			 
			 map=list.get(position);
			 map.remove("text");
			 
			 map.put("text", text);
			 if(str4.equals("complate"))
			 {
				 Log.i("ok","page");
			   map.put("image",R.drawable.ok);
			   qrray[position] = 1;
			 }
			 else {
				 qrray[position] = 0;
				 map.put("image",R.drawable.page);
			 }
			 list.set(position,map);
			 
			 adapter=new SimpleAdapter(context,list,R.layout.listview,new String[]{"image","Date","day","text"},new int[]{R.id.imageview,R.id.date,R.id.day,R.id.text});
			 
			   listview.setAdapter(adapter);    //这里出问
				
			   Cursor c=db.getYearTitle(String.valueOf(item.year));
			   
			   if(c.moveToFirst())
			   {
				   do
				   {
					   int nameColumn=c.getColumnIndex(db.KEY_Month);
					   if(c.getString(nameColumn)==String.valueOf(item.month))
					   {
						   int nameColumn1=c.getColumnIndex(db.KEY_Date);
						   if(c.getString(nameColumn1)==date)
						   {
							   int sa=c.getInt(c.getColumnIndexOrThrow(db.KEY_ROWID));
							   db.updateTitle(sa,c.getString(nameColumn1), String.valueOf(item.year), String.valueOf(item.month),text,str4);
							   judge=true;
							   break;
						   }
					   }
				   }while(c.moveToNext());
			   }
			   if(judge==false)
			   {
				  
				   db.insertTitle(date,String.valueOf(year),String.valueOf(month),text,str4);
				   
		
				   Cursor cursor=db.getConcretTitle(String.valueOf(year), String.valueOf(month),date);
				       
					  
			   }
			}
		 }
	   public int weekday(int y,int m,int d)
	   {
			if(m<3)
			y=y-1;

	        return (y + y/4 - y/100 + y/400 + weekarray[m-1] + d) % 7;
	   }
	   public void addShortCut(){
	        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
	        // 设置属性
	        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getResources().getString(R.string.app_name));
	        ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(context, R.drawable.btn_style_alert_dialog_button_pressed);
	        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON,iconRes);
	 
	        // 是否允许重复创建
	        shortcut.putExtra("duplicate", false);
	        Intent intent = new Intent(Intent.ACTION_MAIN);
	        intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
	        intent.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
	        intent.addCategory(Intent.CATEGORY_LAUNCHER);
	        intent.setClass(context, edittext.class);
	        // 设置启动程序
	        System.out.println("createIcon");
	        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
	        context.sendBroadcast(shortcut);
	    }
	   
	

	
}
