package org.scauhci.studentAssistant.entity;

import java.util.HashMap;
import java.util.Map;

public class CourseQualityMap {

	private static CourseQualityMap instance;
	
	public static CourseQualityMap getInstance(){
		if(instance==null){
			instance=new CourseQualityMap();
		}
		return instance;
	}
	private CourseQualityMap(){
		initMap();
	}
	private Map<String,String> map;
	
	public Map<String, String> getMap() {
		return map;
	}
	public void initMap(){
		map=new HashMap<String, String>();
		map.put("����", "01");
		map.put("��ѡ", "02");
		map.put("��ѡ", "03");
		map.put("��ѡ", "04");
		map.put("ʵ��", "05");
		map.put("��ѡ", "06");
		map.put("У��ѡ", "11");
	}
}
