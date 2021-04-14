package com.project.calendarAssistant.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;


@Embeddable
public class Calendar {
	private int employeeId;
	 
	@ElementCollection (fetch=FetchType.EAGER)
	List<String> blockedDurationWithMeetingIds = new ArrayList<>();
	public Calendar() {}

	public Calendar(int id) {
		this.employeeId = id;

	}
	
	public void setblockedDurationWithMeetingIdsWithMeetIds(String meetingTime) {
		this.blockedDurationWithMeetingIds.add(meetingTime);
	}

	public List<String> getblockedDurationWithMeetingIds() {
		return blockedDurationWithMeetingIds;
	}

	@Override
	public String toString() {
		return "Calendar [employeeId=" + employeeId + ", blockedDurationWithMeetingIds=" + blockedDurationWithMeetingIds.toString()
				+ "]";
	}

	
}
