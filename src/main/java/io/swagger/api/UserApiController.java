package io.swagger.api;

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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-12T15:22:53.754Z[GMT]")
@RestController
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

        user = userService.addUser(user);

        UserDTO response = modelMapper.map(user, UserDTO.class);

        return new ResponseEntity<UserDTO>(response, HttpStatus.CREATED);
    }

    public ResponseEntity<Void> deleteUser(@Parameter(in = ParameterIn.PATH, description = "UserId to delete a user", required=true, schema=@Schema()) @PathVariable("userid") String userid) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<List<UserDTO>> getAllUsers(@Min(0)@Parameter(in = ParameterIn.QUERY, description = "number of records to skip for pagination" ,schema=@Schema(allowableValues={  }
)) @Valid @RequestParam(value = "skip", required = false) Integer skip,@Min(0) @Max(50) @Parameter(in = ParameterIn.QUERY, description = "maximum number of records to return" ,schema=@Schema(allowableValues={  }, maximum="50"
)) @Valid @RequestParam(value = "limit", required = false) Integer limit) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<UserDTO>>(objectMapper.readValue("[ {\n  \"password\" : \"fhdnd_Hdkf\",\n  \"phone\" : \"612345345\",\n  \"transaction limit\" : 500,\n  \"dateOfBirth\" : \"17-09-1990\",\n  \"day limit\" : 1000,\n  \"id\" : 0,\n  \"fullname\" : \"Kees Post\",\n  \"userRole\" : \"Employee\",\n  \"email\" : \"PieterBG@gmail.com\",\n  \"username\" : \"Kees1978\"\n}, {\n  \"password\" : \"fhdnd_Hdkf\",\n  \"phone\" : \"612345345\",\n  \"transaction limit\" : 500,\n  \"dateOfBirth\" : \"17-09-1990\",\n  \"day limit\" : 1000,\n  \"id\" : 0,\n  \"fullname\" : \"Kees Post\",\n  \"userRole\" : \"Employee\",\n  \"email\" : \"PieterBG@gmail.com\",\n  \"username\" : \"Kees1978\"\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<UserDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<UserDTO>>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<String> loginUser(@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody Login body) {
        //User User = new User();
        //User.setUsername(body.getUsername());
        //User.setPassword(body.getPassword());

        return new ResponseEntity<String>(HttpStatus.OK);
    }

    public ResponseEntity<UserDTO> updateUser(@Parameter(in = ParameterIn.PATH, description = "UserId to edit a user", required=true, schema=@Schema()) @PathVariable("userid") String userid,@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody UserDTO body) {


        return new ResponseEntity<UserDTO>(HttpStatus.NOT_IMPLEMENTED);
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

}
