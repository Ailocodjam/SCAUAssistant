package org.scauhci.studentAssistant.main;

import java.util.List;

import org.scauhci.studentAssistant.R;
import org.scauhci.studentAssistant.entity.ExamInformation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ExamInfoAdaptor extends BaseAdapter {
	
	private List<ExamInformation> examInfos;
	private LayoutInflater inflater;
	private Context mainContext = null;
	

	public ExamInfoAdaptor(List<ExamInformation> examInfos, Context mainContext) {
		super();
		this.examInfos = examInfos;
		this.mainContext = mainContext;
		this.inflater = LayoutInflater.from(mainContext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return examInfos.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return examInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		if (view == null) {
			// 取得要显示的行View
			view = inflater.inflate(R.layout.list, null);
			//Log.d("MyDebug", "we have to new another view");
		}
		TextView tv=(TextView)view.findViewById(R.id.scoreText);
		tv.setText(examInfos.get(position).getCourse_name());
		final int ind=position;
		tv.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(mainContext, ExamInfoDetail.class);
				Bundle bd = new Bundle();
				bd.putSerializable("examInformation", examInfos.get(ind));
				
				intent.putExtras(bd);
				mainContext.startActivity(intent);
			}});
		return view;
	}

}
