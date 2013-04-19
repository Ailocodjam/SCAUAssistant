package org.scauhci.studentAssistant.main;

import org.scauhci.studentAssistant.R;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

public class CustomDialog {
	private Context context;
	private PopupWindow popupWindow;
	private TextView title, message;
	private Button yes, no;

	public CustomDialog(Context context) {
		this.context = context;

		View view = LayoutInflater.from(this.context).inflate(
				R.layout.custom_dialog, null);
		title = (TextView) view.findViewById(R.id.dialog_title);
		message = (TextView) view.findViewById(R.id.dialog_message);
		yes = (Button) view.findViewById(R.id.dialog_yes);
		no = (Button) view.findViewById(R.id.dialog_no);

		popupWindow = new PopupWindow(view, LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
	}

	// ����Yes��ť
	public void setPositiveButton(boolean show_or_not,
			View.OnClickListener listener) {
		if (show_or_not) {
			this.yes.setVisibility(View.VISIBLE);
		} else {
			this.yes.setVisibility(View.GONE);
		}
		if (listener != null) {
			this.yes.setOnClickListener(listener);
		} else {
			this.yes.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dismiss();
				}
			});
		}
	}

	// ����No��ť
	public void setNegativeButton(boolean show_or_not,
			View.OnClickListener listener) {
		if (show_or_not) {
			this.no.setVisibility(View.VISIBLE);
		} else {
			this.no.setVisibility(View.GONE);
		}
		if (listener != null) {
			this.no.setOnClickListener(listener);
		} else {
			this.no.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dismiss();
				}
			});
		}
	}

	// ���ñ�����
	public void setTitle(String title) {
		this.title.setText(title);
	}

	// ������ʾ����Ϣ
	public void setMessage(String message) {
		this.message.setText(message);
	}

	// ���öԻ���Ķ������
	public void setAnimationStyle(int animation) {
		popupWindow.setAnimationStyle(animation);
	}

	// ���ڸ����ؼ���ʾ
	public void showAsDropDown(View parent) {
		// ���ڸ����ؼ���ʾ
		popupWindow.showAsDropDown(parent);
		// ʹ��ۼ�
		popupWindow.setFocusable(true);
		// ����������������ʧ
		popupWindow.setOutsideTouchable(true);
		// ˢ��״̬
		popupWindow.update();
	}

	// ����Ļ�о�����ʾ
	public void showAtCenter() {
		popupWindow.showAtLocation(new View(context), Gravity.CENTER, 0, 0);
		// ʹ��ۼ�
		popupWindow.setFocusable(true);
		// ����������������ʧ
		popupWindow.setOutsideTouchable(true);
		// ˢ��״̬
		popupWindow.update();
	}

	// ���ضԻ���
	public void dismiss() {
		popupWindow.dismiss();
	}
}
