package io.swagger.controller;

import io.swagger.api.ApiException;
import io.swagger.api.TransactionsApiController;
import io.swagger.model.ApiResponse;
import io.swagger.model.TransactionRequest;
import io.swagger.service.TransactionService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    private static final Logger log = LoggerFactory.getLogger(TransactionsApiController.class);


//    @RequestMapping("/TestTransfer/{userId}")
//    public ResponseEntity createTransaction(@PathVariable("userId") Long userId){
//        return transactionService.MakeTransaction(userId);

//    @RequestMapping("/TestTransfer/{UserDTO}")
//    public User createTransaction(@PathVariable("UserDTO") User UserDTO){
//        User userobej = UserDTO;
//        return transactionService.MakeTransaction(userobej);
//    }


    @RequestMapping("/TestTransfer/{userId}")
    public ResponseEntity createTransaction(@PathVariable Long userId){
        return transactionService.MakeTransaction(userId);
    }

    public ResponseEntity transferMoney(
            @Parameter(in = ParameterIn.DEFAULT, description = "", required = true, schema = @Schema()) @Valid @RequestBody TransactionRequest body) {
        try {
            // require user to be authenticated
            //User currentUser = authenticationService.requireAuthenticated(request);

            // do the transaction
            //transactionService.transferMoney(currentUser, body.toTransaction());

            return new ResponseEntity(HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error", e);
            return new ResponseEntity<>(new ApiResponse<>("Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
