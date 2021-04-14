package com.project.calendarAssistant.rulesEngine;

import static org.mockito.BDDMockito.given;

import com.project.calendarAssistant.attributes.Position;
import com.project.calendarAssistant.entity.Employee;
import com.project.calendarAssistant.entity.Meeting;
import com.project.calendarAssistant.repository.EmployeeRepository;
import com.project.calendarAssistant.repository.MeetingRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
public class MeetingRulesEngineTest {
	
	private static final Logger log = LoggerFactory.getLogger(MeetingRulesEngineTest.class);
	
	@Mock
	private MeetingRepository meetingRepository;
	@Mock
	private EmployeeRepository employeeRepository;
	@InjectMocks
	private MeetingRulesEngine meetingRulesEngine;
	
	private Meeting m1, m2;
	private Employee e1, e2, e3;
	
	@BeforeEach
	public void setup() {
		
	}
	
	@Test
	public void resolveConflictForEmployeeWhenMeetingSetByThem() throws Exception {
		
		ArrayList<Integer> participantIds= new ArrayList<Integer>() {{
			add(3);add(2);add(4);add(1);
		}};	
		
		m1 = new Meeting(1, 1,participantIds,"2021-02-12 17:00:00", "2021-02-12 17:30:00");
		m2 = new Meeting(2, 2,participantIds,"2021-02-12 16:00:00", "2021-02-12 18:00:00");
		
		e1 = new Employee(1, Position.SSE, "Karthiga", "Mohan");
		e2 = new Employee(2, Position.DIRECTOR, "Anita", "Das");
		e1.getCalendar().setblockedDurationWithMeetingIdsWithMeetIds("2021-02-12 16:00:00;2021-02-12 18:00:00;2");
		e1.getCalendar().setblockedDurationWithMeetingIdsWithMeetIds("2021-02-12 17:00:00;2021-02-12 17:30:00;1");
		
		given(employeeRepository.findById(1)).willReturn(Optional.of(e1));
		given(employeeRepository.findById(2)).willReturn(Optional.of(e2));
		
		List<String> calendarAfterConflictResolution = new ArrayList<>() {{
			add("2021-02-12 17:00:00;2021-02-12 17:30:00;1");
		}};
		List<Integer> participantsAfterResolution = new ArrayList<>(){{
			add(3); add(2); add(4);
		}};

		Assert.assertTrue(meetingRulesEngine.runResolveEngine(e1, m1, m2));
		Assert.assertEquals(e1.getCalendar().getblockedDurationWithMeetingIds(), calendarAfterConflictResolution);
		Assert.assertEquals(m2.getParticipantIds(),participantsAfterResolution );
		
	}
	
	@Test
	public void resolveConflictForEmployeeWhenMeetingSetByDifferentPositions() throws Exception {
		
		ArrayList<Integer> participantIds= new ArrayList<Integer>() {{
			add(3);add(2);add(1);
		}};	
		
		m1 = new Meeting(1, 1,participantIds,"2021-02-12 17:00:00", "2021-02-12 17:30:00");
		m2 = new Meeting(2, 2,participantIds,"2021-02-12 16:00:00", "2021-02-12 18:00:00");
		
		e1 = new Employee(1, Position.SSE, "Karthiga", "Mohan");
		e2 = new Employee(2, Position.DIRECTOR, "Anita", "Das");
		e3 = new Employee(3, Position.INTERN, "Anjali", "Singh");
		
		e3.getCalendar().setblockedDurationWithMeetingIdsWithMeetIds("2021-02-12 16:00:00;2021-02-12 18:00:00;2");
		e3.getCalendar().setblockedDurationWithMeetingIdsWithMeetIds("2021-02-12 17:00:00;2021-02-12 17:30:00;1");
		
		given(employeeRepository.findById(1)).willReturn(Optional.of(e1));
		given(employeeRepository.findById(2)).willReturn(Optional.of(e2));
		
		List<String> calendarAfterConflictResolution = new ArrayList<>() {{
			add("2021-02-12 16:00:00;2021-02-12 18:00:00;2");
		}};
		List<Integer> participantsAfterResolution = new ArrayList<>(){{
			 add(2); add(1);
		}};

		Assert.assertTrue(meetingRulesEngine.runResolveEngine(e3, m1, m2));
		Assert.assertEquals(e3.getCalendar().getblockedDurationWithMeetingIds(), calendarAfterConflictResolution);
		Assert.assertEquals(m2.getParticipantIds(),participantsAfterResolution );
		
	}
	
	
	
	@Test
	public void resolveConflictForEmployeeWhenConflictingMeetingsSetBySameHost() throws Exception {
		
		ArrayList<Integer> participantIds= new ArrayList<Integer>() {{
			add(3);add(2);add(1);
		}};	
		
		ArrayList<Integer> participantIdsHigher= new ArrayList<Integer>() {{
			add(3);add(2);add(4);add(1);
		}};
		
		m1 = new Meeting(1, 1,participantIds,"2021-02-12 17:00:00", "2021-02-12 17:30:00");
		m2 = new Meeting(2, 1,participantIdsHigher,"2021-02-12 16:00:00", "2021-02-12 18:00:00");
		
		e1 = new Employee(1, Position.SSE, "Karthiga", "Mohan");
		e2 = new Employee(2, Position.DIRECTOR, "Anita", "Das");
		
		e2.getCalendar().setblockedDurationWithMeetingIdsWithMeetIds("2021-02-12 16:00:00;2021-02-12 18:00:00;2");
		e2.getCalendar().setblockedDurationWithMeetingIdsWithMeetIds("2021-02-12 17:00:00;2021-02-12 17:30:00;1");
		given(employeeRepository.findById(1)).willReturn(Optional.of(e1));
		
		List<String> calendarAfterConflictResolution = new ArrayList<>() {{
			add("2021-02-12 16:00:00;2021-02-12 18:00:00;2");
		}};
		List<Integer> participantsAfterResolution = new ArrayList<>(){{
			 add(3); add(1);
		}};

		Assert.assertTrue(meetingRulesEngine.runResolveEngine(e2, m1, m2));
		Assert.assertEquals(e2.getCalendar().getblockedDurationWithMeetingIds(), calendarAfterConflictResolution);
		Assert.assertEquals(m1.getParticipantIds(),participantsAfterResolution );
		
	}
	
	

}
