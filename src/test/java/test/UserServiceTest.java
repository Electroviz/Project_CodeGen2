package test;

import io.swagger.enums.UserRoleEnum;
import io.swagger.model.entity.User;
import io.swagger.service.UserService;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    //@Autowired
    UserService userService = new UserService();

    @Test
    void addUserShouldNotReturnNull() {
        User firstUser = new User();
        firstUser.setUsername("test123");
        firstUser.setFullname("Jantje Egberts");
        firstUser.setEmail("jantje@live.nl");
        firstUser.setPassword("geheim123");
        firstUser.setPhone("+310628495028");
        firstUser.setDateOfBirth("12-03-1997");
        //firstUser.setRole(UserRoleEnum.ROLE_EMPLOYEE);
        firstUser.setRoles(new ArrayList<>(Arrays.asList(UserRoleEnum.ROLE_EMPLOYEE)));
        firstUser.setTransactionLimit(BigDecimal.valueOf(3000.0));
        firstUser.setDayLimit(BigDecimal.valueOf(3000.0));
        Assertions.assertNotNull(userService.addUser(firstUser, false).getBody());
    }

    @Test
    void checkIfStringIsEmailWorks() {
        Assertions.assertFalse(userService.checkIfStringIsEmail("test.com"));
    }

    @Test
    void checkIfUserInputIsWordReturnsFalseIfNotWord() {
        userService = new UserService();

        Assertions.assertFalse(userService.checkIfUserInputIsWord("test123123"));
    }

    @Test
    void login() {
        String token = userService.login("test123", "geheim123");
        Assertions.assertNotNull(token);
    }

    @Test
    void getUserIdByUsernameTest(){
        User user = new User();
        user.setUsername("Test");
        try {
            Long id = userService.getUserIdByUsername("Test");
            Assertions.assertNotNull(id);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    void getUsersWithoutBankaccount(){
        try {
            List<User> userList = userService.getUsersWithoutBankAccount();
            assertNotNull(userList);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    void getUserById(){
        try {
            User user = userService.getUserById(12L);
            assertNotNull(user);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}