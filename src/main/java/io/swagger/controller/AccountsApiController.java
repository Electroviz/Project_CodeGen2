package io.swagger.controller;

import io.swagger.annotations.Api;
import io.swagger.api.AccountsApi;
import io.swagger.enums.UserRoleEnum;
import io.swagger.model.BankAccount;
import io.swagger.model.entity.User;
import io.swagger.service.BankAccountService;
import io.swagger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-12T15:22:53.754Z[GMT]")
@RestController
@CrossOrigin
@Api(tags= {"Accounts"})
public class AccountsApiController implements AccountsApi {

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private UserService userService;

    //melle
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

    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('CUSTOMER')")
    public ResponseEntity getTotalBalanceForUserId(@PathVariable("userId") Long userId) {
        User u = this.getLoggedInUser();

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

    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('CUSTOMER')")
    public ResponseEntity postBankAccountsForUserByUserId(@PathVariable("userId") Long userId) {
        return bankAccountService.PostOneSavingsAccountAndCurrentAccountForUser(userId);
    }

    //melle

    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('CUSTOMER')")
    public ResponseEntity getBankAccountByUserId(@PathVariable("userId") long userId) {
        User u = this.getLoggedInUser();

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

    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity getAllBankAccountsController(){

        return bankAccountService.GetAllBankAccounts();
    }

    //melle

    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('CUSTOMER')")
    public ResponseEntity getBankAccountInfoByIbanController(@PathVariable("IBAN") String IBAN) {
        //protection for requesting bankaccount info
        User u = this.getLoggedInUser();

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


    private User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.getUserById(userService.getUserIdByUsername(authentication.getName()));
    }
}
