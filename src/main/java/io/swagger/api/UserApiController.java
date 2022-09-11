package io.swagger.api;

import io.swagger.annotations.Api;
import io.swagger.enums.UserRoleEnum;
import io.swagger.model.Login;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.UserDTO;
import io.swagger.model.entity.User;
import io.swagger.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.dom4j.util.UserDataDocumentFactory;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-12T15:22:53.754Z[GMT]")
@RestController
@CrossOrigin
@RequestMapping("/user")
@Api(tags= {"Users"})
public class UserApiController implements UserApi {

    private static final Logger log = LoggerFactory.getLogger(UserApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private UserService userService;

    @org.springframework.beans.factory.annotation.Autowired
    public UserApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<UserDTO> createUser(@Parameter(in = ParameterIn.DEFAULT, description = "Get user information", required=true, schema=@Schema()) @Valid @RequestBody UserDTO body) {


        ModelMapper modelMapper = new ModelMapper();
        User user = modelMapper.map(body, User.class);
        ResponseEntity response = null;
        if(user.getRole() == UserRoleEnum.ROLE_EMPLOYEE) response = userService.addUser(user, true);
        else response = userService.addUser(user, false);

        return new ResponseEntity(response, HttpStatus.CREATED);
    }


    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity getAll(){

        List<User> users = userService.getAll();

        ModelMapper modelMapper = new ModelMapper();

        List<UserDTO> dtos = users
                .stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());

        return ResponseEntity.status(200).body(dtos);
    }



    public ResponseEntity<UserDTO> userUseridGet(@Parameter(in = ParameterIn.PATH, description = "UserId to get user information", required=true, schema=@Schema()) @PathVariable("userid") String userid) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<UserDTO>(objectMapper.readValue("{\n  \"password\" : \"fhdnd_Hdkf\",\n  \"phone\" : \"612345345\",\n  \"transaction limit\" : 500,\n  \"dateOfBirth\" : \"17-09-1990\",\n  \"day limit\" : 1000,\n  \"id\" : 0,\n  \"fullname\" : \"Kees Post\",\n  \"userRole\" : \"Employee\",\n  \"email\" : \"PieterBG@gmail.com\",\n  \"username\" : \"Kees1978\"\n}", UserDTO.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<UserDTO>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<UserDTO>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity getUserIdByJwtTokenVerification() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            String uname = authentication.getName();
            if(uname != null) {
                if(uname.length() > 0) {
                    Long uId = userService.getUserIdByUsername(uname);
                    if(uId != -1) return ResponseEntity.status(200).body(uId);
                    else return ResponseEntity.status(400).body("Bad Request");
                }
            }

            return ResponseEntity.status(400).body("Bad Request");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Bad Request");
        }
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity getUsersWithoutBankAccounts() {
        List<User> usersWithoutBankAccounts = userService.getUsersWithoutBankAccount();

        return ResponseEntity.status(200).body(usersWithoutBankAccounts);
    }

    public ResponseEntity add(@RequestBody UserDTO userDTO){

        ModelMapper modelMapper = new ModelMapper();
        User user = modelMapper.map(userDTO, User.class);

        ResponseEntity response = userService.addUser(user,false);

        return ResponseEntity.status(201).body(response);
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity addAsEmployee(@RequestBody UserDTO userDTO){

        ModelMapper modelMapper = new ModelMapper();
        User user = modelMapper.map(userDTO, User.class);

        ResponseEntity response = userService.addUser(user,true);

        return ResponseEntity.status(201).body(response);
    }


    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity getUserById(@PathVariable("id") Long id){

        ModelMapper modelMapper = new ModelMapper();

        User user = userService.findById(id);

        UserDTO response = modelMapper.map(user, UserDTO.class);

        return ResponseEntity.status(201).body(response);
    }

    public String login(@RequestBody Login login){

        String test = "test";
        return userService.login(login.getUsername(), login.getPassword());
    }
}
