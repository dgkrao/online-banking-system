package com.bank.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/otp")
public class OtpController {

    private Map<String, String> otpMap = new HashMap<>();

    @GetMapping("/send")
    public String sendOtp(@RequestParam String mobile) {
        String otp = String.valueOf(new Random().nextInt(9000) + 1000);
        otpMap.put(mobile, otp);
        System.out.println("Sending OTP " + otp + " to " + mobile); // Replace with SMS send logic
        return "OTP sent";
    }

    @GetMapping("/verify")
    public Map<String, String> verifyOtp(@RequestParam String mobile, @RequestParam String otp) {
        Map<String, String> response = new HashMap<>();
        if (otp.equals(otpMap.get(mobile))) {
            otpMap.remove(mobile);
            response.put("message", "OTP verified");
        } else {
            response.put("message", "Invalid OTP");
        }
        return response;
    }
}
