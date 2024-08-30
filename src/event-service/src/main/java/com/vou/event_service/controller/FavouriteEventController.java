package com.vou.event_service.controller;

import com.vou.event_service.common.InternalServerError;
import com.vou.event_service.common.NotFoundResponse;
import com.vou.event_service.common.SuccessResponse;
import com.vou.event_service.dto.FavouriteEventDetailDTO;
import com.vou.event_service.model.Event;
import com.vou.event_service.model.FavouriteEvent;
import com.vou.event_service.service.BrandClient;
import com.vou.event_service.service.EventService;
import com.vou.event_service.service.FavouriteEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/favourite-events")
@CrossOrigin
public class FavouriteEventController {
    @Autowired
    private FavouriteEventService favouriteEventService;
    @Autowired
    private EventService eventService;


    @PostMapping("/{id_event}/users/{id_player}")
    public ResponseEntity<?> addOrRemoveFavouriteEvent(@PathVariable Long id_event, @PathVariable Long id_player) {
        try {
            Event existEvent = eventService.findEventById(id_event);
            if (existEvent == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new NotFoundResponse("Sự kiện không tồn tại"));
            }
            FavouriteEvent existFavouriteEvent = favouriteEventService.findByIdEvent(id_event);
            if  (existFavouriteEvent != null) {
                return ResponseEntity.ok(new SuccessResponse("Xóa khỏi danh sách sự kiện yêu thích thành công!", HttpStatus.OK, eventService));
            }
            FavouriteEvent favouriteEvent = favouriteEventService.addFavouriteEvent(id_event, id_player, existEvent);
            if (favouriteEvent == null) {
                return ResponseEntity.internalServerError().body(new InternalServerError("Cố lỗi xảy ra khi cố gắng thêm sự kiện yêu thích, hãy thử lại sau!"));
            }
            return ResponseEntity.ok(new SuccessResponse("Thêm sự kiện yêu thích thành công!", HttpStatus.OK, favouriteEvent));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new InternalServerError("Có lỗi xảy ra khi cố gắng thêm sự kiện yêu thích, hãy thử lại sau!"));
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getFavouriteEventList(@RequestParam("id_player") Long id_player) {
        try {
            // Event DTO phai co event, favourite event va brand image url
            List<FavouriteEvent> favouriteEventList = favouriteEventService.findByIdPlayer(id_player);
            if (favouriteEventList == null) {
                return ResponseEntity.internalServerError().body(new InternalServerError("Có lỗi xảy ra khi cố gắng truy cập danh sách sự kiện yêu thích, hãy thử lại sau!"));
            }
            if (favouriteEventList.toArray().length == 0) {
                return ResponseEntity.ok(new SuccessResponse("Bạn chưa thêm sự kiện yêu thích nào.", HttpStatus.OK, favouriteEventList));
            }
            try {
                List<FavouriteEventDetailDTO> fEventDetail = favouriteEventService.getAllFavouriteEvent(favouriteEventList);
                return ResponseEntity.ok(new SuccessResponse("Truy cập danh sách sự kiện yêu thích thành công!", HttpStatus.OK, fEventDetail));
            } catch (Exception e) {
                return ResponseEntity.internalServerError().body(new InternalServerError("Có lỗi xảy ra khi cố gắng truy cập danh sách sự kiện yêu thích, hãy thử lại sau!"));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new InternalServerError("Có lỗi xảy ra khi cố gắng truy cập danh sách sự kiện yêu thích, hãy thử lại sau!"));
        }
    }
}
