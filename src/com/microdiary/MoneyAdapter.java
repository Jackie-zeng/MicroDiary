package com.microdiary;

/**
 * 理财适配器
 */

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.microdiary.entity.Diary;
import com.microdiary.entity.Money;

public class MoneyAdapter extends BaseAdapter {
	
	
	private Context context;
	ArrayList<Money> moneys;

	public MoneyAdapter(Context context, ArrayList<Money> moneys){
		this.context = context;
		this.moneys = moneys;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return moneys.size();
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
		if(convertView == null) {
			
			View v = LayoutInflater.from(context).inflate(R.layout.money_item, null);
			item = new Item();
			item.moneyLayout = (RelativeLayout) v.findViewById(R.id.moneyLayout);
			item.date = (TextView) v.findViewById(R.id.date);
			item.value = (TextView) v.findViewById(R.id.value);
			item.remark = (TextView) v.findViewById(R.id.remark);
			
			
			v.setTag(item);
			convertView = v;
		}
		else {
			item = (Item) convertView.getTag();
		}
		
		Money money = moneys.get(position);
		if(position % 2 == 0) {
			item.moneyLayout.setBackgroundResource(R.drawable.mm_listitem);
		}
		else {
			item.moneyLayout.setBackgroundResource(R.drawable.mm_listitem2);
		}
		item.date.setText(money.getDate());
		item.value.setText(money.getValue());
		if(money.getRemark().equals(""))
			item.remark.setText("备注：无");
		else 
			item.remark.setText("备注：" + money.getRemark());
		
		return convertView;
	}

	
	class Item {
		RelativeLayout moneyLayout;
		TextView date;
		TextView value;
	    TextView remark;
	}
}
