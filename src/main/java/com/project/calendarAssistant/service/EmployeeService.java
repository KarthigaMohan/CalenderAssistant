package com.project.calendarAssistant.service;

import java.util.List;

import com.project.calendarAssistant.entity.Employee;

public interface EmployeeService {
	
	public List<Employee> listEmployees();
	public Employee findEmployee(int employeeId);
	public Employee saveEmployee(Employee emp) ;
	public List<String> getFreeDurationsBetweenEmployees(List<Integer> employeeIds, String freeDuration) ;

}
