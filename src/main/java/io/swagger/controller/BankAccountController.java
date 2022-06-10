package io.swagger.controller;

import io.swagger.model.BankAccount;
import io.swagger.model.TransactionInfo;
import io.swagger.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BankAccountController {

    @Autowired
    private BankAccountService bankAccountService;

    //melle
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, value= "/putBankAccountType/{type}/{IBAN}")
    public ResponseEntity putBankAccountTypeByIBAN(@PathVariable("type") String type, @PathVariable("IBAN") String IBAN) {
        type = type.replaceAll("[{}]",""); //make sure that the {variable} quotes are not taking into consideration
        BankAccount.AccountTypeEnum bankAccountType = BankAccount.AccountTypeEnum.valueOf(type);
        BankAccount bankAccountByIban = bankAccountService.GetBankAccountByIban(IBAN);
        if(bankAccountByIban == null) return ResponseEntity.status(400).body("unknown IBAN or TYPE");
        else {
            return bankAccountService.PutBankAccountType(bankAccountType,bankAccountService.GetBankAccountByIban(IBAN));
        }
    }

    //melle
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, value="/initBankAccounts/{userId}")
    public ResponseEntity postBankAccountsForUserByUserId(@PathVariable("userId") Long userId) {
        return bankAccountService.PostOneSavingsAccountAndCurrentAccountForUser(userId);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value="/bankAccounts/{userId}")
    public ResponseEntity testFunc(@PathVariable("userId") long userId) {
        return bankAccountService.GetBankAccountsByUserId(userId);
    }

    //melle
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE,value = "/allBankAccounts")
    public ResponseEntity getAccountByIBANController(){
        if (bankAccountService.GetAllBankAccounts().getStatusCode().isError()){
            return ResponseEntity.status(400).body("Bad Request");
        }
        else {
            return ResponseEntity.status(201).body(bankAccountService.GetAllBankAccounts());
        }
    }

    //Nicky
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE,value="/getBankAccount/{FULLNAME}")
    public ResponseEntity getBankAccountInfoByName(@PathVariable("fullName") String fullName) {
        List<String> ibansToReturn = bankAccountService.getAccountByName(fullName);

        if(ibansToReturn != null) {
            return new ResponseEntity<List>(ibansToReturn,HttpStatus.FOUND);
        }
        else {
            return ResponseEntity.status(400).body(ibansToReturn);
        }
    }

    //Nicky
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,value="/getBankAccount/{IBAN}/Deposit")
    public ResponseEntity accountDeposit(@PathVariable("IBAN") String IBAN, @RequestBody Double amount) {
        TransactionInfo transactionInfo = bankAccountService.AccountDeposit(IBAN, amount);

        if(ibansToReturn != null) {
            return new ResponseEntity<List>(ibansToReturn,HttpStatus.FOUND);
        }
        else {
            return ResponseEntity.status(400).body(ibansToReturn);
        }
    }

    //Nicky
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,value="/getBankAccount/{IBAN}/Withdraw")
    public ResponseEntity accountWithdraw(@PathVariable("amount") float amount) {
        List<String> ibansToReturn = bankAccountService.getAccountByName(fullName);

        if(ibansToReturn != null) {
            return new ResponseEntity<List>(ibansToReturn,HttpStatus.FOUND);
        }
        else {
            return ResponseEntity.status(400).body(ibansToReturn);
        }
    }
}
