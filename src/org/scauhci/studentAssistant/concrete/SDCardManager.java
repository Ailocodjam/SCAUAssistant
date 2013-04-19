package org.scauhci.studentAssistant.concrete;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.scauhci.studentAssistant.model.LessonRecord;

//import android.util.Log;



public class SDCardManager {

	public static void saveLessonRecordToSDCard(LessonRecord record){
		String path = "/sdcard/.lessonRecord/";
		File file_path = new File(path);
		if(!file_path.exists()){
			file_path.mkdirs();
		}	
		if (record != null) {
			try {
				ObjectOutputStream out = new ObjectOutputStream(
						new FileOutputStream(path + "record"));
				out.writeObject(record);
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private static LessonRecord loadLessonRecordFromSDCard(){
		try{
			ObjectInputStream in = new ObjectInputStream (new FileInputStream ("/sdcard/lessonRecord/record"));
			Object o = in.readObject();
			if(o instanceof LessonRecord){
				return (LessonRecord)o;
			}else{
				return null;
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	public static boolean isDataCache(){
		File file = new File("/sdcard/.lessonRecord/record");
		return file.exists();
	}
	
	public static LessonRecord getLessonRecord(){
		//Log.d("loading file","from sdcard");
		System.out.println("loading file from sdcard");
		if(isDataCache()){
			return loadLessonRecordFromSDCard();
		}
		return null;
	}
}
