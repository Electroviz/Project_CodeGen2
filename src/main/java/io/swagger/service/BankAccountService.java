package io.swagger.service;

import io.swagger.model.BankAccount;
import io.swagger.repository.BankAccountRepository;
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


        //bankAccountRepository.save(account);

        if(account.getAccountType() != BankAccount.AccountTypeEnum.CURRENT && account.getAccountType() != BankAccount.AccountTypeEnum.SAVINGS) {
            return ResponseEntity.status(400).body(account);
        }
        else {
            return ResponseEntity.status(201).body(account);
        }
    }

    public void DeleteBankAccount(String iban){

    }


    public String GenerateIban(){
        Random rand = new Random();
        String iban = "NL";
        for (int i = 0; i < 2; i++)
        {
            int n = rand.nextInt(10) + 0;
            iban += Integer.toString(n);
        }
        iban += "INHO0";
        for (int i = 0; i < 9; i++)
        {
            int n = rand.nextInt(10) + 0;
            iban += Integer.toString(n);
        }
        return iban;
    }

    public Integer GenerateID(){
        Random rand = new Random();
        String id = "";
        for (int i = 0; i < 8; i++)
        {
            int n = rand.nextInt(10) + 0;
            id += Integer.toString(n);
        }
        return Integer.parseInt(id);
    }
}