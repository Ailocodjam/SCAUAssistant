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
	
	//百度MapAPI的管理类
	public static BMapManager mBMapMan = null;
	
	// 授权Key
	public String mStrKey = "FB9580F61C0F1B9C034A970A660212B493F38B83";
	boolean m_bKeyRight = true;	// 授权Key正确，验证通过
	
	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
	public static class MyGeneralListener implements MKGeneralListener {
		@Override
		public void onGetNetworkState(int iError) {
			Log.d("MyGeneralListener", "onGetNetworkState error is "+ iError);
			Toast.makeText(AssistantApplication.assistantApp.getApplicationContext(), "您的网络出错啦！",
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void onGetPermissionState(int iError) {
			Log.d("MyGeneralListener", "onGetPermissionState error is "+ iError);
			if (iError ==  MKEvent.ERROR_PERMISSION_DENIED) {
				// 授权Key错误：
				Toast.makeText(AssistantApplication.assistantApp.getApplicationContext(), 
						"请在BMapApiDemoApp.java文件输入正确的授权Key！",
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
		// 初始化地图sdk成功，设置定位监听时间
		if (isSuccess) {
		    mBMapMan.getLocationManager().setNotifyInternal(10, 5);
		}else {
			System.out.println("地图sdk初始化失败，不能使用sdk");
		}
		
		notifyEvents=new ArrayList<NotifyEvent>();
	}
	

	@Override
	//建议在您app的退出之前调用mapadpi的destroy()函数，避免重复初始化带来的时间消耗
	public void onTerminate() {
		// TODO Auto-generated method stub
		if (mBMapMan != null) {
			mBMapMan.destroy();
			mBMapMan = null;
		}
		super.onTerminate();
	}
}
