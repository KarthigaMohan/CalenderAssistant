# CalenderAssistant
Overview

A calendar assistant is a program that assists the calendar owner by
automatically resolving the meeting conflicts in the calendar of the owner by running rules engine. 

Simple rule engine: 
1. If the calendar owner is the meeting organizer, then this meeting has a
higher priority. 
2. The meeting whose organiser has lower rank is of higher priority. Each
employee in the system has a ‘rank’. Lower rank means higher position,
for example- Director's rank is higher than that of a manager's rank,
similarly, the rank of a manager is higher than that of an individual
contributor. 
3. In case two meetings have the same organizer, then the meeting with the
more number of invitees has a higher priority. 

Program functions: 
1. Given a list of meetings as input, find out the list of conflicting meetings
2. On the same input as (1), find out the resolved calendar that has no
conflicts i.e. all the conflicts are resolved by the rule engine. The rule
engine can be specified at run time. 
3. Given the calendar of 2 employees as input, find out the free slots where
a meeting of a fixed duration (say half an hour) can be scheduled. 

TODO: 
Error management and logging. 
