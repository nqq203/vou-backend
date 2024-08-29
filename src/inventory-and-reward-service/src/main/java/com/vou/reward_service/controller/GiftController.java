//package com.vou.reward_service.controller;
//
//import com.vou.reward_service.model.ItemRepo;
//import com.vou.reward_service.service.GiftService;
//import jdk.jfr.Event;
//import org.apache.coyote.Response;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/v1/gifts")
//@CrossOrigin
//public class GiftController {
//    private final GiftService giftService;
//    public GiftController(GiftService giftService) {
//        this.giftService = giftService;
//    }
//
//    @PostMapping("")
//    public ResponseEntity<?> sendGift(
//            @RequestParam("id_item") Long id_item,
//            @RequestParam("id_user") Long id_user,
//            @RequestParam("username") String username,
//            @RequestParam("email") String email)
//    {
//
//    }
//}
