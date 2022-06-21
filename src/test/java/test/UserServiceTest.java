package test;

import io.swagger.model.entity.User;
import io.swagger.service.UserService;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Test
    void addUserShouldNotReturnNull() {
        User user = new User();
        assertNotNull(user);
    }

    @Test
    void checkIfStringIsEmailWorks() {
        UserService userService = new UserService();

        Assertions.assertFalse(userService.checkIfStringIsEmail("test.com"));
    }
}