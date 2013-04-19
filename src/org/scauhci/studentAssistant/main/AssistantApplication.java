package org.scauhci.studentAssistant.main;

import java.util.ArrayList;
import java.util.List;

import org.scauhci.studentAssistant.model.LessonRecord;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKEvent;
import com.baidu.mapapi.MKGeneralListener;

import cn.edu.scau.scauAssistant.notification.NotifyEvent;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class AssistantApplication extends Application {
	
	private LessonRecord record;
	private boolean isLogin=false;
	private List<NotifyEvent> notifyEvents;
	
	
	public List<NotifyEvent> getNotifyEvents() {
		return notifyEvents;
	}

	public void setNotifyEvents(List<NotifyEvent> notifyEvents) {
		this.notifyEvents = notifyEvents;
	}

	public LessonRecord getRecord() {
		return record;
	}

	public void setRecord(LessonRecord record) {
		this.record = record;
	}

	public boolean isLogin() {
		return isLogin;
	}

	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}
	
    public static AssistantApplication assistantApp;
	
	//�ٶ�MapAPI�Ĺ�����
	public static BMapManager mBMapMan = null;
	
	// ��ȨKey
	public String mStrKey = "FB9580F61C0F1B9C034A970A660212B493F38B83";
	boolean m_bKeyRight = true;	// ��ȨKey��ȷ����֤ͨ��
	
	// �����¼���������������ͨ�������������Ȩ��֤�����
	public static class MyGeneralListener implements MKGeneralListener {
		@Override
		public void onGetNetworkState(int iError) {
			Log.d("MyGeneralListener", "onGetNetworkState error is "+ iError);
			Toast.makeText(AssistantApplication.assistantApp.getApplicationContext(), "���������������",
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void onGetPermissionState(int iError) {
			Log.d("MyGeneralListener", "onGetPermissionState error is "+ iError);
			if (iError ==  MKEvent.ERROR_PERMISSION_DENIED) {
				// ��ȨKey����
				Toast.makeText(AssistantApplication.assistantApp.getApplicationContext(), 
						"����BMapApiDemoApp.java�ļ�������ȷ����ȨKey��",
						Toast.LENGTH_LONG).show();
				AssistantApplication.assistantApp.m_bKeyRight = false;
			}
		}
	}

	@Override
    public void onCreate() {
		super.onCreate();
		Log.v("BMapApiDemoApp", "onCreate");
		assistantApp = this;
		mBMapMan = new BMapManager(this);
		boolean isSuccess = mBMapMan.init(this.mStrKey, new MyGeneralListener());
		// ��ʼ����ͼsdk�ɹ������ö�λ����ʱ��
		if (isSuccess) {
		    mBMapMan.getLocationManager().setNotifyInternal(10, 5);
		}else {
			System.out.println("��ͼsdk��ʼ��ʧ�ܣ�����ʹ��sdk");
		}
		
		notifyEvents=new ArrayList<NotifyEvent>();
	}
	

	@Override
	//��������app���˳�֮ǰ����mapadpi��destroy()�����������ظ���ʼ��������ʱ������
	public void onTerminate() {
		// TODO Auto-generated method stub
		if (mBMapMan != null) {
			mBMapMan.destroy();
			mBMapMan = null;
		}
		super.onTerminate();
	}
}
