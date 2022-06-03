package io.swagger.controller;

import io.swagger.model.BankAccount;
import io.swagger.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;

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

    @RequestMapping(value = "/createBankAccount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity registerNewBankAccountController(@RequestBody BankAccount account){
        ResponseEntity<String> response = bankAccountService.CreateNewBankAccount();
        if (response.getStatusCode().isError()) {
            return new ResponseEntity(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity(response, HttpStatus.CREATED);
        }
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE,value="/get/{IBAN}")
    public ResponseEntity getBankAccountInfoByIbanController(@PathVariable("IBAN") String IBAN) {
        
    }
}
