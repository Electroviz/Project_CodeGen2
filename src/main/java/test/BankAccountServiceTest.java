package test;

import io.swagger.model.BankAccount;


public class BankAccountServiceTest {

    @Test
    void generateIbanShouldBeValid() {
        BankAccount account = new BankAccount();
        account.setIban("NL01INHO0000000001");
        assertTrue(account.getIban().matches("NL\\d{2}INHO0\\d{9}"));
    }
}