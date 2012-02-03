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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.bruns.restrooms.android.data.RestroomData;

public class RestroomDataProvider {

	public List<RestroomData> getRestroomData() {
		List<RestroomData> result = new ArrayList<RestroomData>();
		try {
			InputStream stream = KmlRestroomParser.class.getResource(
					"Nette_Toilette_Bremen.kml").openStream();
			KmlRestroomParser kmlParser = new KmlRestroomParser(stream);
			result = kmlParser.getRestroomData();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

}
