package com.github.cawtoz.parking.service;

import com.github.cawtoz.parking.model.security.Role;
import com.github.cawtoz.parking.model.security.User;
import com.github.cawtoz.parking.repository.ParkingRepository;
import com.github.cawtoz.parking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ParkingRepository parkingRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User register(User user) {
        parkingRepository.save(user.getParking());
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}
