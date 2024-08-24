package com.vou.event_service.service;

import com.vou.event_service.common.NotFoundException;
import com.vou.event_service.entity.CreateEventRequest;
import com.vou.event_service.model.Event;
import com.vou.event_service.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    public List<Event> getAllEvents() throws Exception{
        try {
            return eventRepository.findAll();
        }
        catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public Event createEvent(CreateEventRequest request) throws Exception{
        Event newEvent = new Event();
        newEvent.setEventName(request.getEventName());
        newEvent.setNumberOfVouchers(request.getNumberOfVouchers());
        newEvent.setStartDate(request.getStartDate());
        newEvent.setEndDate(request.getEndDate());
        try {
            return eventRepository.save(newEvent);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public Event findEventById(Long id) throws Exception {
        try {
            Event eventFound = eventRepository.findByIdEvent(id);
            return eventFound;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public Event updateEventById(Long id, CreateEventRequest request) throws Exception {
        try {
            Event event = eventRepository.findByIdEvent(id);
            if (event == null)
                throw new NotFoundException("Event not found");
            else {
                Optional.ofNullable(request.getEventName()).ifPresent(action -> event.setEventName(request.getEventName()));
                Optional.ofNullable(request.getEndDate()).ifPresent(action -> event.setEndDate(request.getEndDate()));
                Optional.ofNullable(request.getStartDate()).ifPresent(action -> event.setStartDate(request.getStartDate()));
                Optional.ofNullable(request.getNumberOfVouchers()).ifPresent(action -> event.setNumberOfVouchers(request.getNumberOfVouchers()));
                eventRepository.save(event);
            }
            return event;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public boolean deleteEventById(Long id) throws Exception {
        try {
            Event event = eventRepository.findByIdEvent(id);
            if (event == null)
                throw new NotFoundException("Event not found");
            eventRepository.delete(event);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

}
