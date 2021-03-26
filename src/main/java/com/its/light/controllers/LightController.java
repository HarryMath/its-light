package com.its.light.controllers;

import com.its.light.DTO.Light;
import com.its.light.service.RoomsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.util.Objects;

@Controller
public class LightController {

    private final SimpMessagingTemplate messagingTemplate;
    private final RoomsService roomsService;

    @Autowired
    LightController(SimpMessagingTemplate messagingTemplate, RoomsService service) {
        this.messagingTemplate = messagingTemplate;
        this.roomsService = service;
    }

    @SubscribeMapping("/rooms/{roomId}/light")
    public Light handleConnection(
            @DestinationVariable long roomId,
            SimpMessageHeaderAccessor headers
    ) {
        String ipAddress = (String) Objects.requireNonNull(headers.getSessionAttributes()).get("IP");
        System.out.println("new user required to connect room" + roomId);
        Light light = new Light();
        if( roomsService.connectUser(roomId, ipAddress) ) {
            light.setOn( roomsService.getRoomLight(roomId) );
        }
        return light;
    }

    @MessageMapping("/switch")
    public void switchLight(Light light, SimpMessageHeaderAccessor headers) {
        System.out.println("message received");
        String ipAddress = (String) Objects.requireNonNull(headers.getSessionAttributes()).get("IP");
        boolean validMove = roomsService.switchLight(light, ipAddress);
        if(validMove) {
            messagingTemplate.convertAndSend("/rooms/" + light.getRoomId() + "/light", light);
        }
    }
}
