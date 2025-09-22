package com.example.Timely.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Timely.Models.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    
} 
