package io.swagger.service;

import io.swagger.model.BankAccount;
import io.swagger.repository.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    //eventueel user service hier ook in auto wiren

    public ResponseEntity GetAllBankAccounts() {
        //test account:
        BankAccount newBankAccount = new BankAccount();
        newBankAccount.setUserId(1);
        newBankAccount.setAccountType(BankAccount.AccountTypeEnum.CURRENT);
        newBankAccount.setBalance(0.0);
        newBankAccount.setAbsoluteLimit(0.0);
        newBankAccount.setCreationDate("12-02-2022");
        newBankAccount.setIban("184kjdjanf");

        bankAccountRepository.save(newBankAccount);

        List<BankAccount> allBankAccounts;
        allBankAccounts = bankAccountRepository.findAll();

        if(bankAccountRepository.count() < 1) {
            return ResponseEntity.status(204).body(allBankAccounts);
        }
        else {
            return ResponseEntity.status(201).body(allBankAccounts);
        }

    }

    public ResponseEntity CreateNewBankAccount() {


        BankAccount newBankAccount = new BankAccount();
        newBankAccount.SetBalance(0.0);
        newBankAccount.absoluteLimit(0.0); //-	Balance cannot become lower than a certain number defined per account, referred to as absolute limit
        newBankAccount.SetAccountStatus(BankAccount.AccountStatusEnum.ACTIVE);
        newBankAccount.setIban(generateRandomIban());

        //!!create a check for if the user being connected to this bank account does not already have a current and savings account!!
        return ResponseEntity.status(400).body(newBankAccount);

    }

    public void SaveBankAccount(BankAccount bankAccount) {
        bankAccountRepository.save(bankAccount);
    }

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
        newBankAccount.setCreationDate(new Date());
        newBankAccount.setIban(generateRandomIban());

        //bankAccountRepository.save(account);

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
        int deleteId = 0;
        for (BankAccount bankAccount : allBankAccounts) {
            if(bankAccount.getIban() == iban){
                deleteId = bankAccount.getId();
                break;
            }
        }
        bankAccountRepository.deleteById(deleteId);
    }

}