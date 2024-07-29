package com.VOU.event_service.controller;

import com.VOU.event_service.common.NotFoundResponse;
import com.VOU.event_service.common.SuccessResponse;
import com.VOU.event_service.model.BrandsCoperation;
import com.VOU.event_service.model.Event;
import com.VOU.event_service.repository.BrandsCoperationRepository;
import com.VOU.event_service.repository.EventReposotory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/events")
public class EventController {

    @Autowired
    private EventReposotory eventReposotory;

    @Autowired
    private BrandsCoperationRepository brandsCoperationRepository;

    @GetMapping("")
    public ResponseEntity<?> fetchEvent(){
        List<Event> data = eventReposotory.findAll();
        return ResponseEntity.ok(new SuccessResponse("Create event successfully", HttpStatus.OK,data));
    }

    @PostMapping("")
    public  ResponseEntity<?> createEvent(@RequestBody Event event){
        eventReposotory.save(event);
        return ResponseEntity.ok(new SuccessResponse("Create event successfully", HttpStatus.OK,"" ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> fentchById(@PathVariable long id){
        Optional<Event> data = eventReposotory.findById(id);
        if (data.isPresent()){
            return ResponseEntity.ok(new SuccessResponse("Get a event by ID", HttpStatus.OK,data));
        }
        else{
            return ResponseEntity.ok(new NotFoundResponse());
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable Long id, @RequestBody Event eventDetails) {
        Optional<Event> optionalEvent = eventReposotory.findById(id);

        if (optionalEvent.isPresent()) {
            Event event = optionalEvent.get();
            event.setEventName(eventDetails.getEventName());
            event.setImageUrl(eventDetails.getImageUrl());
            event.setNumberOfVouchers(eventDetails.getNumberOfVouchers());
            event.setStartDate(eventDetails.getStartDate());
            event.setEndDate(eventDetails.getEndDate());

            Event updatedEvent = eventReposotory.save(event);
            return ResponseEntity.ok(new SuccessResponse("Event is updated", HttpStatus.OK,updatedEvent));
        }
        else{
            return ResponseEntity.ok(new NotFoundResponse());
        }
    }
    @DeleteMapping("/id")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id){
        Optional<Event> optionalEvent = eventReposotory.findById(id);
        if (optionalEvent.isPresent()) {
            eventReposotory.deleteById(id);
            return ResponseEntity.ok(new SuccessResponse("Event is deleted", HttpStatus.OK,""));
        }
        else{
            return ResponseEntity.ok(new NotFoundResponse());
        }
    }

    @GetMapping("brands-cooperation")
    public ResponseEntity<?> fetchBrandCoperation(){
        List<BrandsCoperation> data = brandsCoperationRepository.findAll();
        return ResponseEntity.ok(new SuccessResponse("Create aloo successfully", HttpStatus.OK,data));
    }
    @PostMapping("brands-cooperation")
    public  ResponseEntity<?> createBrandCooperation(@RequestBody BrandsCoperation brandsCoperation){
        brandsCoperationRepository.save(brandsCoperation);
        return ResponseEntity.ok(new SuccessResponse("Create BrandsCoperation successfully", HttpStatus.OK,"" ));
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchEventsByName(@RequestParam String name) {
        List<Event> events = eventReposotory.findByEventNameContainingIgnoreCase(name);

        if (events.isEmpty()) {
            return ResponseEntity.ok(new SuccessResponse("Empty", HttpStatus.NO_CONTENT,"" ));
        }

        return ResponseEntity.ok(new SuccessResponse("Fetch successfully", HttpStatus.OK,events ));
    }

}
