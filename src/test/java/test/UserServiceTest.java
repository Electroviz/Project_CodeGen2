package test;

import io.swagger.model.entity.User;
import io.swagger.service.UserService;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Autowired
    UserService userService;
    @Test
    void addUserShouldNotReturnNull() {
        //User user = userService.addUser()
        //assertNotNull(user);
    }

    @Test
    void checkIfStringIsEmailWorks() {
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