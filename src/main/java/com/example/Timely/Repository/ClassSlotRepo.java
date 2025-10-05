package com.example.Timely.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Timely.Models.ClassSlot;
import com.example.Timely.Models.Student;
import com.example.Timely.Models.dto.timetableDTO.teachertimetableDTO;

@Repository
public interface ClassSlotRepo extends JpaRepository<ClassSlot, Long> {

    List<ClassSlot> findByBatch(String batch);

    List<ClassSlot> findAllByBatch(String batch);

    List<ClassSlot> findAllByInstructor(String teacher);

    Optional<List<ClassSlot>> findAllByBatchAndCourseCodeIn(String batch, List<String> courseCodes);

    @Query(value = """
    SELECT 
        cs.course_code AS courseCode,
        cs.start_time AS startTime,
        cs.day_of_week AS dayOfWeek,
        cs.location AS location,
        cs.slot_type AS type,
        string_agg(cs.batch, ',' Order by CAST(SUBSTRING(cs.batch FROM 2) AS INTEGER)) AS batches
    FROM class_slot cs
    WHERE cs.instructor = :instructor
    GROUP BY cs.start_time, cs.day_of_week, cs.course_code, cs.location, cs.slot_type
    ORDER BY cs.start_time, cs.day_of_week
    """, nativeQuery = true)
    List<teachertimetableDTO> findTeacherTimetable(@Param("instructor") String instructor);


    


}
