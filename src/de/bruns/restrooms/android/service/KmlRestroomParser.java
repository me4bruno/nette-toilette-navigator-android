/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.bruns.restrooms.android.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import de.bruns.restrooms.android.data.RestroomData;

/**
 * A very basic KML parser to meet the need of the emulator control panel.
 * <p/>
 * It parses basic Placemark information.
 */
public class KmlRestroomParser {

	private final static String NS_KML_2 = "http://earth.google.com/kml/2."; //$NON-NLS-1$

	private final static String NODE_PLACEMARK = "Placemark"; //$NON-NLS-1$
	private final static String NODE_NAME = "name"; //$NON-NLS-1$
	private final static String NODE_SNIPPET = "description"; //$NON-NLS-1$
	private final static String NODE_COORDINATES = "coordinates"; //$NON-NLS-1$

	private final static Pattern sLocationPattern = Pattern
			.compile("([^,]+),([^,]+)(?:,([^,]+))?");

	private static SAXParserFactory sParserFactory;

	static {
		sParserFactory = SAXParserFactory.newInstance();
		sParserFactory.setNamespaceAware(true);
	}

	private KmlHandler mHandler;
	private InputSource inputSource;

	/**
	 * Handler for the SAX parser.
	 */
	private static class KmlHandler extends DefaultHandler {

		List<RestroomData> allToilletteData;
		RestroomData currentToiletteData;

		public KmlHandler() {
			allToilletteData = new ArrayList<RestroomData>();
		}

		final StringBuilder mStringAccumulator = new StringBuilder();

		boolean mSuccess = true;

		@Override
		public void startElement(String uri, String localName, String name,
				Attributes attributes) throws SAXException {

			// we only care about the standard GPX nodes.
			try {
				if (uri.startsWith(NS_KML_2)) {
					if (NODE_PLACEMARK.equals(localName)) {
						currentToiletteData = new RestroomData();
						allToilletteData.add(currentToiletteData);
					}
				}
			} finally {
				// no matter the node, we empty the StringBuilder accumulator
				// when we start
				// a new node.
				mStringAccumulator.setLength(0);
			}
		}

		/**
		 * Processes new characters for the node content. The characters are
		 * simply stored, and will be processed when
		 * {@link #endElement(String, String, String)} is called.
		 */
		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			mStringAccumulator.append(ch, start, length);
		}

		@Override
		public void endElement(String uri, String localName, String name)
				throws SAXException {
			if (uri.startsWith(NS_KML_2)) {
				if (NODE_PLACEMARK.equals(localName)) {
					currentToiletteData = null;
				} else if (NODE_NAME.equals(localName)) {
					if (currentToiletteData != null) {
						currentToiletteData.setName(mStringAccumulator
								.toString());
					}
				} else if (NODE_SNIPPET.equals(localName)) {
					if (currentToiletteData != null) {
						currentToiletteData.setDescription(mStringAccumulator
								.toString());
					}
				} else if (NODE_COORDINATES.equals(localName)) {
					if (currentToiletteData != null) {
						parseLocation(currentToiletteData,
								mStringAccumulator.toString());
					}
				}
			}
		}

		@Override
		public void error(SAXParseException e) throws SAXException {
			mSuccess = false;
		}

		@Override
		public void fatalError(SAXParseException e) throws SAXException {
			mSuccess = false;
		}

		/**
		 * Parses the location string and store the information into a
		 * {@link RestroomData}.
		 * 
		 * @param locationNode
		 *            the {@link RestroomData} to receive the location data.
		 * @param location
		 *            The string containing the location info.
		 */
		private void parseLocation(RestroomData locationNode, String location) {
			Matcher m = sLocationPattern.matcher(location);
			if (m.matches()) {
				try {
					double longitude = Double.parseDouble(m.group(1));
					double latitude = Double.parseDouble(m.group(2));

					locationNode.setPosition(longitude, latitude);
				} catch (NumberFormatException e) {
					// wrong data, do nothing.
				}
			}
		}

		List<RestroomData> getWayPoints() {
				return allToilletteData;
		}

		boolean getSuccess() {
			return mSuccess;
		}
	}

	/**
	 * Creates a new GPX parser for a file specified by its full path.
	 * 
	 * @param fileName
	 *            The full path of the GPX file to parse.
	 */
	public KmlRestroomParser(InputStream inputStream) {
		this.inputSource = new InputSource(inputStream);
	}

	public KmlRestroomParser(Reader reader) {
		this.inputSource = new InputSource(reader);
	}

	/**
	 * Parses the GPX file.
	 * 
	 * @return <code>true</code> if success.
	 */
	public boolean parse() {
		try {
			SAXParser parser = sParserFactory.newSAXParser();

			mHandler = new KmlHandler();

			parser.parse(inputSource, mHandler);

			return mHandler.getSuccess();
		} catch (ParserConfigurationException e) {
		} catch (SAXException e) {
		} catch (IOException e) {
		} finally {
		}

		return false;
	}

	/**
	 * Returns the parsed {@link RestroomData} objects, or <code>null</code> if
	 * none were found (or if the parsing failed.
	 */
	public List<RestroomData> getWayPoints() {
		return mHandler.getWayPoints();
	}
}