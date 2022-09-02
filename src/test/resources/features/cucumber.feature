Feature: Accounts Tests

  Scenario: If an account type is to be updated the program should return code 200
    When the user updates the bank account type using the iban
    Then Requeststatus should return 200

  Scenario: If an account status is to be updated the program should return code 200
    When the employee updates the bank account status using the iban
    Then Requeststatus should return 200

  Scenario: Retrieving the account balance using the user id
    When I try to retrieve the balance of an account by userid "12"
    Then Requeststatus should return 200

  Scenario: creating bank accounts for a user using user id
    When the employee creates new bank accounts for a user using userid "12"
    Then Requeststatus should return 201

  Scenario: Retrieve all bank accounts in the system
    When the user retrieves all ibans in the system
    Then Requeststatus should return 200

  Scenario: get the info of a bank account by using an iban
    When the user gives an existing iban
    Then Requeststatus should return 202

  Scenario: get the info of a bank account by using the full name of the user linked to the account
    When the user gives an existing name
    Then Requeststatus should return 202

  Scenario: A customer is making a deposit to his account
    When the customer gives an existing current account iban and a valid ammount to deposit
    Then Status of request is 202

  Scenario: A customer is making a withdrawal to his account
    When the customer gives an existing current account iban and a valid ammount to withdraw
    Then Status of request is 202

  Scenario: the employee tries to delete an account using an iban
    When the customer enters a valid iban to delete
    Then Status of request is 200