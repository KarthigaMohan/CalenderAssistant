package com.project.calendarAssistant.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.calendarAssistant.attributes.Position;
import com.project.calendarAssistant.entity.Employee;
import com.project.calendarAssistant.entity.Meeting;
import com.project.calendarAssistant.repository.EmployeeRepository;
import com.project.calendarAssistant.repository.MeetingRepository;
import com.project.calendarAssistant.service.EmployeeService;
import com.project.calendarAssistant.service.MeetingService;
import java.util.ArrayList;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AssistantControllerTest {
	
	private static final Logger log= LoggerFactory.getLogger(AssistantControllerTest.class);

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	EmployeeService employeeService;
	@MockBean
	MeetingService meetingService;
	@MockBean
	EmployeeRepository employeeRepository;
	@MockBean
	MeetingRepository meetingRepository;
	@MockBean
	Employee employee;
	
	private JacksonTester<Employee> employeeTester;
	private JacksonTester<Meeting> meetingTester;
	
	@BeforeEach
	public void setup() {
		ObjectMapper objMapper = new ObjectMapper();
		objMapper.configure(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES, false);
		JacksonTester.initFields(this, objMapper);
	}
	

	@Ignore
	public void getAllEmployees() throws Exception {
		mockMvc.perform(get("/assistant/employees")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(content().json("[]"));
		}
	
	@Test
	public void getAllMeetings() throws Exception {
//		mockMvc.perform(get("/assistant/meetings")).andExpect(status().isOk())
//				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(content().json("[]"));
		}
	
//	@Ignore
//    public void canRetrieveEmployeeByIdWhenExists() throws Exception {
//        given(employeeRepository.findById(1).get())
//                .willReturn(new Employee(Position.DIRECTOR, "Mannon", "RobotMan");
//
//        // when
//        MockHttpServletResponse response = mvc.perform(
//                get("/superheroes/2")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andReturn().getResponse();
//
//        // then
//        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
//        assertThat(response.getContentAsString()).isEqualTo(
//                jsonSuperHero.write(new SuperHero("Rob", "Mannon", "RobotMan")).getJson()
//        );
//    }

	
	@Ignore
    public void canCreateNewEmployee() throws Exception {
		Employee e = new Employee(1, Position.DIRECTOR, "Karthiga", "Mohan");
		log.info(e.toString());
		log.info(employeeTester.write(e).toString());
		
		log.info(employeeTester.write(new Employee(1, Position.DIRECTOR, "Karthiga", "Mohan")).getJson());
        mockMvc.perform(post("/assistant/employees").contentType(MediaType.APPLICATION_JSON_VALUE).content(
        		employeeTester.write(new Employee(1, Position.DIRECTOR, "Karthiga", "Mohan")).getJson()
                )).andExpect(status().isCreated());
    }
	
	
	@Ignore
    public void canCreateNewMeeting() throws Exception {
		ArrayList<Integer> a = new ArrayList<>();
		a.add(1);
		a.add(2);
		a.add(3);
		
		Meeting e = new Meeting(100, 1,a , "2021-03-31 08:30:00", "2021-03-21 09:00:00");
		//Meeting e = new Meeting(0, 1,a , "2021-03-31 08:30:00", "2021-03-21 09:00:00");
		log.info(e.toString());
		log.info(meetingTester.write(e).toString());
		
		//log.info(employeeTester.write(new Employee(Position.DIRECTOR, "Karthiga", "Mohan")).getJson());
        mockMvc.perform(post("/assistant/meetings").contentType(MediaType.APPLICATION_JSON_VALUE).content(
        		meetingTester.write(new Meeting(100,1, a , "2021-03-31 08:30:00", "2021-03-21 09:00:00")).getJson()
                )).andExpect(status().isCreated());
    }
	
	

}
