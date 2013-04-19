package org.scauhci.studentAssistant.listener;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;

public class BottomTabClickListener implements OnClickListener {

	private int index = 0;
	private ViewPager mTabPager;

	public BottomTabClickListener(int i,ViewPager viewPager) {
		index = i;
		mTabPager=viewPager;
	}

	@Override
	public void onClick(View v) {
		mTabPager.setCurrentItem(index);
	}

}
