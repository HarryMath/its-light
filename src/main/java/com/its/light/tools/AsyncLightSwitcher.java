package com.its.light.tools;

import com.its.light.DTO.Light;
import com.its.light.repository.RoomsRepository;

public class AsyncLightSwitcher extends Thread {

    private final RoomsRepository roomsRepository;
    private final Light light;

    public AsyncLightSwitcher(RoomsRepository repository, Light light) {
        this.roomsRepository = repository;
        this.light = light;
    }

    public void run() {
        roomsRepository.switchLight(light.getRoomId(), light.isOn());
    }
}
