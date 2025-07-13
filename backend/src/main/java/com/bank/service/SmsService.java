package com.bank.service;

import com.bank.model.User;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    @Value("${TWILIO_ACCOUNT_SID}")
    private String accountSid;

    @Value("${TWILIO_AUTH_TOKEN}")
    private String authToken;

    @Value("${TWILIO_PHONE_NUMBER}")
    private String fromNumber;

    public void sendTransactionAlert(User sender, User receiver, double amount) {
        Twilio.init(accountSid, authToken);

        String msgSender = "₹" + amount + " has been debited from your account.";
        String msgReceiver = "₹" + amount + " has been credited to your account.";

        sendSms(sender.getMobile(), msgSender);
        if (receiver != null) {
            sendSms(receiver.getMobile(), msgReceiver);
        }
    }

    private void sendSms(String to, String body) {
        try {
            Message.creator(
                new com.twilio.type.PhoneNumber("+91" + to),
                new com.twilio.type.PhoneNumber(fromNumber),
                body
            ).create();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
