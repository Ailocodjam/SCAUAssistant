package org.scauhci.studentAssistant.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LessonUtil {

	public static String CLASSROOM_UNKNOWN="课室未知";
	private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
	private static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private static String[] lessonTime = { "9:35", "11:40", "14:05", "16:5", "18:10", "21:05" };
	private static String[] lessonJie={"第1,2节","第3,4节","第5,6节","第7,8节","第9,10节","第11,12节"};
	private static long[] compute_time = new long[6];
	
	private volatile static LessonUtil instance;
	
	private LessonUtil(){
		for (int i = 0; i < 6; i++) {
			try {
				compute_time[i] = sdf.parse(lessonTime[i]).getTime();
				//System.out.println(sdf2.format(sdf.parse(ll[i])));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static LessonUtil getInstance(){
		if(instance==null){
			synchronized(LessonUtil.class){
				instance=new LessonUtil();
			}
		}
		return instance;
	}

	public int getNowLessonIndex() {

		Date date = new Date(System.currentTimeMillis());
		long current = 0;
		try {
			current = sdf.parse((sdf.format(date))).getTime();
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		for(int i=0;i<compute_time.length;i++){
			if(current<=compute_time[i]){
				System.out.println("now is :" + lessonJie[i]);
				return i;
			}
		}
		return lessonJie.length-1;
	}
	public String getNowLessonTime(String jie){
		int index=-1;
		for(int i=0;i<lessonJie.length;i++){
			if(lessonJie.equals(jie)){
				index=i;
			}
		}
		if(index!=-1){
			return lessonTime[index];
		}
		return null;
	}
	public int getLessonIndex(String lesson){
		for(int i=0;i<lessonJie.length;i++){
			if(lesson.equalsIgnoreCase(lessonJie[i])){
				return i;
			}
		}
		return 0;
	}
}
