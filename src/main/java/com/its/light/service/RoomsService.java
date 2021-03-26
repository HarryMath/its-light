package com.its.light.service;

import com.its.light.DTO.Light;
import com.its.light.models.Room;
import com.its.light.DTO.RoomDTO;
import com.its.light.repository.RoomsRepository;
import com.its.light.tools.AsyncLightSwitcher;
import com.its.light.tools.CountryDefiner;
import com.its.light.tools.ResponseCodes;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class RoomsService {

    private static final Map<Long, RoomWithUsers> activeRooms = new HashMap<>();

    private final RoomsRepository roomsRepository;
    private final CountryDefiner countryDefiner;
    private final ModelMapper modelMapper;

    @Autowired
    RoomsService(RoomsRepository repository, CountryDefiner countryDefiner) {
        roomsRepository = repository;
        this.countryDefiner = countryDefiner;
        modelMapper = new ModelMapper();
    }

    public boolean switchLight(Light light, String ipAddress){
        if(activeRooms.containsKey(light.getRoomId())) {
            RoomWithUsers room = activeRooms.get(light.getRoomId());
            if( room.validIpAddresses.contains(ipAddress) ) {
                room.room.setLightOn( light.isOn() );
                new AsyncLightSwitcher(roomsRepository, light).start();
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean connectUser(long roomId, String ipAddress) {
        if( !activeRooms.containsKey(roomId) ) {
            Optional<Room> newRoom = roomsRepository.findById(roomId);
            newRoom.ifPresent(room ->
                    activeRooms.put(room.getId(), new RoomWithUsers(room))
            );
        }
        if (activeRooms.containsKey(roomId)) {
            RoomWithUsers room = activeRooms.get(roomId);
            String roomCountry = room.room.getCountry();
            String userCountry = countryDefiner.getCountry(ipAddress);
            if (roomCountry.equalsIgnoreCase(userCountry)) {
                room.validIpAddresses.add(ipAddress);
                return true;
            }
        }
        return false;
    }

    public boolean getRoomLight(long roomId) {
        return activeRooms.containsKey(roomId) && activeRooms.get(roomId).room.isLightOn();
    }

    public List<RoomDTO> getAll() {
        return convertToDTO(roomsRepository.findAll());
    }

    public Room getById(long id, String ipAddress) {
        String country = countryDefiner.getCountry(ipAddress);
        Room room = roomsRepository.findById(id).orElse(new Room());
        return room.getCountry().equalsIgnoreCase(country) ?
                    room : null;
    }

    public long saveNew(Room room) {
        try {
            if( !countryDefiner.isCountryExists(room.getCountry()) ) {
                return ResponseCodes.COUNTRY_NOT_EXISTS;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return ResponseCodes.COUNTRY_NOT_EXISTS;
        }
        room.setLightOn(false);
        return roomsRepository.findByName(room.getName()).isPresent() ?
                ResponseCodes.NAME_TAKEN :
                roomsRepository.save(room).getId();
    }

    public byte delete(long id, String ipAddress) {
        Room room = roomsRepository.findById(id).orElse(new Room());
        if(room.getIpOfCreator().equalsIgnoreCase(ipAddress)) {
            roomsRepository.deleteById(id);
            return ResponseCodes.SUCCESS;
        }
        return ResponseCodes.NO_ACCESS;
    }

    private RoomDTO convertToDTO(Room room) {
        return modelMapper.map(room, RoomDTO.class);
    }

    private List<RoomDTO> convertToDTO(List<Room> rooms) {
        List<RoomDTO> newRooms = new ArrayList<>();
        rooms.forEach(room -> {
            newRooms.add(convertToDTO(room));
        });
        return newRooms;
    }

    private static class RoomWithUsers {
        public Room room;
        public Set<String> validIpAddresses;

        RoomWithUsers(Room room) {
            this.room = room;
            validIpAddresses = new HashSet<>();
        }
    }
}
