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
