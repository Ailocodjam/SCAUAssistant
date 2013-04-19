package org.scauhci.studentAssistant.main;

import java.util.List;

import org.scauhci.studentAssistant.entity.Score;

import org.scauhci.studentAssistant.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ScoreAdaptor extends BaseAdapter {
	
	private LayoutInflater inflater;
	private Context mainContext = null;
	private List<Score> scores;
	
	

	public ScoreAdaptor(List<Score> scores,Context context) {
		super();
		this.mainContext=context;
		this.scores = scores;
		this.inflater = LayoutInflater.from(mainContext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return scores.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return scores.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int index, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View view = arg1;
		if (view == null) {
			// 取得要显示的行View
			view = inflater.inflate(R.layout.list, null);
			//Log.d("MyDebug", "we have to new another view");
		}
		TextView tv=(TextView)view.findViewById(R.id.scoreText);
		tv.setText(scores.get(index).getName());
		final int ind=index;
		tv.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(mainContext, ScoreDetail.class);
				Bundle bd = new Bundle();
				bd.putSerializable("score", scores.get(ind));
				
				intent.putExtras(bd);
				mainContext.startActivity(intent);
			}});
		return view;
	}

}
