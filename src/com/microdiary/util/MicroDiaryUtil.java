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
	 * �����ļ���
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
	 * ����·����ȡͼƬ
	 * @param path
	 * @return
	 */
	public static Bitmap readBitmap(String name)
	{
		File file = null;
		try 
		{
			//����ֻ�������SD��������Ӧ�ó�����з���SD��Ȩ��
			if (Environment.getExternalStorageState()
				.equals(Environment.MEDIA_MOUNTED))
			{
				String filePath = Environment.getExternalStorageDirectory()+ FILE_NAME_PIC + name;
				file = new File(filePath);
			    if (file.exists()) {//�����ļ�����
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
	 * ��ͼƬд��SD��
	 * @param name
	 * @param bitmap
	 */
	public static String writeBitmap(Bitmap bitmap)
	{
		BufferedOutputStream os = null;
		try 
		{
			//����ֻ�������SD��������Ӧ�ó�����з���SD��Ȩ��
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
	 * ����ϵͳ¼��
	 * @param context
	 * @return
	 */
	public static String startRecord(Context context) {
		if (!Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED))
			{
				Toast.makeText(context
					, "SD�������ڣ������SD����"
					, 3000)
					.show();
				return null;
			}
	    try {
				String name = getCurrentTime2() + ".amr";
				// ��������¼������Ƶ�ļ�
				soundFile = new File(Environment
					.getExternalStorageDirectory() + FILE_NAME_AUD + name);
				mRecorder = new MediaRecorder();
				// ����¼����������Դ
				mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
				// ����¼�Ƶ������������ʽ���������������������ʽ֮ǰ���ã�
				mRecorder.setOutputFormat(MediaRecorder
					.OutputFormat.THREE_GPP);
				// ������������ĸ�ʽ
				mRecorder.setAudioEncoder(MediaRecorder
					.AudioEncoder.AMR_NB);
				mRecorder.setOutputFile(soundFile.getAbsolutePath());
				mRecorder.prepare();
				// ��ʼ¼��
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
	 * ����¼��
	 */
	public static void endRecord() {
		if (soundFile != null && soundFile.exists())
		{
			// ֹͣ¼��
			mRecorder.stop();
			// �ͷ���Դ
			mRecorder.release();
			mRecorder = null;
		}
	}
	
	/**
	 * ����¼��
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
	 * ��ȡ��ǰʱ��(��ʽ1)
	 * @return
	 */
	public static String getCurrentTime1() {
		Date date = new Date(System.currentTimeMillis());
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	}
	
	/**
	 * ��ȡ��ǰʱ�䣨��ʽ2��
	 * @return
	 */
	public static String getCurrentTime2() {
		Date date = new Date(System.currentTimeMillis());
		return new SimpleDateFormat("yyyyMMddHHmmss").format(date);
	}
	
	
	/**
	 * ��ȡ��ǰ����
	 * @return
	 */
	public static String getCurrentDate() {
		Date date = new Date(System.currentTimeMillis());
		return new SimpleDateFormat("yyyy-MM-dd").format(date);
	}
	
	/**
	 * ��ȡ��ǰʱ��
	 * @return
	 */
	public static String getCurrentTime() {
		Date date = new Date(System.currentTimeMillis());
		return new SimpleDateFormat("HH:mm").format(date);
	}
}