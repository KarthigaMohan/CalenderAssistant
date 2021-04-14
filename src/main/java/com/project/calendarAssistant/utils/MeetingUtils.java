package com.project.calendarAssistant.utils;

import com.project.calendarAssistant.entity.Employee;
import com.project.calendarAssistant.entity.Meeting;
import com.project.calendarAssistant.exceptions.DateFormatException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MeetingUtils {

	private static final Logger log = LoggerFactory.getLogger(MeetingUtils.class);
	private static final String SEPARATOR = ";";
	private static final SimpleDateFormat DATE_PATTERN = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private MeetingUtils() {}

	/*
	 * Given : Employee and meetingTime 
	 * Returns: Meeting Id on employee calendar having a conflict for meeting time
	 */
	public static List<Integer> getMeetingConflictingWithEmployeeCalendar(Employee emp, String meetingTime) {

		List<Integer> conflictingMeetingsIds = new ArrayList<>();
		log.info("employee details: "+emp.toString());		
		String[] meetingTimingsWithMeetingId = meetingTime.split(SEPARATOR);
		Instant meetingStart = InstantUtils.convertStringToInstant(meetingTimingsWithMeetingId[0]);
		Instant meetingEnd = InstantUtils.convertStringToInstant(meetingTimingsWithMeetingId[1]);

		for (String blockedDurationWithMeetingId : emp.getCalendar().getblockedDurationWithMeetingIds()) {
			log.info("Blocked duration: "+blockedDurationWithMeetingId.toString());
			String[] calendarTimings = blockedDurationWithMeetingId.split(SEPARATOR);
			Instant calendarBusyStart = InstantUtils.convertStringToInstant(calendarTimings[0]);
			Instant calendarBusyEnd = InstantUtils.convertStringToInstant(calendarTimings[1]);

			if (InstantUtils.areInstantsOverlapping(meetingStart, meetingEnd, calendarBusyStart, calendarBusyEnd)) {
				log.info(calendarTimings[2]);
				conflictingMeetingsIds.add(Integer.parseInt(calendarTimings[2]));
				
			}
		}
		return conflictingMeetingsIds;
	}
	
	/*
	 * Given: Meeting ID and Meeting Repository
	 * Checks whether valid meeting ID and date checks
	 * */
	public static boolean runDateChecksOnMeetingTimings(Meeting meeting) {
		
		DATE_PATTERN.setLenient(false);
		try {
			DATE_PATTERN.parse(meeting.getMeetingStartTime());
			DATE_PATTERN.parse(meeting.getMeetingEndTime());
		} catch (ParseException ex) {
			throw new DateFormatException(ex.toString());
		}
		
		Instant startInstant= InstantUtils.convertStringToInstant(meeting.getMeetingStartTime());
		Instant endInstant = InstantUtils.convertStringToInstant(meeting.getMeetingEndTime());
		if (startInstant.isAfter(endInstant) || startInstant.equals(endInstant)) 
			throw new DateFormatException("Meeting start time should be before meeting end time");
			
		return true;
	}


}
