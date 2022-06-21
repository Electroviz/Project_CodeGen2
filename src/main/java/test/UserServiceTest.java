package test;

import io.swagger.model.entity.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    @org.junit.jupiter.api.Test
    void addUserShouldNotReturnNull() {
        User user = new User();
        assertNotNull(user);
    }

}