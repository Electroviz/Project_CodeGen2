package test;

import io.swagger.model.entity.User;
import io.swagger.service.UserService;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Autowired
    UserService userService;
    @Test
    void addUserShouldNotReturnNull() {
        User user = new User();
        userService.addUser(user, false);
        Assertions.assertNotNull(user);
    }

    @Test
    void checkIfStringIsEmailWorks() {
        userService = new UserService();

        Assertions.assertFalse(userService.checkIfStringIsEmail("test.com"));
    }

    @Test
    void checkIfUserInputIsWordReturnsFalseIfNotWord() {
        userService = new UserService();

        Assertions.assertFalse(userService.checkIfUserInputIsWord("test123123"));
    }

    @Test
    void login() {
        String token = userService.login("test", "test");
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