package com.project.calendarAssistant.utils;

import java.time.Instant;
import java.util.Comparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class InstantComparator implements Comparator<Instant[]>{
	
	private static final Logger log = LoggerFactory.getLogger(InstantComparator.class);

	@Override
	public int compare(Instant[] firstInstant, Instant[] secondInstant) {
		
		if (firstInstant.equals(secondInstant)) return 0;		
		log.debug("Comparator is returning: "+ String.valueOf(firstInstant[0].compareTo(secondInstant[0])));
		return firstInstant[0].compareTo(secondInstant[0]);
	}
	
	

}
