package org.scauhci.studentAssistant.widget;

import java.util.List;

import org.scauhci.studentAssistant.entity.Lesson;

import org.scauhci.studentAssistant.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LessonAdapter extends BaseAdapter{
	private List<Lesson> myList = null;
	private Context context;
	
	public LessonAdapter(List<Lesson> list,Context context){
		myList = list;
		this.context=context;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return myList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View myView = convertView;
		if (myView == null) {
			myView = LayoutInflater.from(context).inflate(R.layout.detail, null);
		}
		Lesson lesson = myList.get(position);
		TextView name = (TextView) myView.findViewById(R.id.name);
		
		name.setText(lesson.getName());
		TextView teacher = (TextView) myView.findViewById(R.id.teacher);
		
		teacher.setText(lesson.getTeacher());
		TextView classroom = (TextView) myView.findViewById(R.id.classroom);
		classroom.setText(lesson.getClassroom());
		
		TextView where = (TextView) myView.findViewById(R.id.where);
		where.setText(lesson.getWeekday() + lesson.getLessonTime());
		
		TextView week = (TextView) myView.findViewById(R.id.week);
		week.setText(lesson.getWeek());

		return myView;
	}
	
}
