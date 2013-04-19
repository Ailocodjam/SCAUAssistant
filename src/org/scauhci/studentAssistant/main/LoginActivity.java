package org.scauhci.studentAssistant.main;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.scauhci.studentAssistant.common.RecordXmlHandler;
import org.scauhci.studentAssistant.concrete.SCAURecordFactory;
import org.scauhci.studentAssistant.concrete.SCAUWebFetch;
import org.scauhci.studentAssistant.concrete.SDCardManager;
import org.scauhci.studentAssistant.entity.Lesson;
import org.scauhci.studentAssistant.entity.Schedule;
import org.scauhci.studentAssistant.factory.RecordFactory;
import org.scauhci.studentAssistant.model.LessonRecord;
import org.scauhci.studentAssistant.model.WebFetch;
import org.scauhci.studentAssistant.widget.ConfigActivity;

import org.scauhci.studentAssistant.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class LoginActivity extends Activity {
	private Button loginButton;
	private EditText idEdit;
	private EditText passwordEdit;
	private EditText codeEdit;
	private ImageView verificationCode;
	private String id = "";
	private String password = "";
	private String code = "";
	private WebFetch dao;
	private RecordFactory factory;
	private Bitmap codeBitmap=null;
	private LessonRecord record;

	private final int NETWORK_ERROR = 0;
	private final int ID_ERROR = 1;
	private final int NETWORK_OK = 2;
	private final int LOAD_CODE_SUCCESS=3;
	private final int LOGIN_SUCCESS=4;
	private final int LOAD_LESSONS_ERROR=5;
	private final int LOAD_LESSONS_SUCCESS=6;
	private final int WRITE_FILE_TO_SDCARD_ERROR=7;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.main_login);
		
		//AssistantApplication application=(AssistantApplication) getApplicationContext();
		dao = new SCAUWebFetch();
		factory = new SCAURecordFactory("scau", dao);

		verificationCode = (ImageView) findViewById(R.id.login_verificationCode);

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					InputStream bitmapInput = dao.getVerificationCode();
					
					codeBitmap = BitmapFactory.decodeStream(bitmapInput);
					handler.sendEmptyMessage(LOAD_CODE_SUCCESS);
				} catch (ClientProtocolException e) {
					System.out.println("exception while loading verificationCode img");
					e.printStackTrace();
					handler.sendEmptyMessage(NETWORK_ERROR);
				} catch (IOException e) {
					System.out.println("exception while loading verificationCode img");
					e.printStackTrace();
					handler.sendEmptyMessage(NETWORK_ERROR);
				} catch (Exception ex) {
					System.out.println("exception while loading verificationCode img");
					ex.printStackTrace();
					handler.sendEmptyMessage(NETWORK_ERROR);
				}
			}

		}).start();

		idEdit = (EditText) findViewById(R.id.login_user_edit);
		passwordEdit = (EditText) findViewById(R.id.login_passwd_edit);
		codeEdit = (EditText) findViewById(R.id.login_code_edit);

		loginButton = (Button) findViewById(R.id.login_login_btn);

		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				password = passwordEdit.getText().toString();
				id = idEdit.getText().toString();
				code = codeEdit.getText().toString();
				login();
			}
		});

		/** 返回按钮 **/
		Button back = (Button) findViewById(R.id.login_reback_btn);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.push_down_in,
						R.anim.push_down_out);
			}
		});
	}

	public void login() {
		new Thread(new Runnable(){

			@Override
			public void run() {
				try {
					if (!dao.login(id, password, "学生",code)){
						handler.sendEmptyMessage(ID_ERROR);
					}
					handler.sendEmptyMessage(LOGIN_SUCCESS);
				} catch (ClientProtocolException e) {
					e.printStackTrace();
					handler.sendEmptyMessage(ID_ERROR);
				} catch (IOException e) {
					e.printStackTrace();
					handler.sendEmptyMessage(NETWORK_ERROR);
				} catch (Exception e) {
					e.printStackTrace();
					handler.sendEmptyMessage(NETWORK_ERROR);
				}
			}
			
		}).start();
	}
	
	private Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case NETWORK_ERROR:
				new AlertDialog.Builder(LoginActivity.this).setTitle("网络出错")
			    .setMessage("网络出错了,请检查网络设置，然后重试!").setPositiveButton("知道了", null).show();
			    break;
			case ID_ERROR:
				new AlertDialog.Builder(LoginActivity.this).setTitle("登录出错")
				.setMessage("用户名，密码，验证码出错了，请检查！")
				.setPositiveButton("知道了", null).show();
				break;
			case LOAD_CODE_SUCCESS:
				verificationCode.setImageBitmap(codeBitmap);
				break;
			case LOGIN_SUCCESS:
				System.out.println("login success");
				getLessons();
				break;
			case LOAD_LESSONS_ERROR:
				new AlertDialog.Builder(LoginActivity.this).setTitle("获取课表出错")
				.setMessage("获取课表出错了，请重试！")
				.setPositiveButton("知道了", null).show();
				break;
			case WRITE_FILE_TO_SDCARD_ERROR:
				new AlertDialog.Builder(LoginActivity.this).setTitle("文件写入错误")
				.setMessage("课表文件写入sdcard出错！写入目录：/sdcard/ScauAssistant/lessonRecord.xml")
				.setPositiveButton("知道了", null).show();
				break;
			case LOAD_LESSONS_SUCCESS:
				Intent intent=new Intent(LoginActivity.this,MainActivity.class);

				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
				break;
			}
		}
	};
	
	public void getLessons(){
		AssistantApplication application = (AssistantApplication) getApplicationContext();
		application.setLogin(true);
		new Thread(new Runnable(){

			@Override
			public void run() {
				List<Lesson> lessons = null;
				try {
					lessons = dao.getLessonsFromNetwork(null, null, true);
					record = factory.createRecord(lessons, null, null,id);
					String recordxml=RecordXmlHandler.getScheduleXmlString(record.getSchedule());
					System.out.println("recordxml:"+recordxml);
					if(RecordXmlHandler.writeXmlToFile(recordxml)){
						System.out.println("write file to sdcard success");
					}else{
						handler.sendEmptyMessage(WRITE_FILE_TO_SDCARD_ERROR);
					}
					handler.sendEmptyMessage(LOAD_LESSONS_SUCCESS);
				} catch (Exception e) {
					e.printStackTrace();
					handler.sendEmptyMessage(LOAD_LESSONS_ERROR);
				}
				
			}
		}).start();
		
		
	}

}
