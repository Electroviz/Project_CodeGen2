package io.swagger.controller;

import io.swagger.model.BankAccount;
import io.swagger.model.Transaction;
import io.swagger.model.TransactionInfo;
import io.swagger.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
public class BankAccountController {

    @Autowired
    private BankAccountService bankAccountService;

    //melle
    @CrossOrigin
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

    //Melle
    @CrossOrigin
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, value= "/putAbsoluteLimit/{value}/{IBAN}")
    public ResponseEntity putBankAccountAbsoluteLimit(@PathVariable("value") String value, @PathVariable("IBAN") String IBAN) {
        Double newAbsoluteLimit = null;
        try {
            newAbsoluteLimit = Double.valueOf(value);
        }
        catch(Exception e) {
            return ResponseEntity.status(400).body("Bad Request");
        }

        return bankAccountService.PutBankAccountAbsoluteLimit(newAbsoluteLimit,bankAccountService.GetBankAccountByIban(IBAN));
    }

    //Melle
    @CrossOrigin
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, value = "/putBankAccountStatus/{status}/{IBAN}")
    public ResponseEntity putBankAccountStatusByIban(@PathVariable("status") String status, @PathVariable("IBAN") String IBAN) {
        BankAccount.AccountStatusEnum bankAccountStatus = BankAccount.AccountStatusEnum.valueOf(status);
        BankAccount bankAccountByIban = bankAccountService.GetBankAccountByIban(IBAN);

        if(bankAccountByIban == null || bankAccountStatus == null) return ResponseEntity.status(400).body("Bad Request");
        else {
            return bankAccountService.PutBankAccountStatus(bankAccountStatus,bankAccountByIban);
        }
    }

    //melle
    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value="/totalBalance/{userId}")
    public ResponseEntity getTotalBalanceForUserId(@PathVariable("userId") Long userId) {
        return bankAccountService.GetTotalBalanceByUserId(userId);
    }

    //melle
    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, value="/initBankAccounts/{userId}")
    public ResponseEntity postBankAccountsForUserByUserId(@PathVariable("userId") Long userId) {
        return bankAccountService.PostOneSavingsAccountAndCurrentAccountForUser(userId);
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value="/bankAccounts/{userId}")
    public ResponseEntity testFunc(@PathVariable("userId") long userId) {
        List<BankAccount> bankAccounts = bankAccountService.GetBankAccountsByUserId(userId);
        
        if(bankAccounts.stream().count() == 0 || bankAccounts == null) return ResponseEntity.status(400).body("Bad Request");
        else return ResponseEntity.status(200).body(bankAccounts);
    }

    //melle
    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE,value = "/allBankAccounts")
    public ResponseEntity getAccountByIBANController(){

        return bankAccountService.GetAllBankAccounts();
    }

    //melle
    @CrossOrigin
    @RequestMapping(value = "/createBankAccount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity registerNewBankAccountController(@RequestBody BankAccount account){
        ResponseEntity response = bankAccountService.CreateNewBankAccount();

        if (response.getStatusCode().isError()) {
            return new ResponseEntity(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity(response, HttpStatus.CREATED);
        }
    }

    //melle
    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE,value="/getBankAccount/{IBAN}")
    public ResponseEntity getBankAccountInfoByIbanController(@PathVariable("IBAN") String IBAN) {
        BankAccount ba = bankAccountService.GetBankAccountByIban(IBAN);

        if(ba != null) {
            return new ResponseEntity<BankAccount>(ba,HttpStatus.FOUND);
        }
        else {
            return ResponseEntity.status(400).body("Bad Request");
        }
    }

    //Nicky
    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE,value="/getBankAccount/name/{FULLNAME}")
    public ResponseEntity getBankAccountInfoByName(@PathVariable("FULLNAME") String fullName) {

        List<String> ibansToReturn = bankAccountService.getAccountByName(fullName);

        if(ibansToReturn != null) {
            return new ResponseEntity<List>(ibansToReturn,HttpStatus.ACCEPTED);
        }
        else {
            return ResponseEntity.status(400).body("Bad Request");
        }
    }

    //Nicky
    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,value="/getBankAccount/{IBAN}/Deposit")
    public ResponseEntity accountDeposit(@PathVariable("IBAN") String IBAN, @RequestBody Transaction transaction) {
        TransactionInfo transactionInfo = bankAccountService.AccountDeposit(IBAN, transaction.getAmount());

        if(transactionInfo != null) {
            return new ResponseEntity<TransactionInfo>(transactionInfo,HttpStatus.FOUND);
        }
        else {
            return ResponseEntity.status(400).body("Bad Request");
        }
    }

    //Nicky
    @CrossOrigin
    @RequestMapping(method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE,value="/deleteAccount/{IBAN}")
    public ResponseEntity deleteAccount(@PathVariable("IBAN") String IBAN) {
        BankAccount bankAccount = bankAccountService.DeleteBankAccount(IBAN);

        if(bankAccount != null) {
            return new ResponseEntity<BankAccount>(bankAccount,HttpStatus.OK);
        }
        else {
            return ResponseEntity.status(400).body("Bad Request");
        }
    }

    //Nicky
    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,value="/getBankAccount/{IBAN}/Withdraw")
    public ResponseEntity accountWithdraw(@PathVariable("IBAN") String IBAN, @RequestBody Transaction transaction) {
        TransactionInfo transactionInfo = bankAccountService.AccountWithdraw(IBAN, transaction.getAmount());

        if(transactionInfo != null) {
            return new ResponseEntity<TransactionInfo>(transactionInfo,HttpStatus.FOUND);
        }
        else {
            return ResponseEntity.status(400).body("Bad Request");
        }
    }
}
