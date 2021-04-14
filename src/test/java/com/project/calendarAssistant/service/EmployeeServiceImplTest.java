package com.project.calendarAssistant.service;

import static org.mockito.BDDMockito.given;

import com.project.calendarAssistant.attributes.Position;
import com.project.calendarAssistant.entity.Employee;
import com.project.calendarAssistant.repository.EmployeeRepository;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.assertj.core.util.Arrays;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeServiceImplTest {

	private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImplTest.class);

	@Mock
	private EmployeeRepository employeeRep;
	@InjectMocks
	private EmployeeServiceImpl es;
	private Employee e1;
	private Employee e2;
	private String freeDuration;
	private List<Integer> employeeIds = new ArrayList<>();

	@BeforeEach
	public void setup() {
		
		//TODO: BeforeEach not running check dependencies in pom
		es = new EmployeeServiceImpl();
		
	}

	@Test
	public void getRightFreeDurationsBetweenEmployeesWithFreeDurationInputInMinutes() throws Exception {
		e1 = new Employee(1, Position.SSE, "Karthiga", "Mohan");
		e2 = new Employee(2, Position.DIRECTOR, "Anita", "Das");
		employeeIds.add(1);
		employeeIds.add(2);
		
		given(employeeRep.findById(1)).willReturn(Optional.of(e1));
		given(employeeRep.findById(2)).willReturn(Optional.of(e2));
		
		freeDuration = "2021-02-14 00:45:00";
		e1.getCalendar().setblockedDurationWithMeetingIdsWithMeetIds("2021-02-12 17:00:00;2021-02-12 17:30:00;5");
		e1.getCalendar().setblockedDurationWithMeetingIdsWithMeetIds("2021-02-13 16:15:00;2021-02-13 16:45:00;2");
		e1.getCalendar().setblockedDurationWithMeetingIdsWithMeetIds("2021-02-14 16:15:00;2021-02-14 17:00:00;7");

		e2.getCalendar().setblockedDurationWithMeetingIdsWithMeetIds("2021-02-12 17:00:00;2021-02-12 17:30:00;5");
		e2.getCalendar().setblockedDurationWithMeetingIdsWithMeetIds("2021-02-14 12:15:00;2021-02-14 13:45:00;2");
		e2.getCalendar().setblockedDurationWithMeetingIdsWithMeetIds("2021-02-14 21:15:00;2021-02-14 21:20:00;7");

		List<String> gottenAns =es.getFreeDurationsBetweenEmployees(employeeIds, freeDuration);
		String[] expectedAnsArray = new String[] {"2021-02-14T00:00:00Z;2021-02-14T00:45:00Z","2021-02-14T00:45:00Z;2021-02-14T01:30:00Z","2021-02-14T01:30:00Z;2021-02-14T02:15:00Z","2021-02-14T02:15:00Z;2021-02-14T03:00:00Z","2021-02-14T03:00:00Z;2021-02-14T03:45:00Z","2021-02-14T03:45:00Z;2021-02-14T04:30:00Z","2021-02-14T04:30:00Z;2021-02-14T05:15:00Z","2021-02-14T05:15:00Z;2021-02-14T06:00:00Z","2021-02-14T06:00:00Z;2021-02-14T06:45:00Z","2021-02-14T06:45:00Z;2021-02-14T07:30:00Z","2021-02-14T07:30:00Z;2021-02-14T08:15:00Z","2021-02-14T08:15:00Z;2021-02-14T09:00:00Z","2021-02-14T09:00:00Z;2021-02-14T09:45:00Z","2021-02-14T09:45:00Z;2021-02-14T10:30:00Z","2021-02-14T10:30:00Z;2021-02-14T11:15:00Z","2021-02-14T11:15:00Z;2021-02-14T12:00:00Z","2021-02-14T17:00:00Z;2021-02-14T17:45:00Z","2021-02-14T17:45:00Z;2021-02-14T18:30:00Z","2021-02-14T18:30:00Z;2021-02-14T19:15:00Z","2021-02-14T19:15:00Z;2021-02-14T20:00:00Z","2021-02-14T20:00:00Z;2021-02-14T20:45:00Z","2021-02-14T21:20:00Z;2021-02-14T22:05:00Z","2021-02-14T22:05:00Z;2021-02-14T22:50:00Z","2021-02-14T22:50:00Z;2021-02-14T23:35:00Z"};
		List<Object> expectedAns=Arrays.asList(expectedAnsArray);
		Assert.assertEquals(gottenAns, expectedAns);
		

	}
	
	
	@Test
	public void getRightFreeDurationsBetweenEmployeesWithFreeDurationInputInHours() throws Exception {
		
		e1 = new Employee(1, Position.SSE, "Karthiga", "Mohan");
		e2 = new Employee(2, Position.DIRECTOR, "Anita", "Das");
		employeeIds.add(1);
		employeeIds.add(2);
		
		given(employeeRep.findById(1)).willReturn(Optional.of(e1));
		given(employeeRep.findById(2)).willReturn(Optional.of(e2));
		
		freeDuration = "2021-02-14 05:00:00";
		e1.getCalendar().setblockedDurationWithMeetingIdsWithMeetIds("2021-02-12 17:00:00;2021-02-12 17:30:00;5");
		e1.getCalendar().setblockedDurationWithMeetingIdsWithMeetIds("2021-02-13 16:15:00;2021-02-13 16:45:00;2");
		e1.getCalendar().setblockedDurationWithMeetingIdsWithMeetIds("2021-02-14 16:15:00;2021-02-14 17:00:00;7");

		e2.getCalendar().setblockedDurationWithMeetingIdsWithMeetIds("2021-02-12 17:00:00;2021-02-12 17:30:00;5");
		e2.getCalendar().setblockedDurationWithMeetingIdsWithMeetIds("2021-02-14 12:15:00;2021-02-14 13:45:00;2");
		e2.getCalendar().setblockedDurationWithMeetingIdsWithMeetIds("2021-02-14 21:15:00;2021-02-14 21:20:00;7");
		
		List<String> gottenAns =es.getFreeDurationsBetweenEmployees(employeeIds, freeDuration);
		String[] expectedAnsArray = new String[]{"2021-02-14T00:00:00Z;2021-02-14T05:00:00Z","2021-02-14T05:00:00Z;2021-02-14T10:00:00Z","2021-02-14T16:15:00Z;2021-02-14T21:15:00Z"};
		List<Object> expectedAns = Arrays.asList(expectedAnsArray);
		Assert.assertEquals(gottenAns, expectedAns);

		
	}
	
	@Test
	public void getRightFreeDurationsBetweenEmployeesWithEmptyCalendars() throws Exception {
		
		e1 = new Employee(1, Position.SSE, "Karthiga", "Mohan");
		e2 = new Employee(2, Position.DIRECTOR, "Anita", "Das");
		employeeIds.add(1);
		employeeIds.add(2);
		
		given(employeeRep.findById(1)).willReturn(Optional.of(e1));
		given(employeeRep.findById(2)).willReturn(Optional.of(e2));
		
		freeDuration = "2021-02-14 05:00:00";
		List<String> gottenAns =es.getFreeDurationsBetweenEmployees(employeeIds, freeDuration);
		log.info(gottenAns.toString());
		String[] expectedAnsArray= new String[] {"2021-02-14T00:00:00Z;2021-02-14T05:00:00Z","2021-02-14T05:00:00Z;2021-02-14T10:00:00Z","2021-02-14T10:00:00Z;2021-02-14T15:00:00Z","2021-02-14T15:00:00Z;2021-02-14T20:00:00Z"};
		List<Object> expectedAns= Arrays.asList(expectedAnsArray);
		Assert.assertEquals(gottenAns, expectedAns);
		
		
	}
	
	@Test(expected=DateTimeParseException.class)
	public void invalidDurationThrowsException() throws Exception{
		
		e1 = new Employee(1, Position.SSE, "Karthiga", "Mohan");
		e2 = new Employee(2, Position.DIRECTOR, "Anita", "Das");
		employeeIds.add(1);
		employeeIds.add(2);
		
		given(employeeRep.findById(1)).willReturn(Optional.of(e1));
		given(employeeRep.findById(2)).willReturn(Optional.of(e2));
		
		freeDuration = "2021-02-14 29:00:00";
		es.getFreeDurationsBetweenEmployees(employeeIds, freeDuration);
		
	}

}
