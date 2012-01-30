package de.bruns.restrooms.android.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import de.bruns.restrooms.android.data.RestroomData;

public class KmlRestroomParser {

	private final static String NS_KML_2 = "http://earth.google.com/kml/2.";
	private final static String NODE_PLACEMARK = "Placemark";
	private final static String NODE_NAME = "name";
	private final static String NODE_SNIPPET = "description";
	private final static String NODE_COORDINATES = "coordinates";

	private final static Pattern locationPattern = Pattern
			.compile("([^,]+),([^,]+)(?:,([^,]+))?");

	private List<RestroomData> allRestroomData;

	public KmlRestroomParser(InputStream inputStream) {
		allRestroomData = new ArrayList<RestroomData>();

		InputSource inputSource = new InputSource(inputStream);
		try {
			SAXParserFactory parserFactory = SAXParserFactory.newInstance();
			parserFactory.setNamespaceAware(true);
			SAXParser parser = parserFactory.newSAXParser();
			parser.parse(inputSource, new KmlHandler());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<RestroomData> getRestroomData() {
		return allRestroomData;
	}

	private class KmlHandler extends DefaultHandler {

		private final StringBuilder stringAccumulator;
		private RestroomData currentRestroomData;

		public KmlHandler() {
			stringAccumulator = new StringBuilder();
		}

		@Override
		public void startElement(String uri, String localName, String name,
				Attributes attributes) throws SAXException {
			try {
				if (uri.startsWith(NS_KML_2)) {
					if (NODE_PLACEMARK.equals(localName)) {
						currentRestroomData = new RestroomData();
						allRestroomData.add(currentRestroomData);
					}
				}
			} finally {
				stringAccumulator.setLength(0);
			}
		}

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			stringAccumulator.append(ch, start, length);
		}

		@Override
		public void endElement(String uri, String localName, String name)
				throws SAXException {
			if (uri.startsWith(NS_KML_2)) {
				if (NODE_PLACEMARK.equals(localName)) {
					currentRestroomData = null;
				} else if (NODE_NAME.equals(localName)) {
					if (currentRestroomData != null) {
						currentRestroomData.setName(stringAccumulator
								.toString());
					}
				} else if (NODE_SNIPPET.equals(localName)) {
					if (currentRestroomData != null) {
						currentRestroomData.setDescription(stringAccumulator
								.toString());
					}
				} else if (NODE_COORDINATES.equals(localName)) {
					if (currentRestroomData != null) {
						Matcher m = locationPattern.matcher(stringAccumulator
								.toString());
						if (m.matches()) {
							try {
								double longitude = Double.parseDouble(m
										.group(1));
								double latitude = Double
										.parseDouble(m.group(2));

								currentRestroomData.setPosition(longitude,
										latitude);
							} catch (NumberFormatException e) {
								// wrong data, do nothing.
							}
						}
					}
				}
			}
		}
	}

}