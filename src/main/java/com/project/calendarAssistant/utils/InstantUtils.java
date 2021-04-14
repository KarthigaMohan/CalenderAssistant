package com.project.calendarAssistant.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public final class InstantUtils {

	private InstantUtils() {}
	
	/* Given instants : externalStartInstant, externalEndInstant, calendarStartInstant, calendarEndInstant
	 * Return 		  : whether they overlap
	 */

	public static boolean areInstantsOverlapping(Instant externalStart, Instant externalEnd, Instant calendarStart,
			Instant calendarEnd) {

		if ((externalStart.isAfter(calendarStart) || externalStart.equals(calendarStart))
				&& (externalStart.isBefore(calendarEnd))) {
			return true;
		}

		if ((externalEnd.isAfter(calendarStart))
				&& (externalEnd.isBefore(calendarEnd) || externalEnd.equals(calendarStart))) {
			return true;
		}

		if ((externalStart.isBefore(calendarStart) || externalStart.equals(calendarStart))
				&& (externalEnd.isAfter(calendarEnd) || externalEnd.equals(calendarEnd))) {
			return true;
		}
		return false;
	}

	
	
	public static Instant convertStringToInstant(String dateAndTimeExtended) {
		String[] dateAndTime = dateAndTimeExtended.split("\s");
		LocalDateTime ldt = LocalDateTime.parse(dateAndTime[0] + "T" + dateAndTime[1]);
		return ldt.toInstant(ZoneOffset.UTC);
	}
	
	
}
