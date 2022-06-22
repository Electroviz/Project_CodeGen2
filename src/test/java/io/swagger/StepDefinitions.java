package io.swagger;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.model.BankAccount;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StepDefinitions {

    private HttpHeaders headers = new HttpHeaders();
    private String baseUrl = "http://localhost:8080/bankapi/";
    private RestTemplate template = new RestTemplate();
    private ResponseEntity<?> responseEntity;
    private String response;


    private String createUrl(String url) {
        return baseUrl + url;
    }

    @When("the user updates the bank account type using the iban")
    public void the_user_updates_the_bank_account_type_using_the_iban(String type, String iban) {
        headers.add("Content-type", "application/json");
        HttpEntity<?> entity = new HttpEntity<>("{\"iban\": \"NL28INGB00011122333\",\"accountType\": \"CURRENT\", \"balance\": \"200.0\", \"userId\": \"12\",\"accountStatus\": \"Active\"}", headers);
        responseEntity = template.exchange(
                createUrl("/putBankAccountType/"+type+iban),
                HttpMethod.PUT, entity, String.class
        );
    }

    @When("the employee updates the bank account status using the iban")
    public void the_employee_updates_the_bank_account_status_using_the_iban(String status, String iban) {
        headers.add("Content-type", "application/json");
        HttpEntity<?> entity = new HttpEntity<>("{\"iban\": \"NL28INGB00011122333\",\"accountType\": \"CURRENT\", \"balance\": \"200.0\", \"userId\": \"12\",\"accountStatus\": \"Active\"}", headers);
        responseEntity = template.exchange(
                createUrl("/putBankAccountStatus/"+status+iban),
                HttpMethod.PUT, entity, String.class
        );
    }

    @When("I try to retrieve the balance of an account by userid {string}")
    public void i_try_to_retrieve_the_balance_of_an_account_by_userid(String userid) {
        headers.add("Content-type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>("", headers);

        responseEntity = template.exchange(
                createUrl("totalBalance/"+userid),
                HttpMethod.GET, entity, Double.class
        );
    }

    @When("the employee creates new bank accounts for a user using userid {string}")
    public void the_employee_creates_new_bank_accounts_for_a_user_using_userid(String string) {
        headers.add("Content-type", "application/json");
        HttpEntity<?> entity = new HttpEntity<>("{\"iban\": \"NL28INGB00011122334\",\"accountType\": \"CURRENT\", \"balance\": \"0.0\", \"userId\": \"12\",\"accountStatus\": \"Active\"}", headers);
        responseEntity = template.exchange(
                createUrl("createBankAccount"),
                HttpMethod.POST, entity, String.class
        );
    }

    @When("the user retrieves all ibans in the system")
    public void the_user_retrieves_all_ibans_in_the_system() {
        headers.add("Content-type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>("", headers);

        responseEntity = template.exchange(
                createUrl("allBankAccounts"),
                HttpMethod.GET, entity, String.class
        );
    }

    @When("the user gives an existing iban get bank info")
    public void the_user_gives_an_existing_iban(String iban) {
        headers.add("Content-type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>("", headers);

        responseEntity = template.exchange(
                createUrl("getBankAccount/"+iban),
                HttpMethod.GET, entity, BankAccount.class
        );
    }

    @When("the user gives an existing name retrieve bank info")
    public void the_user_gives_an_existing_name(String fullname) {
        headers.add("Content-type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>("", headers);

        responseEntity = template.exchange(
                createUrl("getBankAccount/name/"+fullname),
                HttpMethod.GET, entity, BankAccount.class
        );
    }

    @When("the customer gives an existing current account iban and a valid ammount to deposit")
    public void the_customer_gives_an_existing_current_account_iban_and_a_valid_ammount_to_deposit(String iban, Double ammount) {
        headers.add("Content-type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>("{\""+ammount+"\": \"20.00\"}", headers);

        responseEntity = template.exchange(
                createUrl("getBankAccount/"+iban+"/Deposit"),
                HttpMethod.POST, entity, BankAccount.class
        );
    }

    @When("the customer gives an existing current account iban and a valid ammount to withdraw")
    public void the_customer_gives_an_existing_current_account_iban_and_a_valid_ammount_to_withdraw(String iban, Double ammount) {
        headers.add("Content-type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>("{\""+ammount+"\": \"20.00\"}", headers);

        responseEntity = template.exchange(
                createUrl("getBankAccount/"+iban+"/Deposit"),
                HttpMethod.POST, entity, BankAccount.class
        );
    }

    @When("the customer enters a valid iban to delete")
    public void the_customer_enters_a_valid_iban_to_delete(String iban) {
        headers.add("Content-type", "application/json");
        HttpEntity<?> entity = new HttpEntity<>("{\"iban\": \"NL28INGB00011122334\",\"accountType\": \"CURRENT\", \"balance\": \"0.0\", \"userId\": \"12\",\"accountStatus\": \"Active\"}", headers);

        responseEntity = template.exchange(
                createUrl("deleteAccount/"+iban),
                HttpMethod.DELETE, entity, BankAccount.class
        );
    }

    @Then("Requeststatus should return 202")
    public void status_of_request_is(Integer int1) {
        int response = responseEntity.getStatusCodeValue();
        Assert.assertEquals(java.util.Optional.ofNullable(int1), response);
    }

    @Then("Requeststatus should return 201")
    public void requeststatus_should_return(Integer int1) {
        int response = responseEntity.getStatusCodeValue();
        Assert.assertEquals(java.util.Optional.ofNullable(int1), response);
    }

    @Then("Requeststatus should return 200")
    public void requeststatus_returns(Integer int1) {
        int response = responseEntity.getStatusCodeValue();
        Assert.assertEquals(java.util.Optional.ofNullable(int1), response);
    }
}
