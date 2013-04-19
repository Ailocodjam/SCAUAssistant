package org.scauhci.studentAssistant.model;

import java.util.List;

import org.scauhci.studentAssistant.entity.Lesson;
import org.scauhci.studentAssistant.entity.Schedule;


public interface LessonRecord {

	/**
	 * ��ȡ��ǰʱ���£���ӽ��Ŀγ̣�����Ӵ�ʱ��ʼ�����춼û�εĿΣ��ͷ���һ��null
	 * @return
	 */
	public Lesson getRecentLesson();
	
	
	/**
	 * ��ȡ��������пγ��б�,�������û�п�,�ͷ���һ��null
	 * @return
	 */
	public List<Lesson> getLessonsByDay(int day);
	
	/**
	 * 
	 */
	public Schedule getSchedule();
	
	
}
