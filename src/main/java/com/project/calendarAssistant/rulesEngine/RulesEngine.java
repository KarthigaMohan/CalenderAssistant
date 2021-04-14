package com.project.calendarAssistant.rulesEngine;

import com.project.calendarAssistant.entity.Employee;
import com.project.calendarAssistant.entity.Meeting;

public interface RulesEngine {
	
	public boolean runResolveEngine(Employee emp, Meeting externalConflictingMeeting, Meeting internalConflictingMeeting);

}
