package com.microdiary;

/**
 * »’º«  ≈‰∆˜
 */

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.microdiary.entity.Diary;

public class DiaryAdapter extends BaseAdapter {
	
	
	private Context context;
	ArrayList<Diary> diaries;
	ArrayList<Diary> diariess;
// normally, adapter always have two fields which are context and listdata;
	
	public DiaryAdapter(Context context, ArrayList<Diary> diaries) {
		this.context = context;
		this.diaries = diaries;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return diaries.size();
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
			
			View v = LayoutInflater.from(context).inflate(R.layout.diary_item, null);
			item = new Item();
			item.date = (TextView) v.findViewById(R.id.date);
			item.content = (TextView) v.findViewById(R.id.content);
			item.picture = (ImageView) v.findViewById(R.id.picture);
			item.audio = (ImageView) v.findViewById(R.id.audio);
			item.video = (ImageView) v.findViewById(R.id.video);
			
			
			v.setTag(item);
			convertView = v;
		}
		else {
			item = (Item) convertView.getTag();
		}
		Diary diary = diaries.get(position);
		item.date.setText(diary.getDate());
		item.content.setText(diary.getContent());
		if(diary.getPicture() == 1)
			item.picture.setVisibility(View.VISIBLE);
		else
			item.picture.setVisibility(View.GONE);
		if(diary.getAudio() == 1)
			item.audio.setVisibility(View.VISIBLE);
		else
			item.audio.setVisibility(View.GONE);
		if(diary.getVideo() == 1) 
			item.video.setVisibility(View.VISIBLE);
		else
			item.video.setVisibility(View.GONE);
		return convertView;
	}

	
	class Item {
		TextView date;
		TextView content;
	    ImageView picture;
	    ImageView audio;
	    ImageView video;
	}
}
