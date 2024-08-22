package com.vou.event_service.controller;

import com.vou.event_service.common.*;
import com.vou.event_service.dto.EventDTO;
import com.vou.event_service.dto.GameInfoDTO;
import com.vou.event_service.dto.QuizDTO;
import com.vou.event_service.entity.CreateBrandsCooperationRequest;
import com.vou.event_service.entity.CreateEventRequest;
import com.vou.event_service.model.BrandsCooperation;
import com.vou.event_service.model.Event;
import com.vou.event_service.service.BrandsCooperationService;
import com.vou.event_service.service.EventService;
import com.vou.event_service.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
@CrossOrigin
public class EventController {

    @Autowired
    private EventService eventService;
    @Autowired
    private QuizService quizService;
    @Autowired
    private BrandsCooperationService brandsCooperationService;

    @GetMapping("/")
    public ResponseEntity<?> fetchEvent(){
        try {
            List<Event> allEvents = eventService.getAllEvents();
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("List of events", HttpStatus.OK, allEvents));
        } catch (Exception e) {
            return ResponseEntity.ok(new InternalServerError());
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> createEvent(@RequestBody EventDTO event){
        GameInfoDTO gameInfoDTO = event.getGameInfoDTO();

        List<QuizDTO> quizzes = gameInfoDTO.getQuiz();
        CreateEventRequest request = new CreateEventRequest(
                event.getEventName(),
                event.getImageUrl(),
                event.getNumberOfVouchers(),
                event.getStartDate(),
                event.getEndDate()
        );
        long brand_id = event.getBrandId();
        try {
            Event result = eventService.createEvent(request);
            CreateBrandsCooperationRequest brandsCooperation = new CreateBrandsCooperationRequest(result.getIdEvent(), brand_id);
            brandsCooperationService.createBrandsCooperation(brandsCooperation);
            quizService.createQuiz(gameInfoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(new CreatedResponse(result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new InternalServerError());
        }
    }

    @GetMapping("/{id_event}")
    public ResponseEntity<?> getEventById(@PathVariable("id_event") long id_event){
        try {
            Event event = eventService.findEventById(id_event);
            if (event == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new NotFoundResponse());
            }
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("Event details", HttpStatus.OK, event));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new InternalServerError());
        }
    }
    @PutMapping("/{id_event}")
    public ResponseEntity<?> updateEvent(@PathVariable("id_event") Long id, @RequestBody CreateEventRequest request) {
        try {
            Event result = eventService.updateEventById(id, request);
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("Event updated", HttpStatus.OK, result));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new NotFoundResponse("Event not found"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new InternalServerError());
        }
    }

    @DeleteMapping("/{id_event}")
    public ResponseEntity<?> deleteEvent(@PathVariable("id_event") Long id_event){
        try {
            boolean result = eventService.deleteEventById(id_event);
            if (result) {
                return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("Event deleted", HttpStatus.OK));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new InternalServerError());
            }
        }
        catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new NotFoundResponse("Event not found"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new InternalServerError());
        }
    }
//
//    @GetMapping("brands-cooperation")
//    public ResponseEntity<?> fetchBrandCoperation(){
//        List<BrandsCooperation> data = brandsCoperationRepository.findAll();
//        return ResponseEntity.ok(new SuccessResponse("Create aloo successfully", HttpStatus.OK,data));
//    }
//    @PostMapping("brands-cooperation")
//    public  ResponseEntity<?> createBrandCooperation(@RequestBody BrandsCooperation brandsCoperation){
//        brandsCoperationRepository.save(brandsCoperation);
//        return ResponseEntity.ok(new SuccessResponse("Create BrandsCooperation successfully", HttpStatus.OK,"" ));
//    }
//
//    @GetMapping("/search")
//    public ResponseEntity<?> searchEventsByName(@RequestParam String name) {
//        List<Event> events = eventReposotory.findByEventNameContainingIgnoreCase(name);
//
//        if (events.isEmpty()) {
//            return ResponseEntity.ok(new SuccessResponse("Empty", HttpStatus.NO_CONTENT,"" ));
//        }
//
//        return ResponseEntity.ok(new SuccessResponse("Fetch successfully", HttpStatus.OK,events ));
//    }

}
