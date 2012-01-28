package de.bruns.restrooms.android.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

public class TimeService {

	private static TimeService instance;
	private static Date testTime;

	public static TimeService instance() {
		if (instance == null) {
			instance = new TimeService();
		}
		return instance;
	}
	
	public Date getTime() {
		return testTime == null ? new Date() : testTime;
	}
	
	public static void useTestTime() {
		DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.GERMAN);
		try {
			testTime = formatter.parse("28.01.12 15:25"); // SATURDAY
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

}
