package io.swagger.service;

import io.swagger.model.BankAccount;
import io.swagger.model.TransactionInfo;
import io.swagger.model.User;
import io.swagger.repository.BankAccountRepository;
import io.swagger.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class BankAccountService {
    @Autowired
    private BankAccountRepository bankAccountRepository;
    private UserRepository userRepository;

    //eventueel user service hier ook in auto wiren

    public ResponseEntity GetAllBankAccounts() {
        //test account:
        BankAccount newBankAccount = new BankAccount();
        newBankAccount.setUserId(1);
        newBankAccount.setAccountType(BankAccount.AccountTypeEnum.CURRENT);
        newBankAccount.setBalance(BigDecimal.valueOf(0));
        newBankAccount.setAbsoluteLimit(BigDecimal.valueOf(0));
        newBankAccount.setCreationDate("12-02-2022");
        newBankAccount.setIban("184kjdjanf");

        bankAccountRepository.save(newBankAccount);

        List<BankAccount> allBankAccounts;
        allBankAccounts = bankAccountRepository.findAll();



        if(bankAccountRepository.count() == 0) {
            return ResponseEntity.status(400).body(allBankAccounts);
        }
        else {
            return ResponseEntity.status(201).body(allBankAccounts);
        }

    }

    public ResponseEntity SetBankAccount(BankAccount account) {
        BankAccount newBankAccount = new BankAccount();
        newBankAccount.SetBalance(0.0);
        newBankAccount.absoluteLimit(0.0); //-	Balance cannot become lower than a certain number defined per account, referred to as absolute limit
        newBankAccount.SetAccountStatus(BankAccount.AccountStatusEnum.ACTIVE);
        newBankAccount.setCreationDate(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
        newBankAccount.setIban(generateRandomIban());


        if(account.getAccountType() != BankAccount.AccountTypeEnum.CURRENT && account.getAccountType() != BankAccount.AccountTypeEnum.SAVINGS) {
            return ResponseEntity.status(400).body(account);
        }
        try {
            bankAccountRepository.save(account);
            return ResponseEntity.status(HttpStatus.CREATED).body(account);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(account);
        }
    }

    //Nicky
    public void DeleteBankAccount(String iban){
        List<BankAccount> allBankAccounts;
        allBankAccounts = bankAccountRepository.findAll();
        boolean canDel = false;
        int deleteId = 0;
        for (BankAccount bankAccount : allBankAccounts) {
            if(bankAccount.getIban() == iban){
                deleteId = bankAccount.getId();
                canDel = true;
                break;
            }
        }
        if (canDel)
        {
            bankAccountRepository.deleteById(deleteId);
        }
    }

    public TransactionInfo AccountDeposit(String iban, Double amount){
        List<BankAccount> allBankAccounts;
        allBankAccounts = bankAccountRepository.findAll();
        TransactionInfo transactionInfo = new TransactionInfo();
        BankAccount depositAccount = null;

        for (BankAccount account : allBankAccounts) {
            if(iban == account.getIban()){
                account.setBalance(account.getBalance() + amount);
                transactionInfo.setAmount(BigDecimal.valueOf(amount));
                transactionInfo.setTimestamp(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
                break;
            }
        }
        //ingelogde userid
        //transactionInfo.setUserIDPerforming();

        return transactionInfo;
    }

}