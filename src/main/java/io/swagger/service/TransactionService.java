package io.swagger.service;

import io.swagger.api.ApiException;
import io.swagger.model.BankAccount;
import io.swagger.model.Transaction;
import io.swagger.model.User;
import io.swagger.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private BankAccountService bankAccountService;
    @Autowired
    private UserService userService;


//    This is a test transaction !!!!
    private double balance1;
    private String name;

    public String bankktest(String name){
        this.name = name;
        return name;
    }
    public double getBalanceTest(){
        return balance1;
    }
    public void deposittest(double amount){
        balance1 += amount;
        System.out.println(name + " has $" + balance1);
    }
    public void withdraw(double amount){
        if (amount < balance1){
            balance1 -= amount;
            System.out.println(name + " has $" + balance1);
        }
        else {
            System.out.println("withdraw by " + name + " fails");
        }

    }
    public ResponseEntity StartTransactionTest(){
        String user1 = bankktest("hans");
        //deposittest(500);
        String user2 = bankktest("Willem");
        deposittest(900);
        return null;
    }
//    End of the test transaction !!!!

    public void createTransaction(User currentUser, Transaction transaction) throws ApiException {

        //Check if amount is valid
        if (transaction.getAmount().doubleValue() <=0)
            throw ApiException.badRequest("Invalid amount");

        //Check the iban from the sender + receiver
        BankAccount fromBankAccount = bankAccountService.GetBankAccountByIban(transaction.getFrom());
        BankAccount toBankAccount = bankAccountService.GetBankAccountByIban(transaction.getTo());

        //Prevent transfers from a savings account to an account of not the same customer
        if(fromBankAccount.getAccountType().equals(BankAccount.AccountTypeEnum.SAVINGS)){
            if (!Objects.equals(fromBankAccount.getUserId(), toBankAccount.getUserId())){
                throw ApiException.badRequest("Transactions from a savings account should be to an account of the same customer");
            }
        }

        //Prevent transfers from an account to a savings account of not the same customer
        if (toBankAccount.getAccountType().equals(BankAccount.AccountTypeEnum.SAVINGS)) {
            if (!Objects.equals(fromBankAccount.getUserId(), toBankAccount.getUserId())) {
                throw ApiException.badRequest("Transactions to a savings account should be to an account of the same customer");
            }
        }

        //Get the from user (the sender)
       User fromUser = userService.findById(fromBankAccount.getUserId()).orElseThrow(() -> ApiException.badRequest("No such from user"));

        // check the transaction limit for the from user
        if (transaction.getAmount().doubleValue() > fromUser.getTransactionLimit().doubleValue()) {
            throw ApiException.badRequest("The transaction amount exceeds the transaction limit");
        }

        // get the from/to bank accounts
        // compute the from account balance
        BigDecimal fromAccountBalance = BigDecimal.valueOf(fromBankAccount.getBalance()).subtract(transaction.getAmount());

        // ensure the new balance is not less than the absolute limit
        if (fromAccountBalance.doubleValue() < fromBankAccount.getAbsoluteLimit().doubleValue()) {
            throw ApiException.badRequest("Transaction failed. Balance in the from account will be less than the absolute limit");
        }

        // ensure the total value of transactions for this person as of today does not exceed the
        // daily limit
        BigDecimal totalDayTransactions = getTotalDayTransactions(fromUser);

        // compute the total day transactions if this transaction were to go through
        BigDecimal newDayTotal = totalDayTransactions.add(transaction.getAmount());
        if (newDayTotal.longValue() > fromUser.getDayLimit().longValue()) {
            throw ApiException.badRequest("Transaction failed. This transaction will exceed your day limit.");
        }

        //Update the senders bank account
        //UPdate the receivers bank account
        //Save the transaction
        // ----  start the  transaction ----

        //Implemt in bankaccountservice
        //Updateuser functie
    }

    public BigDecimal getTotalDayTransactions(User user) {
        BigDecimal total = BigDecimal.valueOf(0);

        // get all the transactions
        List<Transaction> allTransactions = transactionRepository.findAll();
        for (Transaction transaction : allTransactions) {
            // check if the current transaction belongs to the given user.
            if (!Objects.equals(transaction.getUserIDPerforming(), user.getId())) {
                continue;
            }
            // check if the current transaction belongs to current date
            org.threeten.bp.LocalDate transactionDate = transaction.getTimestamp().toLocalDateTime().toLocalDate();
            LocalDate today = LocalDate.now();
            if (!transactionDate.equals(today)) {
                continue;
            }

            // this transaction is of today and was initiated by this user, add the amount to
            // the current totals
            total = total.add(transaction.getAmount());
        }

        return total;
    }


    public int createTransaction2() {
        int tom = 5;

        return  tom;
    }
}
