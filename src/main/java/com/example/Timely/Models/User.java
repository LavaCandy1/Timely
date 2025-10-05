package com.example.Timely.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String name;

    @Column(unique = true)
    String email;
    String password;
    
    @Enumerated(EnumType.STRING)
    RoleEnum role; // e.g., "student", "teacher", "admin"

    public enum RoleEnum {
        STUDENT("Student"),
        TEACHER("Teacher"),
        ADMIN("Admin");

        private final String displayName;
        RoleEnum(String displayName) {
            this.displayName = displayName;
        }
        public String getDisplayName() {
            return displayName;
        }
    }
    
}
