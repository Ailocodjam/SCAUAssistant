package org.scauhci.studentAssistant.main;

import java.util.List;

import org.scauhci.studentAssistant.concrete.SCAURecordFactory;
import org.scauhci.studentAssistant.concrete.SCAUWebFetch;
import org.scauhci.studentAssistant.concrete.SDCardManager;
import org.scauhci.studentAssistant.entity.Lesson;
import org.scauhci.studentAssistant.factory.RecordFactory;
import org.scauhci.studentAssistant.model.LessonRecord;
import org.scauhci.studentAssistant.model.WebFetch;
import org.scauhci.studentAssistant.widget.LessonAdapter;
import org.scauhci.studentAssistant.widget.WidgetMain;

import org.scauhci.studentAssistant.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class LessonActivity extends Activity {

	private String[] day_title = { "星期一", "星期二", "星期三", "星期四", "星期五",
			"星期六" , "星期日"};
	private ViewFlipper flipper = null;
	private TextView title = null;
	private int num;
	private LessonRecord record;
	private RecordFactory factory;
	private Spinner yearsSpinner;
	private Spinner semestersSpinner;
	private String year;
	private String semester;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.detail_layout);
		factory = MainActivity.getFactory();
		// record = SDCardManager.getLessonRecord();
		record = getRecordFromFactory(null, null);
		if (record == null) {
			//finish();
			System.out.println("lesson record is null");
			return;
		}
		initView();

		ImageButton left_bt = (ImageButton) findViewById(R.id.bt_left);
		ImageButton right_bt = (ImageButton) findViewById(R.id.bt_right);

		left_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				flipper.setInAnimation(LessonActivity.this, R.anim.push_left_in);
				flipper.setOutAnimation(LessonActivity.this,
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
				flipper.setInAnimation(LessonActivity.this,
						R.anim.push_right_in);
				flipper.setOutAnimation(LessonActivity.this,
						R.anim.push_right_out);
				flipper.showNext();
				num = (num + 1) % 7;
				title.setText(day_title[num]);
			}
		});
	}

	public LessonRecord getRecordFromFactory(String semester, String year) {
		List<Lesson> lessons = null;
		try {
			lessons = factory.getDao().getLessonsFromNetwork(semester, year,true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			finish();
		}
		if(lessons==null){
			return null;
		}
		return factory.createRecord(lessons, semester, year,"");
	}

	public void initView() {
		num=1;
		ListView[] listView = new ListView[7];
		for (int i = 0; i < 7; i++) {
			listView[i] = new ListView(LessonActivity.this);
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
		flipper.removeAllViews();
		for (int i = 0; i < listView.length; i++) {
			flipper.addView(listView[i]);
		}
		for (int i = 0; i < num; i++) {
			flipper.showNext();
		}
		title = (TextView) findViewById(R.id.title);
		title.setText(day_title[num]);
	}

	private static final int MENU_SELECT = 1;
	private static final int MENU_HELP = 2;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, MENU_SELECT, 0, "选项");
		menu.add(0, MENU_HELP, 0, "帮助");
		return super.onCreateOptionsMenu(menu);
	}

	public void initSpinner(LinearLayout layout) {
		yearsSpinner = (Spinner) layout.findViewById(R.id.select_years);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.years, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		if (yearsSpinner == null) {
			System.out.println("yearSpinner is null");
		}
		yearsSpinner.setAdapter(adapter);
		semestersSpinner = (Spinner) layout.findViewById(R.id.select_semester);
		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
				this, R.array.semesters, android.R.layout.simple_spinner_item);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		semestersSpinner.setAdapter(adapter2);
		yearsSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View arg1,
					int pos, long arg3) {
				year = parent.getItemAtPosition(pos).toString();
				System.out.println("year:" + year);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		semestersSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View arg1, int pos, long arg3) {
						semester = parent.getItemAtPosition(pos).toString();
						System.out.println("semester:" + semester);
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case MENU_SELECT:
			Builder builder = new AlertDialog.Builder(this);
			LinearLayout selectLayout = (LinearLayout) getLayoutInflater()
					.inflate(R.layout.lesson_items, null);
			initSpinner(selectLayout);

			builder.setView(selectLayout);
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							String year = (String) yearsSpinner
									.getSelectedItem();
							String semester = (String) semestersSpinner
									.getSelectedItem();
							System.out.println("year:" + year + "semester:"
									+ semester);
							record = getRecordFromFactory(semester, year);
							if (record == null) {
								System.out.println("lesson record is null");
								//finish();
								return;
							}
							initView();
						}
					});
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

						}

					});
			builder.create().show();
			return true;
		}
		return false;
	}

}
