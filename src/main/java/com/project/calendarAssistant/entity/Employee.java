package com.project.calendarAssistant.entity;

import com.project.calendarAssistant.attributes.Position;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Employee {

	public Employee() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "EMPLOYEE_ID")
	private int employeeId;

	@Column(name = "POSITION")
	private Position employeePosition;

	@Column(name = "FIRST_NAME")
	private String employeeFirstName;

	@Column(name = "LAST_NAME")
	private String employeeLastName;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "blockedDurationWithMeetingIds", column = @Column(name = "BLOCKED_DURATION"))
			,@AttributeOverride(name = "employeeId", column = @Column(name = "EMPLOYEE_ID", insertable = false, updatable = false)) })
	private Calendar calendar;
	
	public Employee(int employeeId, Position employeePosition, String employeeFirstName, String employeeLastName) {
		this.employeeId = employeeId;
		this.employeePosition = employeePosition;
		this.employeeFirstName = employeeFirstName;
		this.employeeLastName = employeeLastName;
		this.calendar= new Calendar(employeeId);
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public Position getEmployeePosition() {
		return employeePosition;
	}

	public void setEmployeePosition(Position employeePosition) {
		this.employeePosition = employeePosition;
	}

	public String getEmployeeFirstName() {
		return employeeFirstName;
	}

	public void setEmployeeFirstName(String employeeFirstName) {
		this.employeeFirstName = employeeFirstName;
	}

	public String getEmployeeLastName() {
		return employeeLastName;
	}

	public void setEmployeeLastName(String employeeLastName) {
		this.employeeLastName = employeeLastName;
	}

	public Calendar getCalendar() {
		return calendar;
	}

	@Override
	public String toString() {
		return "Employee [employeeId=" + employeeId + ", employeePosition=" + employeePosition + ", employeeFirstName="
				+ employeeFirstName + ", employeeLastName=" + employeeLastName + "calendar: "+calendar +"]";
	}

}
