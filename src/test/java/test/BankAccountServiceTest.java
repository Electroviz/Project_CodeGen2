package test;

import io.swagger.Swagger2SpringBoot;
import io.swagger.enums.BankAccountType;
import io.swagger.model.BankAccount;
import io.swagger.model.TransactionInfo;
import io.swagger.model.entity.User;
import io.swagger.service.BankAccountService;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;



@SpringBootTest(classes= Swagger2SpringBoot.class)
class BankAccountServiceTest {

    @Autowired
    BankAccountService bankAccountService;

    @BeforeEach
    public void setup(){
        BankAccount ba = new BankAccount();
        ba.setBalance(200.0);
        ba.setIban("NL82INHO01856227888");
        ba.setUserId(1111);
        ba.setAccountStatus(BankAccount.AccountStatusEnum.ACTIVE);
        ba.setAccountType(BankAccount.AccountTypeEnum.CURRENT);
        ba.setAbsoluteLimit(100.0);

        BankAccount ba2 = new BankAccount();
        ba2.setBalance(200.0);
        ba2.setIban("NL82INHO01856227889");
        ba2.setUserId(1111);
        ba2.setAccountStatus(BankAccount.AccountStatusEnum.ACTIVE);
        ba2.setAccountType(BankAccount.AccountTypeEnum.SAVINGS);
        ba2.setAbsoluteLimit(100.0);

        bankAccountService.SaveBankAccount(ba2);
        bankAccountService.SaveBankAccount(ba);
    }

    @Test
    public void testTest() {

        List<BankAccount> allBa = bankAccountService.GetAllBankAccountsList();

        Assertions.assertTrue(allBa.size() > 0);
        Assertions.assertFalse(allBa.size() == 0);
    }

    @Test
    public void deleteBankAccount() {
        bankAccountService.DeleteBankAccount("NL82INHO01856227888");

        Assertions.assertNull(bankAccountService.GetBankAccountByIban("NL82INHO01856227888"));
    }

    @Test
    public void getAccountByName() {
        List<String> accounts = bankAccountService.getAccountByName("Jantje Egberts");
        assertNotNull(accounts);
    }

    @Test
    public void getTotalBalanceByUserId() {
        Double balance = (Double) bankAccountService.GetTotalBalanceByUserId(Long.valueOf(1111)).getBody();
        assertNotNull(balance);
    }

    @Test
    public void getBankAccountsByUserId() {
        List<BankAccount> accounts = bankAccountService.GetBankAccountsByUserId(Long.valueOf(1111));
        assertNotNull(accounts);
    }

    @Test
    public void getAllBankAccounts() {
        List<BankAccount> allBankAccounts = (List<BankAccount>) bankAccountService.GetAllBankAccounts().getBody();
        assertNotNull(allBankAccounts);
    }

    @Test
    public void getBankAccountByIban() {
        BankAccount account = bankAccountService.GetBankAccountByIban("NL82INHO01856227888");
        assertNotNull(account);
    }

    @Test
    public void IbanHasToBeValid(){
        BankAccount bankAccount = new BankAccount();
        bankAccount.setIban(bankAccountService.GenerateIban());
        Assert.assertTrue(bankAccount.getIban().matches("NL\\d{2}INHO0\\d{9}"));
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
        assertTrue(isValid);
    }

    @Test
    public void accountStatusShouldBeValid()
    {
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
    public void ChangeAccountType(){
        BankAccount account = bankAccountService.GetBankAccountByIban("NL82INHO01856227889");
        bankAccountService.PutBankAccountType(BankAccount.AccountTypeEnum.CURRENT, account);
        assertTrue(bankAccountService.GetBankAccountByIban("NL82INHO01856227889").getAccountType() == BankAccount.AccountTypeEnum.CURRENT);
    }

    @Test
    public void ChangeAccountStatus(){
        BankAccount account = bankAccountService.GetBankAccountByIban("NL82INHO01856227889");
        bankAccountService.PutBankAccountStatus(BankAccount.AccountStatusEnum.INACTIVE, account);
        assertTrue(bankAccountService.GetBankAccountByIban("NL82INHO01856227889").getAccountStatus() == BankAccount.AccountStatusEnum.INACTIVE);
    }

    @Test
    public void ChangeAbsoluteLimit(){
        BankAccount account = bankAccountService.GetBankAccountByIban("NL82INHO01856227889");
        bankAccountService.PutBankAccountAbsoluteLimit(100.0, account);
        assertTrue(bankAccountService.GetBankAccountByIban("NL82INHO01856227889").getAbsoluteLimit() == 100.0);
    }

    @Test
    public void TestNegativeInputAbsoluteLimit(){
        BankAccount account = bankAccountService.GetBankAccountByIban("NL82INHO01856227889");
        double oldLimit = account.getAbsoluteLimit();
        bankAccountService.PutBankAccountAbsoluteLimit(-100.0, account);
        assertTrue(bankAccountService.GetBankAccountByIban("NL82INHO01856227889").getAbsoluteLimit() == oldLimit);
    }

    @Test
    public void CheckIfUserHasBankAccounts(){
        assertTrue(bankAccountService.UserAlreadyHasBankAccounts(1111L));
    }

    @Test
    public void CheckIfIbanBelongsToUser(){
        assertTrue(bankAccountService.CheckIbanBelongsToUser(1111, "NL82INHO01856227889"));
    }

    @Test
    public void CheckIfAccountIsValidForTransaction(){
        assertTrue(bankAccountService.BankAccountIsValidForTransactions("NL82INHO01856227888"));
    }

    @Test
    public void CheckIfAccountIsNotValidForTransaction(){
        assertFalse(bankAccountService.BankAccountIsValidForTransactions("NL82INHO01856227889"));
    }
}