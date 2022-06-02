package io.swagger.controller;

import io.swagger.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BankAccountController {

    @Autowired
    private BankAccountService bankAccountService;

    //voorbeeld van een request
    @RequestMapping(method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE,value = "/all")
    public ResponseEntity getAccountByIBANController(){
        if (bankAccountService.GetAllBankAccounts().getStatusCode().isError()){
            return ResponseEntity.status(400).body("Bad Request");
        }
        else {
            return ResponseEntity.status(201).body(bankAccountService.GetAllBankAccounts());
        }
    }
}
