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

	// 设置Yes按钮
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

	// 设置No按钮
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

	// 设置标题栏
	public void setTitle(String title) {
		this.title.setText(title);
	}

	// 设置显示的信息
	public void setMessage(String message) {
		this.message.setText(message);
	}

	// 设置对话框的动画风格
	public void setAnimationStyle(int animation) {
		popupWindow.setAnimationStyle(animation);
	}

	// 基于给定控件显示
	public void showAsDropDown(View parent) {
		// 基于给定控件显示
		popupWindow.showAsDropDown(parent);
		// 使其聚集
		popupWindow.setFocusable(true);
		// 设置允许在外点击消失
		popupWindow.setOutsideTouchable(true);
		// 刷新状态
		popupWindow.update();
	}

	// 在屏幕中居中显示
	public void showAtCenter() {
		popupWindow.showAtLocation(new View(context), Gravity.CENTER, 0, 0);
		// 使其聚集
		popupWindow.setFocusable(true);
		// 设置允许在外点击消失
		popupWindow.setOutsideTouchable(true);
		// 刷新状态
		popupWindow.update();
	}

	// 隐藏对话框
	public void dismiss() {
		popupWindow.dismiss();
	}
}
