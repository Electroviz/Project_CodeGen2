package io.swagger.service;

import io.swagger.api.ApiException;
import io.swagger.model.BankAccount;
import io.swagger.model.Transaction;
import io.swagger.model.User;
import io.swagger.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;

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





        //Things to implement!!!!
        //Get the from user (the sender)
        //Check transaction limit of the user
        //Get the sender and receiver bank iban
        //Check the new balance is not less than the absolut limit
        //The amount of the transaction should not exceed the day limit
        //Compute the total day transactions if this transaction were to go through

        //Update the senders bank account
        //UPdate the receivers bank account
        //Save the transaction


    }

}
