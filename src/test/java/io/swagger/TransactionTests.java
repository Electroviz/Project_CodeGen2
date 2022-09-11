package io.swagger;

import io.swagger.enums.UserRoleEnum;
import io.swagger.model.BankAccount;
import io.swagger.model.Transaction;
import io.swagger.model.entity.User;
import io.swagger.service.TransactionService;
import io.swagger.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.threeten.bp.LocalDate;
import org.threeten.bp.OffsetDateTime;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TransactionService transactionService;
    @MockBean
    private UserService userService;

    private Transaction transaction;
    private User user;
    private BankAccount account;

    @BeforeEach
    public void setup(){
        user = new User();
        user.setUsername("test123");
        user.setFullname("Jantje Egberts");
        user.setEmail("jantje@live.nl");
        user.setPassword("geheim123");
        user.setPhone("+310628495028");
        user.setDateOfBirth("12-03-1997");
        user.setId(23L);
        //firstUser.setRole(UserRoleEnum.ROLE_EMPLOYEE);
        user.setRoles(new ArrayList<>(Arrays.asList(UserRoleEnum.ROLE_EMPLOYEE)));
        user.setTransactionLimit(BigDecimal.valueOf(3000.0));
        user.setDayLimit(BigDecimal.valueOf(3000.0));
        userService.addUser(user, true);

        account.setIban("NL01INHO0000000002");
        account.setUserId(user.getId());
        account.setBalance(100.0);

        transaction.setTo("NL01INHO0000000002");
        transaction.setFrom("NL01INHO0000000001");
        transaction.setAmount(1.0);
        transaction.setDescription("test");
        transaction.setUserIDPerforming(23);
        transaction.setTimestamp(OffsetDateTime.now());
        transactionService.SaveTransaction(transaction);
    }

    @Test
    void WithdrawTest(){
        boolean withdraw =  transactionService.WithdrawOrDepositMoney(account.getIban(), 1.0, true, Math.toIntExact(Long.valueOf(user.getId())));
        Assertions.assertTrue(withdraw);
    }

    @Test
    void DepositTest(){
        boolean withdraw =  transactionService.WithdrawOrDepositMoney(account.getIban(), 1.0, false, Math.toIntExact(Long.valueOf(user.getId())));
        Assertions.assertTrue(withdraw);
    }

    @Test
    void GetAllTransactions(){
        List<Transaction> transactionList = transactionService.GetAllTransactionsFromDatabase();
        Assertions.assertNotNull(transactionList);
    }

    @Test
    void GetTransactionsBetweerRange(){
        List<Transaction> transactionList = transactionService.GetTransactionsInBetweenDate(OffsetDateTime.MIN, OffsetDateTime.now(), 23);
        Assertions.assertNotNull(transactionList);
    }

    @Test
    void  GetTransactionsByIbans(){
        List<Transaction> transactionList = transactionService.GetTransactionByIbans("NL01INHO0000000001", "NL01INHO0000000002");
        Assertions.assertNotNull(transactionList);
    }

}
