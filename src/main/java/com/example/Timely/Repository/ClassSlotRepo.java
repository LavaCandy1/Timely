package com.example.Timely.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Timely.Models.ClassSlot;
import com.example.Timely.Models.Student;

@Repository
public interface ClassSlotRepo extends JpaRepository<ClassSlot, Long> {

    List<ClassSlot> findByBatch(String batch);

    List<ClassSlot> findAllByBatch(String batch);

    List<ClassSlot> findAllByInstructor(String teacher);

    Optional<List<ClassSlot>> findAllByBatchAndCourseCodeIn(String batch, List<String> courseCodes);

}
