package com.its.light.models;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

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
    private Date creationDate;
    private boolean lightOn = false;
}
