package io.swagger.controller;

import io.swagger.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @RequestMapping("/transaction")
    public ResponseEntity CreateTransactionTest(){
        return transactionService.testTransactionTest();
    }

//    @RequestMapping("/TestTransfer/{userId}")
//    public ResponseEntity createTransaction(@PathVariable("userId") Long userId){
//        return transactionService.MakeTransaction(userId);

//    @RequestMapping("/TestTransfer/{UserDTO}")
//    public User createTransaction(@PathVariable("UserDTO") User UserDTO){
//        User userobej = UserDTO;
//        return transactionService.MakeTransaction(userobej);
//    }


    @RequestMapping("/TestTransfer/{userId}")
    public ResponseEntity createTransaction(@PathVariable Long userId){
        return transactionService.MakeTransaction(userId);
    }
}
