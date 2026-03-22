package com.example.Timely.Repository;

import com.example.Timely.Models.Locations;

import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LocationRepo extends JpaRepository<Locations, Long>{

    @Query("SELECT l FROM Locations l WHERE l.isFunctional = true AND l.roomName NOT IN (" +
       "SELECT cs.location FROM ClassSlot cs WHERE cs.dayOfWeek = :day " +
       "AND cs.startTime = :start)")
    List<Locations> findAvailableLocations(
        @Param("day") String day, 
        @Param("start") LocalTime start
    );
}
