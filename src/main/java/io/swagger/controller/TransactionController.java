package io.swagger.controller;

import io.swagger.model.Transaction;
import io.swagger.service.BankAccountService;
import io.swagger.service.TransactionService;
import io.swagger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.threeten.bp.OffsetDateTime;

import java.util.List;

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

    //Melle
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value="/transactions/all")
    public ResponseEntity GetAllTransactions() {
        return ResponseEntity.status(200).body(transactionService.GetAllTransactionsFromDatabase());
    }

    //Melle
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, value="/transactions/withdraw/{iban}/{amount}")
    public ResponseEntity Withdraw(@PathVariable("iban") String iban, @PathVariable("amount") Double amount) {
        if(transactionService.WithdrawOrDepositMoney(iban,amount,true)) {
            return ResponseEntity.status(200).body("Success");
        }
        else {
            return ResponseEntity.status(400).body("Failed to withdraw");
        }
    }

    //Melle
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, value="/transactions/deposit/{iban}/{amount}")
    public ResponseEntity Deposit(@PathVariable("iban") String iban, @PathVariable("amount") Double amount) {
        if(transactionService.WithdrawOrDepositMoney(iban,amount,false)) {
            return ResponseEntity.status(200).body("Success");
        }
        else {
            return ResponseEntity.status(400).body("Failed to deposit");
        }
    }

    //Melle
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value="/transactions/byDate/{fromDate}/{toDate}")
    public ResponseEntity GetTransactionsByDate(@PathVariable("fromDate") OffsetDateTime fromDate, @PathVariable("todate") OffsetDateTime toDate) {
        List<Transaction> correctTransactions = transactionService.GetTransactionsInBetweenDate(fromDate,toDate,null);

        if(correctTransactions == null || correctTransactions.size() == 0) return ResponseEntity.status(400).body("No transactions in between this date");
        else return ResponseEntity.status(200).body(correctTransactions);
    }

    //Melle
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value="/transactions/byDateAndUser/{fromDate}/{toDate}/{userId}")
    public ResponseEntity GetTransactionsByDateAndUser(@PathVariable("fromDate") OffsetDateTime fromDate, @PathVariable("todate") OffsetDateTime toDate, @PathVariable("userId") Integer userId) {
        List<Transaction> correctTransactions = transactionService.GetTransactionsInBetweenDate(fromDate,toDate,userId);

        if(correctTransactions == null || correctTransactions.size() == 0) return ResponseEntity.status(400).body("No transactions in between this date");
        else return ResponseEntity.status(200).body(correctTransactions);
    }

    //Melle
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value="/transactions/byIbans/{fromIban}/{toIban}")
    public ResponseEntity GetTransactionByIbans(@PathVariable("fromIban") String fromIban, @PathVariable("toIban") String toIban) {
        Transaction t = transactionService.GetTransactionByIbans(fromIban,toIban);
        if(t == null ) return ResponseEntity.status(400).body(null);
        else return ResponseEntity.status(200).body(t);
    }
}
