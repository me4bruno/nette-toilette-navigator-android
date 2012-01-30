package de.bruns.restrooms.android.data;

import java.io.IOException;

import de.bruns.restrooms.android.data.RestroomData;

import junit.framework.TestCase;

public class RestroomDataTest extends TestCase {

	public void testGetDistanceAsString() throws IOException {
		RestroomData restroomData = new RestroomData();
		
		// round values not needed
		restroomData.setDistanceInMeter(1);
		assertEquals("0 m", restroomData.getDistanceAsString());

		restroomData.setDistanceInMeter(12);
		assertEquals("10 m", restroomData.getDistanceAsString());

		restroomData.setDistanceInMeter(123);
		assertEquals("120 m", restroomData.getDistanceAsString());

		restroomData.setDistanceInMeter(1234);
		assertEquals("1,2 km", restroomData.getDistanceAsString());

		restroomData.setDistanceInMeter(12345);
		assertEquals("12,3 km", restroomData.getDistanceAsString());

		restroomData.setDistanceInMeter(12000);
		assertEquals("12,0 km", restroomData.getDistanceAsString());

		// round values needed
		restroomData.setDistanceInMeter(9);
		assertEquals("10 m", restroomData.getDistanceAsString());
		
		restroomData.setDistanceInMeter(98);
		assertEquals("100 m", restroomData.getDistanceAsString());
		
		restroomData.setDistanceInMeter(987);
		assertEquals("990 m", restroomData.getDistanceAsString());
		
		restroomData.setDistanceInMeter(9876);
		assertEquals("9,9 km", restroomData.getDistanceAsString());
		
		restroomData.setDistanceInMeter(98765);
		assertEquals("98,8 km", restroomData.getDistanceAsString());
	}
}
