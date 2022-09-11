package io.swagger.api;

import java.math.BigDecimal;

import io.swagger.annotations.Api;
import io.swagger.enums.UserRoleEnum;
import io.swagger.model.entity.User;
import io.swagger.service.BankAccountService;
import io.swagger.service.TransactionService;
import io.swagger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.threeten.bp.OffsetDateTime;
import io.swagger.model.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-12T15:22:53.754Z[GMT]")
@RestController
@Api(tags= {"Transactions"})
public class TransactionsApiController implements TransactionsApi {


    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    private static final Logger log = LoggerFactory.getLogger(TransactionsApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public TransactionsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }
    /*

    public ResponseEntity<List<Transaction>> getHistoryByIban(@Parameter(in = ParameterIn.PATH, description = "The IBAN", required=true, schema=@Schema()) @PathVariable("iban") String iban,@Min(0)@Parameter(in = ParameterIn.QUERY, description = "number of records to skip for pagination" ,schema=@Schema(allowableValues={  }
)) @Valid @RequestParam(value = "skip", required = false) Integer skip,@Min(0) @Max(50) @Parameter(in = ParameterIn.QUERY, description = "maximum number of records to return" ,schema=@Schema(allowableValues={  }, maximum="50"
)) @Valid @RequestParam(value = "limit", required = false) Integer limit,@Parameter(in = ParameterIn.QUERY, description = "filter transactions from this date" ,schema=@Schema()) @Valid @RequestParam(value = "startDateTime", required = false) OffsetDateTime startDateTime,@Parameter(in = ParameterIn.QUERY, description = "filter transactions to this date" ,schema=@Schema()) @Valid @RequestParam(value = "endDateTime", required = false) OffsetDateTime endDateTime,@Parameter(in = ParameterIn.QUERY, description = "filter transactions by specific IBAN" ,schema=@Schema()) @Valid @RequestParam(value = "specificIBAN", required = false) String specificIBAN,@Parameter(in = ParameterIn.QUERY, description = "filter transactions equel to amount entered" ,schema=@Schema()) @Valid @RequestParam(value = "equelTo", required = false) BigDecimal equelTo,@Parameter(in = ParameterIn.QUERY, description = "filter transactions lower to amount entered" ,schema=@Schema()) @Valid @RequestParam(value = "lowerThen", required = false) BigDecimal lowerThen,@Parameter(in = ParameterIn.QUERY, description = "filter transactions higher to amount entered" ,schema=@Schema()) @Valid @RequestParam(value = "higherThen", required = false) BigDecimal higherThen) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<Transaction>>(objectMapper.readValue("[ {\n  \"amount\" : 1,\n  \"userIDPerforming\" : 1234,\n  \"description\" : \"Payment request Anna\",\n  \"from\" : \"NLxxINHO0xxxxxxxxx\",\n  \"to\" : \"NLxxINHO0xxxxxxxxx\",\n  \"transactionID\" : 123,\n  \"timestamp\" : \"2000-01-23T04:56:07.000+00:00\"\n}, {\n  \"amount\" : 1,\n  \"userIDPerforming\" : 1234,\n  \"description\" : \"Payment request Anna\",\n  \"from\" : \"NLxxINHO0xxxxxxxxx\",\n  \"to\" : \"NLxxINHO0xxxxxxxxx\",\n  \"transactionID\" : 123,\n  \"timestamp\" : \"2000-01-23T04:56:07.000+00:00\"\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<Transaction>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<Transaction>>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Void> transferMoney(@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody Transaction body) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }
    */

    public ResponseEntity transferMoney(@PathVariable("fromIban") String fromIban, @PathVariable("toIban") String toIban, @PathVariable("amount") Double amount) {
        User u = this.getLoggedInUser();

        boolean canPerform = false;
        if(u.getRole() == UserRoleEnum.ROLE_EMPLOYEE) canPerform = true;
        else {
            if(Objects.equals(bankAccountService.GetBankAccountByIban(fromIban).getUserId(), u.getId())) canPerform = true;
        }

        if(canPerform) {
            if (transactionService.IbanHasSufficientMoney(fromIban, amount)) {
                if (transactionService.TransferMoneyFromToIban(toIban, fromIban, amount, u.getId().intValue()))
                    return ResponseEntity.status(200).body(bankAccountService.GetBankAccountByIban(fromIban));
                else return ResponseEntity.status(400).body("Sending Bank Account is invalid");
            } else return ResponseEntity.status(400).body(null);
        }
        else return ResponseEntity.status(401).body(null);
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity GetAllTransactions() {
        return ResponseEntity.status(200).body(transactionService.GetAllTransactionsFromDatabase());
    }

    public ResponseEntity Withdraw(@PathVariable("iban") String iban, @PathVariable("amount") Double amount) {
        User u = this.getLoggedInUser();

        boolean canPerform = false;
        if(Objects.equals(u.getRole(), UserRoleEnum.ROLE_EMPLOYEE)) canPerform = true;
        else {
            if(Objects.equals(bankAccountService.GetBankAccountByIban(iban).getUserId(), u.getId())) canPerform = true;
            else return ResponseEntity.status(401).body(null);
        }

        if(canPerform) {
            if (transactionService.WithdrawOrDepositMoney(iban, amount, true, u.getId().intValue())) {
                return ResponseEntity.status(200).body("Success");
            } else {
                return ResponseEntity.status(400).body("Failed to withdraw");
            }
        }
        else return ResponseEntity.status(401).body(null);
    }

    public ResponseEntity Deposit(@PathVariable("iban") String iban, @PathVariable("amount") Double amount) {
        User u = this.getLoggedInUser();

        boolean canPerform = false;
        if(Objects.equals(u.getRole(), UserRoleEnum.ROLE_EMPLOYEE)) canPerform = true;
        else {
            if(Objects.equals(bankAccountService.GetBankAccountByIban(iban).getUserId(), u.getId())) canPerform = true;
            else return ResponseEntity.status(401).body(null);
        }

        if(canPerform) {
            if (transactionService.WithdrawOrDepositMoney(iban, amount, false, u.getId().intValue())) {
                return ResponseEntity.status(200).body("Success");
            } else {
                return ResponseEntity.status(400).body("Failed to deposit");
            }
        }
        else return ResponseEntity.status(401).body(null);
    }

    // test

    //Melle

    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity GetTransactionsByDate(@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        List<Transaction> correctTransactions = transactionService.GetTransactionsInBetweenDate(format.parse(fromDate),format.parse(toDate),null);

        if(correctTransactions == null || correctTransactions.size() == 0) return ResponseEntity.status(204).body("No transactions in between this date");
        else return ResponseEntity.status(200).body(correctTransactions);
    }

    //Melle
    public ResponseEntity GetTransactionsByDateAndUser(@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate, @PathVariable("userId") Integer userId) throws ParseException {
        User u = this.getLoggedInUser();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        List<Transaction> correctTransactions = new ArrayList<>();
        if(Objects.equals(u.getRole(), UserRoleEnum.ROLE_EMPLOYEE)) {
            correctTransactions = transactionService.GetTransactionsInBetweenDate(format.parse(fromDate),format.parse(toDate),userId);
        }
        else correctTransactions = transactionService.GetTransactionsInBetweenDate(format.parse(fromDate),format.parse(toDate),u.getId().intValue());


        if(correctTransactions == null || correctTransactions.size() == 0) return ResponseEntity.status(204).body("No transactions in between this date");
        else return ResponseEntity.status(200).body(correctTransactions);
    }

    //Melle
    public ResponseEntity GetTransactionByIbans(@PathVariable("fromIban") String fromIban, @PathVariable("toIban") String toIban) {
        User u = this.getLoggedInUser();

        List<Transaction> correctTransactions = new ArrayList<>();
        //only let a customer execute this code if from or to iban is his own iban
        if(Objects.equals(u.getRole(), UserRoleEnum.ROLE_EMPLOYEE)) {
            correctTransactions = transactionService.GetTransactionByIbans(fromIban,toIban);
        }
        else {
            if(bankAccountService.CheckIbanBelongsToUser(u.getId().intValue(),fromIban) || bankAccountService.CheckIbanBelongsToUser(u.getId().intValue(),fromIban)) {
                //user owns one of the ibans so get the transactions
                correctTransactions = transactionService.GetTransactionByIbans(fromIban,toIban);
            }
            else return ResponseEntity.status(401).body(null);
        }

        correctTransactions = transactionService.GetTransactionByIbans(fromIban,toIban);
        if(correctTransactions == null ) return ResponseEntity.status(400).body(null);
        else return ResponseEntity.status(200).body(correctTransactions);
    }

    //Melle
    public ResponseEntity GetTransactionByAmountEqualToNum (@PathVariable("iban") String iban, @PathVariable("amount") Double amount) {
        User u = this.getLoggedInUser();

        List<Transaction> result = new ArrayList<>();

        if(Objects.equals(u.getRole(), UserRoleEnum.ROLE_EMPLOYEE)) {
            result = transactionService.GetTransactionByRelationship(iban,amount,"equal");
        }
        else {
            if(bankAccountService.CheckIbanBelongsToUser(u.getId().intValue(),iban)) {
                result = transactionService.GetTransactionByRelationship(iban,amount,"equal");
            }
            else return ResponseEntity.status(401).body(null);
        }

        if(result.size() == 0) return ResponseEntity.status(204).body(result);
        else return ResponseEntity.status(200).body(result);
    }


    public ResponseEntity GetTransactionByAmountSmallerToNum (@PathVariable("iban") String iban, @PathVariable("amount") Double amount) {
        User u = this.getLoggedInUser();

        List<Transaction> result = new ArrayList<>();

        if(Objects.equals(u.getRole(), UserRoleEnum.ROLE_EMPLOYEE)) {
            result = transactionService.GetTransactionByRelationship(iban,amount,"smaller");
        }
        else {
            if(bankAccountService.CheckIbanBelongsToUser(u.getId().intValue(),iban)) {
                result = transactionService.GetTransactionByRelationship(iban,amount,"smaller");
            }
            else return ResponseEntity.status(401).body(null);
        }

        if(result.size() == 0) return ResponseEntity.status(204).body(result);
        else return ResponseEntity.status(200).body(result);
    }



    public ResponseEntity GetTransactionByAmountBiggerToNum (@PathVariable("iban") String iban, @PathVariable("amount") Double amount) {
        User u = this.getLoggedInUser();

        List<Transaction> result = new ArrayList<>();

        if(Objects.equals(u.getRole(), UserRoleEnum.ROLE_EMPLOYEE)) {
            result = transactionService.GetTransactionByRelationship(iban,amount,"bigger");
        }
        else {
            if(bankAccountService.CheckIbanBelongsToUser(u.getId().intValue(),iban)) {
                result = transactionService.GetTransactionByRelationship(iban,amount,"bigger");
            }
            else return ResponseEntity.status(401).body(null);
        }

        if(result.size() == 0) return ResponseEntity.status(204).body(result);
        else return ResponseEntity.status(200).body(result);
    }


    private User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.getUserById(userService.getUserIdByUsername(authentication.getName()));
    }



}
