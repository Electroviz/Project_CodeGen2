package io.swagger.service;

import io.swagger.api.ApiException;
import io.swagger.model.BankAccount;
import io.swagger.model.Transaction;
import io.swagger.model.User;
import io.swagger.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.OffsetDateTime;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        System.out.println(name + " has $" + balance1 + " After deposit");
    }
    public void withdraw(double amount){
        if (amount < balance1){
            balance1 -= amount;
            System.out.println(name + "  has $" + balance1 + " After withdraw");
        }
        else {
            System.out.println("withdraw by " + name + " fails");
        }

    }
    public ResponseEntity StartTransactionTest(){
        String user1 = bankktest("hans");
        deposittest(500);
        withdraw(200);

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

        // update the "to" bank balance


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

    public List<Transaction> getTransactionHistoryByIban(User currentUser, String iban, int skip, int limit) throws ApiException {
        BankAccount account = bankAccountService.getByIban(iban);

        // get user transactions
        List<Transaction> userTransactions = filterByUser(currentUser);

        // filter the transactions by iban
        List<Transaction> filteredTransactions = new ArrayList<>();
        for (Transaction userTransaction : userTransactions) {
            if(userTransaction.getFrom().equals(iban) || userTransaction.getTo().equals(iban) ){
                filteredTransactions.add(userTransaction);
            }
        }

        // paginate the transaction list
        return paginateTransactions(filteredTransactions, skip, limit);
    }

    public List<Transaction> filterByUser(User currentUser) {
        // collect the transaction entities
        List<Transaction> entities = transactionRepository.findAll();

        // filter the transactions that whose 'from' or 'to' belongs to the current user
        List<Transaction> filteredEntities = new ArrayList<>();
        for (Transaction entity : entities) {
            try {
                // get the 'from' bank account of this transaction
                BankAccount bankAccount = bankAccountService.getByIban(entity.getFrom());
                // verify if the 'from' bank account belongs to the current user
                if (Objects.equals(bankAccount.getUserId(), currentUser.getId())) {
                    filteredEntities.add(entity);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // convert the filtered transaction entities to models
        return filteredEntities.stream().map(this::toModel).collect(Collectors.toList());
    }
    public Transaction toModel(Transaction entity) {
        Transaction model = new Transaction();
        model.setAmount(entity.getAmount());
        model.setTo(entity.getTo());
        model.setFrom(entity.getFrom());
        model.setUserIDPerforming(Math.toIntExact(entity.getUserIDPerforming()));
        model.setDescription(entity.getDescription());
        model.setTransactionID(Math.toIntExact(entity.getTransactionID()));

        // convert the time from java.sql.Timestamp to swagger's own
        LocalDateTime dateTime = entity.getTimestamp().toLocalDateTime();
        model.setTimestamp(OffsetDateTime.parse(dateTime.toString()));
        return model;
    }
    private List<Transaction> paginateTransactions(List<Transaction> transactions, int skip, int limit) {
        try {
            // slice the users based on the skip/limit parameters

            // calculate the beginning & end index
            int start = skip;
            int end = skip + limit;
            // ensure the end index is less than or equal to the maximum number of entities.
            end = Math.min(end, transactions.size());

            // get the data within the skip/limit params
            return transactions.subList(start, end);
        } catch (Exception e) {
            System.err.println(e);
            return Collections.emptyList();
        }
    }

}
