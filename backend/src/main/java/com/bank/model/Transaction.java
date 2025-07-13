package com.bank.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String senderMobile;
    private String recipient;
    private double amount;
    private String type; // CREDIT or DEBIT
    private LocalDateTime timestamp;

    public Transaction() {}

    public Transaction(String senderMobile, String recipient, double amount, String type) {
        this.senderMobile = senderMobile;
        this.recipient = recipient;
        this.amount = amount;
        this.type = type;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }

    public String getSenderMobile() { return senderMobile; }
    public void setSenderMobile(String senderMobile) { this.senderMobile = senderMobile; }

    public String getRecipient() { return recipient; }
    public void setRecipient(String recipient) { this.recipient = recipient; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
