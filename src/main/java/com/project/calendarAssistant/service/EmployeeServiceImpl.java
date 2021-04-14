package com.project.calendarAssistant.service;

import com.project.calendarAssistant.entity.Employee;
import com.project.calendarAssistant.exceptions.DateFormatException;
import com.project.calendarAssistant.exceptions.EmployeeNotFoundException;
import com.project.calendarAssistant.repository.EmployeeRepository;
import com.project.calendarAssistant.utils.InstantComparator;
import com.project.calendarAssistant.utils.InstantUtils;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.TreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	private static final String SEPARATOR = ";";
	private static final String START_OF_DAY_TIME = " 00:00:00";
	private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);
	
	@Autowired
	private EmployeeRepository empRep;

	@Override
	public List<Employee> listEmployees() {
		return (List<Employee>) empRep.findAll();
	}

	@Override
	public Employee findEmployee(int employeeId) {
		Optional<Employee> optionalEmployee = empRep.findById(employeeId);

		if (optionalEmployee.isPresent())
			return optionalEmployee.get();
		else
			throw new EmployeeNotFoundException(employeeId);

	}

	@Override
	public Employee saveEmployee(Employee emp) {
		return empRep.save(emp);
	}

	/*
	 * Given : Two employee IDs , Duration of format: yyyy-MM-hh hh:mm:ss
	 * Result: List of possible free durations of format: yy-MM-dd hh:mm:ss;yy-MM-dd hh:mm:ss
	 * 
	 */
	public List<String> getFreeDurationsBetweenEmployees(List<Integer> employeeIds, String freeDuration){

		Employee[] employees;
		Employee firstEmp = null, secondEmp= null;
		employees = validateAndGetValidEmpoyees(employeeIds) ;
		firstEmp= employees[0];
		secondEmp= employees[1];

		List<String> freeDurations = new ArrayList<String>();

		String[] splitDuration=freeDuration.split("\s");
		LocalDate freeDurationDate=LocalDate.parse(splitDuration[0]);
		Instant freeDurationInstant=InstantUtils.convertStringToInstant(freeDuration);
		
		Instant durationStartOfDayInstant = InstantUtils.convertStringToInstant(freeDurationDate + START_OF_DAY_TIME);
		Instant durationEndOfDayInstant = InstantUtils.convertStringToInstant(freeDurationDate.plusDays(1) + START_OF_DAY_TIME);
		Duration freeDurationTime=Duration.between(durationStartOfDayInstant, freeDurationInstant);
		log.info("Starting instant: "+durationStartOfDayInstant + " End instant: "+ durationEndOfDayInstant + " Duration to check: "+freeDurationTime);
		
		TreeSet<Instant[]> firstEmployeeBlockedInstants = getSortedBlockedDurationInRequiredRange(firstEmp.getCalendar().getblockedDurationWithMeetingIds(), durationStartOfDayInstant, durationEndOfDayInstant);
		TreeSet<Instant[]> secondEmployeeBlockedInstants = getSortedBlockedDurationInRequiredRange(secondEmp.getCalendar().getblockedDurationWithMeetingIds(), durationStartOfDayInstant, durationEndOfDayInstant);
		ListIterator<Instant[]> firstBlockedInstants = new ArrayList<>(firstEmployeeBlockedInstants).listIterator();
		ListIterator<Instant[]> secondBlockedInstants = new ArrayList<>(secondEmployeeBlockedInstants).listIterator();
		
		Instant currentStart=durationStartOfDayInstant;
		Instant currentEnd=currentStart.plus(freeDurationTime);
		Instant[] firstInstants, secondInstants;
		Instant firstTimeStart, firstTimeEnd, secondTimeStart, secondTimeEnd;
		log.debug("Current start: "+currentStart.toString()+" Current End: "+currentEnd.toString());
		
		while (firstBlockedInstants.hasNext()  && secondBlockedInstants.hasNext()) {
			firstInstants=firstBlockedInstants.next();
			secondInstants=secondBlockedInstants.next();
			
			firstTimeStart=firstInstants[0];
			firstTimeEnd=firstInstants[1];
			secondTimeStart=secondInstants[0];
			secondTimeEnd=secondInstants[1];

			if (doesNotOverlapWithAnyDuration(firstTimeStart, secondTimeStart, currentEnd) ) {
				freeDurations.add(currentStart+SEPARATOR+currentEnd);
				currentStart=currentEnd;
				firstInstants= firstBlockedInstants.previous();
				secondInstants= secondBlockedInstants.previous();
				
			}
			
			else if (overlapsWithGivenDurationEnd(firstTimeStart, secondTimeEnd, currentEnd)) {
				firstInstants= firstBlockedInstants.previous();
				currentStart=secondTimeStart;
			}
			
			else if (overlapsWithGivenDurationEnd(secondTimeStart, firstTimeEnd, currentEnd)) {
				secondInstants= secondBlockedInstants.previous();
				currentStart=firstTimeStart;
			}
			
			//overlaps with both
			else {
				currentStart=firstTimeEnd.isAfter(secondTimeEnd) ? firstTimeEnd : secondTimeEnd;
			}
			currentEnd=currentStart.plus(freeDurationTime);
			log.debug("Free duration getting added: "+currentStart+SEPARATOR+currentEnd);
		}
		
		while (firstBlockedInstants.hasNext()) {
			firstInstants=firstBlockedInstants.next();
			firstTimeStart=firstInstants[0];
			firstTimeEnd=firstInstants[1];
			if ((currentEnd.isBefore(firstTimeStart)|| currentEnd.equals(firstTimeStart) ) ){
				freeDurations.add(currentStart+SEPARATOR+currentEnd);
				currentStart=currentEnd;
				firstBlockedInstants.previous();
			}
			else {
				currentStart= firstTimeEnd;
			}
			currentEnd=currentStart.plus(freeDurationTime);
			log.debug("Free duration getting added: "+currentStart+SEPARATOR+currentEnd);
		}
		
		
		while (secondBlockedInstants.hasNext()) {
			firstInstants=secondBlockedInstants.next();
			firstTimeStart=firstInstants[0];
			firstTimeEnd=firstInstants[1];
			if ((currentEnd.isBefore(firstTimeStart)|| currentEnd.equals(firstTimeStart) ) ){
				freeDurations.add(currentStart+SEPARATOR+currentEnd);
				currentStart=currentEnd;
				secondBlockedInstants.previous();
			}
			else {
				currentStart= firstTimeEnd;
			}
			currentEnd=currentStart.plus(freeDurationTime);
			log.debug("Free duration getting added: "+currentStart+SEPARATOR+currentEnd);
			
		}
		

		while (currentEnd.equals(durationEndOfDayInstant) || currentEnd.isBefore(durationEndOfDayInstant)) {
			freeDurations.add(currentStart+SEPARATOR+currentEnd);
			currentStart=currentEnd;
			currentEnd=currentStart.plus(freeDurationTime);
		}
		

		return freeDurations;
	}
	
	
	private Employee[] validateAndGetValidEmpoyees(List<Integer> employeeIds) {
		
		if (employeeIds.size()!=2) throw new RuntimeException("Will require 2 employee details");
		
		Optional<Employee> firstEmpOptional = empRep.findById(employeeIds.get(0));
		Optional<Employee> secondEmpOptional = empRep.findById(employeeIds.get(1));
		if (firstEmpOptional.isEmpty() || secondEmpOptional.isEmpty()) {
			throw new EmployeeNotFoundException(firstEmpOptional.isEmpty() ? employeeIds.get(0): employeeIds.get(1));
		}
		
		Employee firstEmp=firstEmpOptional.get(), secondEmp=secondEmpOptional.get();
		return new Employee[] {firstEmp,secondEmp};
	}

	/*
	 * Get required blocked durations within the same day
	 * 
	 */
	private TreeSet<Instant[]> getSortedBlockedDurationInRequiredRange(List<String> blockedDurations, Instant durationStartInstant, Instant durationEndInstant) {
		
		TreeSet<Instant[]> employeeOrderedBlockedInstants = new TreeSet<>(new InstantComparator());
		for (String blockedDuration: blockedDurations) {
			String[] blockedDurationStartAndEnd = blockedDuration.split(SEPARATOR);
			Instant blockedDurationStart = InstantUtils.convertStringToInstant(blockedDurationStartAndEnd[0]);
			Instant blockedDurationEnd = InstantUtils.convertStringToInstant(blockedDurationStartAndEnd[1]);
			
			if (InstantUtils.areInstantsOverlapping(blockedDurationStart,blockedDurationEnd , durationStartInstant, durationEndInstant))	{		
					employeeOrderedBlockedInstants.add(new Instant[] {blockedDurationStart, blockedDurationEnd});
			}
		}
		return employeeOrderedBlockedInstants;
		
	}
	
	
	private boolean doesNotOverlapWithAnyDuration (Instant firstTimeStart,Instant secondTimeStart, Instant currentEnd) {
		return ((currentEnd.isBefore(firstTimeStart)|| currentEnd.equals(firstTimeStart)) && 
				(currentEnd.equals(secondTimeStart) || currentEnd.isBefore(secondTimeStart)));
		
	}
	
	private boolean overlapsWithGivenDurationEnd(Instant firstTimeStart, Instant secondTimeEnd , Instant currentEnd) {
		return ((currentEnd.isAfter(secondTimeEnd) || currentEnd.equals(secondTimeEnd)) && (
				currentEnd.isBefore(firstTimeStart) || currentEnd.equals(firstTimeStart)));
	}
	

}

