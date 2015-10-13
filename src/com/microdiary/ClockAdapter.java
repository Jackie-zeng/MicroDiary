package com.microdiary;

/**
 * ¿Ì≤∆  ≈‰∆˜
 */

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.microdiary.entity.Clock;
import com.microdiary.entity.Money;

public class ClockAdapter extends BaseAdapter {
	
	
	private Context context;
	ArrayList<Clock> clocks;

	public ClockAdapter(Context context, ArrayList<Clock> clocks){
		this.context = context;
		this.clocks = clocks;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return clocks.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Item item;
		Item item2;
		if(convertView == null) {
			
			View v = LayoutInflater.from(context).inflate(R.layout.clock_item, null);
			item = new Item();
			item.moneyLayout = (RelativeLayout) v.findViewById(R.id.moneyLayout);
			item.date = (TextView) v.findViewById(R.id.date);
			item.content = (TextView) v.findViewById(R.id.content);
			
			
			v.setTag(item);
			convertView = v;
		}
		else {
			item = (Item) convertView.getTag();
		}
		if(position % 2 == 0) {
			item.moneyLayout.setBackgroundResource(R.drawable.mm_listitem);
		}
		else {
			item.moneyLayout.setBackgroundResource(R.drawable.mm_listitem2);
		}
		
		
		Clock clock = clocks.get(position);
		
		item.date.setText(clock.getDate() + " " + clock.getTime());
		item.content.setText("    " + clock.getContent());
		
		
		
		return convertView;
	}

	
	class Item {
		RelativeLayout moneyLayout;
		TextView date;
		TextView content;
	}
}
