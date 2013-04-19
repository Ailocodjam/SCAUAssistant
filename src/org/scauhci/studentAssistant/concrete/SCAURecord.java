package org.scauhci.studentAssistant.concrete;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.scauhci.studentAssistant.entity.Lesson;
import org.scauhci.studentAssistant.entity.LessonUtil;
import org.scauhci.studentAssistant.entity.Schedule;
import org.scauhci.studentAssistant.model.LessonRecord;


public class SCAURecord implements LessonRecord,Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7330248692695639830L;

	private String update_time;
	
	private Schedule schedule;

	public SCAURecord(String update_time, Schedule schedule) {
		this.update_time = update_time;
		this.schedule = schedule;
	}

	@Override
	public Lesson getRecentLesson() {
		int currentLesson=LessonUtil.getInstance().getNowLessonIndex();
		Date date=new Date();
		int day=date.getDay();
		//System.out.println("day:"+day+"current:"+currentLesson);
		List<Lesson> lessons= getLessonsByDay(day);
		if(lessons.size()==0){
			System.out.println("no lesson ");
			return null;
		}
		for(int i=0;i<lessons.size();i++){
			Lesson lesson=lessons.get(i);
			int lt=LessonUtil.getInstance().getLessonIndex(lesson.getLessonTime());
			if(lt>=currentLesson){
				return lesson;
			}
		}
		return null;
	}

	@Override
	public List<Lesson> getLessonsByDay(int day) {
		
		return schedule.getLessonLists().get(day);
	}

	public String getUpdate_time() {
		return update_time;
	}

	@Override
	public Schedule getSchedule() {
		return schedule;
	}
	
	
	

	

}
