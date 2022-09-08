package io.swagger.controller;

import io.swagger.annotations.Authorization;
import io.swagger.enums.UserRoleEnum;
import io.swagger.model.BankAccount;
import io.swagger.model.Transaction;
import io.swagger.model.TransactionInfo;
import io.swagger.model.entity.User;
import io.swagger.service.BankAccountService;
import io.swagger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin
public class BankAccountController {

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private UserService userService;

    //melle
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, value= "/putBankAccountType/{type}/{IBAN}")
    @PreAuthorize("hasRole('EMPLOYEE')")
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
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, value= "/putAbsoluteLimit/{value}/{IBAN}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity putBankAccountAbsoluteLimit(@PathVariable("value") String value, @PathVariable("IBAN") String IBAN) {
        Double newAbsoluteLimit = null;
        try {
            newAbsoluteLimit = Double.valueOf(value);
        }
        catch(Exception e) {
            return ResponseEntity.status(412).body("Bad Request"); //412 'precondition failed'
        }

        return bankAccountService.PutBankAccountAbsoluteLimit(newAbsoluteLimit,bankAccountService.GetBankAccountByIban(IBAN));
    }

    //Melle
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, value = "/putBankAccountStatus/{status}/{IBAN}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity putBankAccountStatusByIban(@PathVariable("status") String status, @PathVariable("IBAN") String IBAN) {
        BankAccount.AccountStatusEnum bankAccountStatus = BankAccount.AccountStatusEnum.valueOf(status);
        BankAccount bankAccountByIban = bankAccountService.GetBankAccountByIban(IBAN);

        if(bankAccountByIban == null || bankAccountStatus == null) return ResponseEntity.status(400).body(null);
        else {
            return bankAccountService.PutBankAccountStatus(bankAccountStatus,bankAccountByIban);
        }
    }

    //melle
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value="/totalBalance/{userId}")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('CUSTOMER')")
    public ResponseEntity getTotalBalanceForUserId(@PathVariable("userId") Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User u = userService.getUserById(userService.getUserIdByUsername(authentication.getName()));

        if(Objects.equals(u.getRole(), UserRoleEnum.ROLE_EMPLOYEE)) {
            //allow the function to be executed for any user
            return bankAccountService.GetTotalBalanceByUserId(userId); //method return response entity
        }
        else {
            if(Objects.equals(u.getId(), userId)) return bankAccountService.GetTotalBalanceByUserId(userId);
            else return ResponseEntity.status(401).body(null);
        }
    }

    //melle
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, value="/initBankAccounts/{userId}")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('CUSTOMER')")
    public ResponseEntity postBankAccountsForUserByUserId(@PathVariable("userId") Long userId) {
        return bankAccountService.PostOneSavingsAccountAndCurrentAccountForUser(userId);
    }

    //melle
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value="/bankAccounts/{userId}")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('CUSTOMER')")
    public ResponseEntity testFunc(@PathVariable("userId") long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User u = userService.getUserById(userService.getUserIdByUsername(authentication.getName()));

        List<BankAccount> bankAccounts = bankAccountService.GetBankAccountsByUserId(userId);

        boolean canPerform = false;
        if(Objects.equals(u.getRole(), UserRoleEnum.ROLE_EMPLOYEE)) {
            canPerform = true;
        }
        else {
            if(u.getId() == userId) canPerform = true;
        }

        if(canPerform) {
            if (bankAccounts.stream().count() == 0 || bankAccounts == null)
                return ResponseEntity.status(404).body(null); //not found
            else return ResponseEntity.status(200).body(bankAccounts); //succes
        } else return ResponseEntity.status(401).body(null); //forbidden
    }

    //melle
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE,value = "/allBankAccounts")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity getAllBankAccountsController(){

        return bankAccountService.GetAllBankAccounts();
    }

    //melle
    @RequestMapping(value = "/createBankAccount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('CUSTOMER')")
    public ResponseEntity registerNewBankAccountController(@RequestBody BankAccount account){
        ResponseEntity response = bankAccountService.CreateNewBankAccount();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User u = userService.getUserById(userService.getUserIdByUsername(authentication.getName()));

        if(u.getRole() != UserRoleEnum.ROLE_EMPLOYEE) {
            if(Objects.equals(u.getId(), account.getUserId()) == false) return ResponseEntity.status(401).body(null);
        }

        if (response.getStatusCode().isError()) {
            return new ResponseEntity(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity(response, HttpStatus.CREATED);
        }
    }

    //melle
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE,value="/getBankAccount/{IBAN}")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('CUSTOMER')")
    public ResponseEntity getBankAccountInfoByIbanController(@PathVariable("IBAN") String IBAN) {
        //protection for requesting bankaccount info
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User u = userService.getUserById(userService.getUserIdByUsername(authentication.getName()));

        boolean canPerform = false;

        if(u.getRole() == UserRoleEnum.ROLE_EMPLOYEE || u.getRole() == UserRoleEnum.ROLE_BANK) canPerform = true;
        else {
            //check if the asked iban is the users iban
            if(bankAccountService.GetBankAccountByIban(IBAN).getUserId() == u.getId()) canPerform = true;
            else canPerform = false;
        }

        if(canPerform) {
            BankAccount ba = bankAccountService.GetBankAccountByIban(IBAN);

            if (ba != null) {
                return new ResponseEntity<BankAccount>(ba, HttpStatus.FOUND);
            } else {
                return ResponseEntity.status(400).body("Bad Request");
            }
        }
        else return ResponseEntity.status(401).body("Bad Request");
    }

    //Nicky
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE,value="/getBankAccount/name/{FULLNAME}")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('CUSTOMER')")
    public ResponseEntity getBankAccountInfoByName(@PathVariable("FULLNAME") String fullName) {
        //Anyone that is logged in should be able to perform this method
        List<String> ibansToReturn = bankAccountService.getAccountByName(fullName);

        if(ibansToReturn != null) {
            return new ResponseEntity<List>(ibansToReturn,HttpStatus.OK);
        }
        else if(ibansToReturn.size() == 0) return ResponseEntity.status(404).body(null);
        else {
            return ResponseEntity.status(400).body("Bad Request");
        }
    }

    //Nicky
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,value="/getBankAccount/{IBAN}/Deposit")
    public ResponseEntity accountDeposit(@PathVariable("IBAN") String IBAN, @RequestBody Transaction transaction) {
        TransactionInfo transactionInfo = bankAccountService.AccountDeposit(IBAN, transaction.getAmount());

        if(transactionInfo != null) {
            return new ResponseEntity<TransactionInfo>(transactionInfo,HttpStatus.ACCEPTED);
        }
        else {
            return ResponseEntity.status(400).body("Bad Request");
        }
    }

    //Nicky
    @RequestMapping(method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE,value="/deleteAccount/{IBAN}")
    @PreAuthorize("hasRole('EMPLOYEE')")
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
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,value="/getBankAccount/{IBAN}/Withdraw")
    public ResponseEntity accountWithdraw(@PathVariable("IBAN") String IBAN, @RequestBody Transaction transaction) {
        TransactionInfo transactionInfo = bankAccountService.AccountWithdraw(IBAN, transaction.getAmount());

        if(transactionInfo != null) {
            return new ResponseEntity<TransactionInfo>(transactionInfo,HttpStatus.ACCEPTED);
        }
        else {
            return ResponseEntity.status(400).body("Bad Request");
        }
    }
}
