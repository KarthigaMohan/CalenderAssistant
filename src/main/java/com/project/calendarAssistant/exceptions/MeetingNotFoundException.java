package com.project.calendarAssistant.exceptions;

public class MeetingNotFoundException extends RuntimeException{

	public MeetingNotFoundException(Integer id) {
		super("Meeting with ID: "+id+" is not found");
	}
}
