package com.project.calendarAssistant.repository;


import com.project.calendarAssistant.entity.Meeting;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingRepository extends CrudRepository<Meeting, Integer>{

}
