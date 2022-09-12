package test;

import io.swagger.Swagger2SpringBoot;
import io.swagger.enums.BankAccountType;
import io.swagger.model.BankAccount;
import io.swagger.model.TransactionInfo;
import io.swagger.model.entity.User;
import io.swagger.service.BankAccountService;
import io.swagger.service.TransactionService;
import jdk.jshell.JShell;
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
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;



@SpringBootTest(classes= Swagger2SpringBoot.class)
public class TransactionServiceTest {

    @Autowired
    TransactionService transactionService;

    @Autowired
    BankAccountService bankAccountService;

    public List<BankAccount> bankAccountsList = new ArrayList<>();


    @BeforeEach
    public void Setup() {
        //create some fake bankAccounts with balance

        BankAccount ba1 = new BankAccount();
        BankAccount ba2 = new BankAccount();

        ba1.setAbsoluteLimit(0.0);
        ba1.setBalance(300.0);
        ba1.setIban(bankAccountService.GenerateIban());
        ba1.setUserId(1);
        ba1.setAccountType(BankAccount.AccountTypeEnum.CURRENT);
        ba1.setAccountStatus(BankAccount.AccountStatusEnum.ACTIVE);


        ba2.setUserId(2);
        ba2.setBalance(500.0);
        ba2.setIban(bankAccountService.GenerateIban());
        ba2.setAbsoluteLimit(ba1.getAbsoluteLimit());
        ba2.setAccountStatus(ba1.getAccountStatus());
        ba2.setAccountType(ba1.getAccountType());

        bankAccountsList.add(ba1);
        bankAccountsList.add(ba2);

        bankAccountService.SaveBankAccount(ba1);
        bankAccountService.SaveBankAccount(ba2);

    }

    //START TRANSFER MONEYS TESTS
    //MELLE:

    @Test
    public void TransferMoneyFromIbanToIbanTest() {
        //should be possible
        boolean result = transactionService.TransferMoneyFromToIban(bankAccountsList.get(0).getIban(),bankAccountsList.get(1).getIban(), 10.0,bankAccountsList.get(0).getUserId().intValue());

        Assertions.assertTrue(result == true);
        Assertions.assertFalse(result == false);
    }

    @Test
    public void TransferMoneyNegativeAmountNotPossibleTest() {
        //should not be possible
        boolean result = transactionService.TransferMoneyFromToIban(bankAccountsList.get(0).getIban(),bankAccountsList.get(1).getIban(), -100.0,bankAccountsList.get(0).getUserId().intValue());


        Assertions.assertTrue(result == false);
        Assertions.assertFalse(result == true);
    }

    @Test
    public void TransferMoneySameIbanNotPossibleTest() {
        //should not be possible
        boolean result = transactionService.TransferMoneyFromToIban(bankAccountsList.get(0).getIban(),bankAccountsList.get(0).getIban(), 100.0,bankAccountsList.get(0).getUserId().intValue());

        Assertions.assertTrue(result == false);
        Assertions.assertFalse(result == true);
    }

    @Test
    public void TransferToMuchMoneyThenActiveBalanceTest() {
        //should not be possible
        boolean result = transactionService.TransferMoneyFromToIban(bankAccountsList.get(0).getIban(),bankAccountsList.get(1).getIban(), (bankAccountsList.get(1).getBalance() + 50.0),bankAccountsList.get(0).getUserId().intValue());

        Assertions.assertTrue(result == false);
        Assertions.assertFalse(result == true);
    }

    @Test
    public void TransferMoneyFromClosedBankAccountTest() {
        //should not be possible
        BankAccount ba = new BankAccount();

        ba.setAbsoluteLimit(1000.0);
        ba.setBalance(300.0);
        ba.setIban(bankAccountService.GenerateIban());
        ba.setUserId(3);
        ba.setAccountType(BankAccount.AccountTypeEnum.CURRENT);
        ba.setAccountStatus(BankAccount.AccountStatusEnum.CLOSED);

        bankAccountService.SaveBankAccount(ba);

        boolean result = transactionService.TransferMoneyFromToIban(ba.getIban(),bankAccountsList.get(1).getIban(), 100.0,ba.getUserId().intValue());

        Assertions.assertTrue(result == false);
        Assertions.assertFalse(result == true);
    }

    @Test
    public void TransferMoneyToClosedBankAccountTest() {
        //should not be possible
        BankAccount ba = new BankAccount();

        ba.setAbsoluteLimit(1000.0);
        ba.setBalance(300.0);
        ba.setIban(bankAccountService.GenerateIban());
        ba.setUserId(3);
        ba.setAccountType(BankAccount.AccountTypeEnum.CURRENT);
        ba.setAccountStatus(BankAccount.AccountStatusEnum.CLOSED);

        bankAccountService.SaveBankAccount(ba);

        boolean result = transactionService.TransferMoneyFromToIban(bankAccountsList.get(0).getIban(),ba.getIban(), 100.0,bankAccountsList.get(0).getUserId().intValue());

        Assertions.assertTrue(result == false);
        Assertions.assertFalse(result == true);
    }

    @Test
    public void TransferMoneyToNonExistingIbanTest() {
        //should not be possible
        boolean result = transactionService.TransferMoneyFromToIban(bankAccountsList.get(0).getIban(),bankAccountService.GenerateIban(), (bankAccountsList.get(0).getBalance() + 50.0),bankAccountsList.get(0).getUserId().intValue());

        Assertions.assertTrue(result == false);
        Assertions.assertFalse(result == true);
    }

    //END TRANSFER MONEYS TESTS

    //START DEPOSIT AND WITHDRAW TESTS

    @Test
    public void WithdrawToMuchMoneyTest() {
        //should not be possible
        BankAccount ba = new BankAccount();

        ba.setAbsoluteLimit(1000.0);
        ba.setBalance(300.0);
        ba.setIban(bankAccountService.GenerateIban());
        ba.setUserId(3);
        ba.setAccountType(BankAccount.AccountTypeEnum.CURRENT);
        ba.setAccountStatus(BankAccount.AccountStatusEnum.ACTIVE);

        bankAccountService.SaveBankAccount(ba);

        boolean result = transactionService.WithdrawOrDepositMoney(ba.getIban(),300.05,true,ba.getUserId().intValue());

        Assertions.assertTrue(result == false);
        Assertions.assertFalse(result == true);
    }

    @Test
    public void WithdrawExactBalance() {
        //should be possible
        BankAccount ba = new BankAccount();

        ba.setAbsoluteLimit(1000.0);
        ba.setBalance(300.0);
        ba.setIban(bankAccountService.GenerateIban());
        ba.setUserId(3);
        ba.setAccountType(BankAccount.AccountTypeEnum.CURRENT);
        ba.setAccountStatus(BankAccount.AccountStatusEnum.ACTIVE);

        bankAccountService.SaveBankAccount(ba);

        boolean result = transactionService.WithdrawOrDepositMoney(ba.getIban(),300.0,true,ba.getUserId().intValue());

        Assertions.assertTrue(result == true);
        Assertions.assertFalse(result == false);
    }

    @Test
    public void MoneyIsBeingDeposited() {
        //should be predefined balance
        Double endingAmount = 500.0;
        BankAccount ba = new BankAccount();

        ba.setAbsoluteLimit(1000.0);
        ba.setBalance(300.0);
        ba.setIban(bankAccountService.GenerateIban());
        ba.setUserId(3);
        ba.setAccountType(BankAccount.AccountTypeEnum.CURRENT);
        ba.setAccountStatus(BankAccount.AccountStatusEnum.ACTIVE);

        bankAccountService.SaveBankAccount(ba);

        boolean result = transactionService.WithdrawOrDepositMoney(ba.getIban(),200.0,false,ba.getUserId().intValue());

        Double endBalance = bankAccountService.GetBankAccountByIban(ba.getIban()).getBalance();

        Assertions.assertTrue(Objects.equals(endingAmount,endBalance) == true);
        Assertions.assertFalse(Objects.equals(endingAmount,endBalance) == false);
    }

    @Test
    public void MoneyDepositNegativeValue() {
        //should not be possible
        boolean result = transactionService.WithdrawOrDepositMoney(bankAccountsList.get(0).getIban(),-200.0,false,bankAccountsList.get(0).getUserId().intValue());

        Assertions.assertTrue(result == false);
        Assertions.assertFalse(result == true);
    }

    //END DEPOSIT AND WITHDRAW TESTS

    //START TRANSACTIONS BY DATE LOOKUP SHOULD FUNCTION PERFECTLY
    @Test
    public void TestTest() {
        System.out.println("Total amount of transactions are: " + transactionService.GetAllTransactionsFromDatabase().size());

        Assertions.assertFalse(true == true);
    }
}
