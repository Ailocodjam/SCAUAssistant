package org.scauhci.studentAssistant.model;

import java.util.List;

import org.scauhci.studentAssistant.entity.Lesson;
import org.scauhci.studentAssistant.entity.Schedule;


public interface LessonRecord {

	/**
	 * 获取当前时间下，最接近的课程，如果从此时开始，这天都没课的课，就返回一个null
	 * @return
	 */
	public Lesson getRecentLesson();
	
	
	/**
	 * 获取当天的所有课程列表,如果当天没有课,就返回一个null
	 * @return
	 */
	public List<Lesson> getLessonsByDay(int day);
	
	/**
	 * 
	 */
	public Schedule getSchedule();
	
	
}
