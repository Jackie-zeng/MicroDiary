package com.microdiary.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.os.Environment;
import android.widget.Toast;

public class MicroDiaryUtil
{
	public static final String FILE_NAME_PIC = "/microdiary/pic/";
	public static final String FILE_NAME_AUD = "/microdiary/aud/";
	public static final String FILE_NAME_VIDEO = "/microdiary/video/";
	
	/**
	 * 创建文件夹
	 */
	public static void createFile() {
		String filePath = Environment.getExternalStorageDirectory()+ FILE_NAME_PIC;
		File file = new File(filePath);
		if(!file.exists()) {
			System.out.println("create pic");
			file.mkdirs();
		}
		filePath = Environment.getExternalStorageDirectory()+ FILE_NAME_AUD;
		file = new File(filePath);
		if(!file.exists()) {
			System.out.println("create aud");
			file.mkdirs();
		}
		filePath = Environment.getExternalStorageDirectory()+ FILE_NAME_VIDEO;
		file = new File(filePath);
		if(!file.exists()) {
			System.out.println("create video");
			file.mkdirs();
		}
	}

	
	/**
	 * 根据路径读取图片
	 * @param path
	 * @return
	 */
	public static Bitmap readBitmap(String name)
	{
		File file = null;
		try 
		{
			//如果手机插入了SD卡，而且应用程序具有访问SD的权限
			if (Environment.getExternalStorageState()
				.equals(Environment.MEDIA_MOUNTED))
			{
				String filePath = Environment.getExternalStorageDirectory()+ FILE_NAME_PIC + name;
				file = new File(filePath);
			    if (file.exists()) {//若该文件存在
			         Bitmap bm = BitmapFactory.decodeFile(filePath);
			         return bm;
			    }
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	
	/**
	 * 将图片写到SD卡
	 * @param name
	 * @param bitmap
	 */
	public static String writeBitmap(Bitmap bitmap)
	{
		BufferedOutputStream os = null;
		try 
		{
			//如果手机插入了SD卡，而且应用程序具有访问SD的权限
			if (Environment.getExternalStorageState()
				.equals(Environment.MEDIA_MOUNTED))
			{
				String name = getCurrentTime2();
				String filePath = Environment.getExternalStorageDirectory()+ FILE_NAME_PIC + name;//Environment.getExternalStorageDirectory()+ FILE_NAME + name;
				File file = new File(filePath);
			    if(file.exists()) {
			    	return name;
			    }
			    else {
			       file.createNewFile();
			    }
//			    file.createNewFile();
				os = new BufferedOutputStream(new FileOutputStream(file));
			    bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
			    os.close();
			    return name;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	
	static File soundFile = null;
	static MediaRecorder mRecorder = null;
	/**
	 * 调用系统录音
	 * @param context
	 * @return
	 */
	public static String startRecord(Context context) {
		if (!Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED))
			{
				Toast.makeText(context
					, "SD卡不存在，请插入SD卡！"
					, 3000)
					.show();
				return null;
			}
	    try {
				String name = getCurrentTime2() + ".amr";
				// 创建保存录音的音频文件
				soundFile = new File(Environment
					.getExternalStorageDirectory() + FILE_NAME_AUD + name);
				mRecorder = new MediaRecorder();
				// 设置录音的声音来源
				mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
				// 设置录制的声音的输出格式（必须在设置声音编码格式之前设置）
				mRecorder.setOutputFormat(MediaRecorder
					.OutputFormat.THREE_GPP);
				// 设置声音编码的格式
				mRecorder.setAudioEncoder(MediaRecorder
					.AudioEncoder.AMR_NB);
				mRecorder.setOutputFile(soundFile.getAbsolutePath());
				mRecorder.prepare();
				// 开始录音
				mRecorder.start();
				return name;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return null;
	}
	
	/**
	 * 结束录音
	 */
	public static void endRecord() {
		if (soundFile != null && soundFile.exists())
		{
			// 停止录音
			mRecorder.stop();
			// 释放资源
			mRecorder.release();
			mRecorder = null;
		}
	}
	
	/**
	 * 播放录音
	 */
	static MediaPlayer mediaPlayer = null;
	public static void playRecord(String path) {
		mediaPlayer = new MediaPlayer();
		try {
			mediaPlayer.setDataSource(path);//Environment.getExternalStorageDirectory()+ FILE_NAME_AUD + name);
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
	 * 获取当前时间(格式1)
	 * @return
	 */
	public static String getCurrentTime1() {
		Date date = new Date(System.currentTimeMillis());
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	}
	
	/**
	 * 获取当前时间（格式2）
	 * @return
	 */
	public static String getCurrentTime2() {
		Date date = new Date(System.currentTimeMillis());
		return new SimpleDateFormat("yyyyMMddHHmmss").format(date);
	}
	
	
	/**
	 * 获取当前日期
	 * @return
	 */
	public static String getCurrentDate() {
		Date date = new Date(System.currentTimeMillis());
		return new SimpleDateFormat("yyyy-MM-dd").format(date);
	}
	
	/**
	 * 获取当前时间
	 * @return
	 */
	public static String getCurrentTime() {
		Date date = new Date(System.currentTimeMillis());
		return new SimpleDateFormat("HH:mm").format(date);
	}
}