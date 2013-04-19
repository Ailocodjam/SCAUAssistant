package org.scauhci.studentAssistant.main;

import java.util.List;

import org.scauhci.studentAssistant.R;

import cn.edu.scau.scauAssistant.notification.NotifyEvent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NotifyEventListAdapter extends BaseAdapter{
	
	private Context context;
	private List<NotifyEvent> notifyEvents;
	
	

	public NotifyEventListAdapter(Context context) {
		super();
		this.context = context;
		AssistantApplication application=(AssistantApplication) context.getApplicationContext();
		notifyEvents=application.getNotifyEvents();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return notifyEvents.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(notifyEvents.size()==0){
			return null;
		}
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.notify_item, null);
		}
		NotifyEvent notifyEvent=notifyEvents.get(position);
		TextView notifyTitleText=(TextView) convertView.findViewById(R.id.notifyTitle);
		TextView notifyDateAndTimeText=(TextView) convertView.findViewById(R.id.notifyDateAndTime);
		
		notifyTitleText.setText(notifyEvent.getTitle());
		notifyDateAndTimeText.setText(notifyEvent.getNotifyDate()+" "+notifyEvent.getNotifyTime());
		LinearLayout notifyLayout=(LinearLayout) convertView.findViewById(R.id.notifyLayout);
		final int ind=position;
		notifyLayout.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(context, NotifyEventDetailActivity.class);
				Bundle bd = new Bundle();
				bd.putSerializable("notifyEvent", notifyEvents.get(ind));
				intent.putExtras(bd);
				context.startActivity(intent);
			}
			
		});
		return convertView;
	}

}
