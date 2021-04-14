package com.project.calendarAssistant.controller;

import com.project.calendarAssistant.entity.Employee;
import com.project.calendarAssistant.entity.Meeting;
import com.project.calendarAssistant.exceptions.DateFormatException;
import com.project.calendarAssistant.exceptions.EmployeeNotFoundException;
import com.project.calendarAssistant.exceptions.MeetingNotFoundException;
import com.project.calendarAssistant.service.EmployeeService;
import com.project.calendarAssistant.service.MeetingService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/assistant")
public class AssistantController {
	
	private static final Logger log = LoggerFactory.getLogger(AssistantController.class);
	
	@Autowired
	private MeetingService meetingService;
	@Autowired
	private EmployeeService employeeService;
	
	
	@GetMapping("/employees")
	public ResponseEntity<List<Employee>> getAllEmployees(){
		return new ResponseEntity<List<Employee>>(employeeService.listEmployees(), HttpStatus.OK);
	} 
	
	@GetMapping("/meetings")
	public ResponseEntity<List<Meeting>> getAllMeetings(){
		return new ResponseEntity<List<Meeting>>(meetingService.listMeetings(), HttpStatus.OK);
	}
	
	@GetMapping("/meetings/{id}")
	public ResponseEntity<Meeting> getMeeting(@PathVariable("id") int meetingId){
		try {
			return new ResponseEntity<Meeting>(meetingService.findMeetingById(meetingId) , HttpStatus.OK);
		}
		catch(MeetingNotFoundException ex) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.toString());
		}
	}
	
	@GetMapping("/employees/{id}")
	public ResponseEntity<Employee> getEmployee(@PathVariable("id") int employeeId){
		try {
			return new ResponseEntity<Employee>(employeeService.findEmployee(employeeId) , HttpStatus.OK);
		}
		catch(EmployeeNotFoundException ex) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.toString());
		}
		
	}
	
	@PostMapping(value="/meetings", 
			consumes= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, 
			produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<Meeting> addMeeting(@RequestBody Meeting meeting){
		try {
			log.info(meeting.toString());
			Meeting savedMeeting= meetingService.saveMeeting(meeting);
			log.info(savedMeeting.toString());
			return new ResponseEntity<Meeting>(savedMeeting, HttpStatus.CREATED);
		}
		catch(DateFormatException ex){
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.toString());
		}
		
	}
	
	@PostMapping(value="/employees", 
			consumes= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, 
			produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee){
		Employee savedEmployee=employeeService.saveEmployee(employee);
	    return new ResponseEntity<Employee>(savedEmployee, HttpStatus.CREATED);
		
	}
	
	@GetMapping(value="/meetings/conflict/{meetingList}")
	public ResponseEntity<List<Integer>> getConflictingMeeting(@PathVariable("meetingList") List<Integer> meetingList){
		return new ResponseEntity<List<Integer>>(meetingService.getConflictingMeetings(meetingList), HttpStatus.OK);
	}
	
	@GetMapping(value="/meetings/resolve/{meetingList}")
	public ResponseEntity<List<Integer>> getResolvedMeetings(@PathVariable("meetingList") List<Integer> meetingList){
		return new ResponseEntity<List<Integer>>(meetingService.getResolvedCalendars(meetingList), HttpStatus.OK);
	}
	
	@GetMapping(value="/employees/free/{employeeIds}&{freeDuration}")
	public ResponseEntity<List<String>> getFreeDurationsOfEmployees(@PathVariable("employeeIds") List<Integer> employeeIds, 
			@PathVariable("freeDuration") String freeDuration){
		return new ResponseEntity<List<String>>(employeeService.getFreeDurationsBetweenEmployees(employeeIds, freeDuration), HttpStatus.OK);
	}
}
