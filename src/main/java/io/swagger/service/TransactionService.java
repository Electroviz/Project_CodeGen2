package io.swagger.service;

import io.swagger.api.ApiException;
import io.swagger.controller.TransactionController;
import io.swagger.model.BankAccount;
import io.swagger.model.Transaction;
import io.swagger.model.User;
import io.swagger.repository.TransactionRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    @Autowired
    private TransactionRespository transactionRespository;
    @Autowired
    private BankAccountService bankAccountService;


    public void createTransaction(User currentUser, Transaction transaction) throws ApiException {

        //Check if amount is valid
        if (transaction.getAmount().doubleValue() <=0)
            throw ApiException.badRequest("Invalid amount");
        //Check the iban from the sender + receiver
        BankAccount fromBankAccount = bankAccountService.GetBankAccountByIban(transaction.getFrom());
        BankAccount toBankAccount = bankAccountService.GetBankAccountByIban(transaction.getTo());


    }

}
