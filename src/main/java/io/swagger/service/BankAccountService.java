package io.swagger.service;

import io.swagger.model.BankAccount;
import io.swagger.model.Transaction;
import io.swagger.model.TransactionInfo;
import io.swagger.model.entity.TransactionTest;
import io.swagger.model.entity.User;
import io.swagger.repository.BankAccountRepository;
import io.swagger.repository.TransactionRepository;
import io.swagger.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.threeten.bp.OffsetDateTime;

import java.math.BigDecimal;
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

    @Autowired
    private TransactionRepository transactionRepository;


    public ResponseEntity PutBankAccountType(BankAccount.AccountTypeEnum type, BankAccount bankAccount) {
        if(bankAccount != null) {
            bankAccount.setAccountType(type);
            bankAccountRepository.save(bankAccount);
            return ResponseEntity.status(200).body(bankAccount);
        }
        else return ResponseEntity.status(400).body("bad request");
    }

    //melle
    public ResponseEntity GetTotalBalanceByUserId(Long userId) {
        List<BankAccount> bankAccounts = this.GetBankAccountsByUserId(userId);
        double totalAmount = 0;
        for(int i = 0; i < bankAccounts.size(); i++) totalAmount += bankAccounts.get(i).getBalance();

        if(bankAccounts.stream().count() < 2) return ResponseEntity.status(400).body(null);
        else return ResponseEntity.status(200).body(totalAmount);
    }

    //melle
    public List<BankAccount> GetBankAccountsByUserId(Long userId) {
        List<BankAccount> bankAccounts = bankAccountRepository.findByuserId(userId);

        return bankAccounts;
    }

    //melle
    public ResponseEntity PostOneSavingsAccountAndCurrentAccountForUser(Long userId) {
        //check if the userId has not already have a savings or current account
        User userById = userService.getUserById(userId);
        if(userById != null) {
            if(UserAlreadyHasBankAccounts(userId) == false)
                return ResponseEntity.status(200).body(CreateSavingsAndCurrentAccount(userId));
            else
                return ResponseEntity.status(400).body("user already has bank accounts");
        }
        else {
            return ResponseEntity.status(404).body("No user account found for this user id");
        }
    }

    public boolean UserAlreadyHasBankAccounts(Long userId) {
        List<BankAccount> bankAccounts = bankAccountRepository.findByuserId(userId);
        if(bankAccounts != null && bankAccounts.stream().count() > 1) return true;
        else return false;
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
    public boolean BankAccountIsSavings() {
        return false;
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
    public BankAccount DeleteBankAccount(String iban){
        List<BankAccount> allBankAccounts;
        allBankAccounts = bankAccountRepository.findAll();
        BankAccount deletedAccount = null;
        boolean canDel = false;
        Long deleteId = Long.valueOf(0);
        for (BankAccount bankAccount : allBankAccounts) {
            if(bankAccount.getIban().equals(iban)){
                deleteId = bankAccount.getId();
                canDel = true;
                deletedAccount = bankAccount;
                break;
            }
        }
        if (canDel)
        {
            bankAccountRepository.deleteById(deleteId);
        }
        return deletedAccount;
    }

    //Nicky
    public TransactionInfo AccountDeposit(String iban, Double amount){
        TransactionInfo transactionInfo = new TransactionInfo();
        Transaction depositTrans = new Transaction(); //make object with transaction entity

        System.out.println(GetBankAccountByIban(iban).getAccountType());
        if(GetBankAccountByIban(iban) != null && GetBankAccountByIban(iban).getAccountType() == BankAccount.AccountTypeEnum.CURRENT){
            BankAccount account = GetBankAccountByIban(iban);
            //if(amount < user.getTransactionLimit() && amount < user.getDayLimit()){
            if(amount > 0){
                account.setBalance(account.getBalance() + amount);
            }
            else if(amount == 0.0){
                return transactionInfo = null;
            }
            else {
                amount = amount * -1;
                account.setBalance(account.getBalance() + amount);
            }
            //}

            //maak nieuwe transaction aan
            depositTrans.setAmount(amount);
            depositTrans.setDescription("Deposit");
            depositTrans.setTimestamp(OffsetDateTime.now());
            depositTrans.setFrom("NL01INHO0000000001");
            depositTrans.setTo(iban);
            depositTrans.setUserIDPerforming(Math.toIntExact(Long.valueOf(account.getUserId())));

            //sla de transaction op en update het account
            transactionRepository.save(depositTrans);
            bankAccountRepository.save(account);

            //maak het transactioninfo object aan
            transactionInfo.setAmount(BigDecimal.valueOf(amount));
            transactionInfo.setTimestamp(OffsetDateTime.now());
            transactionInfo.setUserIDPerforming(Math.toIntExact(Long.valueOf(account.getUserId())));
        }
        else{
            return transactionInfo = null;
        }

        return transactionInfo;
    }

    //Nicky
    public TransactionInfo AccountWithdraw(String iban, Double amount){
        TransactionInfo transactionInfo = new TransactionInfo();
        Transaction withdrawTrans = new Transaction();

        if(GetBankAccountByIban(iban) != null && GetBankAccountByIban(iban).getAccountType() == BankAccount.AccountTypeEnum.CURRENT){
            BankAccount account = GetBankAccountByIban(iban);
            //if(amount < user.getTransactionLimit() && amount < user.getDayLimit() && (account.getBalance() - amount)  > account.getAbsoluteLimit()){
            if((account.getBalance() - amount) >= account.getAbsoluteLimit()) {
                if (amount > 0) {
                    account.setBalance(account.getBalance() - amount);
                } else if (amount == 0) {
                    return transactionInfo = null;
                } else {
                    amount = amount * -1;
                    account.setBalance(account.getBalance() - amount);
                }
            }
            else{
                return transactionInfo = null;
            }
            //}

            withdrawTrans.setAmount(amount);
            withdrawTrans.setDescription("Withdraw");
            withdrawTrans.setTimestamp(OffsetDateTime.now());
            withdrawTrans.setTo("NL01INHO0000000001");
            withdrawTrans.setFrom(iban);
            withdrawTrans.setUserIDPerforming(Math.toIntExact(Long.valueOf(account.getUserId())));

            //sla de transaction op en update het account
            transactionRepository.save(withdrawTrans);
            bankAccountRepository.save(account);

            //maak het transactioninfo object aan
            transactionInfo.setAmount(BigDecimal.valueOf(amount));
            transactionInfo.setTimestamp(OffsetDateTime.now());
            transactionInfo.setUserIDPerforming(Math.toIntExact(Long.valueOf(account.getUserId())));
        }
        else{
            return transactionInfo = null;
        }

        return transactionInfo;
    }

    //Nicky
    public List<String> getAccountByName(String fullname){
        List<String> returnIbans = new ArrayList<>();
        List<BankAccount> allBankAccounts = bankAccountRepository.findAll();
        List<User> allUsers = userRepository.findAll();
        Long userCompareId = null;

        for (User user : allUsers)
        {
            if(user.getFullname().equals(fullname))
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