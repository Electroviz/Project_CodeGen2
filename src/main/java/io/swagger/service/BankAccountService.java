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
        //employee check toevoegen
        List<BankAccount> allBankAccounts;
        allBankAccounts = bankAccountRepository.findAll();

        if(bankAccountRepository.count() < 1) {
            return ResponseEntity.status(204).body(allBankAccounts);
        }
        else {
            return ResponseEntity.status(201).body(allBankAccounts);
        }

    }

    public ResponseEntity SetBankAccount(BankAccount body) {
        BankAccount account = new BankAccount();
        account.setUserId(GenerateID());
        account.setIban(GenerateIban());
        account.setBalance(BigDecimal.valueOf(0));
        account.setAbsoluteLimit(body.getAbsoluteLimit());
        account.setCreationDate(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
        account.setAccountType(body.getAccountType());

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


    public String GenerateIban(){
        //Bestaat de iban check toevoegen
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
        //Bestaat de id check toevoegen
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