package io.swagger;

import io.swagger.configuration.LocalDateConverter;
import io.swagger.configuration.LocalDateTimeConverter;

import io.swagger.model.BankAccount;
import io.swagger.model.User;
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
        ourMainBankAccount.setUserId(new Long(1));

        bankAccountService.SaveBankAccount(ourMainBankAccount);

        //melle
        //create 10 fake bank accounts without user associations
        for(int i = 0; i < 10; i++)
            bankAccountService.CreateDummyDataBankAccount();

//        //create fake users and transactions
//        User firstUser = new User();
//        firstUser.username("Jantje");
//        firstUser.fullname("Jantje Egberts");
//        firstUser.email("jantje@live.nl");
//        firstUser.password("jantje123");
//        firstUser.phone("+310628495028");
//        firstUser.dateOfBirth("12-03-1997");
//        firstUser.userRole(User.UserRoleEnum.CUSTOMER);
//        firstUser.transactionLimit(BigDecimal.valueOf(3000.0));
//        firstUser.dayLimit(BigDecimal.valueOf(30000.0));
//
//        userService.SaveUser(firstUser);
//
//        User secondUser = new User();
//        secondUser.username("Gerard");
//        secondUser.fullname("Gerard Van Brankenstein");
//        secondUser.email("gerard@live.nl");
//        secondUser.password("gerard123");
//        secondUser.phone("+310688473022");
//        secondUser.dateOfBirth("01-11-1988");
//        secondUser.userRole(User.UserRoleEnum.CUSTOMER);
//        secondUser.transactionLimit(BigDecimal.valueOf(3000.0));
//        secondUser.dayLimit(BigDecimal.valueOf(30000.0));
//
//        userService.SaveUser(secondUser);

    }

    public static void main(String[] args) throws Exception {
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
