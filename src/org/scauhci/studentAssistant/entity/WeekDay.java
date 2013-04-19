package org.scauhci.studentAssistant.entity;

import java.util.HashMap;
import java.util.Map;

public class WeekDay {

	private static WeekDay instance;
	
	public static WeekDay getInstance(){
		if(instance==null){
			instance=new WeekDay();
		}
		return instance;
	}
	private WeekDay(){
		initWeekdayMap();
	}
	
    private Map<String,Integer> weekdayMap;
	
	public Map<String, Integer> getWeekdayMap() {
		return weekdayMap;
	}

	public void initWeekdayMap(){
		weekdayMap=new HashMap<String, Integer>();
		weekdayMap.put("周一", 1);
		weekdayMap.put("周二", 2);
		weekdayMap.put("周三", 3);
		weekdayMap.put("周四", 4);
		weekdayMap.put("周五", 5);
		weekdayMap.put("周六", 6);
		weekdayMap.put("周日", 0);
	}
}
