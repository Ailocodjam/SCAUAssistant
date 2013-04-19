package org.scauhci.studentAssistant.listener;

import org.scauhci.studentAssistant.R;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class MainViewPageChangeListener implements OnPageChangeListener {
	
	private ImageView mTabImg;// ����ͼƬ
	private ImageView homeTab, classifyTab, lessonsTab, settingTab;
	private int zero = 0;// ����ͼƬƫ����
	private int currIndex = 0;// ��ǰҳ�����
	private int one;// ����ˮƽ����λ��
	private int two;
	private int three;
	
	
	private Resources resources;
	
	public MainViewPageChangeListener(Context context,int displayWidth,int displayHeight,ImageView homeTab,
			ImageView classifyTab,ImageView lessonsTab,ImageView settingTab,ImageView mTabImg){
		one = displayWidth / 4; // ����ˮƽ����ƽ�ƴ�С
		two = one * 2;
		three = one * 3;
		Log.i("info", "��ȡ����Ļ�ֱ���Ϊ" + one+" " + two+" " + three+" " + " X " + displayHeight);
		
		resources=context.getResources();

		this.homeTab=homeTab;
		this.classifyTab=classifyTab;
		this.lessonsTab=lessonsTab;
		this.settingTab=settingTab;
		this.mTabImg=mTabImg;
	}
	
	@Override
	public void onPageSelected(int arg0) {
		Animation animation = null;
		switch (arg0) {
		case 0:
			homeTab.setImageDrawable(resources.getDrawable(
					R.drawable.tab_home_pressed));
			if (currIndex == 1) {
				animation = new TranslateAnimation(one, 0, 0, 0);
				classifyTab.setImageDrawable(resources.getDrawable(
						R.drawable.tab_classify_normal));
			} else if (currIndex == 2) {
				animation = new TranslateAnimation(two, 0, 0, 0);
				lessonsTab.setImageDrawable(resources.getDrawable(
						R.drawable.tab_lessons_normal));
			} else if (currIndex == 3) {
				animation = new TranslateAnimation(three, 0, 0, 0);
				settingTab.setImageDrawable(resources.getDrawable(
						R.drawable.tab_settings_normal));
			}
			break;
		case 1:
			classifyTab.setImageDrawable(resources.getDrawable(
					R.drawable.tab_classify_pressed));
			if (currIndex == 0) {
				animation = new TranslateAnimation(zero, one, 0, 0);
				homeTab.setImageDrawable(resources.getDrawable(
						R.drawable.tab_home_normal));
			} else if (currIndex == 2) {
				animation = new TranslateAnimation(two, one, 0, 0);
				lessonsTab.setImageDrawable(resources.getDrawable(
						R.drawable.tab_lessons_normal));
			} else if (currIndex == 3) {
				animation = new TranslateAnimation(three, one, 0, 0);
				settingTab.setImageDrawable(resources.getDrawable(
						R.drawable.tab_settings_normal));
			}
			break;
		case 2:
			lessonsTab.setImageDrawable(resources.getDrawable(
					R.drawable.tab_lessons_pressed));
			if (currIndex == 0) {
				animation = new TranslateAnimation(zero, two, 0, 0);
				homeTab.setImageDrawable(resources.getDrawable(
						R.drawable.tab_home_normal));
			} else if (currIndex == 1) {
				animation = new TranslateAnimation(one, two, 0, 0);
				classifyTab.setImageDrawable(resources.getDrawable(
						R.drawable.tab_classify_normal));
			} else if (currIndex == 3) {
				animation = new TranslateAnimation(three, two, 0, 0);
				settingTab.setImageDrawable(resources.getDrawable(
						R.drawable.tab_settings_normal));
			}
			break;
		case 3:
			settingTab.setImageDrawable(resources.getDrawable(
					R.drawable.tab_settings_pressed));
			if (currIndex == 0) {
				animation = new TranslateAnimation(zero, three, 0, 0);
				homeTab.setImageDrawable(resources.getDrawable(
						R.drawable.tab_home_normal));
			} else if (currIndex == 1) {
				animation = new TranslateAnimation(one, three, 0, 0);
				classifyTab.setImageDrawable(resources.getDrawable(
						R.drawable.tab_classify_normal));
			} else if (currIndex == 2) {
				animation = new TranslateAnimation(two, three, 0, 0);
				lessonsTab.setImageDrawable(resources.getDrawable(
						R.drawable.tab_lessons_normal));
			}
			break;
		}
		currIndex = arg0;
		animation.setFillAfter(true);// True:ͼƬͣ�ڶ�������λ��
		animation.setDuration(150);
		mTabImg.startAnimation(animation);
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}
}
