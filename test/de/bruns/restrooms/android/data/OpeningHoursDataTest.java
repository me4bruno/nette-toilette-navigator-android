package de.bruns.restrooms.android.data;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Locale;

import junit.framework.TestCase;

public class OpeningHoursDataTest extends TestCase{

	private static final DateFormat FORMATTER = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.GERMAN);

	public void testDaily() throws IOException {
		OpeningHoursData data = new OpeningHoursData("taeglich 00:00-24:00 Uhr");
		assertEquals("YYYYYY|YYYYYY|YYYYYY|YYYYYY|YYYYYY|YYYYYY|YYYYYY|YYYYYY",
				data.asTestString(OpeningHoursData.MONDAY));
		assertEquals("YYYYYY|YYYYYY|YYYYYY|YYYYYY|YYYYYY|YYYYYY|YYYYYY|YYYYYY",
				data.asTestString(OpeningHoursData.TUESDAY));
		assertEquals("YYYYYY|YYYYYY|YYYYYY|YYYYYY|YYYYYY|YYYYYY|YYYYYY|YYYYYY",
				data.asTestString(OpeningHoursData.WEDNESDAY));
		assertEquals("YYYYYY|YYYYYY|YYYYYY|YYYYYY|YYYYYY|YYYYYY|YYYYYY|YYYYYY",
				data.asTestString(OpeningHoursData.THURSDAY));
		assertEquals("YYYYYY|YYYYYY|YYYYYY|YYYYYY|YYYYYY|YYYYYY|YYYYYY|YYYYYY",
				data.asTestString(OpeningHoursData.FRIDAY));
		assertEquals("YYYYYY|YYYYYY|YYYYYY|YYYYYY|YYYYYY|YYYYYY|YYYYYY|YYYYYY",
				data.asTestString(OpeningHoursData.SATUDAY));
		assertEquals("YYYYYY|YYYYYY|YYYYYY|YYYYYY|YYYYYY|YYYYYY|YYYYYY|YYYYYY",
				data.asTestString(OpeningHoursData.SUNDAY));
	}


	public void testDaily1200_2300() throws IOException {
		OpeningHoursData data = new OpeningHoursData("taeglich 12:00-23:00 Uhr");
		assertEquals("NNNNNN|NNNNNN|NNNNNN|NNNNNN|YYYYYY|YYYYYY|YYYYYY|YYYYNN",
				data.asTestString(OpeningHoursData.MONDAY));
		assertEquals("NNNNNN|NNNNNN|NNNNNN|NNNNNN|YYYYYY|YYYYYY|YYYYYY|YYYYNN",
				data.asTestString(OpeningHoursData.TUESDAY));
		assertEquals("NNNNNN|NNNNNN|NNNNNN|NNNNNN|YYYYYY|YYYYYY|YYYYYY|YYYYNN",
				data.asTestString(OpeningHoursData.WEDNESDAY));
		assertEquals("NNNNNN|NNNNNN|NNNNNN|NNNNNN|YYYYYY|YYYYYY|YYYYYY|YYYYNN",
				data.asTestString(OpeningHoursData.THURSDAY));
		assertEquals("NNNNNN|NNNNNN|NNNNNN|NNNNNN|YYYYYY|YYYYYY|YYYYYY|YYYYNN",
				data.asTestString(OpeningHoursData.FRIDAY));
		assertEquals("NNNNNN|NNNNNN|NNNNNN|NNNNNN|YYYYYY|YYYYYY|YYYYYY|YYYYNN",
				data.asTestString(OpeningHoursData.SATUDAY));
		assertEquals("NNNNNN|NNNNNN|NNNNNN|NNNNNN|YYYYYY|YYYYYY|YYYYYY|YYYYNN",
				data.asTestString(OpeningHoursData.SUNDAY));
	}


	public void testDaily1030_2330() throws IOException {
		OpeningHoursData data = new OpeningHoursData("taeglich 10:30-23:30 Uhr");
		assertEquals("NNNNNN|NNNNNN|NNNNNN|NNNYYY|YYYYYY|YYYYYY|YYYYYY|YYYYYN",
				data.asTestString(OpeningHoursData.MONDAY));
		assertEquals("NNNNNN|NNNNNN|NNNNNN|NNNYYY|YYYYYY|YYYYYY|YYYYYY|YYYYYN",
				data.asTestString(OpeningHoursData.TUESDAY));
		assertEquals("NNNNNN|NNNNNN|NNNNNN|NNNYYY|YYYYYY|YYYYYY|YYYYYY|YYYYYN",
				data.asTestString(OpeningHoursData.WEDNESDAY));
		assertEquals("NNNNNN|NNNNNN|NNNNNN|NNNYYY|YYYYYY|YYYYYY|YYYYYY|YYYYYN",
				data.asTestString(OpeningHoursData.THURSDAY));
		assertEquals("NNNNNN|NNNNNN|NNNNNN|NNNYYY|YYYYYY|YYYYYY|YYYYYY|YYYYYN",
				data.asTestString(OpeningHoursData.FRIDAY));
		assertEquals("NNNNNN|NNNNNN|NNNNNN|NNNYYY|YYYYYY|YYYYYY|YYYYYY|YYYYYN",
				data.asTestString(OpeningHoursData.SATUDAY));
		assertEquals("NNNNNN|NNNNNN|NNNNNN|NNNYYY|YYYYYY|YYYYYY|YYYYYY|YYYYYN",
				data.asTestString(OpeningHoursData.SUNDAY));
	}
	
	public void test_Sa_Di_1500_0200() throws IOException {
		OpeningHoursData data = new OpeningHoursData("Sa-Di 15:00 - 02:00 Uhr");
		assertEquals("YYYYNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|YYYYYY|YYYYYY|YYYYYY",
				data.asTestString(OpeningHoursData.MONDAY));
		assertEquals("YYYYNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|YYYYYY|YYYYYY|YYYYYY",
				data.asTestString(OpeningHoursData.TUESDAY));
		assertEquals("YYYYNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN",
				data.asTestString(OpeningHoursData.WEDNESDAY));
		assertEquals("NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN",
				data.asTestString(OpeningHoursData.THURSDAY));
		assertEquals("NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN",
				data.asTestString(OpeningHoursData.FRIDAY));
		assertEquals("NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|YYYYYY|YYYYYY|YYYYYY",
				data.asTestString(OpeningHoursData.SATUDAY));
		assertEquals("YYYYNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|YYYYYY|YYYYYY|YYYYYY",
				data.asTestString(OpeningHoursData.SUNDAY));
	}

	public void test_Sa_0800_1800() throws IOException {
		OpeningHoursData data = new OpeningHoursData("Sa 8:00-18:00 Uhr");
		assertEquals("NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN",
				data.asTestString(OpeningHoursData.MONDAY));
		assertEquals("NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN",
				data.asTestString(OpeningHoursData.TUESDAY));
		assertEquals("NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN",
				data.asTestString(OpeningHoursData.WEDNESDAY));
		assertEquals("NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN",
				data.asTestString(OpeningHoursData.THURSDAY));
		assertEquals("NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN",
				data.asTestString(OpeningHoursData.FRIDAY));
		assertEquals("NNNNNN|NNNNNN|NNNNYY|YYYYYY|YYYYYY|YYYYYY|NNNNNN|NNNNNN",
				data.asTestString(OpeningHoursData.SATUDAY));
		assertEquals("NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN",
				data.asTestString(OpeningHoursData.SUNDAY));
	}

	public void test_So_And_Feiertag() throws IOException {
		OpeningHoursData data = new OpeningHoursData("So+Feiertag 12:00-24:00");
		assertEquals("NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN",
				data.asTestString(OpeningHoursData.MONDAY));
		assertEquals("NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN",
				data.asTestString(OpeningHoursData.TUESDAY));
		assertEquals("NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN",
				data.asTestString(OpeningHoursData.WEDNESDAY));
		assertEquals("NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN",
				data.asTestString(OpeningHoursData.THURSDAY));
		assertEquals("NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN",
				data.asTestString(OpeningHoursData.FRIDAY));
		assertEquals("NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN",
				data.asTestString(OpeningHoursData.SATUDAY));
		assertEquals("NNNNNN|NNNNNN|NNNNNN|NNNNNN|YYYYYY|YYYYYY|YYYYYY|YYYYYY",
				data.asTestString(OpeningHoursData.SUNDAY));
	}

	public void test_Mi_Mo_1800_2300() throws IOException {
		OpeningHoursData data = new OpeningHoursData("Mi-Mo 18:00-23:00");
		assertEquals("NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|YYYYYY|YYYYNN",
				data.asTestString(OpeningHoursData.MONDAY));
		assertEquals("NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN",
				data.asTestString(OpeningHoursData.TUESDAY));
		assertEquals("NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|YYYYYY|YYYYNN",
				data.asTestString(OpeningHoursData.WEDNESDAY));
		assertEquals("NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|YYYYYY|YYYYNN",
				data.asTestString(OpeningHoursData.THURSDAY));
		assertEquals("NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|YYYYYY|YYYYNN",
				data.asTestString(OpeningHoursData.FRIDAY));
		assertEquals("NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|YYYYYY|YYYYNN",
				data.asTestString(OpeningHoursData.SATUDAY));
		assertEquals("NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|YYYYYY|YYYYNN",
				data.asTestString(OpeningHoursData.SUNDAY));
	}
	
	public void test_Mo_Sa_0800_1800() throws IOException {
		OpeningHoursData data = new OpeningHoursData("Mo-Sa 8:00-18:00 Uhr");
		assertEquals("NNNNNN|NNNNNN|NNNNYY|YYYYYY|YYYYYY|YYYYYY|NNNNNN|NNNNNN",
				data.asTestString(OpeningHoursData.MONDAY));
		assertEquals("NNNNNN|NNNNNN|NNNNYY|YYYYYY|YYYYYY|YYYYYY|NNNNNN|NNNNNN",
				data.asTestString(OpeningHoursData.TUESDAY));
		assertEquals("NNNNNN|NNNNNN|NNNNYY|YYYYYY|YYYYYY|YYYYYY|NNNNNN|NNNNNN",
				data.asTestString(OpeningHoursData.WEDNESDAY));
		assertEquals("NNNNNN|NNNNNN|NNNNYY|YYYYYY|YYYYYY|YYYYYY|NNNNNN|NNNNNN",
				data.asTestString(OpeningHoursData.THURSDAY));
		assertEquals("NNNNNN|NNNNNN|NNNNYY|YYYYYY|YYYYYY|YYYYYY|NNNNNN|NNNNNN",
				data.asTestString(OpeningHoursData.FRIDAY));
		assertEquals("NNNNNN|NNNNNN|NNNNYY|YYYYYY|YYYYYY|YYYYYY|NNNNNN|NNNNNN",
				data.asTestString(OpeningHoursData.SATUDAY));
		assertEquals("NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN",
				data.asTestString(OpeningHoursData.SUNDAY));
	}

	public void test_Mo_Fr_0800_1800_Sa_1000_1500_So_1400_1800()
			throws IOException {
		OpeningHoursData data = new OpeningHoursData(
				"Mo-Fr 8:00-18:00 Uhr, Sa 10:00-15:00 Uhr, So 14:00-18:00 Uhr");
		assertEquals("NNNNNN|NNNNNN|NNNNYY|YYYYYY|YYYYYY|YYYYYY|NNNNNN|NNNNNN",
				data.asTestString(OpeningHoursData.MONDAY));
		assertEquals("NNNNNN|NNNNNN|NNNNYY|YYYYYY|YYYYYY|YYYYYY|NNNNNN|NNNNNN",
				data.asTestString(OpeningHoursData.TUESDAY));
		assertEquals("NNNNNN|NNNNNN|NNNNYY|YYYYYY|YYYYYY|YYYYYY|NNNNNN|NNNNNN",
				data.asTestString(OpeningHoursData.WEDNESDAY));
		assertEquals("NNNNNN|NNNNNN|NNNNYY|YYYYYY|YYYYYY|YYYYYY|NNNNNN|NNNNNN",
				data.asTestString(OpeningHoursData.THURSDAY));
		assertEquals("NNNNNN|NNNNNN|NNNNYY|YYYYYY|YYYYYY|YYYYYY|NNNNNN|NNNNNN",
				data.asTestString(OpeningHoursData.FRIDAY));
		assertEquals("NNNNNN|NNNNNN|NNNNNN|NNYYYY|YYYYYY|NNNNNN|NNNNNN|NNNNNN",
				data.asTestString(OpeningHoursData.SATUDAY));
		assertEquals("NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNYY|YYYYYY|NNNNNN|NNNNNN",
				data.asTestString(OpeningHoursData.SUNDAY));
	}

	public void test_Mo_Fr_1000_1300_And_1500_1800() throws IOException {
		OpeningHoursData data = new OpeningHoursData(
				"Mo-Fr 10:00-13:00 + 15:00-18:00 Uhr");
		assertEquals("NNNNNN|NNNNNN|NNNNNN|NNYYYY|YYNNNN|YYYYYY|NNNNNN|NNNNNN",
				data.asTestString(OpeningHoursData.MONDAY));
		assertEquals("NNNNNN|NNNNNN|NNNNNN|NNYYYY|YYNNNN|YYYYYY|NNNNNN|NNNNNN",
				data.asTestString(OpeningHoursData.TUESDAY));
		assertEquals("NNNNNN|NNNNNN|NNNNNN|NNYYYY|YYNNNN|YYYYYY|NNNNNN|NNNNNN",
				data.asTestString(OpeningHoursData.WEDNESDAY));
		assertEquals("NNNNNN|NNNNNN|NNNNNN|NNYYYY|YYNNNN|YYYYYY|NNNNNN|NNNNNN",
				data.asTestString(OpeningHoursData.THURSDAY));
		assertEquals("NNNNNN|NNNNNN|NNNNNN|NNYYYY|YYNNNN|YYYYYY|NNNNNN|NNNNNN",
				data.asTestString(OpeningHoursData.FRIDAY));
		assertEquals("NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN",
				data.asTestString(OpeningHoursData.SATUDAY));
		assertEquals("NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN",
				data.asTestString(OpeningHoursData.SUNDAY));
	}

	public void test_Mo_Do_1600_0200_Fr_So_1400_0300() throws IOException {
		OpeningHoursData data = new OpeningHoursData(
				"Mo-Do 16:00-2:00, Fr-So 14:00-3:00 Uhr");
		assertEquals("YYYYYY|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNYYYY|YYYYYY|YYYYYY",
				data.asTestString(OpeningHoursData.MONDAY));
		assertEquals("YYYYNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNYYYY|YYYYYY|YYYYYY",
				data.asTestString(OpeningHoursData.TUESDAY));
		assertEquals("YYYYNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNYYYY|YYYYYY|YYYYYY",
				data.asTestString(OpeningHoursData.WEDNESDAY));
		assertEquals("YYYYNN|NNNNNN|NNNNNN|NNNNNN|NNNNNN|NNYYYY|YYYYYY|YYYYYY",
				data.asTestString(OpeningHoursData.THURSDAY));
		assertEquals("YYYYNN|NNNNNN|NNNNNN|NNNNNN|NNNNYY|YYYYYY|YYYYYY|YYYYYY",
				data.asTestString(OpeningHoursData.FRIDAY));
		assertEquals("YYYYYY|NNNNNN|NNNNNN|NNNNNN|NNNNYY|YYYYYY|YYYYYY|YYYYYY",
				data.asTestString(OpeningHoursData.SATUDAY));
		assertEquals("YYYYYY|NNNNNN|NNNNNN|NNNNNN|NNNNYY|YYYYYY|YYYYYY|YYYYYY",
				data.asTestString(OpeningHoursData.SUNDAY));
	}

	public void testEmptyStringAsUnknow() throws IOException {
		OpeningHoursData data = new OpeningHoursData("");
		assertEquals("UUUUUU|UUUUUU|UUUUUU|UUUUUU|UUUUUU|UUUUUU|UUUUUU|UUUUUU",
				data.asTestString(OpeningHoursData.MONDAY));
		assertEquals("UUUUUU|UUUUUU|UUUUUU|UUUUUU|UUUUUU|UUUUUU|UUUUUU|UUUUUU",
				data.asTestString(OpeningHoursData.TUESDAY));
		assertEquals("UUUUUU|UUUUUU|UUUUUU|UUUUUU|UUUUUU|UUUUUU|UUUUUU|UUUUUU",
				data.asTestString(OpeningHoursData.WEDNESDAY));
		assertEquals("UUUUUU|UUUUUU|UUUUUU|UUUUUU|UUUUUU|UUUUUU|UUUUUU|UUUUUU",
				data.asTestString(OpeningHoursData.THURSDAY));
		assertEquals("UUUUUU|UUUUUU|UUUUUU|UUUUUU|UUUUUU|UUUUUU|UUUUUU|UUUUUU",
				data.asTestString(OpeningHoursData.FRIDAY));
		assertEquals("UUUUUU|UUUUUU|UUUUUU|UUUUUU|UUUUUU|UUUUUU|UUUUUU|UUUUUU",
				data.asTestString(OpeningHoursData.SATUDAY));
		assertEquals("UUUUUU|UUUUUU|UUUUUU|UUUUUU|UUUUUU|UUUUUU|UUUUUU|UUUUUU",
				data.asTestString(OpeningHoursData.SUNDAY));
	}
	
	public void testNoHoursAsUnknown() throws IOException {
		OpeningHoursData data = new OpeningHoursData("Mo-So");
		assertEquals("UUUUUU|UUUUUU|UUUUUU|UUUUUU|UUUUUU|UUUUUU|UUUUUU|UUUUUU",
				data.asTestString(OpeningHoursData.MONDAY));
		assertEquals("UUUUUU|UUUUUU|UUUUUU|UUUUUU|UUUUUU|UUUUUU|UUUUUU|UUUUUU",
				data.asTestString(OpeningHoursData.TUESDAY));
		assertEquals("UUUUUU|UUUUUU|UUUUUU|UUUUUU|UUUUUU|UUUUUU|UUUUUU|UUUUUU",
				data.asTestString(OpeningHoursData.WEDNESDAY));
		assertEquals("UUUUUU|UUUUUU|UUUUUU|UUUUUU|UUUUUU|UUUUUU|UUUUUU|UUUUUU",
				data.asTestString(OpeningHoursData.THURSDAY));
		assertEquals("UUUUUU|UUUUUU|UUUUUU|UUUUUU|UUUUUU|UUUUUU|UUUUUU|UUUUUU",
				data.asTestString(OpeningHoursData.FRIDAY));
		assertEquals("UUUUUU|UUUUUU|UUUUUU|UUUUUU|UUUUUU|UUUUUU|UUUUUU|UUUUUU",
				data.asTestString(OpeningHoursData.SATUDAY));
		assertEquals("UUUUUU|UUUUUU|UUUUUU|UUUUUU|UUUUUU|UUUUUU|UUUUUU|UUUUUU",
				data.asTestString(OpeningHoursData.SUNDAY));
	}
	
	public void testIsOpen() throws IOException, ParseException {
		OpeningHoursData data = new OpeningHoursData(
				"Mo-Fr 8:00-18:00 Uhr, Sa 10:00-15:00 Uhr, So 14:30-18:30 Uhr");
		
		// Monday
		assertEquals(OpeningHoursData.CLOSE_VALUE, data.isOpen(FORMATTER.parse("23.01.12 00:00")));
		assertEquals(OpeningHoursData.CLOSE_VALUE, data.isOpen(FORMATTER.parse("23.01.12 00:59")));
		assertEquals(OpeningHoursData.CLOSE_VALUE, data.isOpen(FORMATTER.parse("23.01.12 07:59")));
		
		assertEquals(OpeningHoursData.OPEN_VALUE, data.isOpen(FORMATTER.parse("23.01.12 08:00")));
		assertEquals(OpeningHoursData.OPEN_VALUE, data.isOpen(FORMATTER.parse("23.01.12 08:01")));
		assertEquals(OpeningHoursData.OPEN_VALUE, data.isOpen(FORMATTER.parse("23.01.12 09:30")));
		assertEquals(OpeningHoursData.OPEN_VALUE, data.isOpen(FORMATTER.parse("23.01.12 12:00")));
		assertEquals(OpeningHoursData.OPEN_VALUE, data.isOpen(FORMATTER.parse("23.01.12 12:00")));
		assertEquals(OpeningHoursData.OPEN_VALUE, data.isOpen(FORMATTER.parse("23.01.12 17:00")));
		assertEquals(OpeningHoursData.OPEN_VALUE, data.isOpen(FORMATTER.parse("23.01.12 17:59")));
		
		assertEquals(OpeningHoursData.CLOSE_VALUE, data.isOpen(FORMATTER.parse("23.01.12 18:00")));
		assertEquals(OpeningHoursData.CLOSE_VALUE, data.isOpen(FORMATTER.parse("23.01.12 18:01")));
		assertEquals(OpeningHoursData.CLOSE_VALUE, data.isOpen(FORMATTER.parse("23.01.12 19:32")));
		assertEquals(OpeningHoursData.CLOSE_VALUE, data.isOpen(FORMATTER.parse("23.01.12 23:59")));
		
		// Tuesday
		assertEquals(OpeningHoursData.CLOSE_VALUE, data.isOpen(FORMATTER.parse("24.01.12 00:00")));
		assertEquals(OpeningHoursData.CLOSE_VALUE, data.isOpen(FORMATTER.parse("24.01.12 07:59")));
		assertEquals(OpeningHoursData.OPEN_VALUE, data.isOpen(FORMATTER.parse("24.01.12 08:00")));
		assertEquals(OpeningHoursData.OPEN_VALUE, data.isOpen(FORMATTER.parse("24.01.12 17:59")));
		assertEquals(OpeningHoursData.CLOSE_VALUE, data.isOpen(FORMATTER.parse("24.01.12 18:00")));
		assertEquals(OpeningHoursData.CLOSE_VALUE, data.isOpen(FORMATTER.parse("24.01.12 23:59")));
		
		// Wednesday
		assertEquals(OpeningHoursData.CLOSE_VALUE, data.isOpen(FORMATTER.parse("25.01.12 00:00")));
		assertEquals(OpeningHoursData.CLOSE_VALUE, data.isOpen(FORMATTER.parse("25.01.12 07:59")));
		assertEquals(OpeningHoursData.OPEN_VALUE, data.isOpen(FORMATTER.parse("25.01.12 08:00")));
		assertEquals(OpeningHoursData.OPEN_VALUE, data.isOpen(FORMATTER.parse("25.01.12 17:59")));
		assertEquals(OpeningHoursData.CLOSE_VALUE, data.isOpen(FORMATTER.parse("25.01.12 18:00")));
		assertEquals(OpeningHoursData.CLOSE_VALUE, data.isOpen(FORMATTER.parse("25.01.12 23:59")));
		
		// Thursday
		assertEquals(OpeningHoursData.CLOSE_VALUE, data.isOpen(FORMATTER.parse("26.01.12 00:00")));
		assertEquals(OpeningHoursData.CLOSE_VALUE, data.isOpen(FORMATTER.parse("26.01.12 07:59")));
		assertEquals(OpeningHoursData.OPEN_VALUE, data.isOpen(FORMATTER.parse("26.01.12 08:00")));
		assertEquals(OpeningHoursData.OPEN_VALUE, data.isOpen(FORMATTER.parse("26.01.12 17:59")));
		assertEquals(OpeningHoursData.CLOSE_VALUE, data.isOpen(FORMATTER.parse("26.01.12 18:00")));
		assertEquals(OpeningHoursData.CLOSE_VALUE, data.isOpen(FORMATTER.parse("26.01.12 23:59")));
		
		// Friday
		assertEquals(OpeningHoursData.CLOSE_VALUE, data.isOpen(FORMATTER.parse("27.01.12 00:00")));
		assertEquals(OpeningHoursData.CLOSE_VALUE, data.isOpen(FORMATTER.parse("27.01.12 07:59")));
		assertEquals(OpeningHoursData.OPEN_VALUE, data.isOpen(FORMATTER.parse("27.01.12 08:00")));
		assertEquals(OpeningHoursData.OPEN_VALUE, data.isOpen(FORMATTER.parse("27.01.12 17:59")));
		assertEquals(OpeningHoursData.CLOSE_VALUE, data.isOpen(FORMATTER.parse("27.01.12 18:00")));
		assertEquals(OpeningHoursData.CLOSE_VALUE, data.isOpen(FORMATTER.parse("27.01.12 23:59")));
		
		// Saturday
		assertEquals(OpeningHoursData.CLOSE_VALUE, data.isOpen(FORMATTER.parse("28.01.12 00:00")));
		assertEquals(OpeningHoursData.CLOSE_VALUE, data.isOpen(FORMATTER.parse("28.01.12 09:59")));
		assertEquals(OpeningHoursData.OPEN_VALUE, data.isOpen(FORMATTER.parse("28.01.12 10:00")));
		assertEquals(OpeningHoursData.OPEN_VALUE, data.isOpen(FORMATTER.parse("28.01.12 14:59")));
		assertEquals(OpeningHoursData.CLOSE_VALUE, data.isOpen(FORMATTER.parse("28.01.12 15:00")));
		assertEquals(OpeningHoursData.CLOSE_VALUE, data.isOpen(FORMATTER.parse("28.01.12 23:59")));
		
		// Sunday
		assertEquals(OpeningHoursData.CLOSE_VALUE, data.isOpen(FORMATTER.parse("29.01.12 00:00")));
		assertEquals(OpeningHoursData.CLOSE_VALUE, data.isOpen(FORMATTER.parse("29.01.12 14:29")));
		assertEquals(OpeningHoursData.OPEN_VALUE, data.isOpen(FORMATTER.parse("29.01.12 14:30")));
		assertEquals(OpeningHoursData.OPEN_VALUE, data.isOpen(FORMATTER.parse("29.01.12 18:29")));
		assertEquals(OpeningHoursData.CLOSE_VALUE, data.isOpen(FORMATTER.parse("29.01.12 18:30")));
		assertEquals(OpeningHoursData.CLOSE_VALUE, data.isOpen(FORMATTER.parse("29.01.12 23:59")));
	}
	
}
