package de.bruns.restrooms.android.data;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OpeningHoursData {

	private static String OPEN_CHARACTER = "Y";
	private static String CLOSE_CHARACTER = "N";

	private static Map<String, Integer> WEEKDAYS = new HashMap<String, Integer>();
	static {
		WEEKDAYS.put("MO", Integer.valueOf(0));
		WEEKDAYS.put("DI", Integer.valueOf(1));
		WEEKDAYS.put("MI", Integer.valueOf(2));
		WEEKDAYS.put("DO", Integer.valueOf(3));
		WEEKDAYS.put("FR", Integer.valueOf(4));
		WEEKDAYS.put("SA", Integer.valueOf(5));
		WEEKDAYS.put("SO", Integer.valueOf(6));
	}

	public static int MONDAY = 0;
	public static int TUESDAY = 1;
	public static int WEDNESDAY = 2;
	public static int THURSDAY = 3;
	public static int FRIDAY = 4;
	public static int SATUDAY = 5;
	public static int SUNDAY = 6;

	private boolean[][] openingHours;

	public OpeningHoursData() {
		openingHours = new boolean[7][48];
	}

	public OpeningHoursData(String openingHoursString) {
		this();

		for (int d = 0; d < 7; d++) {
			for (int h = 0; h < openingHours[d].length; h++) {
				openingHours[d][h] = false;
			}
		}

		String[] splitOpeningHoursStrings = openingHoursString.split(",");
		for (String splitOpeningHoursString : splitOpeningHoursStrings) {

			String splitOpeningHoursStringTrimmed = splitOpeningHoursString
					.trim();
			int firstIndexOfSpace = splitOpeningHoursStringTrimmed.indexOf(" ");
			String dayString = splitOpeningHoursStringTrimmed.substring(0,
					firstIndexOfSpace);
			String timeString = splitOpeningHoursStringTrimmed
					.substring(firstIndexOfSpace);

			String[] splitTimeIntervals = timeString.split("\\+");
			for (String splitTimeInterval : splitTimeIntervals) {
				String[] splitTimes = splitTimeInterval.trim().split("-");
				String startString = splitTimes[0].trim();
				int start = convertStringToHalfHours(startString);
				String endString = splitTimes[1].replace(" Uhr", "");
				int end = convertStringToHalfHours(endString);

				for (int d = 0; d < 7; d++) {
					boolean isOpenDay = isDayInDayString(dayString, d);
					for (int h = 0; h < openingHours[d].length; h++) {
						boolean isOpenHour = start <= h && h < end && isOpenDay;
						if (isOpenHour) {
							openingHours[d][h] = true;
						}
					}
				}
			}
		}
	}

	/**
	 * Example: "taeglich", "Mo-Fr", "Sa"
	 */
	private boolean isDayInDayString(String dayString, int dayIndex) {
		boolean result = false;
		String[] days = dayString.split("-");
		if (days.length == 2) {
			int startDay = WEEKDAYS.get(days[0].toUpperCase()).intValue();
			int endDay = WEEKDAYS.get(days[1].toUpperCase()).intValue();
			result = startDay <= dayIndex && dayIndex <= endDay;
		} else if (days.length == 1) {
			String dayAsUppercase = days[0].trim().toUpperCase();
			if (dayAsUppercase.equals("TAEGLICH")) {
				result = true;
			} else {
				result = WEEKDAYS.containsKey(dayAsUppercase)
						&& WEEKDAYS.get(dayAsUppercase).intValue() == dayIndex;
			}
		} else {
			throw new RuntimeException("Unparsable dayString: " + dayString);
		}
		return result;
	}

	private int convertStringToHalfHours(String timeString) {
		String doubleTimeString = timeString.replace(":", ".");
		String halfTimeString = doubleTimeString.replace(".30", ".50");
		return (int) (Double.parseDouble(halfTimeString) * 2);
	}

	public String asTestString(int weekday) {
		StringBuffer openString = new StringBuffer(48);
		for (int i = 0; i < openingHours[weekday].length; i++) {
			if (i != 0 && i % 6 == 0) {
				openString.append("|");
			}
			openString.append(openingHours[weekday][i] ? OPEN_CHARACTER
					: CLOSE_CHARACTER);
		}
		return openString.toString();
	}

	public boolean isOpen(Date currentTime) {
		Calendar instance = Calendar.getInstance();
		instance.setTime(currentTime);
		// -2 instead of +5 is correct, but the mod operation should return a positive number
		int dayOfWeekIndex = (instance.get(Calendar.DAY_OF_WEEK) + 5) % 7; 
		int hourOfDayIndex = (instance.get(Calendar.HOUR_OF_DAY) * 2);
		if (instance.get(Calendar.MINUTE) >= 30) {
			hourOfDayIndex = hourOfDayIndex + 1;
		}

		boolean[] openingHoursOfThisDay = openingHours[dayOfWeekIndex];
		return openingHoursOfThisDay[hourOfDayIndex];
	}
}
