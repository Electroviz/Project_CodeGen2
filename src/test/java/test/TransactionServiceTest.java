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

        ba1.setAbsoluteLimit(1000.0);
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

    @Test
    public void TransferMoneyNegativeAmountNotPossibleTest() {
        boolean result = transactionService.TransferMoneyFromToIban(bankAccountsList.get(0).getIban(),bankAccountsList.get(1).getIban(), -100.0,bankAccountsList.get(0).getUserId().intValue());


        Assertions.assertTrue(result == false);
        Assertions.assertFalse(result == true);
    }

    @Test
    public void TransferMoneySameIbanNotPossibleTest() {
        boolean result = transactionService.TransferMoneyFromToIban(bankAccountsList.get(0).getIban(),bankAccountsList.get(0).getIban(), 100.0,bankAccountsList.get(0).getUserId().intValue());

        Assertions.assertTrue(result == false);
        Assertions.assertFalse(result == true);
    }

    @Test
    public void TransferToMuchMoneyThenActiveBalanceTest() {
        boolean result = transactionService.TransferMoneyFromToIban(bankAccountsList.get(0).getIban(),bankAccountsList.get(1).getIban(), (bankAccountsList.get(0).getBalance() + 50.0),bankAccountsList.get(0).getUserId().intValue());

        Assertions.assertTrue(result == false);
        Assertions.assertFalse(result == true);
    }

    @Test
    public void TransferMoneyFromClosedBankAccountTest() {
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

}
