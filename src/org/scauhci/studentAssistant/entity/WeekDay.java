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
		weekdayMap.put("��һ", 1);
		weekdayMap.put("�ܶ�", 2);
		weekdayMap.put("����", 3);
		weekdayMap.put("����", 4);
		weekdayMap.put("����", 5);
		weekdayMap.put("����", 6);
		weekdayMap.put("����", 0);
	}
}
