package org.scauhci.studentAssistant.entity;

import java.io.Serializable;

public class Lesson implements Serializable{

	private String name;
	private String weekday;
	private String lessonTime;
	private String week;
	private String teacher;
	private String classroom;
	private String notifyTime;
	
	
	
	public Lesson(){
		notifyTime="";
	}

	public Lesson(String name, String weekday, String lessonTime, String week,
			String teacher, String classroom) {
		super();
		this.name = name;
		this.weekday = weekday;
		this.lessonTime = lessonTime;
		this.week = week;
		this.teacher = teacher;
		this.classroom = classroom;
		this.notifyTime="";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWeekday() {
		return weekday;
	}

	public void setWeekday(String weekday) {
		this.weekday = weekday;
	}

	public String getLessonTime() {
		return lessonTime;
	}

	public void setLessonTime(String lessonTime) {
		this.lessonTime = lessonTime;
	}

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	public String getClassroom() {
		return classroom;
	}

	public void setClassroom(String classroom) {
		this.classroom = classroom;
	}
	
	

	public String getNotifyTime() {
		return notifyTime;
	}

	public void setNotifyTime(String notifyTime) {
		this.notifyTime = notifyTime;
	}

	@Override
	public String toString() {
		return "Lesson [name=" + name + ", Weekday=" + weekday + ", LessonTime=" + lessonTime
				+ ", week=" + week + ", Teacher=" + teacher + ", classroom="
				+ classroom + "]";
	}
	
}
