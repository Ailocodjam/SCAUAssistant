package org.scauhci.studentAssistant.main;

import java.util.ArrayList;
import java.util.List;

import org.scauhci.studentAssistant.R;
import org.scauhci.studentAssistant.entity.Score;
import org.scauhci.studentAssistant.model.WebFetch;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class ScoreActivity extends ListActivity {

	private List<Score> scores;
	private WebFetch webFetch;
	
	private Spinner yearsSpinner;
	private Spinner semestersSpinner;
	private String year;
	private String semester;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.list);

		webFetch = MainActivity.getFactory().getDao();
		initView(null,null);
	}
	
	public void initView(String semester,String year){
		try {
			scores = webFetch.getScoresFromNetwork(semester,year, null);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TextView scoreText = (TextView) findViewById(R.id.scoreText);
		if (scores == null) {
			scoreText.setText("当前学期还没有成绩！");
			scores = new ArrayList<Score>();
		} else {
			scoreText.setText(year+"年 第"+semester+"学期 成绩：");
		}
		ScoreAdaptor adaptor = new ScoreAdaptor(scores, this);
		setListAdapter(adaptor);
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
						initView(semester,year);
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
		case MENU_HELP:
			
			return true;
		}
		return false;
	}


}
