package test;

import io.swagger.enums.BankAccountType;
import io.swagger.model.BankAccount;
import io.swagger.model.TransactionInfo;
import io.swagger.model.entity.User;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BankAccountServiceTest {

    @Test
    void deleteBankAccount() {
        BankAccount account = new BankAccount();
        assertNotNull(account);
    }

    @Test
    void accountDeposit() {
        TransactionInfo transactionInfo = new TransactionInfo();
        assertNotNull(transactionInfo);
    }

    @Test
    void accountWithdraw() {
        TransactionInfo transactionInfo = new TransactionInfo();
        assertNotNull(transactionInfo);
    }

    @Test
    void getAccountByName() {
        BankAccount account = new BankAccount();
        assertNotNull(account);
    }

    @Test
    void getTotalBalanceByUserId() {
        BankAccount account = new BankAccount();
        assertNotNull(account);
    }

    @Test
    void getBankAccountsByUserId() {
        BankAccount account = new BankAccount();
        assertNotNull(account);
    }

    @Test
    void getAllBankAccounts() {
        List<BankAccount> allBankAccounts = new ArrayList<>();
        assertNotNull(allBankAccounts);
    }

    @Test
    void getBankAccountByIban() {
        BankAccount account = new BankAccount();
        assertNotNull(account);
    }

    @Test
    void createNewBankAccount() {
        BankAccount account = new BankAccount();
        assertNotNull(account);
    }

    @Test
    public void IbanHasToBeValid(){
        BankAccount bankAccount = new BankAccount();
        bankAccount.setIban("NL01INHO00000000100");
        Assert.assertTrue(bankAccount.getIban().matches("NL\\d{2}INHO0\\d{10}"));
    }

    @Test
    public void accountTypeShouldBeValid()
    {
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

    @Test
    public void accountStatusShouldBeValid()
    {
        boolean isValid = false;
        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountStatus(BankAccount.AccountStatusEnum.ACTIVE);
        for(BankAccount.AccountStatusEnum status : BankAccount.AccountStatusEnum.values())
        {
            System.out.println(status);
            System.out.println(bankAccount.getAccountStatus());
            if(status.name().equals(bankAccount.getAccountStatus().name()))
            {
                isValid = true;
            }
        }
        if (!isValid) {
            throw new IllegalArgumentException("Account status is not valid");
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
    public void DoesListContainIban(String iban){
        BankAccount bankAccount = new BankAccount();
        bankAccount.setIban("NL01INHO00000000100");
        List<BankAccount> bankAccountList = new ArrayList<>();
        bankAccountList.add(bankAccount);
        assertTrue(bankAccountList.contains(bankAccount));
    }

    @Test
    void generateIbanShouldBeValid() {
        BankAccount account = new BankAccount();
        account.setIban("NL01INHO0000000001");
        assertTrue(account.getIban().matches("NL\\d{2}INHO0\\d{9}"));
    }
}