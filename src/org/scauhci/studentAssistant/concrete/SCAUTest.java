package org.scauhci.studentAssistant.concrete;

import org.scauhci.studentAssistant.model.WebFetch;

public class SCAUTest {

	public static void main(String[] args){
		WebFetch webFetch=new SCAUWebFetch();
		try {
			webFetch.login("200930740528", "7713416", "");
			//webFetch.getLessonsFromNetwork(null, null, true);
			//webFetch.login("zhc", "suntek", "");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
