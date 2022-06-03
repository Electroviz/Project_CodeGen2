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
        newBankAccount.setBalance(0.0);
        newBankAccount.setAbsoluteLimit(0.0);
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

    public ResponseEntity CreateNewBankAccount() {
        BankAccount newBankAccount = new BankAccount();
        newBankAccount.SetBalance(0.0);
        newBankAccount.absoluteLimit(0.0); //-	Balance cannot become lower than a certain number defined per account, referred to as absolute limit
        newBankAccount.SetAccountStatus(BankAccount.AccountStatusEnum.ACTIVE);
        newBankAccount.setIban(generateRandomIban());

        //set a check for if the user being connected to this bank account does not already have a current and savings account
        return ResponseEntity.status(400).body(newBankAccount);

    }

    public void SaveBankAccount(BankAccount bankAccount) {
        bankAccountRepository.save(bankAccount);
    }

    private String generateRandomIban() {
        boolean succes = true;
        String newIban = "";
        List<BankAccount> allBankAccounts = (List<BankAccount>) GetAllBankAccounts();
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