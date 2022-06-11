package io.swagger.service;

import io.swagger.api.ApiException;
import io.swagger.model.BankAccount;
import io.swagger.model.Transaction;
import io.swagger.model.entity.User;
import io.swagger.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private BankAccountService bankAccountService;
    @Autowired
    private UserService userService;

    private double balance;
    private String name;

    public ResponseEntity testTransactionTest(){
        String naam1 = "Hans";
        BankAccounTest(naam1);
        DepositTest(500);
        withdrawTest(100);

        return null;
    }

    public void BankAccounTest(String name){
        this.name = name;
        //balance = 0.0;
    }
    public double GetBalanceTest(){
        return balance;
    }
    public String GetNameTest(){
        return name;
    }
    public void DepositTest(double amount){
        balance += amount;
        System.out.println(name + " has $ " + balance + " after deposit");
    }
    public void withdrawTest(double amount){
        if (amount <= balance){
            balance -= amount;
            System.out.println(name + " has $ " + balance + "After withdraw!");
        }
        else{
            System.out.println("Withdraw by "+ name + " Fails!!");
        }
    }
    public void TransferTest(double amount1){
        if (this.balance < amount1){
            System.out.println("Transfer fails");
        }
        else{
            this.balance -= amount1;
            //acc.balance += amount1;
            System.out.println("Account of " + this.name + " becomes $ " + this.balance);
            //System.out.println("Account of " + acc.name + " becomes $ " + this.balance);
        }

    }
    public ResponseEntity MakeTransaction(Long userId) {
        User userById = userService.getUserById(userId);

        BankAccount Bank1 = bankAccountService.GetBankAccountByIban("NL26INHO07371341839");
        //Long userId =  Bank1.getUserId();
        User user = userService.getUserById(userId);
        String iban = String.valueOf(bankAccountService.GetBankAccountsByUserId(userId));

        // System.out.println(iban);

//        Double balanceSender = from.getBalance();
//        Double balanceReceiver = To.getBalance();
//
//
//        from.setBalance(balanceReceiver+100);
//        To.SetBalance(balanceReceiver+100);
        if(userById != null) {
            System.out.println(userById.getUsername());
            System.out.println(userById.getUserRole());
            return ResponseEntity.status(200).body("goedzo");
        }
        else {
            return ResponseEntity.status(404).body("Faya bro");
        }

    }



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

}
