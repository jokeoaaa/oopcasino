package org.example.groupassignment.services;

import org.example.groupassignment.models.User;
import org.example.groupassignment.repositories.UserRepo;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setBalance(1000.0);
        return userRepository.save(user);
    }

    // other methods
}
