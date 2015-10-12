package com.microdiary;

/**
 * 微日记页面
 */

import java.io.IOException;
import java.util.ArrayList;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.microdiary.dao.DiaryDB;
import com.microdiary.entity.Diary;
import com.microdiary.util.MicroDiaryUtil;

public class MicroRiji {
	
	
	public MainMicroDiary context;
	public View view;                //微日记view
	public ListView diaryList;          //日记列表
	public ImageButton writeDiary;      //写日记的按扭
	public SelectDialog diaryDialog = null;    //日记弹窗
	public AutoCompleteTextView search;
	
	
	public DiaryDB db = null;
	
	/*
	 * 弹窗组件
	 */
	public TextView title;    //标题
	public Button opImage;   //右上角按扭图标
	public TextView date;    //日期时间
	public EditText  content;   //内容
	public ImageView picture;   //照片图标
	public ImageView audio;    //录音图标
	public ImageView video;    //录像图标
	
	
	/*
	 * 弹窗类型
	 */
	public final int WRITE_DIALOG = 1;    //写日记
	public final int EDIT_DIALOG = 2;     //编辑日记
	public final int REMOVE_DIALOG = 3;   //删除日记
	public final int READ_DIALOG = 4;     //读日记
	
	
	/*
	 * 附件类型
	 */
	public final int PICTURE = 5;
	public final int AUDIO = 6;
	public final int VIDEO = 7;
	
	public ArrayList<Diary> diaries = null;  //日记列表
	public int currentIndex = -1;
	public Diary currentDiary = null;  //当前日记
	
	ArrayList<Diary> searchList;   //搜索日记列表
	
	
	/**
	 * 构造函数
	 * @param context
	 * @param view
	 */
	public MicroRiji(MainMicroDiary context, View view) {
		this.context = context;
		this.view = view;
		db = new DiaryDB(context); 
		writeDiary = (ImageButton) view.findViewById(R.id.writeDiary);
		diaryList = (ListView) view.findViewById(R.id.diaryList);
		search = (AutoCompleteTextView) view.findViewById(R.id.editText1);
		diaries = db.select();//返回list；里面是diary--是对象--的引用。
	    refresh();
	    writeDiary.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub 
				showDiaryDialog(WRITE_DIALOG);
			}
		});
	    
	    search.setThreshold(1);
	    search.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				showSearchResult();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				showSearchResult();
			}
		});
	    search.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				currentDiary = db.select(search.getText().toString(), search.getText().toString()).get(0);
				showDiaryDialog(READ_DIALOG);
				search.setText("");
			}
		});
	}
	
	public MicroRiji(MicroRiji microRiji, View view2) {
		// TODO Auto-generated constructor stub
	}
	   
	   

	/**
	 * 搜索结果
	 */
	public void showSearchResult() {
		String key = search.getText().toString();
		System.out.println("KEY-->" + key);
		if(key.equals(""))
			return;
		searchList = db.search(key);
		String[] dates = new String[searchList.size()];
		for(int i = 0; i < dates.length; i++) {
			dates[i] = searchList.get(i).getDate();
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(MicroRiji.this.context, 
				android.R.layout.simple_dropdown_item_1line, dates);
//		adapter.notifyDataSetChanged();
		search.setAdapter(adapter);
		search.showDropDown();
		adapter.notifyDataSetChanged();
		
	}
	
	/**
	 * 刷新日记列表
	 */
	public void refresh() {
		
		/*
		for(int i = 0; i < 10; i++) {
			Diary diary = new Diary();
			diary.setDate("2012-12-10 12:12:12");
			diary.setContent("我的日记内容我的日记内容" +
					"我的日记内容我的日记内容" +
					"我的日记内容我的日记内容" +
					"我的日记内容我的日记内容" +
					"我的日记内容我的日记内容" +
					"我的日记内容我的日记内容我的日记内容我的日记内容我的日记内容");
			diary.setPicture((int)(Math.random() * 2));
			diary.setAudio((int)(Math.random() * 2));
			diary.setVideo((int)(Math.random() * 2));
			list.add(diary);
		}*/
		DiaryAdapter adapter = new DiaryAdapter(context, diaries);
		diaryList.setAdapter(adapter);
		diaryList.setSelection(currentIndex);
		diaryList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
//				Toast.makeText(context, "ITEM", 3000).show();
				currentDiary = diaries.get(arg2);
				currentIndex = arg2;
				showDiaryDialog(READ_DIALOG);
			}
		});
		diaryList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				currentDiary = diaries.get(arg2);
				currentIndex = arg2;
				showEditOrRemoveDialog();
				return false;
			}
		});
	}
	
	/**
	 * 
	 * 
	 */
	public void showEditOrRemoveDialog() {
		final SelectDialog dialog = new SelectDialog(context, R.style.dialog, R.layout.edit_remove_diary_dialog);
		dialog.show();
		Button editDiary = (Button) dialog.findViewById(R.id.editDiary);
		Button removeDiary = (Button) dialog.findViewById(R.id.removeDiary);
		editDiary.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				showDiaryDialog(EDIT_DIALOG);
			}
		});
		removeDiary.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				showDiaryDialog(REMOVE_DIALOG);
			}
		});
	}
	
	
	public void showRedoOrReadDialog(final int tag) {
		final SelectDialog dialog = new SelectDialog(context, R.style.dialog, R.layout.redo_read_diary_dialog);
		dialog.show();
		Button redo = (Button) dialog.findViewById(R.id.redo);
		Button read = (Button) dialog.findViewById(R.id.read);
		if(tag == PICTURE) {
			redo.setText("重新拍摄");
			read.setText("显示照片");
		}
		else if(tag == AUDIO) {
			redo.setText("重新录音");
			read.setText("播放录音");
		}
		else if(tag == VIDEO){
			redo.setText("重新录像");
			read.setText("播放录像");
		}
		redo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				if(tag == PICTURE) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					context.startActivityForResult(intent, 1);
				}
				else if(tag == AUDIO) {
					Intent intent = new Intent(Intent.ACTION_GET_CONTENT);  
					intent.setType("audio/amr");   
					intent.setClassName("com.android.soundrecorder",  
					"com.android.soundrecorder.SoundRecorder");  
					context.startActivityForResult(intent, 2);  
				}
				else if(tag == VIDEO){
					Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
				    context.startActivityForResult(intent, 3);
				}
			}
		});
		read.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				
				if(tag == PICTURE) {
					showPicture();
				}
				else if(tag == AUDIO) {
					playRecord();
				}
				else if(tag == VIDEO){
					playVideo();
				}
			}
		});
	}
	
	public void showDiaryDialog(final int tag) {
		diaryDialog = new SelectDialog(MicroRiji.this.context,  R.style.dialog, R.layout.diary_dialog);
		diaryDialog.show();
		picture = (ImageView) diaryDialog.findViewById(R.id.img_picture);
		audio = (ImageView) diaryDialog.findViewById(R.id.img_audio);
		video = (ImageView) diaryDialog.findViewById(R.id.img_video);
		title = (TextView) diaryDialog.findViewById(R.id.diaryText);
		opImage = (Button) diaryDialog.findViewById(R.id.ok);
		date = (TextView) diaryDialog.findViewById(R.id.date);
		content = (EditText) diaryDialog.findViewById(R.id.content);
		
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

	public void writeDialog() {

		title.setText("写日记");
//		opImage.setImageResource(R.drawable.ok);
		opImage.setText("完成");
		date.setText(MicroDiaryUtil.getCurrentTime1());
		content.setText("");
		picture.setImageResource(R.drawable.picture_null);
		audio.setImageResource(R.drawable.music_2_null);
		video.setImageResource(R.drawable.movies_null);
		currentDiary = new Diary();
		
		opImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String dateStr = date.getText().toString();
				String contentStr = content.getText().toString();
				if(contentStr.equals("") && currentDiary.getAudio() == 0 && currentDiary.getPicture() == 0
						&& currentDiary.getVideo() == 0) {
					Toast.makeText(context, "您的日记空空的哦~~", 3000).show();
					return;
				}
				currentDiary.setDate(dateStr);
				currentDiary.setContent(contentStr);
				db.insert(currentDiary.getDate(), currentDiary.getContent(), 
						currentDiary.getPicture(), currentDiary.getAudio(), currentDiary.getVideo(),
						currentDiary.getAudioAddress(), currentDiary.getPhotoAddress(), currentDiary.getVideoAddress());
//				refresh();gag
				
				Diary diary = db.selectFirst().get(0);
				diaries.add(0, diary);
				currentIndex = 0;
				refresh();
				diaryDialog.dismiss();
				
				Toast.makeText(context, "保存成功", 3000).show();
			}
		});
		
		picture.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(currentDiary.getPicture() == 0) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					context.startActivityForResult(intent, 1);
				}
				else {
		    showPicture();
					showRedoOrReadDialog(PICTURE);
				}
			}
		});
		audio.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(currentDiary.getAudio() == 0) {
					Intent intent = new Intent(Intent.ACTION_GET_CONTENT);  
					intent.setType("audio/amr");   
					intent.setClassName("com.android.soundrecorder",  
					"com.android.soundrecorder.SoundRecorder");  
					context.startActivityForResult(intent, 2);  
				}
				else {
//					playRecord();
					showRedoOrReadDialog(AUDIO);
				}
			}
		});
		video.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(currentDiary.getVideo() == 0) {
					Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
				    context.startActivityForResult(intent, 3);
				}
				else {
//					playVideo();
					showRedoOrReadDialog(VIDEO);
				}
			}
		});
	}
	
	public void editDialog() {
		title.setText("修改日记");
//		opImage.setImageResource(R.drawable.ok);
		opImage.setText("修改");
		date.setText(currentDiary.getDate());
		content.setText(currentDiary.getContent());
		if(currentDiary.getPicture() == 0)
			picture.setImageResource(R.drawable.picture_null);
		else
			picture.setImageResource(R.drawable.picture);
		if(currentDiary.getAudio() == 0)
			audio.setImageResource(R.drawable.music_2_null);
		else
			audio.setImageResource(R.drawable.music_2);
		if(currentDiary.getVideo() == 0) 
			video.setImageResource(R.drawable.movies_null);
		else
			video.setImageResource(R.drawable.movies);
		
		
		opImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String dateStr = date.getText().toString();
				String contentStr = content.getText().toString();
				if(contentStr.equals("") && currentDiary.getAudio() == 0 && currentDiary.getPicture() == 0
						&& currentDiary.getVideo() == 0) {
					Toast.makeText(context, "您的日记空空的哦~~", 3000).show();
					return;
				}
				currentDiary.setDate(dateStr);
				currentDiary.setContent(contentStr);
				db.update(currentDiary.getDid(), currentDiary.getDate(), currentDiary.getContent(), 
						currentDiary.getPicture(), currentDiary.getAudio(), currentDiary.getVideo(),
						currentDiary.getAudioAddress(), currentDiary.getPhotoAddress(), currentDiary.getVideoAddress());
				diaries.set(currentIndex, currentDiary);
				refresh();
				diaryDialog.dismiss();
				Toast.makeText(context, "修改成功", 3000).show();
			}
		});
		
		picture.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(currentDiary.getPicture() == 0) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					context.startActivityForResult(intent, 1);
				}
				else {
//				    showPicture();
					showRedoOrReadDialog(PICTURE);
				}
			}
		});
		audio.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(currentDiary.getAudio() == 0) {
					Intent intent = new Intent(Intent.ACTION_GET_CONTENT);  
					intent.setType("audio/amr");   
					intent.setClassName("com.android.soundrecorder",  
					"com.android.soundrecorder.SoundRecorder");  
					context.startActivityForResult(intent, 2);  
				}
				else {
//					playRecord();
					showRedoOrReadDialog(AUDIO);
				}
			}
		});
		video.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(currentDiary.getVideo() == 0) {
					Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
				    context.startActivityForResult(intent, 3);
				}
				else {
//					playVideo();
					showRedoOrReadDialog(VIDEO);
				}
			}
		});
	}
	
	public void removeDialog() {
		title.setText("删除日记");
//		opImage.setImageResource(R.drawable.remove);
		opImage.setText("删除");
		date.setText(currentDiary.getDate());
		content.setText(currentDiary.getContent());
		content.setEnabled(false);
		if(currentDiary.getPicture() == 0)
			picture.setImageResource(R.drawable.picture_null);
		else
			picture.setImageResource(R.drawable.picture);
		if(currentDiary.getAudio() == 0)
			audio.setImageResource(R.drawable.music_2_null);
		else
			audio.setImageResource(R.drawable.music_2);
		if(currentDiary.getVideo() == 0) 
			video.setImageResource(R.drawable.movies_null);
		else
			video.setImageResource(R.drawable.movies);
		
		opImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				db.delete(currentDiary.getDid());
				Toast.makeText(context, "删除成功", 3000).show();
				diaries.remove(currentIndex);
				if(currentIndex != 0)
					currentIndex--;
				refresh();
				diaryDialog.dismiss();
				
			}
		});
		
		picture.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(currentDiary.getPicture() == 0) {
					
				}
				else {
				    showPicture();
				}
			}
		});
		audio.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(currentDiary.getAudio() == 0) {
					
				}
				else {
					playRecord();
				}
			}
		});
		video.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(currentDiary.getVideo() == 0) {
					
				}
				else {
					playVideo();
				}
			}
		});
	}
	
	public void readDialog() {
		title.setText("查看日记");
//		opImage.setImageResource(R.drawable.ok);
//		opImage.setVisibility(View.GONE);
		opImage.setText("确定");
		date.setText(currentDiary.getDate());
		content.setText(currentDiary.getContent());
		content.setEnabled(false);
		if(currentDiary.getPicture() == 0)
			picture.setImageResource(R.drawable.picture_null);
		else
			picture.setImageResource(R.drawable.picture);
		if(currentDiary.getAudio() == 0)
			audio.setImageResource(R.drawable.music_2_null);
		else
			audio.setImageResource(R.drawable.music_2);
		if(currentDiary.getVideo() == 0) 
			video.setImageResource(R.drawable.movies_null);
		else
			video.setImageResource(R.drawable.movies);
		
		opImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				diaryDialog.dismiss();
			}
		});
		
		picture.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(currentDiary.getPicture() == 0) {
					
				}
				else {
				    showPicture();
				}
			}
		});
		audio.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(currentDiary.getAudio() == 0) {
					
				}
				else {
					playRecord();
				}
			}
		});
		video.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(currentDiary.getVideo() == 0) {
					
				}
				else {
					playVideo();
				}
			}
		});
		opImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				diaryDialog.dismiss();
			}
		});
	}
	
	/**
	 * 显示照片
	 */
	public void showPicture() {
		String pictureAddress = currentDiary.getPhotoAddress();
		SelectDialog pictureDialog = new SelectDialog(context, R.style.dialog, R.layout.image_dialog);
		pictureDialog.show();
		ImageView image = (ImageView) pictureDialog.findViewById(R.id.imageView);
		
		image.setImageBitmap(MicroDiaryUtil.readBitmap(pictureAddress));
	}
	
	/**
	 * 播放录音
	 */
 
   
   
	public void playRecord() {
		final MediaPlayer mediaPlayer = new MediaPlayer();
		try {
			mediaPlayer.setDataSource(currentDiary.getAudioAddress());//Environment.getExternalStorageDirectory()+ FILE_NAME_AUD + name);
			mediaPlayer.prepare();
			mediaPlayer.start();
			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				
				@Override
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub
					mediaPlayer.release();
				}
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 
	
	/**
	 * 播放录像
	 */
	public void playVideo() {
		final SelectDialog videoDialog = new SelectDialog(context, R.style.dialog, R.layout.video_dialog);
		videoDialog.show();
		VideoView videoView = (VideoView) videoDialog.findViewById(R.id.videoView);
		MediaController controller = new MediaController(context);
		videoView.setVideoPath(currentDiary.getVideoAddress());
		videoView.setMediaController(controller);
		controller.setMediaPlayer(videoView);
		videoView.requestFocus();
		videoView.start();
		videoView.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				videoDialog.dismiss();
				
			}
		});
	}
	
}
