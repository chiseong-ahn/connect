package com.scglab.connect.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
	
	public static String getToday() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
		String today = sdf.format(date);
		return today;
	}
	
	public static String getYesterday() {
		Date dDate = new Date();
		dDate = new Date(dDate.getTime()+(1000*60*60*24*-1));
		SimpleDateFormat dSdf = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
		String yesterday = dSdf.format(dDate);
		return yesterday;
	}
	
}
