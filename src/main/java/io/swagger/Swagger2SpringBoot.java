package io.swagger;

import io.swagger.configuration.LocalDateConverter;
import io.swagger.configuration.LocalDateTimeConverter;

import io.swagger.enums.UserRoleEnum;
import io.swagger.model.BankAccount;
import io.swagger.model.entity.User;
import io.swagger.service.BankAccountService;
import io.swagger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import springfox.documentation.oas.annotations.EnableOpenApi;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

@SpringBootApplication
@EnableOpenApi
@ComponentScan(basePackages = { "io.swagger", "io.swagger.api" , "io.swagger.configuration"})
public class Swagger2SpringBoot implements CommandLineRunner {
    @Autowired
    BankAccountService bankAccountService;

    @Autowired
    UserService userService;


    @Override
    public void run(String... arg0) throws Exception {

        if (arg0.length > 0 && arg0[0].equals("exitcode")) {
            throw new ExitException();
        }

        //melle
        //create the banks own account
        BankAccount ourMainBankAccount = new BankAccount();
        ourMainBankAccount.setIban("NL01INHO0000000001");
        ourMainBankAccount.setBalance(1000000000.0); //miljard
        ourMainBankAccount.setAbsoluteLimit(-1000000000.0); //minus 1 miljard in het rood
        ourMainBankAccount.setAccountType(BankAccount.AccountTypeEnum.CURRENT); //not a savings account
        ourMainBankAccount.setUserId(-1);

        bankAccountService.SaveBankAccount(ourMainBankAccount);

        //melle
        //create 10 fake bank accounts without user associations
        for(int i = 0; i < 10; i++)
            bankAccountService.CreateDummyDataBankAccount();


        //Nick
//        create fake users and transactions
          User firstUser = new User();
          firstUser.setUsername("test");
          firstUser.setFullname("Jantje Egberts");
          firstUser.setEmail("jantje@live.nl");
          firstUser.setPassword("geheim");
          firstUser.setPhone("+310628495028");
          firstUser.setDateOfBirth("12-03-1997");
          firstUser.setRole(UserRoleEnum.ROLE_EMPLOYEE);
          firstUser.setTransactionLimit(BigDecimal.valueOf(3000.0));
          firstUser.setDayLimit(BigDecimal.valueOf(3000.0));

          userService.addUser(firstUser);

        User second = new User();
        second.setUsername("Eland");
        second.setFullname("Eland Egberts");
        second.setEmail("Eland@live.nl");
        second.setPassword("jantje123");
        second.setPhone("+310628495028");
        second.setDateOfBirth("12-03-1997");
        second.setRole(UserRoleEnum.ROLE_CUSTOMER);
        second.setTransactionLimit(BigDecimal.valueOf(3000.0));
        second.setDayLimit(BigDecimal.valueOf(3000.0));

        userService.addUser(second);
//          bankAccountService.CreateDummyDataBankAccount(firstUser.getId(), BankAccount.AccountTypeEnum.CURRENT);
//          bankAccountService.CreateDummyDataBankAccount(firstUser.getId(), BankAccount.AccountTypeEnum.SAVINGS);

//        User secondUser = new User();

//        secondUser.setUsername("Gerard");
//        secondUser.setFullname("Gerard Van Brankenstein");
//        secondUser.setEmail("gerard@live.nl");
//        secondUser.setPassword("gerard123");
//        secondUser.setPhone("+310688473022");
//        secondUser.setDateOfBirth("01-11-1988");
//        secondUser.setUserRole(User.UserRoleEnum.CUSTOMER);
//        secondUser.setTransactionLimit(BigDecimal.valueOf(3000.0));
//        secondUser.setDayLimit(BigDecimal.valueOf(30000.0));
//
//        bankAccountService.CreateDummyDataBankAccount(secondUser.getId(), BankAccount.AccountTypeEnum.CURRENT);
//        bankAccountService.CreateDummyDataBankAccount(secondUser.getId(), BankAccount.AccountTypeEnum.SAVINGS);
//
//
//        userService.addUser(secondUser);

    }

    public static void main(String[] args) throws Exception {
        var lastUpdated = new Date().getTime();
        new SpringApplication(Swagger2SpringBoot.class).run(args);
    }

    @Configuration
    static class CustomDateConfig extends WebMvcConfigurerAdapter {
        @Override
        public void addFormatters(FormatterRegistry registry) {
            registry.addConverter(new LocalDateConverter("yyyy-MM-dd"));
            registry.addConverter(new LocalDateTimeConverter("yyyy-MM-dd'T'HH:mm:ss.SSS"));
        }
    }

    class ExitException extends RuntimeException implements ExitCodeGenerator {
        private static final long serialVersionUID = 1L;

        @Override
        public int getExitCode() {
            return 10;
        }

    }
}
