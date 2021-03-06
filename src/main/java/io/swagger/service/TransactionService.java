package io.swagger.service;

import io.swagger.api.ApiException;

import io.swagger.model.BankAccount;
import io.swagger.model.Transaction;


import io.swagger.model.entity.TransactionEntity;
import io.swagger.model.entity.User;
import io.swagger.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public void transferMoney(User currentUser, Transaction transaction) throws ApiException {
        // configure the transaction's user performing id
        System.out.println(currentUser.getUsername() + transaction.getAmount());
        //transaction.setUserIDPerforming(Math.toIntExact(currentUser.getId()));



        // ensure the amount is valid
        if (transaction.getAmount().doubleValue() < 0) {
            throw ApiException.badRequest("Invalid amount");
        }

        // transactions cannot directly be made *to* the banks own account
        //authenticationService.requireNotSuperAccount(transaction.getTo());

        // get the from/to bank accounts
        BankAccount fromBankAccount = bankAccountService.GetBankAccountByIban(transaction.getFrom());
        BankAccount toBankAccount = bankAccountService.GetBankAccountByIban(transaction.getTo());

        // if the user is a customer, ensure the account belongs to them

        // Prevent transfers from a savings account to an account that is not of the same customer.
        if (fromBankAccount.getAccountType().equals(BankAccount.AccountTypeEnum.SAVINGS)) {
            if (!Objects.equals(fromBankAccount.getUserId(), toBankAccount.getUserId())) {
                    ResponseEntity.status(400).body("Transactions from a savings account should be to an account of the same customer");
            }
        }

        // Prevent transfers to a savings account to an account that is not of the same customer
        if (toBankAccount.getAccountType().equals(BankAccount.AccountTypeEnum.SAVINGS)) {
            if (!Objects.equals(fromBankAccount.getUserId(), toBankAccount.getUserId())) {
                ResponseEntity.status(400).body("Transactions to a savings account should be to an account of the same customer");
            }
        }

        // get the from user
        User fromUser = userService.findById(fromBankAccount.getUserId());
        if (fromUser == null){
            ApiException.badRequest("No such from user");
        }

        // get the from/to bank accounts
        // compute the from account balance
        BigDecimal fromAccountBalance = BigDecimal.valueOf(fromBankAccount.getBalance()).subtract(transaction.getAmount());
        // ----  start the  transaction ----

        // update the "to" bank balance
        BigDecimal toBalance = BigDecimal.valueOf(toBankAccount.getBalance()).add(transaction.getAmount());
        toBankAccount.setBalance(toBalance.doubleValue());
        bankAccountService.updateBankAccount(currentUser, toBankAccount.getIban(), toBankAccount, false);
        // update the "from" balance
        fromBankAccount.setBalance(fromAccountBalance.doubleValue());

        bankAccountService.updateBankAccount(currentUser, fromBankAccount.getIban(), fromBankAccount);
        // save the transaction
        TransactionEntity transactionEntity = toEntity(transaction);
        transactionRepository.save(transactionEntity);
        // ---- end the transaction ----
    }


    public BigDecimal getTotalDayTransactions(User user) {
        BigDecimal total = BigDecimal.valueOf(0);

        // get all the transactions
        List<TransactionEntity> allTransactions = transactionRepository.findAll();
        for (TransactionEntity transaction : allTransactions) {
            // check if the current transaction belongs to the given user.
            if (!Objects.equals(transaction.getUserIDPerforming(), user.getId())) {
                continue;
            }
            // check if the current transaction belongs to current date
//            LocalDate transactionDate = transaction.getTimestamp().toLocalDateTime().toLocalDate();
//            LocalDate today = LocalDate.now();
//            if (!transactionDate.equals(today)) {
//                continue;
//            }

            // this transaction is of today and was initiated by this user, add the amount to
            // the current totals
            total = total.add(transaction.getAmount());
        }

        return total;
    }



    public TransactionEntity toEntity(Transaction transaction) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());


        TransactionEntity entity = new TransactionEntity();
        entity.setAmount(transaction.getAmount());
        entity.setToAccount(transaction.getTo());
        entity.setFromAccount(transaction.getFrom());
        entity.setUserIDPerforming(new Long(transaction.getUserIDPerforming()));
        entity.setDescription(transaction.getDescription());
        entity.setTimestamp(timestamp);
        return entity;
    }
}
