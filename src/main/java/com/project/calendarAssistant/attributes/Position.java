package com.project.calendarAssistant.attributes;

public enum Position {
	DIRECTOR(0),
	MANAGER(1),
	LEAD(2),
	SSE(3),
	SE(4),
	INTERN(5);
	
	private int rank;
	private Position(int rank) {
		this.rank=rank;
	}
	
	public int getRank() {
		return rank;
	}
}
