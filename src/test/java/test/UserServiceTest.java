package test;

import io.swagger.Swagger2SpringBoot;
import io.swagger.enums.UserRoleEnum;
import io.swagger.model.entity.User;
import io.swagger.service.UserService;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes= Swagger2SpringBoot.class)
class UserServiceTest {

    @Autowired
    UserService userService;
    @Test
    void addUserShouldNotReturnNull() {
        User user = new User();
        assertNotNull(user);
    }

    @Test
    void createNewRegularUserTest() {
        User u = CreateFakeUser("janus321","Janus Kogelaar","januskogelaar@live.nl", "geheim123");


        ResponseEntity result = userService.addUser(u,false);

        assertTrue((result.getStatusCodeValue() == 200 || result.getStatusCodeValue() == 201) && userService.getUserIdByUsername("janus321") != -1);
        assertFalse(result.getStatusCodeValue() >= 300 || userService.getUserIdByUsername("janus321") == -1);
    }

    @Test
    void passwordEncodingIsWorkingTest() {
        User u = CreateFakeUser("janus321","Janus Kogelaar","januskogelaar@live.nl", "geheim123");
        ResponseEntity result = userService.addUser(u,false);
        if(result.getStatusCodeValue() < 300) {
            User findUserFromDatabase = userService.getUserById(userService.getUserIdByUsername("janus321"));
            if(findUserFromDatabase != null) {
                Assertions.assertTrue(!Objects.equals(findUserFromDatabase.getPassword(), "geheim123"));
                Assertions.assertFalse(Objects.equals(findUserFromDatabase.getPassword(), "geheim123"));
            }
            else Assertions.assertFalse(findUserFromDatabase == null);

        }
        else Assertions.assertFalse(result.getStatusCodeValue() >= 300);

    }

    @Test
    void UsersWithoutBankAccountsFunctionsCorrectlyTest() {
        Integer originalUsersWithoutBankAcountSize = userService.getUsersWithoutBankAccount().size();

        User u = CreateFakeUser("janus321","Janus Kogelaar","januskogelaar@live.nl", "geheim123");
        User u2 = CreateFakeUser("janus3213","Jakkie Kogelares","januskogelaers@live.nl", "geheim123");

        userService.addUser(u,false);
        userService.addUser(u2,false);

        Integer newUsersWithoutBankAcountSize = userService.getUsersWithoutBankAccount().size();

        Assertions.assertTrue(newUsersWithoutBankAcountSize - originalUsersWithoutBankAcountSize == 2);
        Assertions.assertFalse(newUsersWithoutBankAcountSize - originalUsersWithoutBankAcountSize != 2);
    }

    @Test
    void UserRegisteringWithUniqueUsernameTest() {
        User firstRegister = CreateFakeUser("janus321","Janus Kogelaar","januskogelaar@live.nl", "geheim123");

        userService.addUser(firstRegister,false);

        User secondRegister = CreateFakeUser("janus321","Janus Kogelaar","januskogelaar@live.nl", "geheim123"); // same username

        ResponseEntity result1 = userService.addUser(secondRegister,false); //should not be possible
        ResponseEntity result2 = userService.addUser(secondRegister,true); //should not be possible

        secondRegister.setUsername("janus3321");
        ResponseEntity result3 = userService.addUser(secondRegister,false);

        assertTrue((result1.getStatusCodeValue() >= 300 && result2.getStatusCodeValue() >= 300) && result3.getStatusCodeValue() < 300);
        assertFalse(result1.getStatusCodeValue() < 300 || result2.getStatusCodeValue() < 300 || result3.getStatusCodeValue() >= 300);
    }

    private User CreateFakeUser(String username,String fullName, String email, String plainPassword) {
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setFullname(fullName);
        newUser.setEmail(email);
        newUser.setPassword(plainPassword);
        newUser.setPhone("+310628495028");
        newUser.setDateOfBirth("12-03-1997");
        newUser.setTransactionLimit(BigDecimal.valueOf(3000.0));
        newUser.setDayLimit(BigDecimal.valueOf(3000.0));

        return newUser;
    }

    @Test
    void checkIfStringIsEmailWorks() {
        UserService userService = new UserService();

        Assertions.assertFalse(userService.checkIfStringIsEmail("test.com"));
    }

    @Test
    void checkIfUserInputIsWordReturnsFalseIfNotWord() {
        UserService userService = new UserService();

        Assertions.assertFalse(userService.checkIfUserInputIsWord("test123123"));
    }

    @Test
    void login() {
    }
}