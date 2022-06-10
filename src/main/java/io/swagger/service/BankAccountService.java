package io.swagger.service;

import io.swagger.model.BankAccount;
import io.swagger.model.entity.User;
import io.swagger.repository.BankAccountRepository;
import io.swagger.repository.UserRepository;
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
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


    public ResponseEntity PutBankAccountType(BankAccount.AccountTypeEnum type, BankAccount bankAccount) {
        if(bankAccount != null) {
            bankAccount.setAccountType(type);
            bankAccountRepository.save(bankAccount);
            return ResponseEntity.status(200).body(bankAccount);
        }
        else return ResponseEntity.status(400).body("bad request");
    }

    //melle
    public ResponseEntity GetBankAccountsByUserId(Long userId) {
        return ResponseEntity.status(200).body(bankAccountRepository.findByuserId(userId));
    }

    //melle
    public ResponseEntity PostOneSavingsAccountAndCurrentAccountForUser(Long userId) {
        //check if the userId has not already have a savings or current account
        User userById = userService.getUserById(userId);
        if(userById != null) {
            return ResponseEntity.status(200).body(CreateSavingsAndCurrentAccount(userId));
        }
        else {
            return ResponseEntity.status(404).body("No bank accounts found for this user id");
        }
    }

    //melle
    private List<BankAccount> CreateSavingsAndCurrentAccount(long userId) {
        List<BankAccount> newBankAccounts = new ArrayList<BankAccount>();

        for(int i = 0; i < 2; i++) {

            BankAccount newBankAccount = new BankAccount();
            newBankAccount.setIban(this.generateRandomIban());
            newBankAccount.setBalance(0.0);
            newBankAccount.setAbsoluteLimit(0.0);
            if(i == 0) newBankAccount.accountType(BankAccount.AccountTypeEnum.CURRENT);
            else if(i == 1) newBankAccount.accountType(BankAccount.AccountTypeEnum.SAVINGS);

            newBankAccount.userId(userId);
            bankAccountRepository.save(newBankAccount);
            newBankAccounts.add(newBankAccount);
        }

        return newBankAccounts;
    }

    //melle
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
            return ResponseEntity.status(404).body(allBankAccounts);
        }
        else {
            return ResponseEntity.status(201).body(allBankAccounts);
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

    public void CreateDummyDataBankAccount(Long userId, BankAccount.AccountTypeEnum type) {
        BankAccount newBankAccount = new BankAccount();
        newBankAccount.setIban(this.generateRandomIban());
        newBankAccount.setBalance(ThreadLocalRandom.current().nextDouble(300, 1800));
        newBankAccount.setAbsoluteLimit(0.0);
        newBankAccount.accountType(type);
        newBankAccount.userId(userId);

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
        newBankAccount.setBalance(0.0);
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
                deleteId = bankAccount.getUserId();
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
                account.setBalance(account.getBalance() + BigDecimal.valueOf(amount));
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