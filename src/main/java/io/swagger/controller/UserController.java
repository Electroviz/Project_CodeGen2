package io.swagger.controller;

import io.swagger.model.Login;
import io.swagger.model.UserDTO;
import io.swagger.model.entity.User;
import io.swagger.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/getall", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAll(){

        List<User> users = userService.getAll();

        ModelMapper modelMapper = new ModelMapper();

        List<UserDTO> dtos = users
                .stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());

        return ResponseEntity.status(200).body(dtos);
    }

    //melle
    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value= "/getUserIdJwtValidation")
    public ResponseEntity getUserIdByJwtTokenVerification() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            String uname = authentication.getName();
            if(uname != null) {
                if(uname.length() > 0) {
                    Long uId = userService.getUserIdByUsername(uname);
                    if(uId != -1) return ResponseEntity.status(200).body(uId);
                    else return ResponseEntity.status(400).body("");
                }
            }

            return ResponseEntity.status(400).body("");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("");
        }
    }

    //melle
    @CrossOrigin
    @RequestMapping(value = "/getAllUsersWithoutBankAccounts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUsersWithoutBankAccounts() {
        List<User> usersWithoutBankAccounts = userService.getUsersWithoutBankAccount();

        return ResponseEntity.status(200).body(usersWithoutBankAccounts);
    }

    //melle
    @CrossOrigin
    @RequestMapping(value = "/testLogin/{username}/{password}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity loginAttempt(@PathVariable("username") String username, @PathVariable("password") String password) {

        User u = this.userService.TestLoginAttempt(username, password);

        if(u == null) return ResponseEntity.status(400).body(null);
        else return ResponseEntity.status(200).body(u);

    }

    //melle
//    @CrossOrigin
//    @RequestMapping(value = "/userByJwtToken/{token}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity getUserByJwtToken(@PathVariable("token") String token) {
//        Authentication ath = new SecurityContextHolder.getContext().getAuthentication();
//        return ResponseEntity.status(200).body()
//    }

    @RequestMapping(value = "/registeruser", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity add(@RequestBody UserDTO userDTO){

        ModelMapper modelMapper = new ModelMapper();
        User user = modelMapper.map(userDTO, User.class);

        user = userService.addUser(user);

        UserDTO response = modelMapper.map(user, UserDTO.class);

        return ResponseEntity.status(201).body(response);
    }

    @CrossOrigin
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUserById(@PathVariable("id") Long id){

        ModelMapper modelMapper = new ModelMapper();

        User user = userService.findById(id);

        UserDTO response = modelMapper.map(user, UserDTO.class);

        return ResponseEntity.status(201).body(response);
    }

    @CrossOrigin
    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public String login(@RequestBody Login login){

        String test = "test";
        return userService.login(login.getUsername(), login.getPassword());
    }

}
