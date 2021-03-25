package com.its.light.DTO;

import lombok.Data;

@Data
public class RoomDTO {
    private long id;
    private String name;
    private String country;
    private String creationDate;
    private String ipOfCreator;
}
