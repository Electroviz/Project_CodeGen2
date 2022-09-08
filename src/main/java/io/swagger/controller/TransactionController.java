package io.swagger.controller;

import io.swagger.enums.UserRoleEnum;
import io.swagger.model.BankAccount;
import io.swagger.model.Transaction;
import io.swagger.model.entity.User;
import io.swagger.service.BankAccountService;
import io.swagger.service.TransactionService;
import io.swagger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.threeten.bp.OffsetDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User u = userService.getUserById(userService.getUserIdByUsername(authentication.getName()));

        boolean canPerform = false;
        if(u.getRole() == UserRoleEnum.ROLE_EMPLOYEE) canPerform = true;
        else {
            if(Objects.equals(bankAccountService.GetBankAccountByIban(fromIban).getUserId(), u.getId())) canPerform = true;
        }

        if(canPerform) {
            if (transactionService.IbanHasSufficientMoney(fromIban, amount)) {
                if (transactionService.TransferMoneyFromToIban(toIban, fromIban, amount, u.getId().intValue()))
                    return ResponseEntity.status(200).body(bankAccountService.GetBankAccountByIban(fromIban));
                else return ResponseEntity.status(400).body("Sending Bank Account is invalid");
            } else return ResponseEntity.status(400).body(null);
        }
        else return ResponseEntity.status(401).body(null);
    }

    //Melle
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value="/transactions/all")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity GetAllTransactions() {
        return ResponseEntity.status(200).body(transactionService.GetAllTransactionsFromDatabase());
    }

    //Melle
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, value="/transactions/withdraw/{iban}/{amount}")
    public ResponseEntity Withdraw(@PathVariable("iban") String iban, @PathVariable("amount") Double amount) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User u = userService.getUserById(userService.getUserIdByUsername(authentication.getName()));

        boolean canPerform = false;
        if(Objects.equals(u.getRole(), UserRoleEnum.ROLE_EMPLOYEE)) canPerform = true;
        else {
            if(Objects.equals(bankAccountService.GetBankAccountByIban(iban).getUserId(), u.getId())) canPerform = true;
            else return ResponseEntity.status(401).body(null);
        }

        if(canPerform) {
            if (transactionService.WithdrawOrDepositMoney(iban, amount, true, u.getId().intValue())) {
                return ResponseEntity.status(200).body("Success");
            } else {
                return ResponseEntity.status(400).body("Failed to withdraw");
            }
        }
        else return ResponseEntity.status(401).body(null);
    }

    //Melle
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, value="/transactions/deposit/{iban}/{amount}")
    public ResponseEntity Deposit(@PathVariable("iban") String iban, @PathVariable("amount") Double amount) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User u = userService.getUserById(userService.getUserIdByUsername(authentication.getName()));

        boolean canPerform = false;
        if(Objects.equals(u.getRole(), UserRoleEnum.ROLE_EMPLOYEE)) canPerform = true;
        else {
            if(Objects.equals(bankAccountService.GetBankAccountByIban(iban).getUserId(), u.getId())) canPerform = true;
            else return ResponseEntity.status(401).body(null);
        }

        if(canPerform) {
            if (transactionService.WithdrawOrDepositMoney(iban, amount, false, u.getId().intValue())) {
                return ResponseEntity.status(200).body("Success");
            } else {
                return ResponseEntity.status(400).body("Failed to deposit");
            }
        }
        else return ResponseEntity.status(401).body(null);
    }

    //Melle
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value="/transactions/byDate/{fromDate}/{toDate}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity GetTransactionsByDate(@PathVariable("fromDate") OffsetDateTime fromDate, @PathVariable("todate") OffsetDateTime toDate) {
        List<Transaction> correctTransactions = transactionService.GetTransactionsInBetweenDate(fromDate,toDate,null);

        if(correctTransactions == null || correctTransactions.size() == 0) return ResponseEntity.status(204).body("No transactions in between this date");
        else return ResponseEntity.status(200).body(correctTransactions);
    }

    //Melle
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value="/transactions/byDateAndUser/{fromDate}/{toDate}/{userId}")
    public ResponseEntity GetTransactionsByDateAndUser(@PathVariable("fromDate") OffsetDateTime fromDate, @PathVariable("todate") OffsetDateTime toDate, @PathVariable("userId") Integer userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User u = userService.getUserById(userService.getUserIdByUsername(authentication.getName()));

        List<Transaction> correctTransactions = new ArrayList<>();
        if(Objects.equals(u.getRole(), UserRoleEnum.ROLE_EMPLOYEE)) {
            correctTransactions = transactionService.GetTransactionsInBetweenDate(fromDate,toDate,userId);
        }
        else correctTransactions = transactionService.GetTransactionsInBetweenDate(fromDate,toDate,u.getId().intValue());


        if(correctTransactions == null || correctTransactions.size() == 0) return ResponseEntity.status(204).body("No transactions in between this date");
        else return ResponseEntity.status(200).body(correctTransactions);
    }

    //Melle
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value="/transactions/byIbans/{fromIban}/{toIban}")
    public ResponseEntity GetTransactionByIbans(@PathVariable("fromIban") String fromIban, @PathVariable("toIban") String toIban) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User u = userService.getUserById(userService.getUserIdByUsername(authentication.getName()));

        List<Transaction> correctTransactions = new ArrayList<>();
        //only let a customer execute this code if from or to iban is his own iban
        if(Objects.equals(u.getRole(), UserRoleEnum.ROLE_EMPLOYEE)) {
            correctTransactions = transactionService.GetTransactionByIbans(fromIban,toIban);
        }
        else {
            if(bankAccountService.CheckIbanBelongsToUser(u.getId().intValue(),fromIban) || bankAccountService.CheckIbanBelongsToUser(u.getId().intValue(),fromIban)) {
                //user owns one of the ibans so get the transactions
                correctTransactions = transactionService.GetTransactionByIbans(fromIban,toIban);
            }
            else return ResponseEntity.status(401).body(null);
        }

        correctTransactions = transactionService.GetTransactionByIbans(fromIban,toIban);
        if(correctTransactions == null ) return ResponseEntity.status(400).body(null);
        else return ResponseEntity.status(200).body(correctTransactions);
    }

    //Melle
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value="/transactions/byAmountIsEqual/{iban}/{amount}")
    public ResponseEntity GetTransactionByAmountEqualToNum (@PathVariable("iban") String iban, @PathVariable("amount") Double amount) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User u = userService.getUserById(userService.getUserIdByUsername(authentication.getName()));

        List<Transaction> result = new ArrayList<>();

        if(Objects.equals(u.getRole(), UserRoleEnum.ROLE_EMPLOYEE)) {
            result = transactionService.GetTransactionByRelationship(iban,amount,"equal");
        }
        else {
            if(bankAccountService.CheckIbanBelongsToUser(u.getId().intValue(),iban)) {
                result = transactionService.GetTransactionByRelationship(iban,amount,"equal");
            }
            else return ResponseEntity.status(401).body(null);
        }

        if(result.size() == 0) return ResponseEntity.status(204).body(result);
        else return ResponseEntity.status(200).body(result);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value="/transactions/byAmountIsSmaller/{iban}/{amount}")
    public ResponseEntity GetTransactionByAmountSmallerToNum (@PathVariable("iban") String iban, @PathVariable("amount") Double amount) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User u = userService.getUserById(userService.getUserIdByUsername(authentication.getName()));

        List<Transaction> result = new ArrayList<>();

        if(Objects.equals(u.getRole(), UserRoleEnum.ROLE_EMPLOYEE)) {
            result = transactionService.GetTransactionByRelationship(iban,amount,"smaller");
        }
        else {
            if(bankAccountService.CheckIbanBelongsToUser(u.getId().intValue(),iban)) {
                result = transactionService.GetTransactionByRelationship(iban,amount,"smaller");
            }
            else return ResponseEntity.status(401).body(null);
        }

        if(result.size() == 0) return ResponseEntity.status(204).body(result);
        else return ResponseEntity.status(200).body(result);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value="/transactions/byAmountIsBigger/{iban}/{amount}")
    public ResponseEntity GetTransactionByAmountBiggerToNum (@PathVariable("iban") String iban, @PathVariable("amount") Double amount) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User u = userService.getUserById(userService.getUserIdByUsername(authentication.getName()));

        List<Transaction> result = new ArrayList<>();

        if(Objects.equals(u.getRole(), UserRoleEnum.ROLE_EMPLOYEE)) {
            result = transactionService.GetTransactionByRelationship(iban,amount,"bigger");
        }
        else {
            if(bankAccountService.CheckIbanBelongsToUser(u.getId().intValue(),iban)) {
                result = transactionService.GetTransactionByRelationship(iban,amount,"bigger");
            }
            else return ResponseEntity.status(401).body(null);
        }

        if(result.size() == 0) return ResponseEntity.status(204).body(result);
        else return ResponseEntity.status(200).body(result);
    }
}
