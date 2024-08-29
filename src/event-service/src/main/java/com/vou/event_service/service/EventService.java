package com.vou.event_service.service;

import com.vou.event_service.common.NotFoundException;
import com.vou.event_service.dto.EventDetailDTO;
import com.vou.event_service.dto.GameInfoDTO;
import com.vou.event_service.dto.InventoryDetailDTO;
import com.vou.event_service.dto.ListEventDTO;
import com.vou.event_service.entity.CreateEventRequest;
import com.vou.event_service.model.BrandsCooperation;
import com.vou.event_service.model.Event;
import com.vou.event_service.repository.BrandsCooperationRepository;
import com.vou.event_service.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private QuizService quizService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private BrandsCooperationRepository brandsCooperationRepository;

    public List<Event> getAllEvents() throws Exception{
        try {
            return eventRepository.findAll();
        }
        catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    public List<ListEventDTO> getAllEventOfBrand(Long brandId){
        List<Long> eventIds = brandsCooperationRepository.findAllByIdBrand(brandId)
                .stream()
                .map(BrandsCooperation::getIdEvent)
                .collect(Collectors.toList());

        List<Event> events =  eventRepository.findAllById(eventIds);
        return events.stream()
                .map(ListEventDTO::new)  // Convert each Event to ListEventDTO using the constructor
                .collect(Collectors.toList());
    }
    public List<ListEventDTO> getAllEventActive(){
        Timestamp currentTimestamp = Timestamp.from(Instant.now());;
        List<Event> events = eventRepository.findActiveEvents(currentTimestamp);

        return events.stream()
                .map(ListEventDTO::new)  // Convert each Event to ListEventDTO using the constructor
                .collect(Collectors.toList());

    }
    public EventDetailDTO getAnEvent(Long eventId)throws Exception{
        try {
            Event event = eventRepository.findByIdEvent(eventId);
            GameInfoDTO gameInfoDTO = quizService.getGameInfo(event.getIdEvent());
            InventoryDetailDTO inventoryDetailDTO = inventoryService.getInventoryInfo(event.getIdEvent());
            List<BrandsCooperation> brandsCooperations = brandsCooperationRepository.findAllByIdEvent(event.getIdEvent());

            EventDetailDTO eventDetailDTO = new EventDetailDTO(
                    event.getIdEvent(),
                    event.getEventName(),
                    event.getNumberOfVouchers(),
                    event.getImageUrl(),
                    event.getStartDate(),
                    event.getEndDate(),
                    brandsCooperations,
                    gameInfoDTO,
                    inventoryDetailDTO
            );
            return eventDetailDTO;
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
            return eventRepository.findByIdEvent(id);
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

    public Boolean uploadEventImage(Event event, String bannerUrl) throws Exception {
        try {
            event.setImageUrl(bannerUrl);
            eventRepository.save(event);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }
}
