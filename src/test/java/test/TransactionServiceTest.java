package test;

import io.swagger.Swagger2SpringBoot;
import io.swagger.enums.BankAccountType;
import io.swagger.model.BankAccount;
import io.swagger.model.Transaction;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        BankAccount ba = fakeBankAccount(3,300.0, BankAccount.AccountTypeEnum.CURRENT, BankAccount.AccountStatusEnum.CLOSED);

        boolean result = transactionService.TransferMoneyFromToIban(ba.getIban(),bankAccountsList.get(1).getIban(), 100.0,ba.getUserId().intValue());

        Assertions.assertTrue(result == false);
        Assertions.assertFalse(result == true);
    }

    @Test
    public void TransferMoneyFromOneUserIbanToSavingsAccountOtherUser() {
        BankAccount baCurrent = fakeBankAccount(3,300.0, BankAccount.AccountTypeEnum.CURRENT, BankAccount.AccountStatusEnum.ACTIVE);
        BankAccount baCurrentSavings = fakeBankAccount(3,300.0, BankAccount.AccountTypeEnum.SAVINGS, BankAccount.AccountStatusEnum.ACTIVE);
        BankAccount baSavings = fakeBankAccount(4,300.0, BankAccount.AccountTypeEnum.SAVINGS, BankAccount.AccountStatusEnum.CLOSED);

        boolean resultCurrentToSavingsDifferentUsers = transactionService.TransferMoneyFromToIban(baSavings.getIban(),baCurrent.getIban(),20.0,baCurrent.getUserId().intValue());
        boolean resultCurrentToOwnSavings = transactionService.TransferMoneyFromToIban(baCurrentSavings.getIban(),baCurrent.getIban(),20.0,baCurrent.getUserId().intValue());

        Assertions.assertTrue(resultCurrentToSavingsDifferentUsers == false && resultCurrentToOwnSavings);
        Assertions.assertFalse(resultCurrentToSavingsDifferentUsers == true || resultCurrentToOwnSavings == false);
    }

    @Test
    public void TransferMoneyToClosedBankAccountTest() {
        //should not be possible
        BankAccount ba = fakeBankAccount(3,300.0, BankAccount.AccountTypeEnum.CURRENT, BankAccount.AccountStatusEnum.CLOSED);

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
        BankAccount ba = fakeBankAccount(3,300.0, BankAccount.AccountTypeEnum.CURRENT, BankAccount.AccountStatusEnum.ACTIVE);

        boolean result = transactionService.WithdrawOrDepositMoney(ba.getIban(),300.05,true,ba.getUserId().intValue());

        Assertions.assertTrue(result == false);
        Assertions.assertFalse(result == true);
    }

    @Test
    public void WithdrawExactBalanceTest() {
        //should be possible
        BankAccount ba = fakeBankAccount(3,300.0, BankAccount.AccountTypeEnum.CURRENT, BankAccount.AccountStatusEnum.ACTIVE);


        boolean result = transactionService.WithdrawOrDepositMoney(ba.getIban(),300.0,true,ba.getUserId().intValue());

        Assertions.assertTrue(result == true);
        Assertions.assertFalse(result == false);
    }

    @Test
    public void MoneyIsBeingDepositedTest() {
        //should be predefined balance
        Double endingAmount = 500.0;

        BankAccount ba = fakeBankAccount(3,300.0, BankAccount.AccountTypeEnum.CURRENT, BankAccount.AccountStatusEnum.ACTIVE);


        boolean result = transactionService.WithdrawOrDepositMoney(ba.getIban(),200.0,false,ba.getUserId().intValue());

        Double endBalance = bankAccountService.GetBankAccountByIban(ba.getIban()).getBalance();

        Assertions.assertTrue(Objects.equals(endingAmount,endBalance) == true);
        Assertions.assertFalse(Objects.equals(endingAmount,endBalance) == false);
    }

    @Test
    public void MoneyDepositNegativeValueTest() {
        //should not be possible
        boolean result = transactionService.WithdrawOrDepositMoney(bankAccountsList.get(0).getIban(),-200.0,false,bankAccountsList.get(0).getUserId().intValue());

        Assertions.assertTrue(result == false);
        Assertions.assertFalse(result == true);
    }

    //END DEPOSIT AND WITHDRAW TESTS

    //START GET TRANSACTIONS (COLLECT THE EXPECTED DATA)

    @Test
    public void GetCorrectTransactionsByDateTest() throws ParseException {
        //to iban , from iban
        BankAccount ba = fakeBankAccount(4,300.0, BankAccount.AccountTypeEnum.CURRENT, BankAccount.AccountStatusEnum.ACTIVE);

        boolean result1 = transactionService.TransferMoneyFromToIban(bankAccountsList.get(1).getIban(),ba.getIban(),50.0,ba.getUserId().intValue());
        boolean result2 = transactionService.TransferMoneyFromToIban(bankAccountsList.get(1).getIban(),ba.getIban(),80.0,ba.getUserId().intValue());

        if(result1 && result2) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            List<Transaction> transactionsForBa = transactionService.GetTransactionsInBetweenDate(format.parse("1999-01-01"), format.parse("2050-09-12"),ba.getUserId().intValue());

            Assertions.assertTrue(transactionsForBa.size() == 2);
            Assertions.assertFalse(transactionsForBa.size() < 2);
        }
        else Assertions.assertFalse(result1 != result2);
    }

    @Test
    public void GetTransactionWhereBalanceIsEqualTest() {
        BankAccount ba1 = fakeBankAccount(1,300.0, BankAccount.AccountTypeEnum.CURRENT, BankAccount.AccountStatusEnum.ACTIVE);
        BankAccount ba2 = fakeBankAccount(2,500.0, BankAccount.AccountTypeEnum.CURRENT, BankAccount.AccountStatusEnum.ACTIVE);

        transactionService.TransferMoneyFromToIban(ba2.getIban(),ba1.getIban(),50.0,ba1.getUserId().intValue());
        transactionService.TransferMoneyFromToIban(ba2.getIban(),ba1.getIban(),50.000001,ba1.getUserId().intValue());

        List<Transaction> transactionsEqual1 = transactionService.GetTransactionByRelationship(ba2.getIban(),50.0,"equal");
        List<Transaction> transactionsEqual2 = transactionService.GetTransactionByRelationship(ba2.getIban(),Double.valueOf("50"),"equal");

        Assertions.assertTrue(transactionsEqual1.size() == 1 && transactionsEqual2.size() == 1);
        Assertions.assertFalse(transactionsEqual1.size() == 0 || transactionsEqual2.size() == 0);
    }

    @Test
    public void GetTransactionWhereBalanceIsSmallerTest() {
        BankAccount ba1 = fakeBankAccount(1,300.0, BankAccount.AccountTypeEnum.CURRENT, BankAccount.AccountStatusEnum.ACTIVE);
        BankAccount ba2 = fakeBankAccount(2,500.0, BankAccount.AccountTypeEnum.CURRENT, BankAccount.AccountStatusEnum.ACTIVE);

        transactionService.TransferMoneyFromToIban(ba2.getIban(),ba1.getIban(),50.0,ba1.getUserId().intValue());
        transactionService.TransferMoneyFromToIban(ba2.getIban(),ba1.getIban(),51.0,ba1.getUserId().intValue());

        List<Transaction> transactionsSmaller1 = transactionService.GetTransactionByRelationship(ba2.getIban(),50.001,"smaller");
        List<Transaction> transactionsSmaller2 = transactionService.GetTransactionByRelationship(ba2.getIban(),Double.valueOf("51"),"smaller");

        Assertions.assertTrue(transactionsSmaller1.size() == 1 && transactionsSmaller2.size() == 1);
        Assertions.assertFalse(transactionsSmaller1.size() == 0 || transactionsSmaller2.size() == 0);
    }

    @Test
    public void GetTransactionWhereBalanceIsBiggerTest() {
        BankAccount ba1 = fakeBankAccount(1,300.0, BankAccount.AccountTypeEnum.CURRENT, BankAccount.AccountStatusEnum.ACTIVE);
        BankAccount ba2 = fakeBankAccount(2,500.0, BankAccount.AccountTypeEnum.CURRENT, BankAccount.AccountStatusEnum.ACTIVE);

        transactionService.TransferMoneyFromToIban(ba2.getIban(),ba1.getIban(),50.0,ba1.getUserId().intValue());
        transactionService.TransferMoneyFromToIban(ba2.getIban(),ba1.getIban(),49.0,ba1.getUserId().intValue());

        List<Transaction> transactionsBigger1 = transactionService.GetTransactionByRelationship(ba2.getIban(),49.9999999,"bigger");
        List<Transaction> transactionsBigger2 = transactionService.GetTransactionByRelationship(ba2.getIban(),Double.valueOf("49"),"bigger");

        Assertions.assertTrue(transactionsBigger1.size() == 1 && transactionsBigger2.size() == 1);
        Assertions.assertFalse(transactionsBigger1.size() == 0 || transactionsBigger2.size() == 0);
    }

    @Test
    public void GetTransactionWhereBalanceIsBiggerMultipleOtherTransactionsTest() {
        BankAccount ba1 = fakeBankAccount(1,300.0, BankAccount.AccountTypeEnum.CURRENT, BankAccount.AccountStatusEnum.ACTIVE);
        BankAccount ba2 = fakeBankAccount(2,500.0, BankAccount.AccountTypeEnum.CURRENT, BankAccount.AccountStatusEnum.ACTIVE);
        BankAccount ba3 = fakeBankAccount(3,800.0, BankAccount.AccountTypeEnum.CURRENT, BankAccount.AccountStatusEnum.ACTIVE);
        BankAccount ba4 = fakeBankAccount(4,900.0, BankAccount.AccountTypeEnum.CURRENT, BankAccount.AccountStatusEnum.ACTIVE);

        transactionService.TransferMoneyFromToIban(ba2.getIban(),ba1.getIban(),50.0,ba1.getUserId().intValue());
        transactionService.TransferMoneyFromToIban(ba2.getIban(),ba1.getIban(),49.0,ba1.getUserId().intValue());

        //fake transactions to check result
        transactionService.TransferMoneyFromToIban(ba3.getIban(),ba4.getIban(),150.0,ba4.getUserId().intValue());
        transactionService.TransferMoneyFromToIban(ba1.getIban(),ba3.getIban(),104.0,ba4.getUserId().intValue());

        List<Transaction> transactionsBigger1 = transactionService.GetTransactionByRelationship(ba2.getIban(),49.9999999,"bigger");
        List<Transaction> transactionsBigger2 = transactionService.GetTransactionByRelationship(ba2.getIban(),Double.valueOf("49"),"bigger");
    }





    private BankAccount fakeBankAccount(int ownerId, Double balance, BankAccount.AccountTypeEnum type, BankAccount.AccountStatusEnum status) {

        BankAccount ba = new BankAccount();

        ba.setAbsoluteLimit(-200.0);
        ba.setBalance(balance);
        ba.setIban(bankAccountService.GenerateIban());
        ba.setUserId(ownerId);
        ba.setAccountType(type);
        ba.setAccountStatus(status);

        bankAccountService.SaveBankAccount(ba);

        return ba;
    }


}
