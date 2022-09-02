package io.swagger.controller;

import io.swagger.service.BankAccountService;
import io.swagger.service.TransactionService;
import io.swagger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class TransactionController {

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    //Melle
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, value="/transactions/{fromIban}/{toIban}/{amount}")
    public ResponseEntity transferMoney(@PathVariable("fromIban") String fromIban, @PathVariable("toIban") String toIban, @PathVariable("amount") Double amount) {
        //eigenlijk moet de fromIban opgehaald worden aan de hand van ingelogde user.
        if(transactionService.IbanHasSufficientMoney(fromIban,amount)) {
            if(transactionService.TransferMoneyFromToIban(fromIban,toIban,amount)) return ResponseEntity.status(200).body(fromIban);
            else return ResponseEntity.status(400).body(fromIban);
        }
        else return ResponseEntity.status(400).body(null);
    }

    //    @CrossOrigin
//    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value="/bankAccounts/{userId}")
//    public ResponseEntity testFunc(@PathVariable("userId") long userId) {
//        List<BankAccount> bankAccounts = bankAccountService.GetBankAccountsByUserId(userId);
//
//        if(bankAccounts.stream().count() == 0 || bankAccounts == null) return ResponseEntity.status(400).body("Bad Request");
//        else return ResponseEntity.status(200).body(bankAccounts);
//    }
//
//    //melle
//    @CrossOrigin
//    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE,value = "/allBankAccounts")
//    public ResponseEntity getAccountByIBANController(){
//
//        return bankAccountService.GetAllBankAccounts();
//    }
}
