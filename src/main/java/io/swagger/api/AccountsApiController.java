package io.swagger.api;

import io.swagger.annotations.Api;
import io.swagger.controller.BankAccountController;
import io.swagger.enums.UserRoleEnum;
import io.swagger.model.BankAccount;
import io.swagger.model.Transaction;
import io.swagger.model.TransactionInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.entity.User;
import io.swagger.service.BankAccountService;
import io.swagger.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
    public ResponseEntity testFunc(@PathVariable("userId") long userId) {
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
    public ResponseEntity registerNewBankAccountController(@RequestBody BankAccount account){
        ResponseEntity response = bankAccountService.CreateNewBankAccount();

        User u = this.getLoggedInUser();

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

    public ResponseEntity accountWithdraw(@PathVariable("IBAN") String IBAN, @RequestBody Transaction transaction) {
        TransactionInfo transactionInfo = bankAccountService.AccountWithdraw(IBAN, transaction.getAmount());

        if(transactionInfo != null) {
            return new ResponseEntity<TransactionInfo>(transactionInfo,HttpStatus.ACCEPTED);
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
