package org.scauhci.studentAssistant.entity;

import java.io.Serializable;

public class ExamInformation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6010018078642036986L;

	private String course_name;
	private String time;
	private String place;
	private String exam_style;
	private String seat;
	private String school_area;
	
	
	public ExamInformation(String course_name, String time, String place,
			String exam_style, String seat, String school_area) {
		super();
		this.course_name = course_name;
		this.time = time;
		this.place = place;
		this.exam_style = exam_style;
		this.seat = seat;
		this.school_area = school_area;
	}
	public String getCourse_name() {
		return course_name;
	}
	public void setCourse_name(String course_name) {
		this.course_name = course_name;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public String getExam_style() {
		return exam_style;
	}
	public void setExam_style(String exam_style) {
		this.exam_style = exam_style;
	}
	public String getSeat() {
		return seat;
	}
	public void setSeat(String seat) {
		this.seat = seat;
	}
	public String getSchool_area() {
		return school_area;
	}
	public void setSchool_area(String school_area) {
		this.school_area = school_area;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "{"+"course_name:"+course_name+",time:"+time+",place:"+place+
		       ",exam_style:"+exam_style+",seat:"+seat+",school_area:"+school_area;
		
	}
	
	
}
