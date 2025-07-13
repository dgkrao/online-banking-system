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
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SmsService smsService;

    public String transferMoney(String senderMobile, String recipient, double amount, boolean isBankTransfer) {
        User sender = userRepository.findByMobile(senderMobile);
        if (sender == null || sender.getBalance() < amount) {
            return "Insufficient balance or sender not found";
        }

        sender.setBalance(sender.getBalance() - amount);
        userRepository.save(sender);

        Transaction debitTxn = new Transaction(senderMobile, recipient, amount, "DEBIT");
        transactionRepository.save(debitTxn);

        if (!isBankTransfer) {
            User receiver = userRepository.findByMobile(recipient);
            if (receiver != null) {
                receiver.setBalance(receiver.getBalance() + amount);
                userRepository.save(receiver);

                Transaction creditTxn = new Transaction(recipient, senderMobile, amount, "CREDIT");
                transactionRepository.save(creditTxn);

                // Notify both users
                emailService.sendTransactionAlert(sender, receiver, amount);
                smsService.sendTransactionAlert(sender, receiver, amount);
            }
        }

        return "success";
    }

    public List<Transaction> getTransactions(String mobile) {
        return transactionRepository.findBySenderMobile(mobile);
    }
}
