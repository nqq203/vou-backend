package com.vou.event_service.service;

import com.vou.event_service.common.NotFoundException;
import com.vou.event_service.dto.EventDetailDTO;
import com.vou.event_service.dto.GameInfoDTO;
import com.vou.event_service.dto.InventoryDetailDTO;
import com.vou.event_service.dto.ListEventDTO;
import com.vou.event_service.entity.CreateEventRequest;
import com.vou.event_service.model.BrandsCooperation;
import com.vou.event_service.model.Event;
import com.vou.event_service.model.FavouriteEvent;
import com.vou.event_service.repository.BrandsCooperationRepository;
import com.vou.event_service.repository.EventRepository;
import com.vou.event_service.repository.FavouriteEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
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
    @Autowired
    private FavouriteEventRepository favouriteEventRepository;
    @Autowired
    private BrandClient brandClient;

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
                .map(event -> new ListEventDTO(event, brandClient.getBrandLogo(event.getCreatedBy()).orElse(null)))  // Convert each Event to ListEventDTO using the constructor
                .collect(Collectors.toList());
    }
    public List<ListEventDTO> getAllEventActive(){
        Timestamp currentTimestamp = Timestamp.from(Instant.now());;
        List<Event> events = eventRepository.findActiveEvents(currentTimestamp);
        System.out.println("Danh sách events: " + events);
        System.out.println("Vào getAllEventActive và đã tìm xong");

        return events.stream()
                .map(event -> new ListEventDTO(event, brandClient.getBrandLogo(event.getCreatedBy()).orElse(null)))  // Convert each Event to ListEventDTO using the constructor
                .collect(Collectors.toList());

    }
//    public EventDetailDTO getAnEvent(Long eventId)throws Exception{
//        try {
//            Event event = eventRepository.findByIdEvent(eventId);
//            GameInfoDTO gameInfoDTO = quizService.getGameInfo(event.getIdEvent());
//            InventoryDetailDTO inventoryDetailDTO = inventoryService.getInventoryInfo(event.getIdEvent());
//            List<BrandsCooperation> brandsCooperations = brandsCooperationRepository.findAllByIdEvent(event.getIdEvent());
//
//            EventDetailDTO eventDetailDTO = new EventDetailDTO(
//                    event.getIdEvent(),
//                    event.getEventName(),
//                    event.getNumberOfVouchers(),
//                    event.getImageUrl(),
//                    event.getStartDate(),
//                    event.getEndDate(),
//                    brandsCooperations,
//                    gameInfoDTO,
//                    inventoryDetailDTO
//            );
//            return eventDetailDTO;
//        }
//        catch (Exception e) {
//            throw new Exception(e.getMessage());
//        }
//    }

    public Event createEvent(CreateEventRequest request) throws Exception{
        Event newEvent = new Event();
        newEvent.setEventName(request.getEventName());
        newEvent.setNumberOfVouchers(request.getNumberOfVouchers());
        newEvent.setStartDate(request.getStartDate());
        newEvent.setEndDate(request.getEndDate());
        newEvent.setCreatedBy(request.getCreatedBy());
        newEvent.setDeletedDate(null);
        newEvent.setRemainingVouchers(request.getNumberOfVouchers());
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
            List<FavouriteEvent> favouriteEvents = favouriteEventRepository.findAllByIdEvent(id);
            if (favouriteEvents.toArray().length == 0) {
                return false;
            }
            if (event == null)
                throw new NotFoundException("Event not found");
            Timestamp now = new Timestamp(System.currentTimeMillis());
            favouriteEvents.forEach(fEvent -> {
                fEvent.setDeletedDate(now);
                favouriteEventRepository.save(fEvent);
            });

            event.setDeletedDate(now);
            eventRepository.save(event);
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
            System.out.println(e.getMessage());
            return false;
        }
    }

    public int updateRemainingVouchers(Long eventId) throws Exception {
        try {
            return eventRepository.decreaseEventRemainingVoucherByIdEvent(eventId);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    public List<Event> findEventStartByUserName(String username) throws Exception {
        try {
            LocalDateTime now = LocalDateTime.now();

            LocalDateTime threeDaysBefore = now.plusDays(3);

            Timestamp currentDate = Timestamp.valueOf(now);
            Timestamp startDateCheck = Timestamp.valueOf(threeDaysBefore);
            return eventRepository.findEventStartingInThreeDaysUserName(username,currentDate,startDateCheck);
    }
    
    public Event getEventById(Long id_event) throws Exception {
        try {
            return eventRepository.findByIdEvent(id_event);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public int increaseShareCount(Long id_event) throws Exception {
        try {
            Event event = eventRepository.findByIdEvent(id_event);
            event.setShareCount(event.getShareCount() + 1);
            eventRepository.save(event);
            return 1;
        } catch (Exception e) {
            throw new Exception("Error using service to increase share count");
        }
    }
}
