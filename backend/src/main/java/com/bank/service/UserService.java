package com.bank.service;

import com.bank.model.User;
import com.bank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SmsService smsService;

    @Autowired
    private EmailService emailService;

    private Map<String, String> otpStore = new ConcurrentHashMap<>();

    public String register(User user) {
        User existing = userRepository.findByMobile(user.getMobile());
        if (existing != null) {
            return "User already registered with this mobile number.";
        }

        user.setVerified(false);
        user.setBalance(200000.0); // Initial balance
        userRepository.save(user);

        String otp = generateOtp();
        otpStore.put(user.getMobile(), otp);

        smsService.sendSms(user.getMobile(), "Your OTP for Online Banking registration is: " + otp);
        emailService.sendEmail(user.getEmail(), "OTP Verification", "Your OTP is: " + otp);

        return "User registered. OTP sent to mobile and email.";
    }

    public String verifyOtp(String mobile, String otp) {
        String storedOtp = otpStore.get(mobile);
        if (storedOtp != null && storedOtp.equals(otp)) {
            User user = userRepository.findByMobile(mobile);
            if (user != null) {
                user.setVerified(true);
                userRepository.save(user);
                otpStore.remove(mobile);
                return "OTP verification success. You can now login.";
            }
        }
        return "Invalid OTP or mobile number.";
    }

    public String resendOtp(String mobile) {
        User user = userRepository.findByMobile(mobile);
        if (user == null) return "Mobile number not registered.";

        String otp = generateOtp();
        otpStore.put(mobile, otp);

        smsService.sendSms(user.getMobile(), "Your OTP (resend) is: " + otp);
        emailService.sendEmail(user.getEmail(), "Resend OTP", "Your new OTP is: " + otp);

        return "OTP resent successfully.";
    }

    public User login(String mobile, String password) {
        User user = userRepository.findByMobile(mobile);
        if (user != null && user.getPassword().equals(password) && user.isVerified()) {
            return user;
        }
        return null;
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    private String generateOtp() {
        Random rand = new Random();
        int otp = 100000 + rand.nextInt(900000); // 6-digit OTP
        return String.valueOf(otp);
    }
}
