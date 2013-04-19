package org.scauhci.studentAssistant.main;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.scauhci.studentAssistant.common.RecordXmlHandler;
import org.scauhci.studentAssistant.concrete.SCAURecord;
import org.scauhci.studentAssistant.entity.Lesson;
import org.scauhci.studentAssistant.entity.Schedule;
import org.scauhci.studentAssistant.factory.RecordFactory;
import org.scauhci.studentAssistant.listener.BottomTabClickListener;
import org.scauhci.studentAssistant.listener.MainViewPageChangeListener;
import org.scauhci.studentAssistant.model.LessonRecord;
import org.scauhci.studentAssistant.widget.LessonAdapter;

import org.scauhci.studentAssistant.R;

import cn.edu.scau.scauAssistant.baidumap.BusLineSearch;
import cn.edu.scau.scauAssistant.baidumap.RoutePlan;
import cn.edu.scau.scauAssistant.service.LessonAlarmReceiver;
import cn.edu.scau.scauAssistant.service.NotifyEventAlarmReceiver;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class MainActivity extends Activity {
	private static RecordFactory factory;
	private ViewPager mTabPager;
	private PopupWindow menuWindow;
    
	private LessonRecord record;
	
	private static View lessonsView;
	private static SharedPreferences sharedPreferences;
	
	private AssistantApplication application=null;

	public static void launch(Context c) {
		Intent intent = new Intent(c, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		c.startActivity(intent);
	}

	public static RecordFactory getFactory() {
		return factory;
	}

	public static void setFactory(RecordFactory factory) {
		MainActivity.factory = factory;
	}

	public static void setLessonsView(View view) {
		lessonsView = view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.main);
		// 启动activity时不自动弹出软键盘
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		application=(AssistantApplication) getApplicationContext();

		sharedPreferences=getSharedPreferences("scau_assistant", Activity.MODE_PRIVATE);
		
		mTabPager = (ViewPager) findViewById(R.id.tabpager);
		
		
		Display currDisplay = getWindowManager().getDefaultDisplay();// 获取屏幕当前分辨率
		int displayWidth = currDisplay.getWidth();
		int displayHeight = currDisplay.getHeight();

		ImageView homeTab = (ImageView) findViewById(R.id.home_tab);
		ImageView classifyTab = (ImageView) findViewById(R.id.classify_tab);
		ImageView lessonsTab = (ImageView)findViewById(R.id.lessons_tab);
		ImageView settingTab = (ImageView) findViewById(R.id.setting_tab);
		ImageView mTabImg=(ImageView)findViewById(R.id.img_tab_now);
		
		MainViewPageChangeListener mPageChangeListener=new MainViewPageChangeListener(MainActivity.this,
				displayWidth,displayHeight,homeTab,classifyTab,lessonsTab,settingTab,mTabImg);
		mTabPager.setOnPageChangeListener(mPageChangeListener);
		
		
		
		homeTab.setOnClickListener(new BottomTabClickListener(0,mTabPager));
		classifyTab.setOnClickListener(new BottomTabClickListener(1,mTabPager));
		lessonsTab.setOnClickListener(new BottomTabClickListener(2,mTabPager));
		settingTab.setOnClickListener(new BottomTabClickListener(3,mTabPager));
		// InitImageView();//使用动画
		// 将要分页显示的View装入数组中
		LayoutInflater mLi = LayoutInflater.from(this);
		View homeView = mLi.inflate(R.layout.main_home, null);
		View notifyView = mLi.inflate(R.layout.main_classify, null);
		View settingView = mLi.inflate(R.layout.main_setting, null);
		
		initLessonsView(mLi);  //初始化课表界面
		initNotifyView(notifyView);//初始化提醒事务界面
		initSettingView(settingView);  //初始化设置界面
		initHomeView(homeView);//初始化公交查询界面
		

		// 每个页面的view数据
		final ArrayList<View> views = new ArrayList<View>();
		views.add(homeView);
		views.add(notifyView);
		views.add(lessonsView);
		views.add(settingView);
		// 填充ViewPager的数据适配器
		PagerAdapter mPagerAdapter = new PagerAdapter() {

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return views.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager) container).removeView(views.get(position));
			}

			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager) container).addView(views.get(position));
				return views.get(position);
			}
		};

		mTabPager.setAdapter(mPagerAdapter);
		startLessonAlarm();
		startNotifyEventAlarm();
	}
	
	private void startNotifyEventAlarm(){
		AlarmManager aManager=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(this,NotifyEventAlarmReceiver.class);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(this, 0, intent, 0);
        aManager.setRepeating(AlarmManager.RTC, 0, 60*1000, pendingIntent);
	}
	
	private void startLessonAlarm(){
		
        AlarmManager aManager=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(this,LessonAlarmReceiver.class);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(this, 0, intent, 0);
        aManager.setRepeating(AlarmManager.RTC, 0, 60*1000, pendingIntent);
	}
	
	private void initHomeView(View homeView){
		LinearLayout buslineSearch=(LinearLayout) homeView.findViewById(R.id.buslineSearch);
		buslineSearch.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, BusLineSearch.class);
				startActivity(intent);
				overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
			}
		});
		LinearLayout routeplanSearch=(LinearLayout) homeView.findViewById(R.id.routeplanSearch);
		routeplanSearch.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, RoutePlan.class);
				startActivity(intent);
				overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
			}
		});
	}
	private void initSettingView(View settingView){
		//设置checkbox的sharedPreferences值
		final CheckBox isLessonsRemindStartCB=(CheckBox) settingView.findViewById(R.id.isLessonsRemindStart);
		boolean isLessonsRemindStart=sharedPreferences.getBoolean("isLessonsRemindStart", false);
		isLessonsRemindStartCB.setChecked(isLessonsRemindStart);
		isLessonsRemindStartCB.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				sharedPreferences.edit().putBoolean("isLessonsRemindStart", isLessonsRemindStartCB.isChecked()).commit();
			}
			
		});
		final CheckBox isNotifyEventOnCB=(CheckBox) settingView.findViewById(R.id.isNotifyEventOn);
		boolean isNotifyEventOn=sharedPreferences.getBoolean("isNotifyEventOn", false);
		isNotifyEventOnCB.setChecked(isNotifyEventOn);
		isNotifyEventOnCB.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				sharedPreferences.edit().putBoolean("isNotifyEventOn", isNotifyEventOnCB.isChecked()).commit();
			}
			
		});
		final CheckBox isRingNotifyOnCB=(CheckBox) settingView.findViewById(R.id.isRingNotifyOn);
		boolean isRingNotifyOn=sharedPreferences.getBoolean("isRingNotifyOn", false);
		isRingNotifyOnCB.setChecked(isRingNotifyOn);
		isRingNotifyOnCB.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				sharedPreferences.edit().putBoolean("isRingNotifyOn",isRingNotifyOnCB.isChecked()).commit();
			}
			
		});
		final CheckBox isHiberNotifyOnCB=(CheckBox) settingView.findViewById(R.id.isHiberNotifyOn);
		boolean isHiberNotifyOn=sharedPreferences.getBoolean("isHiberNotifyOn", false);
		isHiberNotifyOnCB.setChecked(isHiberNotifyOn);
		isHiberNotifyOnCB.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				sharedPreferences.edit().putBoolean("isHiberNotifyOn", isHiberNotifyOnCB.isChecked()).commit();
			}
			
		});
		Button exitButton=(Button)settingView.findViewById(R.id.exitButton);
		exitButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				CustomDialog exit = new CustomDialog(MainActivity.this);
				exit.setTitle("提示信息");
				exit.setMessage("确定要退出本应用么？");
				exit.setPositiveButton(true, new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						MainActivity.this.finish();
					}
				});
				exit.setNegativeButton(true, null);
				exit.setAnimationStyle(R.style.popupwindowAnim);
				exit.showAtCenter();
				
			}
			
		});
	}
	
	private void initNotifyView(View notifyView){
		ListView notify_events_list=(ListView) notifyView.findViewById(R.id.notify_events_list);
		NotifyEventListAdapter adapter=new NotifyEventListAdapter(this);
		notify_events_list.setAdapter(adapter);
		
		Button addEventBtn=(Button)notifyView.findViewById(R.id.add_event);
		addEventBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, AddNotifyEventActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
			}
			
		});
	}

	public void initLessonsView(LayoutInflater mLi) {
		if (RecordXmlHandler.isRecordXmlCache()) {
			Schedule schedule = RecordXmlHandler.getLessonsFromXML();
			record=new SCAURecord("now date",schedule);//注意：第一个参数待定。
			application.setRecord(record);
			createLessonsListView();
		} else {
			lessonsView = mLi.inflate(R.layout.main_lessons, null);
			LinearLayout scheduleFromFangzheng =(LinearLayout)lessonsView.findViewById(R.id.scheduleFromFangzheng);
			scheduleFromFangzheng.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(MainActivity.this, LoginActivity.class);
					startActivity(intent);
					overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
				}

			});
		}
	}
	
	private int lessonDayNum;
	public void createLessonsListView(){
		LayoutInflater mLi = LayoutInflater.from(this);
		lessonsView = mLi.inflate(R.layout.lessons_all, null);
		final String[] day_title = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		final ViewFlipper flipper = (ViewFlipper) lessonsView.findViewById(R.id.flipper);
		final TextView title = (TextView) lessonsView.findViewById(R.id.title);
		

		ImageButton left_bt = (ImageButton) lessonsView.findViewById(R.id.bt_left);
		ImageButton right_bt = (ImageButton) lessonsView.findViewById(R.id.bt_right);

		left_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				flipper.setInAnimation(MainActivity.this, R.anim.push_left_in);
				flipper.setOutAnimation(MainActivity.this,
						R.anim.push_left_out);
				flipper.showPrevious();
				lessonDayNum = (lessonDayNum + 6) % 7;
				title.setText(day_title[lessonDayNum]);
			}
		});

		right_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				flipper.setInAnimation(MainActivity.this,
						R.anim.push_right_in);
				flipper.setOutAnimation(MainActivity.this,
						R.anim.push_right_out);
				flipper.showNext();
				lessonDayNum = (lessonDayNum + 1) % 7;
				title.setText(day_title[lessonDayNum]);
			}
		});
			ListView[] listView = new ListView[7];
			for (int i = 0; i < 7; i++) {
				listView[i] = new ListView(this);
				listView[i].setDivider(getResources().getDrawable(R.color.green));
				listView[i].setDividerHeight(3);
				List<Lesson> lessons = record.getLessonsByDay(i);
				if (lessons.size() == 0) {
					TextView textTitle = new TextView(MainActivity.this);
					textTitle.setText("今天没课!");
					textTitle.setGravity(Gravity.CENTER);
					textTitle.setTextSize(19);
					listView[i].addHeaderView(textTitle);
				}
				listView[i].setAdapter(new LessonAdapter(lessons, MainActivity.this));
			}
			for (int i = 0; i < listView.length; i++) {
				flipper.addView(listView[i]);
			}
			Date now = new Date();
		    lessonDayNum = now.getDay();
			for (int i = 0; i < lessonDayNum; i++) {
				flipper.showNext();
			}
			title.setText(day_title[lessonDayNum]);
			Button updateLessons=(Button)lessonsView.findViewById(R.id.update_lessons);
			updateLessons.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(MainActivity.this, LoginActivity.class);
					startActivity(intent);
					overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
					
				}
			});
	}	

}
