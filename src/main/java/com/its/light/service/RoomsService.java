package com.its.light.service;

import com.its.light.DTO.RoomDTO;
import com.its.light.models.Room;
import com.its.light.repository.RoomsRepository;
import com.its.light.tools.CountryDefiner;
import com.its.light.tools.ResponseCodes;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoomsService {

    private final RoomsRepository roomsRepository;
    private final CountryDefiner countryDefiner;
    private final ModelMapper modelMapper;

    @Autowired
    RoomsService(RoomsRepository repository, CountryDefiner countryDefiner) {
        roomsRepository = repository;
        this.countryDefiner = countryDefiner;
        modelMapper = new ModelMapper();
    }

    public List<RoomDTO> getAll() {
        return convertToDTO(roomsRepository.findAll());
    }

    public Room getById(long id, String ipAddress) {
        try {
            String country = countryDefiner.getCountry(ipAddress);
            Room room = roomsRepository.findById(id).orElse(new Room());
            return room.getCountry().equalsIgnoreCase(country) ?
                    room : null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public long saveNew(Room room) {
        room.setLightOn(false);
        return roomsRepository.findByName(room.getName()).isPresent() ?
                ResponseCodes.NAME_TAKEN :
                roomsRepository.save(room).getId();
    }

    public byte update(Room room) {
        roomsRepository.save(room);
        return ResponseCodes.SUCCESS;
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

}
