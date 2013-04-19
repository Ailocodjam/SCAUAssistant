package org.scauhci.studentAssistant.main;

import org.scauhci.studentAssistant.R;

import cn.edu.scau.scauAssistant.notification.NotifyEvent;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class NotifyEventDetailActivity extends Activity {

	private NotifyEvent notifyEvent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.notify_detail);
		Bundle bundle=this.getIntent().getExtras();
		notifyEvent=(NotifyEvent)bundle.getSerializable("notifyEvent");
		
		TextView notifyTitle=(TextView)findViewById(R.id.notifyTitle);
		notifyTitle.setText(notifyEvent.getTitle());
		
		TextView notifyDate=(TextView)findViewById(R.id.notifyDate);
		notifyDate.setText(notifyEvent.getNotifyDate());
		
		TextView notifyTime=(TextView)findViewById(R.id.notifyTime);
		notifyTime.setText(notifyEvent.getNotifyTime());
		
		TextView notifyContent=(TextView)findViewById(R.id.notifyContent);
		notifyContent.setText(notifyEvent.getContent());
		
		
		/** ·µ»Ø°´Å¥  **/
		Button back = (Button)findViewById(R.id.backButton);
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
			}
		});
	}
	
	
}
