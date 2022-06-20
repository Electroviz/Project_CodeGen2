package io.swagger.api;

import java.math.BigDecimal;

import io.swagger.jwt.JwtTokenProvider;
import io.swagger.model.ApiResponse;
import io.swagger.model.TransactionRequest;
import io.swagger.model.entity.User;
import io.swagger.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.threeten.bp.OffsetDateTime;
import io.swagger.model.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-12T15:22:53.754Z[GMT]")
@RestController
public abstract class TransactionsApiController implements TransactionsApi {

    private static final Logger log = LoggerFactory.getLogger(TransactionsApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @org.springframework.beans.factory.annotation.Autowired
    public TransactionsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

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

    public ResponseEntity<io.swagger.model.ApiResponse<Void>> transferMoney(
            @Parameter(in = ParameterIn.DEFAULT, description = "", required = true, schema = @Schema()) @Valid @RequestBody TransactionRequest body) {
        try {
            // require user to be authenticated
            User currentUser = (User) jwtTokenProvider.getAuthentication(String.valueOf(request));

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
