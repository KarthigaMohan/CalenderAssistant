package com.project.calendarAssistant.exceptions;

public class EmployeeNotFoundException extends RuntimeException{
	public EmployeeNotFoundException(Integer id) {
		super("Employee with ID: "+id+" is not found");
	}

}
