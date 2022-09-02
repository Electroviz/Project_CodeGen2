package io.swagger.service;

import io.swagger.api.ApiException;
import io.swagger.model.BankAccount;
import io.swagger.model.Transaction;
import io.swagger.model.entity.User;
import io.swagger.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Objects;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private BankAccountService bankAccountService;
    @Autowired
    private UserService userService;


    public boolean IbanHasSufficientMoney(String iban, Double amountToTransfer) {
        BankAccount BankAccountByIban = bankAccountService.GetBankAccountByIban(iban);
        if(BankAccountByIban != null && BankAccountByIban.getBalance() - amountToTransfer >= 0) return true;
        else return false;
    }

    public boolean TransferMoneyFromToIban(String toIban, String fromIban, Double amount) {
        BankAccount fromBankAccount = bankAccountService.GetBankAccountByIban(fromIban);
        BankAccount toBankAccount = bankAccountService.GetBankAccountByIban(toIban);

        fromBankAccount.setBalance(fromBankAccount.getBalance() - amount);
        bankAccountService.SaveBankAccount(fromBankAccount);

        toBankAccount.setBalance(toBankAccount.getBalance() + amount);
        bankAccountService.SaveBankAccount(toBankAccount);

        return true;
    }




//    public void createTransaction(User currentUser, Transaction transaction) throws ApiException {
//
//        //Check if amount is valid
//        if (transaction.getAmount().doubleValue() <=0)
//            throw ApiException.badRequest("Invalid amount");
//
//        //Check the iban from the sender + receiver
//        BankAccount fromBankAccount = bankAccountService.GetBankAccountByIban(transaction.getFrom());
//        BankAccount toBankAccount = bankAccountService.GetBankAccountByIban(transaction.getTo());
//
//        //Prevent transfers from a savings account to an account of not the same customer
//        if(fromBankAccount.getAccountType().equals(BankAccount.AccountTypeEnum.SAVINGS)){
//            if (!Objects.equals(fromBankAccount.getUserId(), toBankAccount.getUserId())){
//                throw ApiException.badRequest("Transactions from a savings account should be to an account of the same customer");
//            }
//        }
//
//        //Prevent transfers from an account to a savings account of not the same customer
//        if (toBankAccount.getAccountType().equals(BankAccount.AccountTypeEnum.SAVINGS)) {
//            if (!Objects.equals(fromBankAccount.getUserId(), toBankAccount.getUserId())) {
//                throw ApiException.badRequest("Transactions to a savings account should be to an account of the same customer");
//            }
//        }
//
//        //Get the from user (the sender)
//       //User fromUser = userService.findById(fromBankAccount.getUserId()).orElseThrow(() -> ApiException.badRequest("No such from user"));
//
//
//
//
//
//        //Things to implement!!!!
//        //Get the from user (the sender)
//        //Check transaction limit of the user
//        //Get the sender and receiver bank iban
//        //Check the new balance is not less than the absolut limit
//        //The amount of the transaction should not exceed the day limit
//        //Compute the total day transactions if this transaction were to go through
//
//        //Update the senders bank account
//        //UPdate the receivers bank account
//        //Save the transaction
//
//
//    }

}
