package test;

import io.swagger.model.BankAccount;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BankAccountServiceTest {

    @Test
    void deleteBankAccount() {
    }

    @Test
    void accountDeposit() {
    }

    @Test
    void accountWithdraw() {
    }

    @Test
    void getAccountByName() {
    }

    @Test
    void generateIbanShouldBeValid() {
        BankAccount account = new BankAccount();
        account.setIban("NL01INHO0000000001");
        assertTrue(account.getIban().matches("NL\\d{2}INHO0\\d{9}"));
    }
}