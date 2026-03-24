package com.example.Timely.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Timely.Models.Student;

@Repository
public interface StudentRepo extends JpaRepository<Student, Long> {

    Optional<Student> findByEnrollmentNumber(String enrollmentNumber);

    @Query("SELECT s.email FROM Student s WHERE s.batch = :batch")
    Optional<List<String>> findEmailsByBatch(@Param("batch") String batch);

}
