package cn.edu.scau.scauAssistant.service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.scauhci.studentAssistant.entity.Lesson;
import org.scauhci.studentAssistant.entity.LessonUtil;
import org.scauhci.studentAssistant.main.AssistantApplication;
import org.scauhci.studentAssistant.model.LessonRecord;

import cn.edu.scau.scauAssistant.notification.NotifyEvent;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;

public class NotifyEventAlarmReceiver extends BroadcastReceiver {

	private MediaPlayer mMediaPlayer;
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context=context;
        System.out.println("running alarm receiver");
        SharedPreferences sharedPreferences=context.getSharedPreferences("scau_assistant", Activity.MODE_PRIVATE);
        boolean isNotifyEventOn=sharedPreferences.getBoolean("isNotifyEventOn", false);
        if(isNotifyEventOn){
            System.out.println("isNotifyEventOn true");
            AssistantApplication application=(AssistantApplication) context.getApplicationContext();
        	List<NotifyEvent> notifyEvents=application.getNotifyEvents();
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        	Date date = new Date(System.currentTimeMillis());
    		long current = 0;
    		try {
				current = sdf.parse((sdf.format(date))).getTime();
				while(notifyEvents.size()>0){
	        		NotifyEvent event=notifyEvents.get(0);
	        		String dateAndTime=event.getNotifyDate()+" "+event.getNotifyTime();
	        		long eventTime=sdf.parse(dateAndTime).getTime();
	        		System.out.println("current:"+current+" eventTime:"+eventTime);
	        		if(Math.abs(current-eventTime)<10*1000){
	        			notifyEvents.remove(0);
	        			playAlarmRing();
	        			break;
	        		}
	        		if(eventTime>=current){
	        			System.out.println("eventTime>=current");
	        			break;
	        		}
	        		notifyEvents.remove(0);
	        	}
			} catch (ParseException e) {
				e.printStackTrace();
			}
        	
        }
    }

    private void playAlarmRing() {
    	System.out.println("playing alarm ring");
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        try {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(context, uri);
            final AudioManager audioManager = (AudioManager) context
                    .getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mMediaPlayer.setLooping(false);
                mMediaPlayer.prepare();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.start();
    }

    private void StopAlarmRing() {
        mMediaPlayer.stop();
    }

}
