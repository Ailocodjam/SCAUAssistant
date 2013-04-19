package org.scauhci.studentAssistant.main;

import org.scauhci.studentAssistant.R;
import org.scauhci.studentAssistant.entity.Score;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class ScoreDetail extends Activity {
	private Score score;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.score_detail);
		
		Bundle bundle=this.getIntent().getExtras();
		score=(Score)bundle.getSerializable("score");
		TextView name=(TextView)findViewById(R.id.score_name);
		name.setText(score.getName());
		TextView quality=(TextView)findViewById(R.id.score_quality);
		quality.setText(score.getQuality());
		TextView credit=(TextView)findViewById(R.id.score_credit);
		credit.setText(score.getCredit()+"");
		TextView score_point=(TextView)findViewById(R.id.score_point);
		score_point.setText(score.getScore_point()+"");
		TextView mark=(TextView)findViewById(R.id.score_mark);
		mark.setText(score.getMark()+"");
		TextView course_code=(TextView)findViewById(R.id.course_code);
		course_code.setText(score.getCourse_code());
		TextView college=(TextView)findViewById(R.id.college);
		college.setText(score.getCollege()+"");
		TextView auxiliary_mark=(TextView)findViewById(R.id.auxiliary_mark);
		auxiliary_mark.setText(score.getAuxiliary_mark()+"");
	}
	
}
