package org.scauhci.studentAssistant.concrete;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.scauhci.studentAssistant.entity.Lesson;
import org.scauhci.studentAssistant.entity.Schedule;
import org.scauhci.studentAssistant.entity.WeekDay;
import org.scauhci.studentAssistant.factory.RecordFactory;
import org.scauhci.studentAssistant.model.LessonRecord;
import org.scauhci.studentAssistant.model.WebFetch;


public class SCAURecordFactory extends RecordFactory{


	public SCAURecordFactory(String schoolName, WebFetch dao) {
		super(schoolName, dao);
		// TODO Auto-generated constructor stub
	}
	
	public SCAURecordFactory(String schoolName){
		super(schoolName);
		setDao(new SCAUWebFetch());
	}

	/**
	 * 真正实例化Record的代码在这里.
	 */
	/* (non-Javadoc)
	 * @see org.scauhci.studentAssistant.factory.RecordFactory#createRecord(java.util.List, java.lang.String, java.lang.String)
	 */
	@Override
	public LessonRecord createRecord(List<Lesson> list,String semester,String year,String userid){
		Map<Integer,List<Lesson> > lessonLists=new HashMap<Integer, List<Lesson> >();
		Iterator it=list.iterator();
		for(int i=0;i<7;i++){
			lessonLists.put(i, new ArrayList<Lesson>());
		}
		Map<String,Integer> weekdayMap=WeekDay.getInstance().getWeekdayMap();
		while(it.hasNext()){
			Lesson ls=(Lesson) it.next();
			lessonLists.get(weekdayMap.get(ls.getWeekday())).add(ls);
		}
		if(semester==null){
			semester="1";
		}
		if(year==null){
			Date date=new Date();
			year=date.getYear()+"";
		}
		Schedule schedule=new Schedule(userid,semester,year, lessonLists);
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String update_time = sdf.format(date);
		return new SCAURecord(update_time, schedule);
	}

}
