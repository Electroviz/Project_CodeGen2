package io.swagger.controller;

import io.swagger.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
public class TransactionController {
    @Autowired
    private TransactionService transactionService;


    @RequestMapping("/transaction")
    public ResponseEntity start() {

        return transactionService.StartTransactionTest();
    }

}
