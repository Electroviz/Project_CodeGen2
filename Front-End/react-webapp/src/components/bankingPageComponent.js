import React from 'react';
import { useState } from 'react';
import axios from 'axios';

const bankingPageComponent = () => {
    var instance = axios.create();
    var jwtToken = getCookie("jwt");
    var userId = -1;
    var userRights = "none";
    var allBankAccountsInfo = null;
    var loggedInUser = null;


    const GetIbanByFullName = (e) => {
        if(jwtToken == "") {
            let name = "jwt" + "=";
            let decodedCookie = decodeURIComponent(document.cookie);
            let ca = decodedCookie.split(';');
            for(let i = 0; i <ca.length; i++) {
            let c = ca[i];
            while (c.charAt(0) == ' ') {
                c = c.substring(1);
            }
            if (c.indexOf(name) == 0) {
                jwtToken = c.substring(name.length, c.length);
            }
            }
        }

        instance.get('http://localhost:8080/api/getBankAccount/name/'+ document.getElementById("inputFullNameToIban").value, {
            headers: {
                'Content-Type': null,
                Authorization: "Bearer " + jwtToken,
            }
        })
        .then(res => {
            if(res.status >= 200 && res.status <= 300) {
                if(res.data == "") document.getElementById("fullNameToIbanResult").innerHTML = "Has no bank account";
                else document.getElementById("fullNameToIbanResult").innerHTML = res.data;
            }
            else {
                document.getElementById("fullNameToIbanResult").innerHTML = "Not Found"
            }
            
        })
        .catch((error) => console.log(error));
        
    }

    const CreateBankAccountsForUser = async (e) => {
        if(userId != -1) {
            axios.interceptors.request.use(
                config => {
                    config.headers.Authorization = 'Bearer ' + getCookie("jwt");
                    return config;
                },
                error => {
                    return Promise.reject(error);
                }
            )

            const result = await axios.post('http://localhost:8080/api/initBankAccounts/' + userId);
            if(result.status == 200) window.location.reload();
            else console.log(result);
        }
    }

    async function GetAndSetUserIdByJwtToken() {
        const jwtToken = getToken();

        const result = await axios.get('http://localhost:8080/api/user/getUserIdJwtValidation');

        if(result.status == 200) {
            document.cookie = "userId = " + result.data;
            userId = result.data;
        }
        else window.location.href = "/";
        

    }

    const SetBankingInfoCurrentUser = () => {
        //first get and set the User id by JWT token, if JWT token is not valid anymore return to login page
        //get jwt token if not set yet
        //set total balance, daily limit, transaction limit, set his info voor CURRENT & SAVINGS IBAN balance, absolute limit

        setTimeout(async function() {
            axios.interceptors.request.use(
                config => {
                    config.headers.Authorization = 'Bearer ' + getCookie("jwt");
                    return config;
                },
                error => {
                    return Promise.reject(error);
                }
            );
    
            GetAndSetUserIdByJwtToken();
    
            //set banking info
            try {
                const bankingInfoRequest = await axios.get('http://localhost:8080/api/bankAccounts/' + getCookie("userId")); 
                if(bankingInfoRequest.status == 200) {
                    //set the banking info
                    var bankingInfoData = bankingInfoRequest.data;
                    allBankAccountsInfo = bankingInfoData;
                    for(var i = 0; i < bankingInfoData.length; i++) {
                        var bankInfo = bankingInfoData[i];
                        if(bankInfo["accountType"] == "Current") {
                            document.getElementById("currentIban").innerHTML = bankInfo["iban"];
                            document.getElementById("currentBalance").innerHTML = bankInfo["balance"];
                            document.getElementById("currentAbsoluteLimit").innerHTML = bankInfo["absolute limit"];
                            console.log(bankInfo);
                        }
                        else {
                            document.getElementById("savingIban").innerHTML = bankInfo["iban"];
                            document.getElementById("savingBalance").innerHTML = bankInfo["balance"];
                            document.getElementById("savingAbsoluteLimit").innerHTML = bankInfo["absolute limit"];
                        }
                    }
                    
                    var totalBalance = 0;
                    for(var i = 0; i < bankingInfoData.length; i++) {
                        totalBalance = (totalBalance + bankingInfoData[i]["balance"]);
                    }
    
                    document.getElementById("displayTotalBalance").innerHTML = "€ " + totalBalance;
    
                    // console.log(bankingInfoData);
                }
            }
            catch (error) {
                document.getElementById("displayTotalBalance").innerHTML = "€ 0";

                //show button to create bank accounts for user ID
                document.getElementById("createBankAccountsButt").style.visibility = "visible"; 
                document.getElementById("currentUsersBankingInfo").remove();
            }


            //set user info
            try {
                ///get/{id}
                const userResult = await axios.get('http://localhost:8080/api/user/get/' + getCookie("userId"));

                if(userResult.status >= 200 && userResult.status < 300) {
                    var userData = userResult.data;
                    document.getElementById("currentUserFullName").innerHTML = userData["fullname"];
                    document.getElementById("displayDailyLimit").innerHTML = userData["dayLimit"];
                    document.getElementById("displayTransactionLimit").innerHTML = userData["trasnactionLimit"];

                    loggedInUser = userData; 

                    var userRights = userData["role"];
                    if(userRights != "ROLE_EMPLOYEE") document.getElementById("EmployeeContainer").remove();
                    else {
                                //load all info for the employee
                                loadAllUsersWithoutBankAccounts();
                                loadAllBankAccountInfo();
                                loadAllTransactionInfo();
                                loadAllUsersInfo();
                    }
                }
            }
            catch (error) {
                document.getElementById("EmployeeContainer").remove();
            }
        },200);
    }

    const ChangeBankAccountStatus = async (e) => {
        var iban = document.getElementById("changeStatusIban").value;
        var status = document.getElementById("changeStatus").value.toUpperCase();

        try {
            const result = await axios.put('http://localhost:8080/api/putBankAccountStatus/' + status + '/' + iban);
            if(result.status >= 200 && result.status < 300) {
                loadAllBankAccountInfo();
            }
            else {
                alert("Failed to change the status");
            }
        }
        catch (error) {
            alert("Failed to change the status");
        }
    }

    const FindTransactionHistoryByComparison = async (e) => {
        try {
            var amount = document.getElementById('transactionComparisonAmount').value;
            var comparison = document.getElementById('transactionComparisonType').value;
            var result = null;

            if(comparison == "=") result = await axios.get('http://localhost:8080/api/transactions/byAmountIsEqual/' + GetLoggedInUsersCurrentIban() + '/' + amount);
            else if(comparison == "<") result = await axios.get('http://localhost:8080/api/transactions/byAmountIsSmaller/' + GetLoggedInUsersCurrentIban() + '/' + amount);
            else if(comparison == ">") result = await axios.get('http://localhost:8080/api/transactions/byAmountIsBigger/' + GetLoggedInUsersCurrentIban() + '/' + amount);
            
            if(result != null && result.status >= 200 && result.status < 300) {
                if(result.data.length > 0) {
                    document.getElementById("transactionHistoryWithIbanWrapper").style.display = "block";
                    document.getElementById('transactionHistoryWithIbanText').innerHTML = "Transactions by comparison " + comparison + " with amount: €" + amount;

                    var container = document.getElementById("allTransactionHistoryWithIban");
                    container.innerHTML = "";
    
                    for(let i = 0; i < result.data.length; i++) {
                        var bankAccInfoElm = document.createElement("p");
                        bankAccInfoElm.style.textAlign = "center";
                        bankAccInfoElm.style.marginBottom = "0.8rem";
                        bankAccInfoElm.style.borderBottom = "0.1rem solid black";
                        bankAccInfoElm.innerHTML = 
                        "<b>from:</b> " + result.data[i]["from"] + ", " +
                        "<b>to:</b> " + result.data[i]["to"] + ", " +
                        "<b>amount:</b> €" + result.data[i]["amount"] + ", " +
                        "<b>date:</b> " + result.data[i]["timestamp"] + ", " +
                        "<b>description:</b> " + result.data[i]["description"];
                        
                        
                        container.append(bankAccInfoElm);
                    }
                }
                else document.getElementById('transactionHistoryWithIbanText ').innerHTML = "No result found for comparison";
            }
            else alert('Could not find a transactions you where looking for.');
        }
        catch (error) {
            console.log(result + error);
            alert('Something went wrong when finding the transactions you where looking for.');
        }
    }

    const FindTransactionHistory = async (e) => {
        try {
            var transactionHistoryIban = document.getElementById("transactionHistoryIban").value;
            const result = await axios.get('http://localhost:8080/api/transactions/byIbans/' + GetLoggedInUsersCurrentIban() + '/' + transactionHistoryIban);
            // /transactions/byIbans/{fromIban}/{toIban}
            if(result.status >= 200 && result.status < 300) { 
                document.getElementById("transactionHistoryWithIbanWrapper").style.display = "block";
                document.getElementById("transactionHistoryWithIbanText").innerHTML = "See transaction history with iban: <u>" + transactionHistoryIban + '</u>';

                var container = document.getElementById("allTransactionHistoryWithIban");
                container.innerHTML = "";

                for(let i = 0; i < result.data.length; i++) {
                    var bankAccInfoElm = document.createElement("p");
                    bankAccInfoElm.style.textAlign = "center";
                    bankAccInfoElm.style.marginBottom = "0.8rem";
                    bankAccInfoElm.style.borderBottom = "0.1rem solid black";
                    bankAccInfoElm.innerHTML = 
                    "<b>from:</b> " + result.data[i]["from"] + ", " +
                    "<b>to:</b> " + result.data[i]["to"] + ", " +
                    "<b>amount:</b> €" + result.data[i]["amount"] + ", " +
                    "<b>date:</b> " + result.data[i]["timestamp"] + ", " +
                    "<b>description:</b> " + result.data[i]["description"];
                    
                    
                    container.append(bankAccInfoElm);
                }
            }
            else {

            }
        }
        catch (error) {

        }
    }

    const SendMoneyToIban = async (e) => {

        try {
            const result = await axios.put('http://localhost:8080/api/transactions/' + GetLoggedInUsersCurrentIban() + '/' + document.getElementById("sendMoneyIban").value + '/' + document.getElementById("sendMoneyAmount").value);

            if(result.status >= 200 && result.status < 300) {
                alert("Succesfully send € " +  document.getElementById("sendMoneyAmount").value + " to iban: " + document.getElementById("sendMoneyIban").value);
                window.location.reload();
            }
        }
        catch (error) {
            alert("Failed to send € " + document.getElementById("sendMoneyAmount").value + " to iban: " + document.getElementById("sendMoneyIban").value);
        }
    }

    function GetLoggedInUsersCurrentIban() {
        if(allBankAccountsInfo != null) {
            for(var i = 0; i < allBankAccountsInfo.length; i++) if(allBankAccountsInfo[i]["accountType"] == "Current") return allBankAccountsInfo[i]["iban"];
        }
        else return "";
    }

    function GetLoggedInUsersSavingsIban() {
        if(allBankAccountsInfo != null) {
            for(var i = 0; i < allBankAccountsInfo.length; i++) if(allBankAccountsInfo[i]["accountType"] == "Savings") return allBankAccountsInfo[i]["iban"];
        }
        else return "";
    }

    const PerformTransactionForIbans = async (e) => {
        var ibanFrom = document.getElementById("employeeTransactionFromIban").value;
        var ibanTo = document.getElementById("employeeTransactionToIban").value;
        var amount = document.getElementById("employeeTransactionAmount").value;

        try {
            const result = await axios.put('http://localhost:8080/api/transactions/' + ibanFrom + '/' + ibanTo + '/' + amount);

            if(result.status >= 200 && result.status < 300) {
                alert("Succesfully transfered money from iban: " + ibanFrom + " to iban: " + ibanTo + " , and the amount transfered: €" + amount);
                loadAllBankAccountInfo();
            }
            else alert("Could not transfer money from iban: " + ibanFrom + " to iban: " + ibanTo + " , and the amount transfered: €" + amount);
        }
        catch (error) {
            alert("Could not transfer money from iban: " + ibanFrom + " to iban: " + ibanTo + " , and the amount transfered: €" + amount);
        }
    }

    const ChangeBankAccountAbsoluteLimit = async (e) => {
        var iban = document.getElementById("changeAbsoluteLimitIban").value;
        var value = document.getElementById("changeAbsoluteLimitValue").value;

        try {
            const result = await axios.put('http://localhost:8080/api/putAbsoluteLimit/' + value + '/' + iban);

            if(result.status >= 200 && result.status < 300) {
                loadAllBankAccountInfo();
            }
            else alert("Failed to change the absolute limit");
        }
        catch (error) {
            alert("Failed to change the absolute limit");
        }
    }

    const EmployeeCreateBankAccountForUser = async (e) => {
        // /initBankAccounts/{userId}
        
        try {
            const result = await axios.post('http://localhost:8080/api/initBankAccounts/' + document.getElementById("inputCreateBankAccUserId").value);

            if(result.status >= 200 && result.status < 300) {
                loadAllUsersWithoutBankAccounts();
            }
            else alert("Failed to create bank account for user");
        }
        catch (error) {
            alert("Failed to create bank account for user");
        }
    }
    
    function loadAllUsersInfo() {
            // getall 
            instance.get('http://localhost:8080/api/user/getall', {
                headers: {
                    'Content-Type': null,
                    Authorization: "Bearer " + jwtToken,
                }
                })
                .then(res => { 
                    if(res.status >= 200 && res.status <= 300) {
                        var container = document.getElementById("allUsersList");
                        container.innerHTML = "";
                        if(res.data.length > 0) {
                            for(let i = 0; i < res.data.length; i++) {
                                var userAccInfoElm = document.createElement("p");
                                userAccInfoElm.style.textAlign = "center";
                                userAccInfoElm.style.marginBottom = "0.8rem";
                                userAccInfoElm.style.borderBottom = "0.1rem solid black";
                                userAccInfoElm.innerHTML = 
                                "<b>Id:</b> " + res.data[i]["id"] + ", " +
                                "<b>Fullname:</b> " + res.data[i]["fullname"] + ", " +
                                "<b>Email:</b> " + res.data[i]["email"] + ", " +
                                "<b>Phone:</b> " + res.data[i]["phone"] + ", " +
                                "<b>Role:</b> " + res.data[i]["role"] + ", " +
                                "<b>Day limit:</b> " + res.data[i]["dayLimit"] + ", " +
                                "<b>Transaction limit:</b> " + res.data[i]["transactionLimit"];
    
                                container.append(userAccInfoElm);
                            }
                        }
                        else {
                            var emptyTextElm = document.createElement("p");
                            emptyTextElm.innerHTML = "No users exists without a bank account";
                            container.append(emptyTextElm);
                        }
                    }
                    else {
                        
                    }
                });
    }

    function loadAllUsersWithoutBankAccounts() {
        instance.get('http://localhost:8080/api/user/getAllUsersWithoutBankAccounts', {
            headers: {
                'Content-Type': null,
                Authorization: "Bearer " + jwtToken,
            }
            })
            .then(res => { 
                if(res.status >= 200 && res.status <= 300) {
                    var container = document.getElementById("allUsersWithoutBankAccountsList");
                    container.innerHTML = "";
                    if(res.data.length > 0) {
                        for(let i = 0; i < res.data.length; i++) {
                            var userAccInfoElm = document.createElement("p");
                            userAccInfoElm.style.textAlign = "center";
                            userAccInfoElm.style.marginBottom = "0.8rem";
                            userAccInfoElm.style.borderBottom = "0.1rem solid black";
                            userAccInfoElm.innerHTML = 
                            "<b>Id:</b> " + res.data[i]["id"] + ", " +
                            "<b>Fullname:</b> " + res.data[i]["fullname"] + ", " +
                            "<b>Email:</b> " + res.data[i]["email"] + ", " +
                            "<b>Phone:</b> " + res.data[i]["phone"] + ", " +
                            "<b>Role:</b> " + res.data[i]["role"];

                            container.append(userAccInfoElm);
                        }
                    }
                    else {
                        var emptyTextElm = document.createElement("p");
                        emptyTextElm.innerHTML = "No users exists without a bank account";
                        container.append(emptyTextElm);
                    }
                }
                else {
                    
                }
            });
    }

    function loadAllTransactionInfo() {
        instance.get('http://localhost:8080/api/transactions/byAmountIsBigger/' + GetLoggedInUsersCurrentIban() + '/-1', {
            headers: {
                'Content-Type': null,
                Authorization: "Bearer " + jwtToken,
            }
            })
            .then(res => { 
                if(res.status >= 200 && res.status <= 300) {
                    var container = document.getElementById("allTransactionsHistory");
                    container.innerHTML = "";
                    console.log(res.data);
                    for(let i = 0; i < res.data.length; i++) {
                        var bankAccInfoElm = document.createElement("p");
                        bankAccInfoElm.style.textAlign = "center";
                        bankAccInfoElm.style.marginBottom = "0.8rem";
                        bankAccInfoElm.style.borderBottom = "0.1rem solid black";
                        bankAccInfoElm.innerHTML = 
                        "<b>from:</b> " + res.data[i]["from"] + ", " +
                        "<b>to:</b> " + res.data[i]["to"] + ", " +
                        "<b>amount:</b> €" + res.data[i]["amount"] + ", " +
                        "<b>date:</b> " + res.data[i]["timestamp"] + ", " +
                        "<b>description:</b> " + res.data[i]["description"];
                        
                        
                        container.append(bankAccInfoElm);
                    }
                }
            });
    }

    function loadAllBankAccountInfo() {
        instance.get('http://localhost:8080/api/allBankAccounts', {
            headers: {
                'Content-Type': null,
                Authorization: "Bearer " + jwtToken,
            }
            })
            .then(res => { 
                if(res.status >= 200 && res.status <= 300) {
                    var container = document.getElementById("allBankAccountsList");
                    container.innerHTML = "";
                    for(let i = 0; i < res.data.length; i++) {
                        var bankAccInfoElm = document.createElement("p");
                        bankAccInfoElm.style.textAlign = "center";
                        bankAccInfoElm.style.marginBottom = "0.8rem";
                        bankAccInfoElm.style.borderBottom = "0.1rem solid black";
                        bankAccInfoElm.innerHTML = 
                        "<b>Id:</b> " + res.data[i]["id"] + ", " +
                        "<b>Account type:</b> " + res.data[i]["accountType"] + ", " +
                        "<b>iban:</b> " + res.data[i]["iban"] + ", " +
                        "<b>balance:</b> " + res.data[i]["balance"] + ", " +
                        "<b>Creation date:</b> " + res.data[i]["creationDate"] + ", " +
                        "<b>Status:</b> " + res.data[i]["status"] + ", " +
                        "<b>Owner id:</b> " + res.data[i]["userId"] + ", " +
                        "<b>absolute limit:</b> " + res.data[i]["absolute limit"];
                        
                        
                        container.append(bankAccInfoElm);
                    }
                }
            });
    }

    function getToken() {
        return getCookie("jwt");
     }

     function getCookie(cname) {
        let name = cname + "=";
        let decodedCookie = decodeURIComponent(document.cookie);
        let ca = decodedCookie.split(';');
        for(let i = 0; i <ca.length; i++) {
          let c = ca[i];
          while (c.charAt(0) == ' ') {
            c = c.substring(1);
          }
          if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
          }
        }
        return "";
      }
    
    return (
        <div>
            <div style={{ marginTop: "0.8rem", width: "100%", borderBottom: "0.4rem solid black"}}>
                <h1>BANKING APPLICATION</h1>
                <div style={{ display: "block" }}>
                    <p id="currentUserFullName" style={{ width: "100%", textAlign: "center", fontWeight: "bold", fontSize: "1.4rem", marginBottom: "-0.25rem"}}></p>
                </div>

                <div id="currentUsersBankingInfo">
                    <div style={{ display: "block", marginBottom: "-1.2rem" }}>
                        <p style={{ display: "inline-block"}}>Total Balance: </p>
                        <p id="displayTotalBalance" style={{ display: "inline-block", paddingLeft: "0.3rem"}}></p>
                    </div>
                    <div style={{ display: "block", marginBottom: "-1rem" }}>
                        <p style={{ display: "inline-block"}}>Your daily limit: </p>
                        <p id="displayDailyLimit" style={{ display: "inline-block", paddingLeft: "0.3rem"}}></p>
                    </div>
                    <div style={{ display: "block" }}>
                        <p style={{ display: "inline-block"}}>Your transaction limit: </p>
                        <p id="displayTransactionLimit" style={{ display: "inline-block", paddingLeft: "0.3rem"}}></p>
                    </div>
                    <h2>Current account:</h2>
                    <div style={{ display: "block", marginTop: "-0.8rem" }}>
                        <p style={{ display: "inline-block"}}>IBAN: </p>
                        <p id="currentIban" style={{ display: "inline-block", paddingLeft: "0.3rem"}}></p>
                    </div>
                    <div style={{ display: "block", marginTop: "-1.8rem" }}>
                        <p style={{ display: "inline-block"}}>Balance: </p>
                        <p id="currentBalance" style={{ display: "inline-block", paddingLeft: "0.3rem"}}></p>
                    </div>
                    <div style={{ display: "block", marginTop: "-1.8rem" }}>
                        <p style={{ display: "inline-block"}}>Absolute limit: </p>
                        <p id="currentAbsoluteLimit" style={{ display: "inline-block", paddingLeft: "0.3rem"}}></p>
                    </div>

                    <h3>Savings account:</h3>
                    <div style={{ display: "block", marginTop: "-0.8rem" }}>
                        <p style={{ display: "inline-block"}}>IBAN: </p>
                        <p id="savingIban" style={{ display: "inline-block", paddingLeft: "0.3rem"}}></p>
                    </div>
                    <div style={{ display: "block", marginTop: "-1.8rem" }}>
                        <p style={{ display: "inline-block"}}>Balance: </p>
                        <p id="savingBalance" style={{ display: "inline-block", paddingLeft: "0.3rem"}}></p>
                    </div>
                    <div style={{ display: "block", marginTop: "-1.8rem" }}>
                        <p style={{ display: "inline-block"}}>Absolute limit: </p>
                        <p id="savingAbsoluteLimit" style={{ display: "inline-block", paddingLeft: "0.3rem"}}></p>
                    </div>
                </div>

                <button id="createBankAccountsButt" onClick={ CreateBankAccountsForUser } style={{ display: "block", visibility: "hidden", width: "15%", marginLeft: "42.5%", marginBottom: "3rem", marginTop: "1rem"}}>create your banking accounts</button>
            
            
            
                <div id="makeAnTransactionSection" style={{ marginTop: "-1.8rem", paddingBottom: "3rem"}}>
                    <div style={{ display: "block", textAlign: "center", fontSize: "1.4rem", fontWeight: "bold"}}>
                        <p>Perform a transaction</p>
                    </div>
                    <div style={{ display: "block"}}>
                        <p style={{ fontSize: "1.15rem", marginBottom: "0rem", display: "inline-block"}}>Send money from current bank account to iban: </p>
                        <input id="sendMoneyIban" style={{ display: "inline-block", fontSize: "1.15rem", marginLeft: "0.2rem", marginRight: "0.2rem"}} />
                        <p style={{ fontSize: "1.15rem", marginBottom: "0rem", display: "inline-block"}}> with amount: </p>
                        <input id="sendMoneyAmount" style={{ display: "inline-block", fontSize: "1.15rem", marginLeft: "0.2rem", marginRight: "0.2rem", width: "7rem"}} />
                        <button onClick={ SendMoneyToIban } style={{ display: "inline-block", fontSize: "1.15rem", marginLeft: "0.2rem"}}>Confirm</button>
                    </div>

                    <div>
                        <p style={{ fontSize: "1.4rem", fontWeight: "bold", marginBottom: "0rem"}}>Your transaction History:</p>
                        <div id="allTransactionsHistory" style={{height: "auto", maxHeight: "20rem", width: "50%", marginLeft: "25%", overflow: "auto", display: "block"}}>
                            
                        </div>
                    </div>

                    <div>
                        <div style={{ display: "block"}}>
                            <p style={{ fontSize: "1.15rem", marginBottom: "0rem", display: "inline-block"}}>See transaction history with iban: </p>
                            <input id="transactionHistoryIban" style={{ display: "inline-block", fontSize: "1.15rem", marginLeft: "0.2rem", marginRight: "0.2rem"}} />
                            <button onClick={ FindTransactionHistory } style={{ display: "inline-block", fontSize: "1.15rem", marginLeft: "0.2rem"}}>Confirm</button>
                        </div>

                        <div style={{ display: "block"}}>
                        <p style={{ fontSize: "1.15rem", marginBottom: "0rem", display: "inline-block"}}>Find transaction{'(s)'} where amount: </p>
                        <input id="transactionComparisonAmount" style={{ display: "inline-block", fontSize: "1.15rem", marginLeft: "0.2rem", marginRight: "0.2rem"}} />
                        <p style={{ fontSize: "1.15rem", marginBottom: "0rem", display: "inline-block"}}> is compared to </p>
                        <select id="transactionComparisonType" style={{ display: "inline-block", padding: "0.2rem", marginLeft: "0.2rem", marginRight: "0.2rem", fontSize: "1.2rem"}}>
                            <option>{'='}</option>
                            <option>{'<'}</option>
                            <option>{'>'}</option>
                        </select>
                        <button onClick={ FindTransactionHistoryByComparison } style={{ display: "inline-block", fontSize: "1.15rem", marginLeft: "0.2rem"}}>Confirm</button>
                    </div>

                        <div id="transactionHistoryWithIbanWrapper" style={{ display: 'none'}}>
                            <p id="transactionHistoryWithIbanText" style={{ fontSize: "1.4rem", fontWeight: "bold", marginBottom: "0rem"}}>Your transaction History with iban:</p>
                            <div id="allTransactionHistoryWithIban" style={{height: "auto", maxHeight: "20rem", width: "50%", marginLeft: "25%", overflow: "auto", display: "block"}}>
                            
                            </div>
                        </div>
                    </div>

                </div>
            
            </div>


            <div style={{ marginTop: "-2.2rem", width: "100%", paddingLeft: "0" }}>
                
                <p style={{ marginTop: "6rem"}}>Search iban by Full name</p>
                <input id="inputFullNameToIban" placeholder="full name" />
                <button onClick={ GetIbanByFullName } style={{ display: "block", cursor: "pointer", width:"15%", marginLeft: "42.5%", marginTop: "0.3rem"}}>Search</button>
                <p style={{ display: "inline-block", color: "red", fontWeight: "bold", paddingRight: "0.6rem"}}>result: </p>
                <p id="fullNameToIbanResult" style={{ display: "inline-block", color: "red", fontWeight: "bold"}}>test</p>
            </div>

            <div id="EmployeeContainer" style={{ marginBottom: "40rem"}}>
                <h4 style={{ fontSize: "3rem", borderTop: "0.4rem solid black"}}>Employee</h4>

                <div>
                    <p style={{ fontSize: "1.4rem", fontWeight: "bold", marginBottom: "0rem"}}>All bank accounts info:</p>
                    <div id="allBankAccountsList" style={{height: "auto", maxHeight: "20rem", width: "50%", marginLeft: "25%", overflow: "auto", display: "block"}}>
                        
                    </div>

                    <div style={{ display: "block"}}>
                        <p style={{ fontSize: "1.15rem", marginBottom: "0rem", display: "inline-block"}}>Change status of iban: </p>
                        <input id="changeStatusIban" style={{ display: "inline-block", fontSize: "1.15rem", marginLeft: "0.2rem", marginRight: "0.2rem"}} />
                        <select id="changeStatus" style={{ display: "inline-block"}}>
                            <option>Active</option>
                            <option>Inactive</option>
                            <option>Closed</option>
                        </select>
                        <button onClick={ ChangeBankAccountStatus } style={{ display: "inline-block", fontSize: "1.15rem", marginLeft: "0.2rem"}}>Confirm</button>
                    </div>
                    <div style={{ display: "block"}}>
                        <p style={{ fontSize: "1.15rem", marginBottom: "0rem", display: "inline-block"}}>Change absolute limit for iban: </p>
                        <input id="changeAbsoluteLimitIban" style={{ display: "inline-block", fontSize: "1.15rem", marginLeft: "0.2rem", marginRight: "0.2rem"}} />
                        <p style={{ fontSize: "1.15rem", marginBottom: "0rem", display: "inline-block"}}> to </p>
                        <input id="changeAbsoluteLimitValue" style={{ display: "inline-block", fontSize: "1.15rem", marginLeft: "0.2rem", marginRight: "0.2rem", width: "7rem"}} />
                        <button onClick={ ChangeBankAccountAbsoluteLimit } style={{ display: "inline-block", fontSize: "1.15rem", marginLeft: "0.2rem"}}>Confirm</button>
                    </div>
                    <div style={{ display: "block"}}>
                        <p style={{ fontSize: "1.15rem", marginBottom: "0rem", display: "inline-block"}}>Perform transaction from iban: </p>
                        <input id="employeeTransactionFromIban" style={{ display: "inline-block", fontSize: "1.15rem", marginLeft: "0.2rem", marginRight: "0.2rem"}} />
                        <p style={{ fontSize: "1.15rem", marginBottom: "0rem", display: "inline-block"}}> to iban: </p>
                        <input id="employeeTransactionToIban" style={{ display: "inline-block", fontSize: "1.15rem", marginLeft: "0.2rem", marginRight: "0.2rem"}} />
                        <p style={{ fontSize: "1.15rem", marginBottom: "0rem", display: "inline-block"}}> amount transfered: </p>
                        <input id="employeeTransactionAmount" style={{ display: "inline-block", fontSize: "1.15rem", marginLeft: "0.2rem", marginRight: "0.2rem", width: "7rem"}} />
                        <button onClick={ PerformTransactionForIbans } style={{ display: "inline-block", fontSize: "1.15rem", marginLeft: "0.2rem"}}>Confirm</button>
                    </div>
                </div>

                <div>
                    <p style={{ fontSize: "1.4rem", fontWeight: "bold", marginBottom: "0rem"}}>All users without bankaccount:</p>
                    <div id="allUsersWithoutBankAccountsList" style={{height: "auto", width: "50%", marginLeft: "25%", overflow: "auto", display: "block"}}>
                        
                    </div>
                    <p style={{ fontSize: "1.15rem", marginBottom: "0rem", display: "inline-block"}}>Create bank accounts for user id: </p>
                    <input id="inputCreateBankAccUserId" defaultValue="0" style={{ fontSize: "1.15rem", marginBottom: "0rem", display: "inline-block", marginLeft: "0.2rem", marginRight: "0.2rem", width: "5rem", textAlign: "center" }} placeholder="user id" />
                    <button onClick={ EmployeeCreateBankAccountForUser } style={{ display: "inline-block", fontSize: "1.15rem", marginLeft: "0.2rem"}}>Confirm</button>
                </div>

                <div>
                    <p style={{ fontSize: "1.4rem", fontWeight: "bold", marginBottom: "0rem"}}>All users accounts info:</p>
                    <div id="allUsersList" style={{height: "auto", maxHeight: "20rem", width: "50%", marginLeft: "25%", overflow: "auto", display: "block"}}>
                        
                    </div>
                    {/* <div style={{ display: "block"}}>
                        <p style={{ fontSize: "1.15rem", marginBottom: "0rem", display: "inline-block"}}>Change the <b>daily limit</b> for user id: </p>
                        <input id="changeDailyLimitUserId" style={{ display: "inline-block", fontSize: "1.15rem", marginLeft: "0.2rem", marginRight: "0.2rem", width: "3rem"}} />
                        <p style={{ fontSize: "1.15rem", marginBottom: "0rem", display: "inline-block"}}> to value </p>
                        <input id="changeDailyLimitUserValue" style={{ display: "inline-block", fontSize: "1.15rem", marginLeft: "0.2rem", marginRight: "0.2rem", width: "7rem"}} />
                        <button  style={{ display: "inline-block", fontSize: "1.15rem", marginLeft: "0.2rem"}}>Confirm</button>
                    </div>
                    <div style={{ display: "block"}}>
                        <p style={{ fontSize: "1.15rem", marginBottom: "0rem", display: "inline-block"}}>Change <b>transaction limit</b> for user id: </p>
                        <input id="changeTransactionLimitUserId" style={{ display: "inline-block", fontSize: "1.15rem", marginLeft: "0.2rem", marginRight: "0.2rem", width: "3rem"}} />
                        <p style={{ fontSize: "1.15rem", marginBottom: "0rem", display: "inline-block"}}> to value </p>
                        <input id="changeTransactionLimitUserValue" style={{ display: "inline-block", fontSize: "1.15rem", marginLeft: "0.2rem", marginRight: "0.2rem", width: "7rem"}} />
                        <button  style={{ display: "inline-block", fontSize: "1.15rem", marginLeft: "0.2rem"}}>Confirm</button>
                    </div> */}
                </div>
            </div>

            { SetBankingInfoCurrentUser() }
        </div>

        

    )
}

export default bankingPageComponent;


        





        // let name = "jwt" + "=";
        // let decodedCookie = decodeURIComponent(document.cookie);
        // let ca = decodedCookie.split(';');
        // for(let i = 0; i <ca.length; i++) {
        //   let c = ca[i];
        //   while (c.charAt(0) == ' ') {
        //     c = c.substring(1);
        //   }
        //   if (c.indexOf(name) == 0) {
        //     jwtToken = c.substring(name.length, c.length);
        //   }
        // }

        // if(userId != -1) {
        //     axios.interceptors.request.use(
        //         config => {
        //             config.headers.Authorization = 'Bearer ' + getCookie("jwt");
        //             return config;
        //         },
        //         error => {
        //             return Promise.reject(error);
        //         }
        //     )

        //     const result = await axios.post('http://localhost:8080/api/initBankAccounts/' + userId);
        //     if(result.status == 200) window.location.reload();
        //     else console.log(result);
        // }

        // instance.get('http://localhost:8080/api/user/getUserIdJwtValidation', {
        //     headers: {
        //         'Content-Type': null,
        //         Authorization: "Bearer " + jwtToken,
        //     }
        // })
        // .then(res => {
        //     if(res.status >= 200 && res.status <= 300) {
        //         userId = res.data;

        //         //set rest of the current user data, because the current JWT is a valid JWT token
        //         //set total balance
        //         instance.get('http://localhost:8080/api/totalBalance/' + userId, {
        //         headers: {
        //             'Content-Type': null,
        //             Authorization: "Bearer " + jwtToken,
        //         }
        //         })
        //         .then(res => { 
        //             if(res.status >= 200 && res.status <= 300) document.getElementById("displayTotalBalance").innerHTML = "€ " + res.data;
        //             else document.getElementById("displayTotalBalance").innerHTML = "€ 0";
        //         });

        //         // /bankAccounts/{userId}
        //         instance.get('http://localhost:8080/api/bankAccounts/' + userId, {
        //         headers: {
        //             'Content-Type': null,
        //             Authorization: "Bearer " + jwtToken,
        //         }
        //         })
        //         .then(res => { 
        //             if(res.status >= 200 && res.status <= 300) {
        //                 if(res.data.length > 0) {
        //                     for(let i = 0; i < res.data.length; i++) {
        //                         if(res.data[i]["accountType"] == "Current") {
        //                             document.getElementById("currentIban").innerHTML = res.data[i]["iban"];
        //                             document.getElementById("currentBalance").innerHTML = "€ " + res.data[i]["balance"];
        //                             document.getElementById("currentAbsoluteLimit").innerHTML = "€ " + res.data[i]["absolute limit"];
        //                         }
        //                         else {
        //                             //savings
        //                             document.getElementById("savingIban").innerHTML = res.data[i]["iban"];
        //                             document.getElementById("savingBalance").innerHTML = "€ " + res.data[i]["balance"];
        //                             document.getElementById("savingAbsoluteLimit").innerHTML = "€ " + res.data[i]["absolute limit"];
        //                         }
        //                     }
        //                 }
        //                 else {
        //                     //show button to create bank accounts for user ID
        //                     document.getElementById("createBankAccountsButt").style.visibility = "visible";
        //                 }
        //             }
        //         }).catch((error) => {
        //             document.getElementById("displayTotalBalance").innerHTML = "€ 0";
        //             //show button to create bank accounts for user ID
        //             document.getElementById("createBankAccountsButt").style.visibility = "visible"; 
        //             document.getElementById("currentUsersBankingInfo").remove();
        //         });

        //         if(userId != -1) {
        //             //set user info
        //             instance.get('http://localhost:8080/api/user/get/' + userId, {
        //             headers: {
        //                 'Content-Type': null,
        //                 Authorization: "Bearer " + jwtToken,
        //             }
        //             })
        //             .then(res => { 
        //                 if(res.status >= 200 && res.status <= 300) {
        //                     document.getElementById("currentUserFullName").innerHTML = "Welcome " + res.data["fullname"];
        //                     document.getElementById("displayTransactionLimit").innerHTML = res.data["transactionLimit"];
        //                     document.getElementById("displayDailyLimit").innerHTML = res.data["dailyLimit"];
        //                     userRights = res.data["role"];
        //                     if(userRights != "employee") document.getElementById("EmployeeContainer").remove();
        //                     else {
        //                         //load all info for the employee
        //                         loadAllUsersWithoutBankAccounts();
        //                         loadAllBankAccountInfo();
        //                         loadAllUsersInfo();
        //                     }
        //                 }
        //             });
        //         }
        //     }
        //     else {
        //         if(window.location.pathname == "/")window.location.href = "/login";
        //     }
            
        // })
        // .catch((error) => {
        //     if(window.location.pathname == "/homePage") window.location.href = "/login";
        // });