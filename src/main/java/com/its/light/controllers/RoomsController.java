package com.its.light.controllers;

import com.its.light.DTO.RoomDTO;
import com.its.light.models.Room;
import com.its.light.service.RoomsService;
import com.its.light.tools.ResponseCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class RoomsController {

    private final RoomsService roomsService;

    @Autowired
    RoomsController (RoomsService service) {
        roomsService = service;
    }

    @GetMapping("/api/rooms")
    public List<RoomDTO> getRooms() {
        return roomsService.getAll();
    }

    @GetMapping("/api/rooms/{id}")
    public Room getRoomById(
            @PathVariable("id") long roomId,
            HttpServletRequest request
    ) {
        return roomsService.getById(roomId, request.getRemoteAddr());
    }

    @PostMapping("/api/rooms/new")
    public long createRoom(
            @RequestBody Room room,
            HttpServletRequest request
    ) {
        try {
            room.setIpOfCreator(request.getRemoteAddr());
            return roomsService.saveNew(room);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseCodes.DATABASE_ERROR;
        }
    }

    @GetMapping("/api/rooms/delete")
    public byte deleteRoomById(
            @RequestParam(name = "id", required = false, defaultValue = "-1") long roomId,
            HttpServletRequest request
    ) {
        return roomsService.delete(roomId, request.getRemoteAddr());
    }
}
