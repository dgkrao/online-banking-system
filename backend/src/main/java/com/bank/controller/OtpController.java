package com.bank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bank.service.UserService;

@RestController
@RequestMapping("/api/otp")
@CrossOrigin
public class OtpController {

    @Autowired
    private UserService userService;

    @GetMapping("/verify")
    public String verifyOtp(@RequestParam String mobile, @RequestParam String otp) {
        return userService.verifyOtp(mobile, otp);
    }

    @GetMapping("/resend")
    public String resendOtp(@RequestParam String mobile) {
        return userService.resendOtp(mobile);
    }
}
