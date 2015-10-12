package com.microdiary;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.database.Cursor;
import android.R.drawable;
import android.content.Intent;
import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
public class listviewitem extends Activity{
	public int year;
	public int month;
	public int[] array;
	public String[] str=null;
	public int size;
	public MainMicroDiary weiriji;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Compute(year,month);
	}
	public void initialize(int sa,int sb)
	{
		year=sa;
		month=sb;
	}
	public int[] Compute(int yh,int mon)	
	{
		if((yh%4==0 && yh%100!=0) || yh%400==0)
		{
			if(mon==1||mon==3||mon==5||mon==7||mon==8||mon==10||mon==12)
			{
				array=new int[32];
				size=31;
					for(int i=0;i<=30;i++)
					{
						array[i]=i+1;
					}
					array[31]=31;
					return array;
			}
			if(mon==4||mon==6||mon==9||mon==11)
			{
				array=new int[31];
				size=30;
				for(int i=0;i<=29;i++)
					array[i]=i+1;
				array[30]=30;
				return array;
			}
			array=new int[30];
			size=29;
			for(int i=0;i<=28;i++)
			array[i]=i+1;
			array[29]=29;
			return array;
		}
		else
		{
			if(mon==1||mon==3||mon==5||mon==7||mon==8||mon==10||mon==12)
			{
				array=new int[32];
				size=31;
					for(int i=0;i<=30;i++)
					{
						array[i]=i+1;
					}
					array[31]=31;
					return array;
			}
			if(mon==4||mon==6||mon==9||mon==11)
			{
				array=new int[31];
				size=30;
				for(int i=0;i<=29;i++)
					array[i]=i+1;
				array[30]=30;
				return array;
			}
			array=new int[29];
			size=28;
			for(int i=0;i<=27;i++)
			array[i]=i+1;
			array[28]=28;
			return array;
		}
	}
}
		