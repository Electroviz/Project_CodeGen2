package io.swagger.service;

import io.swagger.api.ApiException;
import io.swagger.controller.TransactionController;
import io.swagger.model.BankAccount;
import io.swagger.model.Transaction;
import io.swagger.model.User;
import io.swagger.repository.TransactionRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

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
        //Prevent transfers from a savings account to an account of not the same customer
        if(fromBankAccount.getAccountType().equals(BankAccount.AccountTypeEnum.SAVINGS)){
            if (!Objects.equals(fromBankAccount.getUserId(), toBankAccount.getUserId())){
                throw ApiException.badRequest("Transactions from a savings account should be to an account of the same customer");
            }
        }
        //Prevent transfers from an account to a savings account of not the same customer
        if (toBankAccount.getAccountType().equals(BankAccount.AccountTypeEnum.SAVINGS)) {
            if (!Objects.equals(fromBankAccount.getUserId(), toBankAccount.getUserId())) {
                throw ApiException.badRequest("Transactions to a savings account should be to an account of the same customer");
            }
        }


    }

}