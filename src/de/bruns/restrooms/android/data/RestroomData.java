package de.bruns.restrooms.android.data;

import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class RestroomData {

	private static final Random RANDOM = new Random(1234);
	private static final NumberFormat KM_FORMATTER = createKilometerFormatter();

	public static final String[] TOILET_IMAGES = new String[] {
		"toilets_green", "toilets_red", "toilets_yellow" };

	public static final String[] TOILET_NAMES = new String[] {
		"Nette Toilette ist gešffnet", "Nette Toilette ist geschlossen", "Keine …ffnungszeiten vorhanden" };
	
	public static final int TOILET_OPEN = 0;
	public static final int TOILET_CLOSE = 1;
	public static final int TOILET_UNCERTAIN = 2;

	private int distanceInMeter;
	private double longitude;
	private double latitude;
	private String name;
	private String description;
	private String nameOfDescription;
	private String openingHours;
	private String address;
	private int isOpen;
	private String filename;
	private OpeningHoursData openingHoursData;

	public RestroomData() {
		isOpen = RANDOM.nextInt(3);
		filename = TOILET_IMAGES[isOpen];
	}

	public String getOpenImageFilename() {
		return filename;
	}

	public String getFullString() {
		return "RestroomData [longitude=" + longitude + ", latitude="
				+ latitude + ", name=" + name + ", nameOfDescription="
				+ nameOfDescription + ", address=" + address
				+ ", openingHours=" + openingHours + ", description="
				+ description + "]";
	}

	@Override
	public String toString() {
		return name;
	}

	public int isOpen() {
		return isOpen;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public int getDistanceInMeter() {
		return distanceInMeter;
	}

	public void setDistanceInMeter(int distanceInMeter) {
		this.distanceInMeter = distanceInMeter;
	}

	public void setPosition(double longitude, double latitude) {
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setDescription(String description) {
		this.description = description;

		address = "";
		openingHours = "";

		String[] splittedString = description.split(",");
		switch (splittedString.length) {
		case 3:
			nameOfDescription = splittedString[0].trim();
			address = splittedString[1].trim();
			openingHours = splittedString[2].trim();
			break;
		case 2:
			nameOfDescription = splittedString[0].trim();
			if (splittedString[1].contains("Uhr")) {
				openingHours = splittedString[1].trim();
			} else {
				address = splittedString[1].trim();
			}
			break;
		default:
			if (splittedString.length > 3) {
				nameOfDescription = splittedString[0].trim();
				address = splittedString[1].trim();

				String afterNameString = description.substring(description
						.indexOf(',') + 1);
				String afterStreetString = afterNameString
						.substring(afterNameString.indexOf(',') + 1);
				openingHours = afterStreetString.trim();
			} else {
				throw new RuntimeException("Unable to parse description: "
						+ description);
			}
		}
		
		openingHoursData = new OpeningHoursData(openingHours);
	}

	public boolean isOpen(Date currentTime) {
		return openingHoursData.isOpen(currentTime);
	}
	
	public String getDescription() {
		return description;
	}

	public String getNameOfDescription() {
		return nameOfDescription;
	}

	public String getOpeningHours() {
		return openingHours;
	}

	public String getAdresse() {
		return address;
	}

	public String getDistanceAsString() {
		if (distanceInMeter >= 1000) {
			return KM_FORMATTER.format(distanceInMeter / (double) 1000) + " km";
		}
		return Math.round(distanceInMeter / (double) 10) * 10 + " m";
	}

	private static NumberFormat createKilometerFormatter() {
		NumberFormat kmFormatter = NumberFormat.getInstance(Locale.GERMAN);
		kmFormatter.setMaximumFractionDigits(1);
		kmFormatter.setMinimumFractionDigits(1);
		return kmFormatter;
	}

}
