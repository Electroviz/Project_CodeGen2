package io.swagger.service;

import io.swagger.model.BankAccount;
import io.swagger.model.TransactionInfo;
import io.swagger.model.entity.User;
import io.swagger.repository.BankAccountRepository;
import io.swagger.repository.UserRepository;
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

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

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
        List<BankAccount> allBankAccounts = bankAccountRepository.findAll();

        if(bankAccountRepository.count() == 0) {
            return ResponseEntity.status(404).body(allBankAccounts);
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


        //bankAccountRepository.save(account);

        if(account.getAccountType() != BankAccount.AccountTypeEnum.CURRENT && account.getAccountType() != BankAccount.AccountTypeEnum.SAVINGS) {
            return ResponseEntity.status(400).body(account);
        }
        else {
            return ResponseEntity.status(201).body(account);
        }
    }

    //Nicky
    public void DeleteBankAccount(String iban){
        List<BankAccount> allBankAccounts;
        allBankAccounts = bankAccountRepository.findAll();
        boolean canDel = false;
        Long deleteId = Long.valueOf(0);
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

    //Nicky
    public TransactionInfo AccountDeposit(String iban, Double amount){
        List<BankAccount> allBankAccounts;
        allBankAccounts = bankAccountRepository.findAll();
        TransactionInfo transactionInfo = new TransactionInfo();
        BankAccount depositAccount = null;

        for (BankAccount account : allBankAccounts) {
            if(iban == account.getIban()){
                //account.setBalance(account.getBalance() + BigDecimal.valueOf(amount));
                transactionInfo.setAmount(BigDecimal.valueOf(amount));
                //transactionInfo.setTimestamp(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
                break;
            }
        }
        //ingelogde userid
        //transactionInfo.setUserIDPerforming();

        return transactionInfo;
    }

    //Nicky
    public List<String> getAccountByName(String fullname){
        List<String> returnIbans = null;
        List<BankAccount> allBankAccounts = bankAccountRepository.findAll();
        List<User> allUsers = userRepository.findAll();
        Long userCompareId = null;
        //-fullname = fullname.replaceAll("%20", " ");

        for (User user : allUsers)
        {
            if(user.getFullname() == fullname)
            {
                userCompareId = Long.valueOf(user.getId());
                break;
            }
        }
        for (BankAccount account : allBankAccounts)
        {
            if(account.getUserId() == userCompareId)
            {
                returnIbans.add(account.getIban());
            }
        }
        return returnIbans;
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