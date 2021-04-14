package com.project.calendarAssistant.rulesEngine;

import com.project.calendarAssistant.entity.Employee;
import com.project.calendarAssistant.entity.Meeting;
import com.project.calendarAssistant.exceptions.MeetingNotFoundException;
import com.project.calendarAssistant.repository.EmployeeRepository;
import com.project.calendarAssistant.repository.MeetingRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MeetingRulesEngine implements RulesEngine{
	
	private static final Logger log= LoggerFactory.getLogger(MeetingRulesEngine.class);
	private static final String TIME_SEPARATOR = ";";
	@Autowired
	private EmployeeRepository empRep;
	@Autowired
	private MeetingRepository meetingRep;

	public boolean runResolveEngine (Employee emp, Meeting conflictingExternalMeeting, Meeting conflictingMeetingInCalendar){
		
		Employee meetingHost = empRep.findById(conflictingMeetingInCalendar.getHostId()).get();
		Employee conflictingMeetingHost = empRep.findById(conflictingExternalMeeting.getHostId()).get();
		
		if (emp.getEmployeeId()== conflictingExternalMeeting.getHostId()) {
			return resolvingConflictDetails(emp, conflictingMeetingInCalendar);
		}
		
		else if (conflictingMeetingInCalendar.getHostId()!=conflictingExternalMeeting.getHostId()) {
			Meeting meetToRemove= meetingHost.getEmployeePosition().getRank() > conflictingMeetingHost.getEmployeePosition().getRank() ?  conflictingMeetingInCalendar : conflictingExternalMeeting;
			return resolvingConflictDetails(emp, meetToRemove);
		}
		
		else if (conflictingMeetingHost.getEmployeeId()==meetingHost.getEmployeeId()) {
			Meeting meetToRemove = conflictingExternalMeeting.getParticipantIds().size() < conflictingMeetingInCalendar.getParticipantIds().size() ? conflictingExternalMeeting : conflictingMeetingInCalendar;
			return resolvingConflictDetails(emp, meetToRemove);
		}
	
		return false;
		}

	/*
	 * Method which performs the actual resolving by removing participant from meeting
	 * and removing meeting from participant calendar
	 * 
	 */
	private boolean resolvingConflictDetails(Employee emp, Meeting meeting) {

		String meetingTime = meeting.getMeetingStartTime() + TIME_SEPARATOR + meeting.getMeetingEndTime() + TIME_SEPARATOR + meeting.getMeetingId();
		meeting.getParticipantIds().remove((Integer) emp.getEmployeeId());
		meetingRep.save(meeting);
		emp.getCalendar().getblockedDurationWithMeetingIds().remove(meetingTime);
		empRep.save(emp);
		
		return true;

	}

}

