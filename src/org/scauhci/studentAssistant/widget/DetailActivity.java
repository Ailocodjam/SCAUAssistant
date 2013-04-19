package org.scauhci.studentAssistant.widget;

//import org.market.feedback.FeedBackUtil;
//import org.market.update.UpdateUtil;

import java.util.Date;
import java.util.List;

import org.scauhci.studentAssistant.concrete.SCAURecordFactory;
import org.scauhci.studentAssistant.entity.Lesson;
import org.scauhci.studentAssistant.model.LessonRecord;

import org.scauhci.studentAssistant.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class DetailActivity extends Activity implements OnTouchListener,
		OnGestureListener {

	private DetailActivity context = this;
	private String[] day_title = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
	private ViewFlipper flipper = null;
	private TextView title = null;
	private int num;
	private LessonRecord record;
	private GestureDetector mGestureDetector;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.detail_layout);
		mGestureDetector = new GestureDetector(this);
		mGestureDetector.setIsLongpressEnabled(true);
		Date now = new Date();
		num = now.getDay();
		record = WidgetMain.getRecord();
		if (record == null) {
			System.out.println("record is null");
			finish();
			return ;
		}
		initView();
		for (int i = 0; i < num; i++) {
			flipper.showNext();
		}
		title = (TextView) findViewById(R.id.title);
		title.setText(day_title[num]);

		ImageButton left_bt = (ImageButton) findViewById(R.id.bt_left);
		ImageButton right_bt = (ImageButton) findViewById(R.id.bt_right);

		left_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				flipper.setInAnimation(DetailActivity.this, R.anim.push_left_in);
				flipper.setOutAnimation(DetailActivity.this,
						R.anim.push_left_out);
				flipper.showPrevious();
				num = (num + 6) % 7;
				title.setText(day_title[num]);
			}
		});

		right_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				flipper.setInAnimation(DetailActivity.this,
						R.anim.push_right_in);
				flipper.setOutAnimation(DetailActivity.this,
						R.anim.push_right_out);
				flipper.showNext();
				num = (num + 1) % 7;
				title.setText(day_title[num]);
			}
		});
	}

	public void initView() {
		ListView[] listView = new ListView[7];
		for (int i = 0; i < 7; i++) {
			listView[i] = new ListView(DetailActivity.this);
			listView[i].setDivider(getResources().getDrawable(R.color.green));
			listView[i].setDividerHeight(3);
			List<Lesson> lessons = record.getLessonsByDay(i);
			if (lessons.size() == 0) {
				TextView textTitle = new TextView(this);
				textTitle.setText("今天没课!");
				textTitle.setGravity(Gravity.CENTER);
				textTitle.setTextSize(19);
				listView[i].addHeaderView(textTitle);
			}
			listView[i].setAdapter(new LessonAdapter(lessons, this));
		}
		flipper = (ViewFlipper) findViewById(R.id.flipper);
		for (int i = 0; i < listView.length; i++) {
			flipper.addView(listView[i]);
		}
	}

	public static final int MESSAGE_TOAST = 1;
	public static final int MESSAGE_UPDATE = 2;
	public static final int MESSAGE_FEEDBACK = 3;
	private final Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_TOAST:
				String toastMessage = msg.getData().getString("toast");
				Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT);
				break;
			case MESSAGE_UPDATE:
				// int appId = UpdateUtil.checkForUpdate(context, context
				// .getPackageName());
				// if (appId != -1) {
				// UpdateUtil.showUpdateDialog(context, context
				// .getString(R.string.app_name), appId);
				// }
				break;
			case MESSAGE_FEEDBACK:
				// FeedBackUtil.showFeedBackDialog(context,
				// context.getPackageName());
				break;
			default:
				break;
			}
		}
	};

	private final int MENU_UPDATE = 1;
	private final int MENU_ABOUT = 2;
	private final int MENU_FEEDBACK = 3;

	// 创建菜单的两个方法
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		menu.add(0, MENU_UPDATE, 1, "更新");
		menu.add(0, MENU_ABOUT, 3, "关于");
		menu.add(0, MENU_FEEDBACK, 2, "反馈");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case MENU_UPDATE:
			mHandler.obtainMessage(MESSAGE_UPDATE).sendToTarget();
			return true;

		case MENU_ABOUT:
			new AlertDialog.Builder(this).setTitle("关于")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setMessage("华农学生课表widget版！")
					.setPositiveButton("知道了", null).show();
			return true;

		case MENU_FEEDBACK:
			mHandler.obtainMessage(MESSAGE_FEEDBACK).sendToTarget();
			return true;
		}
		return false;
	}

	

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	private void showNext() {

		flipper.setInAnimation(DetailActivity.this, R.anim.push_right_in);
		flipper.setOutAnimation(DetailActivity.this, R.anim.push_right_out);
		flipper.showNext();
		num = (num + 1) % 7;
		title.setText(day_title[num]);
	}

	private void showPrevious() {
		flipper.setInAnimation(DetailActivity.this, R.anim.push_left_in);
		flipper.setOutAnimation(DetailActivity.this, R.anim.push_left_out);
		flipper.showPrevious();
		num = (num + 6) % 7;
		title.setText(day_title[num]);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		Log.i("golf", "fling…");
		if (e1.getX() > e2.getX() && e1.getX() - e2.getX() > 30) {
			Log.d("MyDebug", "turn previous");
			showPrevious();
		} else if (e1.getX() < e2.getX() && e2.getX() - e1.getX() > 30) {
			Log.d("MyDebug", "turn next");
			showNext();
		} else {
			Log.d("MyDebug", "ele here no turning");
			return false;
		}
		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		Log.i("golf", "scrolling…");
		if (e1.getX() > e2.getX() && e1.getX() - e2.getX() > 30) {
			Log.d("MyDebug", "turn previous");
			showPrevious();
		} else if (e1.getX() < e2.getX() && e2.getX() - e1.getX() > 30) {
			Log.d("MyDebug", "turn next");
			showNext();
		} else {
			Log.d("MyDebug", "ele here no turning");
			return false;
		}
		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return mGestureDetector.onTouchEvent(event);
	}

}
