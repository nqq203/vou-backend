package com.vou.event_service.controller;

import com.vou.event_service.common.*;
import com.vou.event_service.entity.CreateBrandsCooperationRequest;
import com.vou.event_service.entity.CreateEventRequest;
import com.vou.event_service.dto.*;
import com.vou.event_service.entity.EventImageResponse;
import com.vou.event_service.dto.EventDTO;
import com.vou.event_service.dto.GameInfoDTO;
import com.vou.event_service.dto.InventoryDTO;
import com.vou.event_service.dto.QuizDTO;
import com.vou.event_service.model.BrandsCooperation;
import com.vou.event_service.model.Event;
import com.vou.event_service.service.BrandsCooperationService;
import com.vou.event_service.service.EventService;
import com.vou.event_service.service.InventoryService;
import com.vou.event_service.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.vou.event_service.repository.BrandsCooperationRepository;
import com.vou.event_service.service.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private BrandsCooperationRepository brandsCooperationRepository;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private StorageService storageService;


    @GetMapping("")
    public ResponseEntity<?> fetchEvent(
            @RequestParam(value="brandId", required = false) Long brandId
    ){
        try {
            List<ListEventDTO> listEventDTOs;
            if (brandId != null){
                listEventDTOs = eventService.getAllEventOfBrand(brandId);
                return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("List of events", HttpStatus.OK, listEventDTOs));
            }

            listEventDTOs = eventService.getAllEventActive();
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("List of events", HttpStatus.OK, listEventDTOs));
        } catch (Exception e) {
            return ResponseEntity.ok(new InternalServerError());
        }
    }

    @PostMapping("")
    public ResponseEntity<?> createEvent(@RequestBody EventDTO event){
        GameInfoDTO gameInfoDTO = event.getGameInfoDTO();
        InventoryDTO inventoryDTO = event.getInventoryInfo();
        CreateEventRequest request = new CreateEventRequest(
                event.getEventName(),
                event.getNumberOfVouchers(),
                event.getStartDate(),
                event.getEndDate()
        );
        List<Long> brand_id = event.getBrandId();
        try {
            Event result = eventService.createEvent(request);
            for(int i = 0;i< brand_id.size();i++){
                CreateBrandsCooperationRequest brandsCooperation = new CreateBrandsCooperationRequest(result.getIdEvent(), (long) i);
                brandsCooperationService.createBrandsCooperation(brandsCooperation);
            }
            gameInfoDTO.setEventId(result.getIdEvent());
            inventoryDTO.setEvent_id(result.getIdEvent());
            quizService.createQuiz(gameInfoDTO);
            inventoryService.createInventory(inventoryDTO);

            return ResponseEntity.status(HttpStatus.CREATED).body(new CreatedResponse(result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new InternalServerError());
        }
    }

    @GetMapping("/{id_event}")
    public ResponseEntity<?> getEventById(@PathVariable("id_event") long id_event){
        try {

            EventDetailDTO event = eventService.getAnEvent(id_event);
            if (event == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new NotFoundResponse());
            }
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("Event details", HttpStatus.OK, event));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new InternalServerError());
        }
    }
    @PutMapping("/{id_event}")
    public ResponseEntity<?> updateEvent(@PathVariable("id_event") Long id, @RequestBody EventDetailDTO eventDetailDTO) {
        try {
            CreateEventRequest request = new CreateEventRequest(
                    eventDetailDTO.getEventName(),
                    eventDetailDTO.getNumberOfVouchers(),
                    eventDetailDTO.getStartDate(),
                    eventDetailDTO.getEndDate()
            );
            Event result = eventService.updateEventById(id, request);
            quizService.updateGameInfo(eventDetailDTO.getGameInfoDTO());
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

    @PutMapping("")
    public ResponseEntity<?> uploadEventImage(
            @RequestParam("id_event") Long id_event,
            @ModelAttribute EventImageDTO eventImages
    ) {
        if (!isImageFile(eventImages.getBannerFile()) ||
                !isImageFile(eventImages.getQrImage()) ||
                !isImageFile(eventImages.getVoucherImg())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Loại tập tin không hợp lệ!", HttpStatus.BAD_REQUEST, "Chỉ file hình ảnh mới được chấp nhận!"));
        }
        Event existEvent;
        try {
            existEvent = eventService.findEventById(id_event);
            System.out.println("Find event: " + existEvent);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new InternalServerError("Lỗi hệ thống"));
        }
        if (existEvent == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new NotFoundResponse("Không tìm thấy event!"));
        }

        try {
            String bannerUrl = storageService.uploadImage(eventImages.getBannerFile());
            Boolean isUploaded = eventService.uploadEventImage(existEvent, bannerUrl);
            InventoryImageUrlDTO inventoryUrls = inventoryService.uploadInventoryImages(id_event, eventImages.getQrImage(), eventImages.getVoucherImg());
            if (inventoryUrls == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new InternalServerError("Lỗi hệ thống: Tải qr và ảnh voucher thất bại!"));
            }
            if (bannerUrl == null || !isUploaded) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new InternalServerError("Lỗi hệ thống: Tải banner thất bại!"));
            }
            return ResponseEntity.ok(new SuccessResponse("Tải ảnh thành công!",
                    HttpStatus.OK,
                    new EventImageResponse(bannerUrl, inventoryUrls.getQrImgUrl(), inventoryUrls.getVoucherImgUrl())));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new InternalServerError("Lỗi hệ thống!"));
        }
    }

    private boolean isImageFile(MultipartFile file) {
        return file != null && file.getContentType() != null && file.getContentType().startsWith("image/");
    }
}
