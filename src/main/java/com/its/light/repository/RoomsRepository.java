package com.its.light.repository;

import com.its.light.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface RoomsRepository extends JpaRepository<Room, Long> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE rooms SET light_on = ?2 WHERE id = ?1", nativeQuery = true)
    void switchLight(long roomId, boolean lightOn);

    @Query(value = "SELECT * FROM rooms WHERE name = ?1", nativeQuery = true)
    Optional<Room> findByName(String name);
}
