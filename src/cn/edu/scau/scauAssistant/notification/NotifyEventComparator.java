package cn.edu.scau.scauAssistant.notification;

import java.util.Comparator;

public class NotifyEventComparator implements Comparator<NotifyEvent> {

	@Override
	public int compare(NotifyEvent ne1, NotifyEvent ne2) {
		String ne1DateAndTime=ne1.getNotifyDate()+" "+ne1.getNotifyTime();
		String ne2DateAndTime=ne2.getNotifyDate()+" "+ne2.getNotifyTime();
		return ne1DateAndTime.compareTo(ne2DateAndTime);
	}

}
