package com.example.Timely.Service.Security;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.example.Timely.Models.User;
import com.example.Timely.Repository.UserRepo;
import com.example.Timely.Models.UserPrincipal;


@Service
public class MyUserDetailsService implements UserDetailsService {
    
    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{

        User user = userRepo.findByEmail(email);

        if(user == null) {
            System.out.println("User not Found");
            throw new UsernameNotFoundException("User not found with email: " + email);
        } else {
            return new UserPrincipal(user);
        }
    }
}
