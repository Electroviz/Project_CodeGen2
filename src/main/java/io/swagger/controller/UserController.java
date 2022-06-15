package io.swagger.controller;

import io.swagger.model.UserDTO;
import io.swagger.model.entity.User;
import io.swagger.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
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
    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/testLogin/{username}/{password}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity loginAttempt(@PathVariable("username") String username, @PathVariable("password") String password) {

        User u = this.userService.TestLoginAttempt(username, password);

        if(u == null) return ResponseEntity.status(400).body(null);
        else return ResponseEntity.status(200).body(u);

    }

    @RequestMapping(value = "/registeruser", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity add(@RequestBody UserDTO userDTO){

        ModelMapper modelMapper = new ModelMapper();
        User user = modelMapper.map(userDTO, User.class);

        user = userService.addUser(user);

        UserDTO response = modelMapper.map(user, UserDTO.class);

        return ResponseEntity.status(201).body(response);
    }


}
