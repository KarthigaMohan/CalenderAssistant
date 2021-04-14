package com.project.calendarAssistant.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.persistence.Id;


@Entity
public class Meeting {
	
	public Meeting() {}

	public Meeting(int meetingId, int hostId, ArrayList<Integer> participantIds, String meetingStartTime, String meetingEndTime) {
		this.meetingId=meetingId;
		this.hostId = hostId;
		this.participantIds = participantIds;
		this.meetingStartTime = meetingStartTime;
		this.meetingEndTime = meetingEndTime;
	}

	@Id
	@GeneratedValue (strategy=GenerationType.AUTO)
	@Column (name = "MEETING_ID")
	int meetingId;
	
	@Column (name = "HOST_ID")
	int hostId;
	
	@ElementCollection (fetch=FetchType.EAGER)
	@Column(name = "PARTICIPANT_IDS")
	List<Integer> participantIds=new ArrayList<>();
	
	@Column (name = "START_TIME")
	String meetingStartTime;
	
	@Column (name = "END_TIME")
	String meetingEndTime;
	

	public int getHostId() {
		return hostId;
	}

	public void setHostId(int hostId) {
		this.hostId = hostId;
	}

	public String getMeetingStartTime() {
		return meetingStartTime;
	}

	public void setMeetingStartTime(String meetingStartTime) {
		this.meetingStartTime = meetingStartTime;
	}

	public String getMeetingEndTime() {
		return meetingEndTime;
	}

	public void setMeetingEndTime(String meetingEndTime) {
		this.meetingEndTime = meetingEndTime;
	}

	public List<Integer> getParticipantIds() {
		return participantIds;
	}

	public void setParticipantIds(ArrayList<Integer> participantIds) {
		this.participantIds = participantIds;
	}


	public int getMeetingId() {
		return meetingId;
	}

	public void setMeetingId(int meetingId) {
		this.meetingId = meetingId;
	}


	@Override
	public String toString() {
		return "Meeting [meetingId=" + meetingId + ", hostId=" + hostId + ", participantIds=" + participantIds
				+ ", meetingStartTime=" + meetingStartTime + ", meetingEndTime=" + meetingEndTime + "]";
	}
	
	
}
