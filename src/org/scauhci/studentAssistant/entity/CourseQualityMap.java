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
		map.put("必修", "01");
		map.put("必选", "02");
		map.put("限选", "03");
		map.put("任选", "04");
		map.put("实践", "05");
		map.put("公选", "06");
		map.put("校外选", "11");
	}
}
