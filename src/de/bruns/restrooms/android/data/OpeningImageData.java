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
