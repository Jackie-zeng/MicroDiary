package com.microdiary.entity;

import java.sql.Date;

public class Diary {

	private int did;           //id
	private String date;     //����ʱ��
	private String content;    //����
	private int picture;    //�Ƿ���ͼƬ
	private int audio;    //�Ƿ���¼��
	private int video;     //�Ƿ���¼��
	private String audioAddress;    //¼����ַ
	private String photoAddress;    //��Ƭ��ַ
	private String videoAddress;    //¼���ַ
	
	
	public int getDid() {
		return did;
	}
	public void setDid(int did) {
		this.did = did;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getPicture() {
		return picture;
	}
	public void setPicture(int picture) {
		this.picture = picture;
	}
	public int getAudio() {
		return audio;
	}
	public void setAudio(int audio) {
		this.audio = audio;
	}
	public int getVideo() {
		return video;
	}
	public void setVideo(int video) {
		this.video = video;
	}
	public String getAudioAddress() {
		return audioAddress;
	}
	public void setAudioAddress(String audioAddress) {
		this.audioAddress = audioAddress;
	}
	public String getPhotoAddress() {
		return photoAddress;
	}
	public void setPhotoAddress(String photoAddress) {
		this.photoAddress = photoAddress;
	}
	public String getVideoAddress() {
		return videoAddress;
	}
	public void setVideoAddress(String videoAddress) {
		this.videoAddress = videoAddress;
	}
	
	
}
