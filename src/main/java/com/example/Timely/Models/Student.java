package com.example.Timely.Models;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Student {

    @Id
    String enrollmentNumber; // Like E22CSEU1201 , E22CSEU0523 etc.
    String name;
    String email; // like same as enrollment number but with @bennett.edu.in
    String batch; // B1 to B52

    // course list student is enrolled in
    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private List<String> courseList = new ArrayList<>();

}
