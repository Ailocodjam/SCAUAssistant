package org.scauhci.studentAssistant.main;

import org.scauhci.studentAssistant.R;
import org.scauhci.studentAssistant.entity.ExamInformation;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class ExamInfoDetail extends Activity{
	
	private ExamInformation examInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.examinfo_detail);
		Bundle bundle=this.getIntent().getExtras();
		examInfo=(ExamInformation)bundle.getSerializable("examInformation");
		TextView course_name=(TextView)findViewById(R.id.course_name);
		course_name.setText(examInfo.getCourse_name());
		TextView time=(TextView)findViewById(R.id.exam_time);
		time.setText(examInfo.getTime());
		TextView place=(TextView)findViewById(R.id.exam_place);
		place.setText(examInfo.getPlace());
		TextView exam_style=(TextView)findViewById(R.id.exam_style);
		exam_style.setText(examInfo.getExam_style());
		TextView seat=(TextView)findViewById(R.id.exam_seat);
		seat.setText(examInfo.getSeat());
		TextView school_area=(TextView)findViewById(R.id.school_area);
		school_area.setText(examInfo.getSchool_area());
	}

	
}
