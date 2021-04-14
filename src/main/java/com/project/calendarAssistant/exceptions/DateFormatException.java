package com.project.calendarAssistant.exceptions;

public class DateFormatException extends RuntimeException{
	public DateFormatException(String exception) {
		super("Date is not in required format: "+exception);
	}

}
