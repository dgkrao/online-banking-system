package com.bank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.model.User;
import com.bank.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User registerUser(User user) {
        return userRepository.save(user);
    }

    public User login(String identifier, String password) {
        User user = identifier.contains("@") ?
                userRepository.findByEmail(identifier) :
                userRepository.findByMobile(identifier);

        return (user != null && user.getPassword().equals(password)) ? user : null;
    }

    public User getUserByMobile(String mobile) {
        return userRepository.findByMobile(mobile);
    }

    public double getBalance(String mobile) {
        User user = userRepository.findByMobile(mobile);
        return (user != null) ? user.getBalance() : 0.0;
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }
}
