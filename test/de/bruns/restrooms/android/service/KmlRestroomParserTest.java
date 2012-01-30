package de.bruns.restrooms.android.service;


import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;
import de.bruns.restrooms.android.data.OpeningHoursData;
import de.bruns.restrooms.android.data.RestroomData;
import de.bruns.restrooms.android.service.KmlRestroomParser;

public class KmlRestroomParserTest extends TestCase {

	public void testParseStandardPlacemark() throws IOException {
		InputStream stream = KmlRestroomParserTest.class.getResource(
				"Nette_Toilette_Bremen-Witthus.kml").openStream();
		KmlRestroomParser kmlParser = new KmlRestroomParser(stream);

		assertEquals(1, kmlParser.getRestroomData().size());

		RestroomData witthusPlacemark = kmlParser.getRestroomData().get(0);
		assertEquals(53.192463, witthusPlacemark.getLatitude());
		assertEquals(8.555577, witthusPlacemark.getLongitude());
		assertEquals("Witthus Heimtex-Fachmarkt", witthusPlacemark.getName());
		assertEquals("Witthus Heimtex-Fachmarkt",
				witthusPlacemark.getNameOfDescription());
		assertEquals("Mo-Sa 9:00-18:00 Uhr", witthusPlacemark.getOpeningHours());
		assertEquals("Heidlerchenstr. 3a", witthusPlacemark.getAdresse());
		assertEquals(
				"Witthus Heimtex-Fachmarkt, Heidlerchenstr. 3a, Mo-Sa 9:00-18:00 Uhr",
				witthusPlacemark.getDescription());
	}
	
	public void testNoOpeningHoursPlacemark() throws IOException {
		InputStream stream = KmlRestroomParserTest.class.getResource(
				"Nette_Toilette_Bremen-LloydCaffee.kml").openStream();
		KmlRestroomParser kmlParser = new KmlRestroomParser(stream);
		
		assertEquals(1, kmlParser.getRestroomData().size());

		RestroomData lloydCaffeePlacemark = kmlParser.getRestroomData().get(0);
		assertEquals(53.102871, lloydCaffeePlacemark.getLatitude());
		assertEquals(8.765818, lloydCaffeePlacemark.getLongitude());
		assertEquals("Lloyd Caffee", lloydCaffeePlacemark.getName());
		assertEquals("Lloyd Caffee",
				lloydCaffeePlacemark.getNameOfDescription());
		assertEquals("", lloydCaffeePlacemark.getOpeningHours());
		assertEquals("Fabrikenufer 115", lloydCaffeePlacemark.getAdresse());
		assertEquals(
				"Lloyd Caffee, Fabrikenufer 115,",
				lloydCaffeePlacemark.getDescription());
	}
	
	public void testOpeningHoursWithCommaPlacemark() throws IOException {
		InputStream stream = KmlRestroomParserTest.class.getResource(
				"Nette_Toilette_Bremen-Akropolis.kml").openStream();
		KmlRestroomParser kmlParser = new KmlRestroomParser(stream);
		
		assertEquals(1, kmlParser.getRestroomData().size());

		RestroomData akropolisPlacemark = kmlParser.getRestroomData().get(0);
		assertEquals(53.175743, akropolisPlacemark.getLatitude());
		assertEquals(8.609735, akropolisPlacemark.getLongitude());
		assertEquals("Akropolis,", akropolisPlacemark.getName());
		assertEquals("Akropolis",
				akropolisPlacemark.getNameOfDescription());
		assertEquals("Mi-Mo 18:00- 23:00, So und Feiert. 12:00-15:00 + 18:00-23:30 Uhr", akropolisPlacemark.getOpeningHours());
		assertEquals("Lindenstr. 3", akropolisPlacemark.getAdresse());
		assertEquals(
				"Akropolis, Lindenstr. 3, Mi-Mo 18:00- 23:00, So und Feiert. 12:00-15:00 + 18:00-23:30 Uhr",
				akropolisPlacemark.getDescription());
	}
	
	public void testNoAddressPlacemark() throws IOException {
		InputStream stream = KmlRestroomParserTest.class.getResource(
				"Nette_Toilette_Bremen-Sentosa.kml").openStream();
		KmlRestroomParser kmlParser = new KmlRestroomParser(stream);
		
		assertEquals(1, kmlParser.getRestroomData().size());
		
		RestroomData sentosaPlacemark = kmlParser.getRestroomData().get(0);
		assertEquals(53.096607, sentosaPlacemark.getLatitude());
		assertEquals(8.810620, sentosaPlacemark.getLongitude());
		assertEquals("Sentosa", sentosaPlacemark.getName());
		assertEquals("Sentosa",
				sentosaPlacemark.getNameOfDescription());
		assertEquals("Mo-So 12:00-15:00 u. 17:30-23:00 Uhr", sentosaPlacemark.getOpeningHours());
		assertEquals("", sentosaPlacemark.getAdresse());
		assertEquals(
				"Sentosa, Mo-So 12:00-15:00 u. 17:30-23:00 Uhr",
				sentosaPlacemark.getDescription());
	}

	public void testNoPlacemark() throws IOException {
		InputStream stream = KmlRestroomParserTest.class.getResource(
				"Nette_Toilette_Bremen-Empty.kml").openStream();
		KmlRestroomParser kmlParser = new KmlRestroomParser(stream);
		
		assertEquals(0, kmlParser.getRestroomData().size());
	}

	public void testParseAllPlacemarks() throws IOException {
		InputStream stream = KmlRestroomParser.class.getResource(
				"Nette_Toilette_Bremen.kml").openStream();
		KmlRestroomParser kmlParser = new KmlRestroomParser(stream);

		assertEquals(64, kmlParser.getRestroomData().size());
		
		for (int i = 0; i < kmlParser.getRestroomData().size(); i++) {
			System.out.println("--------------------------------------------------------");
			System.out.println(i + " -> " + kmlParser.getRestroomData().get(i).getOpeningHours());
			OpeningHoursData openingHoursData = kmlParser.getRestroomData().get(i).getOpeningHoursData();
			System.out.println(openingHoursData.asTestString(0));
			System.out.println(openingHoursData.asTestString(1));
			System.out.println(openingHoursData.asTestString(2));
			System.out.println(openingHoursData.asTestString(3));
			System.out.println(openingHoursData.asTestString(4));
			System.out.println(openingHoursData.asTestString(5));
			System.out.println(openingHoursData.asTestString(6));
		}
	}
}
