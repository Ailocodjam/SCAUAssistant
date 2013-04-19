package org.scauhci.studentAssistant.widget;

import org.scauhci.studentAssistant.common.RecordXmlHandler;
import org.scauhci.studentAssistant.concrete.SCAURecord;
import org.scauhci.studentAssistant.concrete.SDCardManager;
import org.scauhci.studentAssistant.entity.Schedule;
import org.scauhci.studentAssistant.model.LessonRecord;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

public class WidgetMain extends AppWidgetProvider{
	
	private static LessonRecord record = null;
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// TODO Auto-generated method stub
		Intent intent=new Intent(context ,UpdateService.class);  
        PendingIntent refreshIntent=PendingIntent.getService(context, 0, intent, 0);  
        AlarmManager alarm=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);  
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, 0, 1200000, refreshIntent);  //�趨һ������Ƶ��
        context.startService(intent);  
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		super.onReceive(context, intent);
	}

	public static LessonRecord getRecord() {
		if(record==null){
			System.out.println("record is null load from sdcard");
			Schedule schedule = RecordXmlHandler.getLessonsFromXML();
			record=new SCAURecord("now date",schedule);//ע�⣺��һ������������
		}
		return record;
	}

	public static void setRecord(LessonRecord record) {
		WidgetMain.record = record;
	}
	
}
