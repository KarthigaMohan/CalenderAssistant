package com.project.calendarAssistant.service;

import com.project.calendarAssistant.entity.Meeting;
import java.util.List;

public interface MeetingService {
	
	public List<Meeting> listMeetings();
	public Meeting findMeetingById(int meetingId);
	public Meeting saveMeeting(Meeting meeting) ;
	public List<Integer> getConflictingMeetings(List<Integer> meetingIds);
	public List<Integer> getResolvedCalendars(List<Integer> meetingIds);
	
}
