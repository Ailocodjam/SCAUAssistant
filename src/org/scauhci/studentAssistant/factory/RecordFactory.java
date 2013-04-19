package org.scauhci.studentAssistant.factory;

import java.util.List;

import org.scauhci.studentAssistant.entity.Lesson;
import org.scauhci.studentAssistant.model.LessonRecord;
import org.scauhci.studentAssistant.model.WebFetch;


public abstract class RecordFactory {
	
	private String schoolName = null;
	private WebFetch dao = null;
	
	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public WebFetch getDao() {
		return dao;
	}

	public void setDao(WebFetch dao) {
		this.dao = dao;
	}

	public RecordFactory(String schoolName, WebFetch dao){
		this.schoolName = schoolName;
		this.dao = dao;
	}
	
	public RecordFactory(String schoolName){
		this.schoolName=schoolName;
	}
	
	/**
	 * 抽象的,创建Record的实例.
	 * @param list
	 * @return
	 */
	public abstract LessonRecord createRecord(List<Lesson> list,String semester,String year,String userid);
	
	
}
