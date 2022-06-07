package io.swagger.service;

import io.swagger.model.BankAccount;
import io.swagger.repository.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class BankAccountService {
    @Autowired
    private BankAccountRepository bankAccountRepository;


    public ResponseEntity PutBankAccountType(BankAccount.AccountTypeEnum type, BankAccount bankAccount) {
        if(bankAccount != null) {
            bankAccount.setAccountType(type);
            bankAccountRepository.save(bankAccount);
            return ResponseEntity.status(200).body(bankAccount);
        }
        else return ResponseEntity.status(400).body("bad request");
    }

    //melle
    public ResponseEntity GetAllBankAccounts() {
        List<BankAccount> allBankAccounts = bankAccountRepository.findAll();

        if(bankAccountRepository.count() == 0) {
            return ResponseEntity.status(400).body(allBankAccounts);
        }
        else {
            return new ResponseEntity<List<BankAccount>>(allBankAccounts,HttpStatus.ACCEPTED);
        }
    }

    //Melle
    public BankAccount GetBankAccountByIban(String iban) {
        iban = iban.replaceAll("[{}]",""); //make sure that the {variable} quotes are not taking into consideration
        //if(iban != null) return new ResponseEntity<String>(String.valueOf(bankAccountRepository.findAll().stream().count()),HttpStatus.FOUND);
        BankAccount correctBankAccount = null;
        for (BankAccount ba : this.bankAccountRepository.findAll()) {
            if (Objects.equals(ba.getIban(), iban)) {
                return ba;
            }
        }

        return null;
    }

    //melle
    public void CreateDummyDataBankAccount() {
        BankAccount newBankAccount = new BankAccount();
        newBankAccount.setIban(this.generateRandomIban());
        newBankAccount.setBalance(ThreadLocalRandom.current().nextDouble(300, 1800));
        newBankAccount.setAbsoluteLimit(0.0);
        newBankAccount.accountType(BankAccount.AccountTypeEnum.CURRENT);
        newBankAccount.userId(ThreadLocalRandom.current().nextInt(0, 100000));

        bankAccountRepository.save(newBankAccount);
    }

    //melle
    public ResponseEntity CreateNewBankAccount() {
        BankAccount newBankAccount = new BankAccount();
        newBankAccount.SetBalance(0.0);
        newBankAccount.absoluteLimit(0.0); //-	Balance cannot become lower than a certain number defined per account, referred to as absolute limit
        newBankAccount.SetAccountStatus(BankAccount.AccountStatusEnum.ACTIVE);
        newBankAccount.setIban(generateRandomIban());

        //!!create a check for if the user being connected to this bank account does not already have a current and savings account!!
        return ResponseEntity.status(400).body(newBankAccount);
    }

    public ResponseEntity getAccountByName(String fullname){
        BankAccount accountToReturn = null;
        List<BankAccount> allBankAccounts;
        allBankAccounts = bankAccountRepository.findAll();
        Integer userCompareId = null;

        //1. pak alle users
        //2. check per user of de gegeven naam overeen komt met user.fullname
        //3. zoja pak het user object en de userId
        //4. check nu of de userId overeenkomt met een userid in een van de bankaccounts
        //5. zoja return de iban's van die accounts.


        //userCompareId = accountCheck.getUserId();
    }

    public void SaveBankAccount(BankAccount bankAccount) {
        bankAccountRepository.save(bankAccount);
    }

    //melle
    private String generateRandomIban() {
        boolean succes = true;
        String newIban = "";
        List<BankAccount> allBankAccounts = bankAccountRepository.findAll();
        do {
            succes = true;
            newIban = this.IbanStringGenerator();
            for (int i = 0; i < allBankAccounts.size(); i++) {
                BankAccount bankAccount = allBankAccounts.get(i);
                String ibanToCheck = bankAccount.GetIBAN();
                if (newIban == ibanToCheck) {
                    succes = false;
                }
            }
        } while(succes == false);

        return newIban;
    }

    private String IbanStringGenerator() {
        Random random = new Random();
        String IBAN = "NL";
        int index = random.nextInt(10);
        IBAN += Integer.toString(index);
        index = random.nextInt(10);
        IBAN += Integer.toString(index);
        IBAN += "INHO0";
        for (int i = 0; i < 10; i++) {
            int n = random.nextInt(10);
            IBAN += Integer.toString(n);
        }

        return IBAN;
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

}