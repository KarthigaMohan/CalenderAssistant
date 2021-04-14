package com.project.calendarAssistant.service;

import com.project.calendarAssistant.entity.Employee;
import com.project.calendarAssistant.entity.Meeting;
import com.project.calendarAssistant.exceptions.EmployeeNotFoundException;
import com.project.calendarAssistant.exceptions.MeetingNotFoundException;
import com.project.calendarAssistant.repository.EmployeeRepository;
import com.project.calendarAssistant.repository.MeetingRepository;
import com.project.calendarAssistant.rulesEngine.MeetingRulesEngine;
import com.project.calendarAssistant.rulesEngine.RulesEngine;
import com.project.calendarAssistant.utils.InstantUtils;
import com.project.calendarAssistant.utils.MeetingUtils;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MeetingServiceImpl implements MeetingService {
	
	private static final Logger log= LoggerFactory.getLogger(MeetingServiceImpl.class);
	private static final String SEPARATOR = ";";

	@Autowired
	private MeetingRepository meetingRep;
	@Autowired
	private EmployeeRepository employeeRep;
	@Autowired
	private RulesEngine meetingRulesEngine;

	@Override
	public List<Meeting> listMeetings() {
		return (List<Meeting>) meetingRep.findAll();
	}

	@Override
	public Meeting findMeetingById(int meetingId) {
		Optional<Meeting> optionalMeeting = meetingRep.findById(meetingId);
		if (optionalMeeting.isPresent())
			return optionalMeeting.get();
		else
			throw new MeetingNotFoundException(meetingId);
	}

	// TODO: Make transactional and dont save meeting if participants not present
	/*
	 * Saves meet details in the Meet DB 
	 * Also updates busy duration and meet for each participant and host
	 */
	public Meeting saveMeeting(Meeting meeting) {
		
		MeetingUtils.runDateChecksOnMeetingTimings(meeting);
		
		for (int participantId : meeting.getParticipantIds()) {
			if (employeeRep.findById(participantId).isEmpty())
				throw new EmployeeNotFoundException(participantId);
		}
		
		Meeting meetingSaved = meetingRep.save(meeting);
		meeting.getParticipantIds().add(meeting.getHostId());
		
		String toUpdate = meetingSaved.getMeetingStartTime() + SEPARATOR + meetingSaved.getMeetingEndTime() + SEPARATOR
				+ meetingSaved.getMeetingId();
		log.debug("Date for meeting being interally saved as : "+toUpdate);

		for (int participantId : meeting.getParticipantIds()) {
			Employee emp = employeeRep.findById(participantId).get();
			emp.getCalendar().getblockedDurationWithMeetingIds().add(toUpdate);
			employeeRep.save(emp);
			log.info("Updating meeting details for participant with employee ID: "+participantId);
			
		}
		return meetingSaved;
	}

	/*
	 * Given : List of meeting IDs 
	 * Return: List of conflicting meetings IDs 
	 * Meeting IDs to be integers
	 */

	public List<Integer> getConflictingMeetings(List<Integer> meetingIds) {
		Set<Integer> conflictMeetingIds = new HashSet<>();

		for (int meetingId : meetingIds) {
			Optional<Meeting> meetingOptional = meetingRep.findById(meetingId);
			if (meetingOptional.isEmpty()) 
				throw new MeetingNotFoundException(meetingId);
			Meeting meeting = meetingOptional.get();
			
			MeetingUtils.runDateChecksOnMeetingTimings(meeting);

			String meetingTime = meeting.getMeetingStartTime() + SEPARATOR + meeting.getMeetingEndTime();
			HashSet<Integer> participantIds= new HashSet<Integer>(meeting.getParticipantIds());
			participantIds.add(meeting.getHostId());

			for (Integer participantId : participantIds) {
				Optional<Employee> empOptional = employeeRep.findById(participantId);
				if (empOptional.isEmpty())
					throw new EmployeeNotFoundException(participantId);
				List<Integer> conflictingMeetIds = MeetingUtils.getMeetingConflictingWithEmployeeCalendar(empOptional.get(), meetingTime );
				conflictingMeetIds.remove((Integer)meetingId);
				for (Integer conflictingMeetId: conflictingMeetIds) {
					conflictMeetingIds.add(conflictingMeetId);
				}
					
			}
		}

		return new ArrayList<>(conflictMeetingIds);
	}

	/*
	 * Given : List of meetings 
	 * Return: List of employee IDs whose calendar is now
	 * resolved of conflicting meetings
	 */
	public List<Integer> getResolvedCalendars(List<Integer> meetingIds) {

		List<Integer> employeeIdsWithResolvedCalendars = new ArrayList<>();
		for (int meetingId : meetingIds) {
			
			Optional<Meeting> meetingOptional = meetingRep.findById(meetingId);
			if (meetingOptional.isEmpty()) 
				throw new MeetingNotFoundException(meetingId);
			
			Meeting meeting = meetingOptional.get();
			MeetingUtils.runDateChecksOnMeetingTimings(meeting);

			String meetingTime = meeting.getMeetingStartTime() + SEPARATOR + meeting.getMeetingEndTime();
			HashSet<Integer> participantIds= new HashSet<Integer>(meeting.getParticipantIds());
			participantIds.add(meeting.getHostId());

			for (Integer participantId : participantIds) {
				Optional<Employee> empOptional = employeeRep.findById(participantId);
				if (empOptional.isEmpty())
					throw new EmployeeNotFoundException(participantId);
				//meetingRulesEngine = new MeetingRulesEngine();
				List<Integer> conflictingMeetIds = MeetingUtils.getMeetingConflictingWithEmployeeCalendar(empOptional.get(),meetingTime);
				conflictingMeetIds.remove((Integer)meetingId);
				for (Integer conflictingMeetId: conflictingMeetIds) {
					if (meetingRulesEngine.runResolveEngine(empOptional.get(), meeting,
							meetingRep.findById(conflictingMeetId).get())) {
						employeeIdsWithResolvedCalendars.add(participantId);
					}
				
				}	
			}
		}

		return employeeIdsWithResolvedCalendars;
	}

}
