/*
 * Copyright 2012 Andreas Bruns
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
