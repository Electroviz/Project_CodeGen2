package io.swagger;

import io.swagger.enums.UserRoleEnum;
import io.swagger.model.entity.User;
import io.swagger.service.UserService;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserServiceTest {

    @Autowired
    private MockMvc mvc;

    @MockBean private UserService userService;
    private User user;

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
        //userService.addUser(user);
    }

    @Test
    void addUserShouldNotReturnNull() throws Exception{
        this.mvc.perform(MockMvcRequestBuilders.post("/api/registeruser/asEmployee")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("")
                .content("{}"))
                .andExpect(status().isCreated());
        //Assertions.assertNotNull(userService.addUser(user, false).getBody());
    }

    @Test
    void checkIfStringIsEmailWorks() {
        Assertions.assertFalse(userService.checkIfStringIsEmail("test.com"));
    }

    @Test
    void checkIfUserInputIsWordReturnsFalseIfNotWord() {
        Assertions.assertFalse(userService.checkIfUserInputIsWord("test123123"));
    }

    @Test
    void login() {
        String token = userService.login("test123", "geheim123");
        Assertions.assertNotNull(token);
    }

    @Test
    void getUsersWithoutBankaccount(){
        try {
            List<User> userList = userService.getUsersWithoutBankAccount();
            System.out.println(userList.stream().count());
            assertNotNull(userList);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    void getUserById(){
        try {
            this.mvc.perform(MockMvcRequestBuilders.get("/api/get/12")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content("{}"))
                    .andExpect(MockMvcResultMatchers.status().isCreated())
                    .andExpect(jsonPath("$.keys()", Matchers.is(1)));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}