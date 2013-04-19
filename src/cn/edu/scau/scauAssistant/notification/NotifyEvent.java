package cn.edu.scau.scauAssistant.notification;

import java.io.Serializable;

public class NotifyEvent implements Serializable {

	private String title;
	private String content;
	private String notifyTime;
	private String notifyDate;
	
	public NotifyEvent() {
		
	}
	public NotifyEvent(String title, String content, String notifyDate,String notifyTime) {
		this.title = title;
		this.content = content;
		this.notifyTime = notifyTime;
		this.notifyDate = notifyDate;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getNotifyTime() {
		return notifyTime;
	}
	public void setNotifyTime(String notifyTime) {
		this.notifyTime = notifyTime;
	}
	public String getNotifyDate() {
		return notifyDate;
	}
	public void setNotifyDate(String notifyDate) {
		this.notifyDate = notifyDate;
	}
	
	
}
