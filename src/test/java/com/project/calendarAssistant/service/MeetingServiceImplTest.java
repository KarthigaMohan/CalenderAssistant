package com.project.calendarAssistant.service;

import static org.mockito.BDDMockito.given;

import com.project.calendarAssistant.attributes.Position;
import com.project.calendarAssistant.entity.Employee;
import com.project.calendarAssistant.entity.Meeting;
import com.project.calendarAssistant.exceptions.MeetingNotFoundException;
import com.project.calendarAssistant.repository.EmployeeRepository;
import com.project.calendarAssistant.repository.MeetingRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(MockitoJUnitRunner.class)
public class MeetingServiceImplTest {

	private static final Logger log = LoggerFactory.getLogger(MeetingServiceImplTest.class);

	@Mock
	private MeetingRepository meetingRepository;
	@Mock
	private EmployeeRepository employeeRepository;
	@InjectMocks
	private MeetingServiceImpl ms;

	private Meeting m1, m2;
	private Employee e1, e2, e3;

	@BeforeEach
	public void setup() {
		// TODO: BeforeEach not running check dependencies in pom
		ms = new MeetingServiceImpl();
	}

	@Ignore
	public void getCorrectListOfConflictingMeetingIdsWithInputConflicting() throws Exception {

		ArrayList<Integer> participantIds = new ArrayList<Integer>() {
			{
				add(3);
				add(2);
			}
		};
		m1 = new Meeting(1, 1, participantIds, "2021-02-12 17:00:00", "2021-02-12 17:30:00");

		e1 = new Employee(1, Position.SSE, "Karthiga", "Mohan");
		e2 = new Employee(2, Position.DIRECTOR, "Anita", "Das");
		e3 = new Employee(3, Position.INTERN, "Anjali", "Singh");

		e1.getCalendar().setblockedDurationWithMeetingIdsWithMeetIds("2021-02-12 17:00:00;2021-02-12 18:00:00;5");
		e1.getCalendar().setblockedDurationWithMeetingIdsWithMeetIds("2021-02-12 16:15:00;2021-02-13 16:45:00;2");
		e1.getCalendar().setblockedDurationWithMeetingIdsWithMeetIds("2021-02-14 16:15:00;2021-02-14 17:00:00;7");

		e2.getCalendar().setblockedDurationWithMeetingIdsWithMeetIds("2021-02-12 17:00:00;2021-02-12 17:30:00;4");
		e2.getCalendar().setblockedDurationWithMeetingIdsWithMeetIds("2021-02-14 12:15:00;2021-02-14 13:45:00;3");
		e2.getCalendar().setblockedDurationWithMeetingIdsWithMeetIds("2021-02-14 21:15:00;2021-02-14 21:20:00;8");

		given(employeeRepository.findById(1)).willReturn(Optional.of(e1));
		given(employeeRepository.findById(2)).willReturn(Optional.of(e2));
		given(employeeRepository.findById(3)).willReturn(Optional.of(e3));
		given(meetingRepository.findById(1)).willReturn(Optional.of(m1));

		List<Integer> meetingList = new ArrayList<Integer>() {
			{
				add(1);
			}
		};

		List<Integer> expectedAns = new ArrayList<Integer>() {
			{
				add(1);
				add(4);
				add(5);
			}
		};
		List<Integer> actualAns = ms.getConflictingMeetings(meetingList);
		log.info(actualAns.toString());
		Assert.assertEquals(actualAns, expectedAns);

	}
	
	@Ignore
	public void getCorrectListOfConflictingMeetingIdsWithOneInputNonConflicting() throws Exception {
	
		ArrayList<Integer> participantIds= new ArrayList<Integer>() {{
			add(3);
			add(2);
		}};		
		m1 = new Meeting(1, 1,participantIds,"2021-02-12 17:00:00", "2021-02-12 17:30:00");
		m2 = new Meeting(5, 2,participantIds,"2021-02-19 17:00:00", "2021-02-19 17:30:00");
		
		e1 = new Employee(1, Position.SSE, "Karthiga", "Mohan");
		e2 = new Employee(2, Position.DIRECTOR, "Anita", "Das");
		e3 = new Employee(3, Position.INTERN, "Anjali", "Singh");
		
		e1.getCalendar().setblockedDurationWithMeetingIdsWithMeetIds("2021-02-12 17:00:00;2021-02-12 18:00:00;5");
		e1.getCalendar().setblockedDurationWithMeetingIdsWithMeetIds("2021-02-12 16:15:00;2021-02-13 16:45:00;2");
		e1.getCalendar().setblockedDurationWithMeetingIdsWithMeetIds("2021-02-14 16:15:00;2021-02-14 17:00:00;7");
		
		e2.getCalendar().setblockedDurationWithMeetingIdsWithMeetIds("2021-02-12 17:00:00;2021-02-12 17:30:00;4");
		e2.getCalendar().setblockedDurationWithMeetingIdsWithMeetIds("2021-02-14 12:15:00;2021-02-14 13:45:00;3");
		e2.getCalendar().setblockedDurationWithMeetingIdsWithMeetIds("2021-02-14 21:15:00;2021-02-14 21:20:00;8");
		
		given(employeeRepository.findById(1)).willReturn(Optional.of(e1));
		given(employeeRepository.findById(2)).willReturn(Optional.of(e2));
		given(employeeRepository.findById(3)).willReturn(Optional.of(e3));
		given(meetingRepository.findById(1)).willReturn(Optional.of(m1));
		given(meetingRepository.findById(2)).willReturn(Optional.of(m2));
		
		List<Integer> meetingList = new ArrayList<Integer>() {{
			add(1);add(2);
		}};
		
		List<Integer> expectedAns = new ArrayList<Integer>() {{
			add(1); add(4); add(5);
		}};
		List<Integer> actualAns= ms.getConflictingMeetings(meetingList);
		log.info(actualAns.toString());
		Assert.assertEquals(actualAns, expectedAns);
		
	}
	
	@Test
	public void getCorrectListOfConflictingMeetingIdsWithNoInputConflicting() throws Exception {
	
		ArrayList<Integer> participantIds= new ArrayList<Integer>() {{
			add(3);
			add(2);
		}};		
		m1 = new Meeting(1, 1,participantIds,"2021-02-12 17:00:00", "2021-02-12 17:30:00");
		m2 = new Meeting(5, 2,participantIds,"2021-02-14 13:00:00", "2021-02-14 14:30:00");
		
		e1 = new Employee(1, Position.SSE, "Karthiga", "Mohan");
		e2 = new Employee(2, Position.DIRECTOR, "Anita", "Das");
		e3 = new Employee(3, Position.INTERN, "Anjali", "Singh");
		
		e1.getCalendar().setblockedDurationWithMeetingIdsWithMeetIds("2021-02-12 17:00:00;2021-02-12 18:00:00;6");
		e1.getCalendar().setblockedDurationWithMeetingIdsWithMeetIds("2021-02-12 16:15:00;2021-02-13 16:45:00;2");
		e1.getCalendar().setblockedDurationWithMeetingIdsWithMeetIds("2021-02-14 16:15:00;2021-02-14 17:00:00;7");
		
		e2.getCalendar().setblockedDurationWithMeetingIdsWithMeetIds("2021-02-12 17:00:00;2021-02-12 17:30:00;4");
		e2.getCalendar().setblockedDurationWithMeetingIdsWithMeetIds("2021-02-14 12:15:00;2021-02-14 13:45:00;3");
		e2.getCalendar().setblockedDurationWithMeetingIdsWithMeetIds("2021-02-14 21:15:00;2021-02-14 21:20:00;8");
		
		given(employeeRepository.findById(1)).willReturn(Optional.of(e1));
		given(employeeRepository.findById(2)).willReturn(Optional.of(e2));
		given(employeeRepository.findById(3)).willReturn(Optional.of(e3));
		given(meetingRepository.findById(1)).willReturn(Optional.of(m1));
		given(meetingRepository.findById(2)).willReturn(Optional.of(m2));
		
		List<Integer> meetingList = new ArrayList<Integer>() {{
			add(1);add(2);
		}};
		
		List<Integer> expectedAns = new ArrayList<Integer>() {{
			add(1); add(2); add(3); add(4); add(6);
		}};
		List<Integer> actualAns= ms.getConflictingMeetings(meetingList);
		Assert.assertEquals(actualAns, expectedAns);
		
	}
	
	
	@Test(expected=MeetingNotFoundException.class)
	public void checkIfExceptionThrownIfMeetingIdInvalid() throws Exception {
		
		ArrayList<Integer> participantIds= new ArrayList<Integer>() {{
			add(3);
			add(2);
		}};
		
		m1 = new Meeting(1, 1,participantIds,"2021-02-12 17:00:00", "2021-02-12 17:30:00");
		e1 = new Employee(1, Position.SSE, "Karthiga", "Mohan");
		e2 = new Employee(2, Position.DIRECTOR, "Anita", "Das");
		ms.getConflictingMeetings(participantIds);
	}
	
	
	@Ignore
	public void resolveMeetingWhenConflictOccurs() throws Exception {
		
		ArrayList<Integer> participantIds= new ArrayList<Integer>() {{
			add(3);add(2);add(1);
		}};		
		m1 = new Meeting(1, 1,participantIds,"2021-02-12 17:00:00", "2021-02-12 17:30:00");
		m2 = new Meeting(2, 2,participantIds,"2021-02-12 16:00:00", "2021-02-12 18:00:00");
		
		e1 = new Employee(1, Position.SSE, "Karthiga", "Mohan");
		e2 = new Employee(2, Position.DIRECTOR, "Anita", "Das");
		e3 = new Employee(3, Position.INTERN, "Anjali", "Singh");
		
		e1.getCalendar().setblockedDurationWithMeetingIdsWithMeetIds("2021-02-12 17:00:00;2021-02-12 17:30:00;1");
		e1.getCalendar().setblockedDurationWithMeetingIdsWithMeetIds("2021-02-12 16:00:00;2021-02-12 18:00:00;2");
		
		e2.getCalendar().setblockedDurationWithMeetingIdsWithMeetIds("2021-02-12 17:00:00;2021-02-12 17:30:00;1");
		e2.getCalendar().setblockedDurationWithMeetingIdsWithMeetIds("2021-02-12 16:00:00;2021-02-12 18:00:00;2");
		
		e3.getCalendar().setblockedDurationWithMeetingIdsWithMeetIds("2021-02-12 17:00:00;2021-02-12 17:30:00;1");
		e3.getCalendar().setblockedDurationWithMeetingIdsWithMeetIds("2021-02-12 16:00:00;2021-02-12 18:00:00;2");
		
		given(employeeRepository.findById(1)).willReturn(Optional.of(e1));
		given(employeeRepository.findById(2)).willReturn(Optional.of(e2));
		given(employeeRepository.findById(3)).willReturn(Optional.of(e3));
		given(meetingRepository.findById(1)).willReturn(Optional.of(m1));
		given(meetingRepository.findById(2)).willReturn(Optional.of(m2));
		
		//TODO: MeetingRulesEngine not calling mocked values
		List<Integer> meetingList = new ArrayList<Integer>() {{
			add(1);add(2);
		}};
		
		List<Integer> expectedAns = new ArrayList<Integer>() {{
			//
		}};
		List<Integer> actualAns= ms.getResolvedCalendars(meetingList);
		log.info(actualAns.toString());
		Assert.assertEquals(actualAns, expectedAns);
		
	}
	
	
}
