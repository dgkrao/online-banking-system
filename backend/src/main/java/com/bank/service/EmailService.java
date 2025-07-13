package com.bank.service;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bank.model.User;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Value("${EMAIL_USERNAME}")
    private String username;

    @Value("${EMAIL_PASSWORD}")
    private String password;

    public void sendTransactionAlert(User sender, User receiver, double amount) {
        String subject = "BlueBank Transaction Notification";
        String senderMsg = "₹" + amount + " has been debited from your account.";
        String receiverMsg = "₹" + amount + " has been credited to your account.";

        sendEmail(sender.getEmail(), subject, senderMsg);
        if (receiver != null) {
            sendEmail(receiver.getEmail(), subject, receiverMsg);
        }
    }

    private void sendEmail(String to, String subject, String text) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props,
            new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username, "BlueBank"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(text);
            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
