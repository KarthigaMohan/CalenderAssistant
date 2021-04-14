package com.project.calendarAssistant;

import com.project.calendarAssistant.attributes.Position;
import com.project.calendarAssistant.entity.Employee;
import com.project.calendarAssistant.entity.Meeting;
import com.project.calendarAssistant.service.EmployeeService;
import com.project.calendarAssistant.service.MeetingService;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;



@SpringBootApplication
public class CalendarAssistantApplication {
	
	private static final Logger log= LoggerFactory.getLogger(CalendarAssistantApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(CalendarAssistantApplication.class, args);
	}
	
//	@Bean
//	public CommandLineRunner demo(EmployeeService es, MeetingService ms) {
//		return (args)->{
//			ArrayList<Integer> a = new ArrayList<>();
//			a.add(2);
//			a.add(3);
//			es.saveEmployee(new Employee(1, Position.SSE,"Karthiga","Mohan"));
//			es.saveEmployee(new Employee(2, Position.SSE,"Anshuman","Jha"));
//			es.saveEmployee(new Employee(3, Position.SSE,"Sid","Pillai"));
//			es.saveEmployee(new Employee(4, Position.SSE,"Sagar","Dhodi"));
//			ms.saveMeeting(new Meeting(100, 1,a , "2021-03-21 08:30:00", "2021-03-21 09:00:00"));
//			
//			for (Employee emp: es.listEmployees()) {
//				log.info("The employee saved is: " + emp.toString());
//				
//			}
//
//			for (Meeting emp: ms.listMeetings()) {
//				log.info("The meeting saved is: " + emp.toString());
//			}
//			
//		};
//		
//	}
	
	
//	@Bean
//	public CommandLineRunner demo(MeetingRepository er) {
//		ArrayList<Integer> l = new ArrayList<>();
//		l.add(1);
//		l.add(2);
//		return (args)->{
//			er.save(new Meeting(1, l, "2021-03-31 08:30:00", "2021-03-21 09:00:00"));
//			
//			for (Meeting emp: er.findAll()) {
//				log.info("The employee saved is: " + emp.toString());
//			}
//
//		};
//		
//	}


}
