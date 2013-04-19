package cn.edu.scau.scauAssistant.service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.scauhci.studentAssistant.entity.Lesson;
import org.scauhci.studentAssistant.entity.LessonUtil;
import org.scauhci.studentAssistant.main.AssistantApplication;
import org.scauhci.studentAssistant.model.LessonRecord;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class LessonAlarmReceiver extends BroadcastReceiver {

    private MediaPlayer mMediaPlayer;
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context=context;
        System.out.println("running alarm receiver");
        SharedPreferences sharedPreferences=context.getSharedPreferences("scau_assistant", Activity.MODE_PRIVATE);
        boolean isLessonsRemindStart=sharedPreferences.getBoolean("isLessonsRemindStart", false);
        if(isLessonsRemindStart){
            System.out.println("isLessonsRemindStart true");
            AssistantApplication application=(AssistantApplication) context.getApplicationContext();
            LessonRecord record=application.getRecord();
            Lesson recentLesson=record.getRecentLesson();
            if(recentLesson!=null){
            	System.out.println("current lesson is not null");
            	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            	Date date = new Date(System.currentTimeMillis());
        		long current = 0;
        		try {
        			current = sdf.parse((sdf.format(date))).getTime();
        			String lessonT=LessonUtil.getInstance().getNowLessonTime(recentLesson.getLessonTime());
            		long lessonSeconds=sdf.parse(lessonT).getTime();
            		System.out.println("current:"+current+" lessonSeconds:"+lessonSeconds);
            		if(Math.abs(current-lessonSeconds)<10*0000){
            			playAlarmRing();
            		}
        		} catch (ParseException e) {
        			e.printStackTrace();
        		}
        		
            }
        }
        /*
        CharSequence text = String.valueOf(minute);
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
        this.context = context;
        Bundle bundle = intent.getExtras();
        Intent serviceIntent = new Intent("chief_musicService");
        serviceIntent.putExtras(bundle);
        if(bundle != null) {
            Log.i("CTO", String.valueOf(bundle.getBoolean("music")));
            if(bundle.getBoolean("music"))
                context.startService(serviceIntent);
            else
                context.stopService(serviceIntent);
        }*/
        //在这里是播放不了的！！
       
        //playAlarmRing();
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
