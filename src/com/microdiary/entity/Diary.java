package com.microdiary.entity;

import java.sql.Date;

public class Diary {

	private int did;           //id
	private String date;     //日期时间
	private String content;    //内容
	private int picture;    //是否有图片
	private int audio;    //是否有录音
	private int video;     //是否有录像
	private String audioAddress;    //录音地址
	private String photoAddress;    //照片地址
	private String videoAddress;    //录像地址
	
	
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
