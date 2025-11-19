package com.example.Timely.Repository;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.Timely.Models.ClassSlot;
import com.example.Timely.Models.dto.timetableDTO.AdminTimetableDTO;
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
        cs.slot_type AS slotType,
        string_agg(cs.batch, ',' Order by CAST(SUBSTRING(cs.batch FROM 2) AS INTEGER)) AS batches,
        cs.cancelled_date AS cancelledDate
    FROM class_slot cs
    WHERE cs.instructor = :instructor
    GROUP BY cs.start_time, cs.day_of_week, cs.course_code, cs.location, cs.slot_type, cs.cancelled_date
    ORDER BY cs.start_time, cs.day_of_week
    """, nativeQuery = true)
    List<teachertimetableDTO> findTeacherTimetable(@Param("instructor") String instructor);

    @Query(value = """
    SELECT 
        cs.course_code AS courseCode,
        cs.start_time AS startTime,
        cs.day_of_week AS dayOfWeek,
        cs.location AS location,
        cs.slot_type AS slotType,
        string_agg(cs.batch, ',' Order by CAST(SUBSTRING(cs.batch FROM 2) AS INTEGER)) AS batches,
        cs.cancelled_date AS cancelledDate
    FROM class_slot cs
    WHERE cs.instructor = :instructor
    GROUP BY cs.start_time, cs.day_of_week, cs.course_code, cs.location, cs.slot_type, cs.cancelled_date
    ORDER BY cs.start_time, cs.day_of_week
    """, nativeQuery = true)
    List<AdminTimetableDTO> findTeacherTimetableForAdmin(@Param("instructor") String instructor);

    @Transactional
    @Modifying(clearAutomatically = true) 
    @Query("UPDATE ClassSlot cs SET cs.cancelledDate = :cancelledDate WHERE " +
           "cs.batch IN :batches AND " +
           "cs.courseCode = :courseCode AND " +
           "cs.dayOfWeek = :dayOfWeek AND " +
           "cs.startTime = :startTime AND " +
           "cs.location = :location")
    int updateCancellationDateForSlots(
        @Param("cancelledDate") Date cancelledDate,
        @Param("batches") List<String> batches,
        @Param("courseCode") String courseCode,
        @Param("dayOfWeek") String dayOfWeek,
        @Param("startTime") Time startTime,
        @Param("location") String location
    );
    


}
