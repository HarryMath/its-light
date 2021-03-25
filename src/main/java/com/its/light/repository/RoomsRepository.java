package com.its.light.repository;

import com.its.light.models.Room;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomsRepository extends JpaRepository<Room, Long> {

    @Query(value = "UPDATE rooms SET lightOn = ?2 WHERE id = ?1", nativeQuery = true)
    void switchLight(long roomId, boolean lightOn);

    @Query(value = "SELECT * FROM rooms WHERE name = ?1", nativeQuery = true)
    Optional<User> findByName(String name);
}
