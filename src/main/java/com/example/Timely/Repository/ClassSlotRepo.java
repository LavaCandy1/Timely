package com.example.Timely.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Timely.Models.ClassSlot;

@Repository
public interface ClassSlotRepo extends JpaRepository<ClassSlot, Long> {

    List<ClassSlot> findByBatch(String batch);

    List<ClassSlot> findAllByBatch(String batch);

    List<ClassSlot> findAllByInstructor(String teacher);
    
}
