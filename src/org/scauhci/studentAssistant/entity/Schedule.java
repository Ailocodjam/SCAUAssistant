package org.scauhci.studentAssistant.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Schedule implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8698317889854651461L;
	private String userId;
	private String semester;
	private String year;
	private Map< Integer,List<Lesson> > lessonLists;
	
	public Schedule(){
		
	}
	
	public Schedule(String userid,String semester, String year,
			Map<Integer, List<Lesson>> lessonLists) {
		this.userId=userid;
		this.semester = semester;
		this.year = year;
		this.lessonLists = lessonLists;
	}
	
	
	
	public String getUserId() {
		return userId;
	}



	public void setUserId(String userId) {
		this.userId = userId;
	}



	public String getSemester() {
		return semester;
	}
	public void setSemester(String semester) {
		this.semester = semester;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public Map<Integer, List<Lesson>> getLessonLists() {
		return lessonLists;
	}
	public void setLessonLists(Map<Integer, List<Lesson> > lessonLists) {
		this.lessonLists = lessonLists;
	}
	
}
