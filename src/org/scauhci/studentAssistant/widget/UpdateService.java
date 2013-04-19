package org.scauhci.studentAssistant.widget;

import org.scauhci.studentAssistant.concrete.SDCardManager;
import org.scauhci.studentAssistant.entity.Lesson;
import org.scauhci.studentAssistant.model.LessonRecord;

import org.scauhci.studentAssistant.R;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

public class UpdateService extends Service {

	private LessonRecord record;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		RemoteViews remoteViews = new RemoteViews(this.getPackageName(),R.layout.widget);
		Lesson lesson = null;
		try {
			record = WidgetMain.getRecord();
			if (record == null) {
				System.out.println("service record is null");
				record = SDCardManager.getLessonRecord();
			}
			lesson = record.getRecentLesson();
		} catch (NullPointerException npe) {
			System.out.println("service null pointer");
			remoteViews.setTextViewText(R.id.lname, "没课");
			remoteViews.setTextViewText(R.id.ltime, "");
			return super.onStartCommand(intent, flags, startId);
		}
		if (lesson == null) {
			System.out.println("现在没课!");
			remoteViews.setTextViewText(R.id.lname, "没课");
			remoteViews.setTextViewText(R.id.ltime, "");
			//return super.onStartCommand(intent, flags, startId);

		} else {
			remoteViews.setTextViewText(R.id.lname, lesson.getName());
			String text;
			if (lesson.getClassroom() == null) {
				text = lesson.getLessonTime();
			} else {
				text = lesson.getLessonTime() +"  "+ lesson.getClassroom();
			}
			remoteViews.setTextViewText(R.id.ltime, text);
		}
		Intent intentClick = new Intent(this, DetailActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				intentClick, 0);
		remoteViews.setOnClickPendingIntent(R.id.layout, pendingIntent);
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
		ComponentName thisWidget = new ComponentName(this, WidgetMain.class);
		appWidgetManager.updateAppWidget(thisWidget, remoteViews);
		return super.onStartCommand(intent, flags, startId);
	}
}
