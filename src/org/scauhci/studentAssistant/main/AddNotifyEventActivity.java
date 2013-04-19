package org.scauhci.studentAssistant.main;

import java.util.Calendar;
import java.util.Collections;

import org.scauhci.studentAssistant.R;

import cn.edu.scau.scauAssistant.notification.NotifyEvent;
import cn.edu.scau.scauAssistant.notification.NotifyEventComparator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

public class AddNotifyEventActivity extends Activity {

	private String notifyTitle="";
	private String notifyContent="";
	private String notifyDate="";
	private String notifyTime="";
	private String nowDate;
	private String nowTime;
	private EditText nTitle,nContent;
	private TextView nDate,nTime;
	private Button addButton;
	private NotifyEvent notifyEvent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.add_notification_event);
		
		nTitle=(EditText)findViewById(R.id.notifyTitle);
		nContent=(EditText)findViewById(R.id.notifyContent);
		
		nDate=(TextView)findViewById(R.id.notifyDate);
		nTime=(TextView)findViewById(R.id.notifyTime);
		
		nDate.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				Calendar c=Calendar.getInstance();
				
				new DatePickerDialog(AddNotifyEventActivity.this,new DatePickerDialog.OnDateSetListener() {
					
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear,
							int dayOfMonth) {
						notifyDate=year+"-"+(monthOfYear<10?"0"+monthOfYear:monthOfYear)+"-"+(dayOfMonth<10?"0"+dayOfMonth:dayOfMonth);
						nDate.setText(notifyDate);
					}
				},c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)).show();
				
			}
			
		});
		nTime.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Calendar c=Calendar.getInstance();
				new TimePickerDialog(AddNotifyEventActivity.this,new TimePickerDialog.OnTimeSetListener() {
					
					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						notifyTime=(hourOfDay<10?"0"+hourOfDay:hourOfDay)+":"+(minute<10?"0"+minute:minute);
						nTime.setText(notifyTime);
					}
				},c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),true).show();
			}
			
		});
		
		addButton=(Button)findViewById(R.id.addButton);
		addButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				notifyTitle=nTitle.getText().toString();
				notifyContent=nContent.getText().toString();
				if(notifyTitle.equals("")){
					new AlertDialog.Builder(AddNotifyEventActivity.this).setTitle("添加提醒事件出错")
				    .setMessage("提醒标题不能为空!").setPositiveButton("知道了", null).show();
				}else if(varifyTime()==false){
					new AlertDialog.Builder(AddNotifyEventActivity.this).setTitle("添加提醒事件出错")
				    .setMessage("提醒时间不能小于当前时间!").setPositiveButton("知道了", null).show();
				}else{
					notifyEvent=new NotifyEvent(notifyTitle,notifyContent,notifyDate,notifyTime);
					AssistantApplication application=(AssistantApplication) getApplicationContext();
					application.getNotifyEvents().add(notifyEvent);
					Collections.sort(application.getNotifyEvents(),new NotifyEventComparator());
					
					Intent intent=new Intent(AddNotifyEventActivity.this,MainActivity.class);

					startActivity(intent);
					finish();
					overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
				}
			}
			
		});
		
		/** 返回按钮  **/
		Button back = (Button)findViewById(R.id.backButton);
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
			}
		});
		initDateAndTime();
	}
	
	private boolean varifyTime(){
		int cmpDate=notifyDate.compareTo(nowDate);
		if(cmpDate<0){
			return false;
		}else if(cmpDate==0){
			if(notifyTime.compareTo(nowTime)<=0){
				return false;
			}
		}
		return true;
	}

	private void initDateAndTime(){
		Calendar c=Calendar.getInstance();
		int year=c.get(Calendar.YEAR);
		int monthOfYear=c.get(Calendar.MONTH);
		int dayOfMonth=c.get(Calendar.DAY_OF_MONTH);
		notifyDate=year+"-"+(monthOfYear<10?"0"+monthOfYear:monthOfYear)+"-"+(dayOfMonth<10?"0"+dayOfMonth:dayOfMonth);
		nowDate=notifyDate;
		nDate.setText(notifyDate);
		int hourOfDay=c.get(Calendar.HOUR_OF_DAY);
		int minute=c.get(Calendar.MINUTE);
		notifyTime=(hourOfDay<10?"0"+hourOfDay:hourOfDay)+":"+(minute<10?"0"+minute:minute);
		nowTime=notifyTime;
		nTime.setText(notifyTime);
	}
	
}
