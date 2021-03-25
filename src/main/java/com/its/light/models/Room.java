package com.its.light.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue
    private long id;

    @Column(unique = true)
    private String name;
    private String country;
    private String creationDate;
    private String ipOfCreator;
    private boolean lightOn = false;
}
