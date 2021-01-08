package com.anabiullin.ppmtool3.services;

import com.anabiullin.ppmtool3.domain.User;
import com.anabiullin.ppmtool3.exceptions.UsernameAlreadyExistsException;
import com.anabiullin.ppmtool3.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User saveUser (User newUser){
        try{
            newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));

            newUser.setUsername(newUser.getUsername());

            newUser.setConfirmPassword("Not showing you confirmPassword");


            return userRepository.save(newUser);
        }catch (Exception e){
    throw new UsernameAlreadyExistsException("Username '"+newUser.getUsername()+"' already exists");
        }


    }
}
