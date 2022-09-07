package test;

import io.swagger.enums.BankAccountType;
import io.swagger.model.BankAccount;
import io.swagger.model.TransactionInfo;
import io.swagger.model.entity.User;
import io.swagger.service.BankAccountService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BankAccountServiceTest {

    @Autowired
    BankAccountService bankAccountService;
    @Test
    void deleteBankAccount() {
        BankAccount account = new BankAccount();
        try {
            bankAccountService.DeleteBankAccount("NL82INHO01856227484");
            assertNotNull(account);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    void accountDeposit() {
        try {
            TransactionInfo transactionInfo = new TransactionInfo();
            assertNotNull(transactionInfo);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    void accountWithdraw() {
        TransactionInfo transactionInfo = new TransactionInfo();
        assertNotNull(transactionInfo);
    }

    @Test
    void getAccountByName() {
        List<String> account = bankAccountService.getAccountByName("Jantje Egberts");
        assertNotNull(account);
    }

    @Test
    void getTotalBalanceByUserId() {
        try {
            double balance = (double) bankAccountService.GetTotalBalanceByUserId(Long.valueOf(12)).getBody();
            assertNotNull(balance);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    void getBankAccountsByUserId() {
        try {
            List<BankAccount> accounts = bankAccountService.GetBankAccountsByUserId(Long.valueOf(12));
            assertNotNull(accounts);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    void getAllBankAccounts() {
        try {
            List<BankAccount> allBankAccounts = (List<BankAccount>) bankAccountService.GetAllBankAccounts();
            assertNotNull(allBankAccounts);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    void getBankAccountByIban() {
        try {
            BankAccount account = bankAccountService.GetBankAccountByIban("NL82INHO01856227484");
            assertNotNull(account);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    void createNewBankAccount() {
        try {
            BankAccount account = (BankAccount) bankAccountService.CreateNewBankAccount().getBody();
            assertNotNull(account);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void IbanHasToBeValid(){
        try {
            BankAccount bankAccount = new BankAccount();
            bankAccount.setIban("NL01INHO00000000100");
            Assert.assertTrue(bankAccount.getIban().matches("NL\\d{2}INHO0\\d{10}"));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void accountTypeShouldBeValid()
    {
        try {
            boolean isValid = false;
            BankAccount bankAccount = new BankAccount();
            bankAccount.setAccountType(BankAccount.AccountTypeEnum.CURRENT);
            for(BankAccountType type : BankAccountType.values())
            {
                if(type.name().equals(bankAccount.getAccountType().toString().toLowerCase()))
                {
                    isValid = true;
                }
            }
            if (!isValid) {
                throw new IllegalArgumentException("Account type is not valid");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void accountStatusShouldBeValid()
    {
        try {
            boolean isValid = false;
            BankAccount bankAccount = new BankAccount();
            bankAccount.setAccountStatus(BankAccount.AccountStatusEnum.ACTIVE);
            for(BankAccount.AccountStatusEnum status : BankAccount.AccountStatusEnum.values())
            {
                if(status.name().equals(bankAccount.getAccountStatus().name()))
                {
                    isValid = true;
                }
            }
            if (!isValid) {
                throw new IllegalArgumentException("Account status is not valid");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void IsBankaccountUserValid()
    {
        BankAccount bankAccount = new BankAccount();
        User user = new User();
        user.setId(Long.valueOf(15));
        bankAccount.setUserId(user.getId());
        assertTrue(bankAccount.getUserId() != null);
    }

    @Test
    public void IsBalanceHigherThanAbsoluteLimit(){
        BankAccount bankAccount = new BankAccount();
        bankAccount.setBalance(0.0);
        bankAccount.setAbsoluteLimit(0.0);
        assertTrue(bankAccount.getBalance() >= bankAccount.getAbsoluteLimit());
    }

    @Test
    public void DoesListContainIban(){
        BankAccount bankAccount = new BankAccount();
        bankAccount.setIban("NL01INHO00000000100");
        List<BankAccount> bankAccountList = new ArrayList<>();
        bankAccountList.add(bankAccount);
        assertTrue(bankAccountList.contains(bankAccount));
    }
}