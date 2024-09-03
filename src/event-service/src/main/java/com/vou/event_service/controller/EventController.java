package com.vou.event_service.controller;

import com.vou.event_service.common.*;
import com.vou.event_service.entity.CreateBrandsCooperationRequest;
import com.vou.event_service.entity.CreateEventRequest;
import com.vou.event_service.dto.*;
import com.vou.event_service.entity.EventDetail;
import com.vou.event_service.entity.EventImageResponse;
import com.vou.event_service.dto.EventDTO;
import com.vou.event_service.dto.GameInfoDTO;
import com.vou.event_service.dto.InventoryDTO;
import com.vou.event_service.dto.QuizDTO;
import com.vou.event_service.model.BrandsCooperation;
import com.vou.event_service.model.Event;
import com.vou.event_service.model.Game;
import com.vou.event_service.repository.EventRepository;
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

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.ExecutionException;
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
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private EventProducer eventProducer;
    @Autowired
    private GameClient gameClient;


    @GetMapping("")
    public ResponseEntity<?> fetchEvent(
            @RequestParam(value="brandId", required = false) Long brandId
    ){
        try {
            List<ListEventDTO> listEventDTOs;
            if (brandId == null){
                listEventDTOs = eventService.getAllEventActive();
                return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("Truy cập danh sách sự kiện thành công", HttpStatus.OK, listEventDTOs));
            }
            listEventDTOs = eventService.getAllEventOfBrand(brandId);
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("Truy cập danh sách sự kiện của nhãn hàng thành công!", HttpStatus.OK, listEventDTOs));

        } catch (Exception e) {
            return ResponseEntity.ok(new InternalServerError());
        }
    }

    @PostMapping("")
    public ResponseEntity<?> createEvent(@RequestBody EventDTO event){
        if(inventoryService.checkVoucherExists(event.getInventoryInfo().getVoucher_code())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BadRequest("Voucher code đã tồn tại!"));
        }
        GameInfoDTO gameInfoDTO = event.getGameInfoDTO();
        InventoryDTO inventoryDTO = event.getInventoryInfo();
        CreateEventRequest request = new CreateEventRequest(
                event.getEventName(),
                event.getNumberOfVouchers(),
                event.getStartDate(),
                event.getEndDate(),
                event.getCreatedBy()
        );
        List<Long> brand_id = event.getBrandId();
        try {
            System.out.println("Trước khi save event");
            Event result = eventService.createEvent(request);
            System.out.println("Save event thành công");
            boolean hasError = false;
            for (Long id : brand_id) {
                CreateBrandsCooperationRequest brandsCooperation = new CreateBrandsCooperationRequest(result.getIdEvent(), id);
                try {
                    brandsCooperationService.createBrandsCooperation(brandsCooperation);
                } catch (Exception e) {
                    hasError = true;
                }
            }
            if (hasError) {
                return ResponseEntity.internalServerError().body(new InternalServerError("Lỗi hệ thống khi cố gắng tạo brand cộng tác"));
            }
            gameInfoDTO.setEventId(result.getIdEvent());
            inventoryDTO.setEvent_id(result.getIdEvent());
            System.out.println("GameInfoIto: " + gameInfoDTO);
            quizService.createQuiz(gameInfoDTO);
            inventoryService.createInventory(inventoryDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(new CreatedResponse("Tạo sự kiện mới thành công", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new InternalServerError("Lỗi hệ thống khi cố gắng tạo sự kiên mới"));
        }
    }

    @GetMapping("/{id_event}")
    public ResponseEntity<ApiResponse> getEventById(@PathVariable("id_event") long id_event) {
        try {

            Event event = eventRepository.findByIdEvent(id_event);

            if (event == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new NotFoundResponse("Không tìm thấy sự kiện"));
            }

            String brandLogo = brandClient.getBrandLogo(event.getCreatedBy()).orElse(null);

            GameInfoDTO gameInfoDTO = quizService.getGameInfo(event.getIdEvent());
            InventoryDetailDTO inventoryDetailDTO = inventoryService.getInventoryInfo(event.getIdEvent());
            List<BrandsCooperation> brandsCooperations = brandsCooperationRepository.findAllByIdEvent(event.getIdEvent());
            Game game = gameClient.findGameByIdGame(gameInfoDTO.getGameId()).orElse(null);
            EventDetail eventDetail = new EventDetail(
                    event.getIdEvent(),
                    event.getEventName(),
                    event.getNumberOfVouchers(),
                    event.getImageUrl(),
                    brandLogo,
                    event.getCreatedBy(),
                    event.getStartDate(),
                    event.getEndDate(),
                    brandsCooperations,
                    gameInfoDTO,
                    inventoryDetailDTO,
                    game.getInstruction()
            );
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("Truy cập chi tiết sự kiện thành công!", HttpStatus.OK, eventDetail));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new InternalServerError("Lỗi hệ thống khi cố gắng truy cập sự kiện!"));
        }
    }
    @PutMapping("/{id_event}")
    public ResponseEntity<?> updateEvent(@PathVariable("id_event") Long id, @RequestBody EventDetailDTO eventDetailDTO) {
        try {
            CreateEventRequest request = new CreateEventRequest(
                    eventDetailDTO.getEventName(),
                    eventDetailDTO.getNumberOfVouchers(),
                    eventDetailDTO.getStartDate(),
                    eventDetailDTO.getEndDate(),
                    eventDetailDTO.getCreatedBy()
            );
            boolean hasError = false;

            List<BrandsCooperation> brandsCooperations = eventDetailDTO.getBrandId();
            brandsCooperationRepository.saveAll(brandsCooperations);
            Event result = eventService.updateEventById(id, request);
            try {
                quizService.updateGameInfo(eventDetailDTO.getGameInfoDTO());
            } catch (Exception e) {
                hasError = true;
            }
            if (hasError) {
                return ResponseEntity.internalServerError().body(new InternalServerError("Lỗi hệ thống khi cố gắng cập nhật lại thông tin trò chơi!"));
            }
            List<ItemDetailDTO> itemDetailDTOS= eventDetailDTO.getInventoryInfo().getItems();
            List<Long> itemIds = itemDetailDTOS.stream()
                    .map(ItemDetailDTO::getIdItem)
                    .collect(Collectors.toList());
            InventoryDTO inventoryDTO =new InventoryDTO(
                    eventDetailDTO.getGameInfoDTO().getGameType(),
                    eventDetailDTO.getInventoryInfo().getVoucher_type(),
                    eventDetailDTO.getInventoryInfo().getVoucher_code(),
                    eventDetailDTO.getInventoryInfo().getVoucher_description(),
                    eventDetailDTO.getInventoryInfo().getVoucher_name(),
                    eventDetailDTO.getInventoryInfo().getVoucher_price(),
                    eventDetailDTO.getInventoryInfo().getAim_coin(),
                    eventDetailDTO.getInventoryInfo().getExpiration_date(),
                    itemIds,
                    eventDetailDTO.getInventoryInfo().getEvent_id()
            );
            try {
                inventoryService.updateInventory(inventoryDTO);
            } catch (Exception e) {
                hasError = true;
            }
            if (hasError) {
                return ResponseEntity.internalServerError().body(new InternalServerError("Lỗi hệ thống khi cố gắng cập nhật lại thông tin voucher!"));
            }
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("Cập nhật sự kiện thành công", HttpStatus.OK, result));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new NotFoundResponse("Không tìm thấy sự kiện"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new InternalServerError());
        }
    }

    @DeleteMapping("/{id_event}")
    public ResponseEntity<?> deleteEvent(@PathVariable("id_event") Long id_event){
        try {
            boolean result = eventService.deleteEventById(id_event);
            if (result) {
                return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("Event deleted", HttpStatus.OK, null));
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
                    .body(new ErrorResponse("Không tìm thấy file hình ảnh", HttpStatus.BAD_REQUEST, null));
        }

        String contentType1 = eventImages.getQrImage().getContentType();
        String contentType2 = eventImages.getBannerFile().getContentType();
        String contentType3 = eventImages.getVoucherImg().getContentType();

        if (!Arrays.asList("image/png", "image/jpeg", "image/jpg").contains(contentType1) &&
                !Arrays.asList("image/png", "image/jpeg", "image/jpg").contains(contentType2) &&
                !Arrays.asList("image/png", "image/jpeg", "image/jpg").contains(contentType3)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("File không hợp lệ", HttpStatus.BAD_REQUEST, "Chỉ cho phép các định dạng png, jpg, jpeg"));
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
