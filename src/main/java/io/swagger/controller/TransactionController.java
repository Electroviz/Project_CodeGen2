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
        //NOTTEEEE USERIDPERFORMING MOET NOG VAN 0 GEHAALD WORDEN EN ADHV JWT TOKEN GEFIXED WORDEN
        if(transactionService.IbanHasSufficientMoney(fromIban,amount)) {
            if(transactionService.TransferMoneyFromToIban(toIban,fromIban,amount,0)) return ResponseEntity.status(200).body(bankAccountService.GetBankAccountByIban(fromIban));
            else return ResponseEntity.status(400).body("Sending Bank Account is invalid");
        }
        else return ResponseEntity.status(400).body(null);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value="/transactions/all")
    public ResponseEntity GetAllTransactions() {
        return ResponseEntity.status(200).body(transactionService.GetAllTransactionsFromDatabase());
    }

    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, value="/transactions/withdraw/{iban}/{amount}")
    public ResponseEntity Withdraw(@PathVariable("iban") String iban, @PathVariable("amount") Double amount) {
        if(transactionService.WithdrawOrDepositMoney(iban,amount,true)) {
            return ResponseEntity.status(200).body("Success");
        }
        else {
            return ResponseEntity.status(400).body("Failed to withdraw");
        }
    }

    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, value="/transactions/deposit/{iban}/{amount}")
    public ResponseEntity Deposit(@PathVariable("iban") String iban, @PathVariable("amount") Double amount) {
        if(transactionService.WithdrawOrDepositMoney(iban,amount,false)) {
            return ResponseEntity.status(200).body("Success");
        }
        else {
            return ResponseEntity.status(400).body("Failed to deposit");
        }
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
