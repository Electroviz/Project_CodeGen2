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
import org.threeten.bp.OffsetDateTime;

import java.util.ArrayList;
import java.util.Date;
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


    //Melle
    public boolean IbanHasSufficientMoney(String iban, Double amountToTransfer) {
        BankAccount BankAccountByIban = bankAccountService.GetBankAccountByIban(iban);
        if(BankAccountByIban != null && BankAccountByIban.getBalance() - amountToTransfer >= 0) return true;
        else return false;
    }

    //Melle
    public List<Transaction> GetTransactionsInBetweenDate(Date firstDate, Date secondDate, Integer userId) {
        List<Transaction> allTransactions = transactionRepository.findAll();
        List<Transaction> correctTransactions = new ArrayList<>();
        for (int i = 0; i < allTransactions.size(); i++) {
            Transaction transaction = allTransactions.get(i);
            if((isAfterDate(firstDate,transaction.getTimestamp()) || datesAreEqual(firstDate,transaction.getTimestamp()) ) &&
                    (isBeforeDate(secondDate,transaction.getTimestamp()) || datesAreEqual(secondDate,transaction.getTimestamp()))
                ) {
                if(userId == null || userId < 0) correctTransactions.add(transaction);
                    //from iban used because otherwise employees transaction history will also contain performed transactions by the employee
                else if(bankAccountService.GetBankAccountByIban(transaction.getFrom()).getUserId().intValue() == userId) correctTransactions.add(transaction);
            }
            else if((isAfterDate(transaction.getTimestamp(),secondDate) || datesAreEqual(transaction.getTimestamp(),secondDate)) &&
                    (isBeforeDate(transaction.getTimestamp(), firstDate) || datesAreEqual(transaction.getTimestamp(), firstDate))) {
                if(userId == null || userId < 0) correctTransactions.add(transaction);
                else if(bankAccountService.GetBankAccountByIban(transaction.getFrom()).getUserId().intValue() == userId) correctTransactions.add(transaction);
            }
        }

        return correctTransactions;
    }

    private boolean datesAreEqual(Date date1, Date date2) {
        int result = date1.compareTo(date2);

        if(result == 0) return true;
        else return false;
    }

    private boolean isBeforeDate(Date date1, Date transaction) {
        int result = date1.compareTo(transaction);

        if(result > 0) return true;
        else return false;
    }

    private boolean isAfterDate(Date date1, Date transaction) {
        int result = date1.compareTo(transaction);

        if(result < 0) return true;
        else return false;
    }

    //Melle
    public List<Transaction> GetTransactionByIbans(String fromIban, String toIban) {
        return QuerySimulatorFindTransactionByIbans(fromIban,toIban);
        //return transactionRepository.QuerySimulatorFindTransactionByIbans(fromIban,toIban);
    }

    private List<Transaction> QuerySimulatorFindTransactionByIbans(String fromIban, String toIban) {
        //Because the custom queries syntax is not working (already tried every possible solution but it doesn't work.)
        List<Transaction> allTransactions = transactionRepository.findAll();
        List<Transaction> correctTransactions = new ArrayList<>();
        for(int i = 0; i < allTransactions.size(); i++) {
            Transaction transaction = allTransactions.get(i);
            if(Objects.equals(transaction.getFrom(), fromIban) && Objects.equals(transaction.getTo(), toIban)) {
                //combination from to is correct and found
                correctTransactions.add(transaction);
            }
            else if(Objects.equals(transaction.getFrom(), toIban) && Objects.equals(transaction.getTo(), fromIban)) {
                //combination in wrong order is found
                correctTransactions.add(transaction);
            }
        }

        return correctTransactions;
    }

    //Melle
    public List<Transaction> GetTransactionByRelationship(String iban, Double num, String comparison) {
        if(Objects.equals(comparison, "equal")) return SimulateQueryTransactionBalance(iban, num, comparison);
        else if(Objects.equals(comparison, "smaller")) return SimulateQueryTransactionBalance(iban, num, comparison);
        else return SimulateQueryTransactionBalance(iban, num, comparison); //if(comparison == "bigger")
    }

    //Melle
    private List<Transaction> SimulateQueryTransactionBalance(String iban, Double amount, String comparison) {
        List<Transaction> allTransactions = transactionRepository.findAll();
        List<Transaction> correctTransactions = new ArrayList<>();

        for(Transaction t : allTransactions) {
            if(t.getTo().equals(iban) || t.getFrom().equals(iban)) {
                if(comparison.equals("equal")) {
                    if(Double.compare(amount, t.getAmount()) == 0) correctTransactions.add(t);
                }
                else if(comparison.equals("smaller")) {
                    if(Double.compare(amount, t.getAmount()) > 0) correctTransactions.add(t);
                }
                else {
                    if(Double.compare(amount, t.getAmount()) < 0) correctTransactions.add(t);
                }
            }
        }

        return correctTransactions;
    }

    //Melle
    public boolean TransferMoneyFromToIban(String toIban, String fromIban, Double amount, Integer userIdPerforming) {
        //check if the bankaccount is not a savings account or a closed account.
        if(bankAccountService.BankAccountsTransactionIsPossible(fromIban,toIban)) {
            BankAccount fromBankAccount = bankAccountService.GetBankAccountByIban(fromIban);
            BankAccount toBankAccount = bankAccountService.GetBankAccountByIban(toIban);

            if(fromBankAccount == null || toBankAccount == null) return false;
            else if(amount <= 0.0) return false;

            //this if statement exists because of Dummy data purpose
            if( userService.getUserById(fromBankAccount.getUserId().longValue()) != null) {
                //CHECK IF THE TRANSACTION LIMIT IS BEING EXCEEDED
                double transactionLimit = userService.getUserById(fromBankAccount.getUserId().longValue()).getTransactionLimit().doubleValue();
                if (amount > transactionLimit && transactionLimit != 0) return false;
            }

            //CHECK IF THE BALANCE IS NOT BECOMMING LOWER THE THE PRE DEFINED ABSOLUTE LIMIT FOR THE USER
            if(fromBankAccount.getBalance() - amount < fromBankAccount.getAbsoluteLimit()) return false;

            //DAY LIMIT CHECK NECESSARY


            fromBankAccount.setBalance(fromBankAccount.getBalance() - amount);
            bankAccountService.SaveBankAccount(fromBankAccount);

            toBankAccount.setBalance(toBankAccount.getBalance() + amount);
            bankAccountService.SaveBankAccount(toBankAccount);

            SaveTransaction(createNewTransaction(fromIban,toIban,userIdPerforming,amount,"TRANSACTION"));
            return true;
        }
        return false;
    }

    //Melle
    public List<Transaction> GetAllTransactionsFromDatabase() {
        return this.transactionRepository.findAll();
    }

//    String from,
//    String to,
//    Int userIDPerforming,
//    Double amount
//    OffsetDateTime timestamp
//    String description
    public Transaction createNewTransaction(String fromIban, String toIban, Integer userPerformingId, Double amount, String description) {
        Transaction newTrans = new Transaction();
        newTrans.setFrom(fromIban);
        newTrans.setTo(toIban);
        newTrans.setUserIDPerforming(userPerformingId);
        newTrans.setAmount(amount);
        newTrans.setDescription(description);
        newTrans.setTimestamp(new Date(System.currentTimeMillis())); //timestamp

        return newTrans;
    }

    //Melle
    public boolean WithdrawOrDepositMoney(String iban, Double amount, boolean isWithdraw, Integer userIdPerforming) {
        BankAccount ba = bankAccountService.GetBankAccountByIban(iban);

        if(amount <= 0) return false;
        else if(ba != null) {
            if(ba.getAccountType() != BankAccount.AccountTypeEnum.SAVINGS && ba.getAccountStatus() != BankAccount.AccountStatusEnum.CLOSED) {
                if(isWithdraw == false) {
                    //is depositing the amount
                    ba.setBalance(ba.getBalance() + amount);
                    bankAccountService.SaveBankAccount(ba);
                }
                else if(isWithdraw == true && ba.getBalance() >= amount) {
                    //is withdrawing the money and has sufficient money to perform this transaction
                    ba.setBalance(ba.getBalance() - amount);
                    bankAccountService.SaveBankAccount(ba);
                }
                else return false;
            }
            else return false;
        }
        else return false;

        if(!isWithdraw) transactionRepository.save(createNewTransaction(iban,iban,userIdPerforming,amount,"DEPOSIT"));
        else transactionRepository.save(createNewTransaction(iban,iban,userIdPerforming,amount,"WITHDRAW"));
        return true;
    }

    public void SaveTransaction(Transaction t) {
        this.transactionRepository.save(t);
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
