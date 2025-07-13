package com.bank.service;

import com.bank.model.Transaction;
import com.bank.model.User;
import com.bank.repository.TransactionRepository;
import com.bank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private SmsService smsService;

    @Autowired
    private EmailService emailService;

    public String transferMoney(TransactionRequest request) {
        User sender = userRepository.findById(request.getSenderId()).orElse(null);
        if (sender == null || !sender.isVerified()) {
            return "Sender not found or not verified.";
        }

        if (request.getAmount() <= 0 || sender.getBalance() < request.getAmount()) {
            return "Insufficient balance or invalid amount.";
        }

        User receiver = null;
        if (request.getToMobile() != null && !request.getToMobile().isEmpty()) {
            receiver = userRepository.findByMobile(request.getToMobile());
        } else if (request.getToAccount() != null && !request.getToAccount().isEmpty()) {
            receiver = userRepository.findByEmail(request.getToAccount()); // assuming account = email
        }

        if (receiver == null || !receiver.isVerified()) {
            return "Receiver not found or not verified.";
        }

        // Perform balance updates
        sender.setBalance(sender.getBalance() - request.getAmount());
        receiver.setBalance(receiver.getBalance() + request.getAmount());

        userRepository.save(sender);
        userRepository.save(receiver);

        // Save debit transaction for sender
        Transaction debitTxn = new Transaction(
            sender.getId(),
            request.getToMobile(),
            request.getToAccount(),
            request.getAmount(),
            "Debit"
        );
        transactionRepository.save(debitTxn);

        // Save credit transaction for receiver
        Transaction creditTxn = new Transaction(
            receiver.getId(),
            sender.getMobile(),
            null,
            request.getAmount(),
            "Credit"
        );
        transactionRepository.save(creditTxn);

        // Notify both sender and receiver
        String msg = "₹" + request.getAmount() + " transferred to " +
                (receiver.getName() + " (" + receiver.getMobile() + ")");

        smsService.sendSms(sender.getMobile(), "Transaction Alert: " + msg);
        smsService.sendSms(receiver.getMobile(), "You've received ₹" + request.getAmount() + " from " + sender.getName());

        emailService.sendEmail(sender.getEmail(), "Money Transferred", msg);
        emailService.sendEmail(receiver.getEmail(), "Money Received", "You received ₹" + request.getAmount() + " from " + sender.getName());

        return "Money transfer success!";
    }

    public List<Transaction> getTransactionsByUserId(Long userId) {
        return transactionRepository.findBySenderId(userId);
    }
}
