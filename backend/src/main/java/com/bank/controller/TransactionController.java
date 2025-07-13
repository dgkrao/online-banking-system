package com.bank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bank.model.Transaction;
import com.bank.service.TransactionRequest;
import com.bank.service.TransactionService;

@CrossOrigin
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/transfer")
    public String transfer(@RequestBody TransactionRequest request) {
        boolean isBankTransfer = (request.getRecipientMobile() == null || request.getRecipientMobile().isEmpty());
        String recipient = isBankTransfer ? request.getRecipientAccount() : request.getRecipientMobile();
        return transactionService.transferMoney(request.getSenderMobile(), recipient, request.getAmount(), isBankTransfer);
    }

    @GetMapping("/history")
    public List<Transaction> getTransactions(@RequestParam String mobile) {
        return transactionService.getTransactions(mobile);
    }
}
