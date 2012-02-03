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
package de.bruns.restrooms.android.data;

public class OpeningImageData {
	
	public static final OpeningImageData[] ALL_IMAGES = new OpeningImageData[] {
		new OpeningImageData("restroom_green", "Nette Toilette ist gešffnet"),
		new OpeningImageData("restroom_red", "Nette Toilette ist geschlossen"),
		new OpeningImageData("restroom_yellow", "Keine …ffnungszeiten vorhanden"),
		new OpeningImageData("you_are_here", "Aktueller Standort")		
	};
	
	public static OpeningImageData getOpeningImageData(int index) {
		return ALL_IMAGES[index];
	}

	public static final int RESTROOM_OPEN = 0;
	public static final int RESTROOM_CLOSE = 1;
	public static final int RESTROOM_UNCERTAIN = 2;
	public static final int MY_LOCATION = 3;

	private String filename;
	private String description;
	
	public OpeningImageData(String filename, String description) {
		this.filename = filename;
		this.description = description;
	}

	public String getFilename() {
		return filename;
	}

	public String getDescription() {
		return description;
	}
}
