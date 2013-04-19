package org.scauhci.studentAssistant.widget;




import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.scauhci.studentAssistant.common.RecordXmlHandler;
import org.scauhci.studentAssistant.concrete.SCAURecord;
import org.scauhci.studentAssistant.concrete.SCAURecordFactory;
import org.scauhci.studentAssistant.concrete.SCAUWebFetch;
import org.scauhci.studentAssistant.concrete.SDCardManager;
import org.scauhci.studentAssistant.entity.Lesson;
import org.scauhci.studentAssistant.entity.Schedule;
import org.scauhci.studentAssistant.factory.RecordFactory;
import org.scauhci.studentAssistant.model.LessonRecord;
import org.scauhci.studentAssistant.model.WebFetch;

import org.scauhci.studentAssistant.R;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;


public class ConfigActivity extends Activity{

	private int mAppWidgetId; 
	private Button loginButton;
	private Button resetButton;
	private EditText idEdit;
	private EditText passwordEdit;
	private RadioGroup radioGroup;
	private LessonRecord record;
	private String id;
	private String password;
	private WebFetch dao;
	private RecordFactory factory;
	
	private final int NETWORK_ERROR = 0;
	private final int ID_ERROR = 1;
	private final int NETWORK_OK = 2;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.login);
		updateWidgetView();
		dao=new SCAUWebFetch();
		factory=new SCAURecordFactory("scau", dao);
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
		    mAppWidgetId = extras.getInt(
		            AppWidgetManager.EXTRA_APPWIDGET_ID, 
		            AppWidgetManager.INVALID_APPWIDGET_ID);
		}
		
		idEdit = (EditText) findViewById(R.id.id);
		passwordEdit = (EditText) findViewById(R.id.password);
		
		loginButton = (Button) findViewById(R.id.login);
		resetButton = (Button) findViewById(R.id.reset);
		radioGroup=(RadioGroup)findViewById(R.id.chooseRadios);
		
		loginButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				password = passwordEdit.getText().toString();
				id = idEdit.getText().toString();
				int result = getLessonFromNetWork();
				if(result == NETWORK_ERROR){
					new AlertDialog.Builder(ConfigActivity.this)
					.setTitle("出错了")
					.setMessage("网络繁忙, 请稍候再试")
					.setPositiveButton("知道了", null).show();
					return;
				} else if (result == ID_ERROR){
					new AlertDialog.Builder(ConfigActivity.this)
					.setTitle("出错了")
					.setMessage("学号,密码出错了,请检查!")
					.setPositiveButton("知道了", null).show();
					return;
				} else {
					Intent resultValue = new Intent();
					resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
					setResult(RESULT_OK, resultValue);
					finish();
				}
			}
		});
		resetButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				idEdit.setText("");
				passwordEdit.setText("");
			}
		});
	}
	
	private int getLessonFromNetWork(){
		try {
			if(!dao.login(id, password,"学生"))
				return ID_ERROR;
			List<Lesson> lessons = dao.getLessonsFromNetwork(null, null,true);
			record=factory.createRecord(lessons, null, null,id);
			SDCardManager.saveLessonRecordToSDCard(record);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//学号或者密码出错
			return ID_ERROR;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return NETWORK_ERROR;
		}catch (Exception e){
			e.printStackTrace();
			return NETWORK_ERROR;
		}
		setLessonService();
		return NETWORK_OK;
	}
	private void setLessonService(){
		WidgetMain.setRecord(record);
		Log.d("get record ","success");
		Intent intent=new Intent(ConfigActivity.this ,UpdateService.class);  
        PendingIntent refreshIntent=PendingIntent.getService(ConfigActivity.this, 0, intent, 0);  
        AlarmManager alarm=(AlarmManager)this.getSystemService(Context.ALARM_SERVICE);  
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, 0, 1200000, refreshIntent);  //设定一个更新频率
        this.startService(intent);
	}
	private void updateWidgetView(){
		System.out.println("record is null load from sdcard");
		Schedule schedule = RecordXmlHandler.getLessonsFromXML();
		record=new SCAURecord("now date",schedule);//注意：第一个参数待定。
		if(record != null){
			setLessonService();
			Intent resultValue = new Intent();
			resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
			setResult(RESULT_OK, resultValue);
			finish();
		}
	}
}
