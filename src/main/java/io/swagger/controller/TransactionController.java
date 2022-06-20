package io.swagger.controller;

import io.swagger.api.ApiException;
import io.swagger.api.TransactionsApiController;
import io.swagger.jwt.JwtTokenProvider;
import io.swagger.model.ApiResponse;
import io.swagger.model.TransactionRequest;
import io.swagger.model.UserDTO;
import io.swagger.model.entity.User;
import io.swagger.service.TransactionService;
import io.swagger.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private static final Logger log = LoggerFactory.getLogger(TransactionsApiController.class);


//    @RequestMapping("/TestTransfer/{userId}")
//    public ResponseEntity createTransaction(@PathVariable("userId") Long userId){
//        return transactionService.MakeTransaction(userId);

//    @RequestMapping("/TestTransfer/{UserDTO}")
//    public User createTransaction(@PathVariable("UserDTO") User UserDTO){
//        User userobej = UserDTO;
//        return transactionService.MakeTransaction(userobej);
//    }


//    public ResponseEntity transferMoney(
//            @Parameter(in = ParameterIn.DEFAULT, description = "", required = true, schema = @Schema()) @Valid @RequestBody TransactionRequest body) {
//        try {
//            // require user to be authenticated
//            User currentUser = jwtTokenProvider.getAuthentication(request);
//
//            // do the transaction
//            //transactionService.transferMoney(currentUser, body.toTransaction());
//
//            return new ResponseEntity(HttpStatus.CREATED);
//        } catch (Exception e) {
//            log.error("Error", e);
//            return new ResponseEntity<>(new ApiResponse<>("Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
    @RequestMapping(value = "/transferMoneyTest", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity transferMoneyTest(@RequestBody TransactionRequest body){
        try {
            // require user to be authenticated
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            Long userid = userService.getUserIdByUsername(authentication.getName());

            User currentUser = userService.getUserById(userid);

            // do the transaction
            transactionService.transferMoney(currentUser, body.toTransaction());

            return new ResponseEntity<>(new io.swagger.model.ApiResponse<>("Success"), HttpStatus.OK);
        } catch (ApiException e) {
            log.error("Error", e);
            return new ResponseEntity<>(new io.swagger.model.ApiResponse<>(e.getMessage()), HttpStatus.valueOf(e.getCode()));
        } catch (Exception e) {
            log.error("Error", e);
            return new ResponseEntity<>(new ApiResponse<>("Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
