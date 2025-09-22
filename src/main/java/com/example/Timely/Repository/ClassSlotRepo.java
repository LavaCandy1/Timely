package com.example.Timely.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Timely.Models.ClassSlot;

@Repository
public interface ClassSlotRepo extends JpaRepository<ClassSlot, Long> {
    
}
