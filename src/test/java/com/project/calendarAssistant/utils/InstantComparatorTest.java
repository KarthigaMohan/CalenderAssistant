package com.project.calendarAssistant.utils;

import java.time.Instant;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class InstantComparatorTest {
	
	Instant[] instant1, instant2;
	InstantComparator comp;
	
	@Before
	public void setup() {
		comp= new InstantComparator();
		
	}
	
	@Test
	public void checkInstantsSortedByStartingTimesForDiffDates() throws Exception {
		instant1= new Instant[] {Instant.parse("2021-02-14T16:15:00Z"), Instant.parse("2021-02-14T17:00:00Z")};
		instant2= new Instant[] {Instant.parse("2021-02-12T16:15:00Z"), Instant.parse("2021-02-14T16:15:00Z")};
		int res = comp.compare(instant1, instant2);
		Assert.assertEquals(res, 1);
	}

	@Test
	public void checkInstantsSortedByStartingTimesForSameDate() throws Exception {
		instant1= new Instant[] {Instant.parse("2021-02-10T12:15:00Z"), Instant.parse("2021-02-14T17:00:00Z")};
		instant2= new Instant[] {Instant.parse("2021-02-10T16:15:00Z"), Instant.parse("2021-02-14T16:15:00Z")};
		int res = comp.compare(instant1, instant2);
		Assert.assertEquals(res, -1);
	}
	
	
	@Test
	public void checkInstantsForEqualInstants() throws Exception {
		instant1= new Instant[] {Instant.parse("2021-02-14T16:15:00Z"), Instant.parse("2021-02-14T17:00:00Z")};
		instant2= new Instant[] {Instant.parse("2021-02-14T16:15:00Z"), Instant.parse("2021-02-14T16:15:00Z")};
		int res = comp.compare(instant1, instant2);
		Assert.assertEquals(res, 0);
	}
}
