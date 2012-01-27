package de.bruns.restrooms.android.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.bruns.restrooms.android.data.RestroomData;

public class RestroomDataProvider {

	public RestroomDataProvider() {
		//
	}

	public List<RestroomData> getRestroomData() {
		List<RestroomData> result = new ArrayList<RestroomData>();
		try {
			InputStream stream = KmlRestroomParser.class.getResource(
					"Nette_Toilette_Bremen.kml").openStream();
			KmlRestroomParser kmlParser = new KmlRestroomParser(stream);
			kmlParser.parse();
			result = kmlParser.getRestroomData();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

}
