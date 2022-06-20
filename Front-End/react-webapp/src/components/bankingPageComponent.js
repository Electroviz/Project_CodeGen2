import React from 'react';
import { useState } from 'react';
import axios from 'axios';

const bankingPageComponent = () => {
    var instance = axios.create();
    var jwtToken = "";
    var userId = -1;
    var userRights = "none";
    var allBankAccountsInfo = null;

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

    const CreateBankAccountsForUser = (e) => {
        if(userId != -1) {
            instance.post('http://localhost:8080/api/initBankAccounts/' + userId,{
                headers: {
                    'Content-Type': null,
                    Authorization: "Bearer " + jwtToken,
                }
            })
            .then(res => {
                //
            });

            window.location.reload();
        }
     }

    const SetBankingInfoCurrentUser = () => {
        //first get and set the User id by JWT token, if JWT token is not valid anymore return to login page
        //get jwt token if not set yet
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

        instance.get('http://localhost:8080/api/user/getUserIdJwtValidation', {
            headers: {
                'Content-Type': null,
                Authorization: "Bearer " + jwtToken,
            }
        })
        .then(res => {
            if(res.status >= 200 && res.status <= 300) {
                userId = res.data;

                //set rest of the current user data, because the current JWT is a valid JWT token
                //set total balance
                instance.get('http://localhost:8080/api/totalBalance/' + userId, {
                headers: {
                    'Content-Type': null,
                    Authorization: "Bearer " + jwtToken,
                }
                })
                .then(res => { 
                    if(res.status >= 200 && res.status <= 300) document.getElementById("displayTotalBalance").innerHTML = "€ " + res.data;
                    else document.getElementById("displayTotalBalance").innerHTML = "€ 0";
                });

                // /bankAccounts/{userId}
                instance.get('http://localhost:8080/api/bankAccounts/' + userId, {
                headers: {
                    'Content-Type': null,
                    Authorization: "Bearer " + jwtToken,
                }
                })
                .then(res => { 
                    if(res.status >= 200 && res.status <= 300) {
                        if(res.data.length > 0) {
                            for(let i = 0; i < res.data.length; i++) {
                                if(res.data[i]["accountType"] == "Current") {
                                    document.getElementById("currentIban").innerHTML = res.data[i]["iban"];
                                    document.getElementById("currentBalance").innerHTML = "€ " + res.data[i]["balance"];
                                    document.getElementById("currentAbsoluteLimit").innerHTML = "€ " + res.data[i]["absolute limit"];
                                }
                                else {
                                    //savings
                                    document.getElementById("savingIban").innerHTML = res.data[i]["iban"];
                                    document.getElementById("savingBalance").innerHTML = "€ " + res.data[i]["balance"];
                                    document.getElementById("savingAbsoluteLimit").innerHTML = "€ " + res.data[i]["absolute limit"];
                                }
                            }
                        }
                        else {
                            //show button to create bank accounts for user ID
                            document.getElementById("createBankAccountsButt").style.visibility = "visible";
                        }
                    }
                }).catch((error) => {
                    document.getElementById("displayTotalBalance").innerHTML = "€ 0";
                    //show button to create bank accounts for user ID
                    document.getElementById("createBankAccountsButt").style.visibility = "visible"; 
                    document.getElementById("currentUsersBankingInfo").remove();
                });

                if(userId != -1) {
                    //set user info
                    instance.get('http://localhost:8080/api/user/get/' + userId, {
                    headers: {
                        'Content-Type': null,
                        Authorization: "Bearer " + jwtToken,
                    }
                    })
                    .then(res => { 
                        if(res.status >= 200 && res.status <= 300) {
                            document.getElementById("currentUserFullName").innerHTML = "Welcome " + res.data["fullname"];
                            userRights = res.data["role"];
                            if(userRights != "employee") document.getElementById("EmployeeContainer").remove();
                            else {
                                //load all info for the employee
                                loadAllUsersWithoutBankAccounts();
                                loadAllBankAccountInfo();
                            }
                        }
                    });
                }
            }
            else {
                if(window.location.pathname == "/")window.location.href = "/login";
            }
            
        })
        .catch((error) => {
            if(window.location.pathname == "/homePage") window.location.href = "/login";
        });

    }

    const ChangeBankAccountStatus = (e) => {
        var iban = document.getElementById("changeStatusIban").value;
        var status = document.getElementById("changeStatus").value.toUpperCase();

        instance.put('http://localhost:8080/api/putBankAccountStatus/' + status + '/' + iban, {
                headers: {
                    'Content-Type': null,
                    Authorization: "Bearer " + jwtToken,
                }
                })
                .then(res => { 
                    if(res.status >= 200 && res.status <= 300) {
                        alert("Succes");
                        loadAllBankAccountInfo();
                    }
                }).catch((error) => {
                    alert("Failed to change the status");
                });
    }

    const EmployeeCreateBankAccountForUser = (e) => {
        // /initBankAccounts/{userId}
        instance.post('http://localhost:8080/api/initBankAccounts/' + document.getElementById("inputCreateBankAccUserId").value, {
                headers: {
                    'Content-Type': null,
                    Authorization: "Bearer " + jwtToken,
                }
                })
                .then(res => { 
                    if(res.status >= 200 && res.status <= 300) {
                        alert("Succes");
                        loadAllUsersWithoutBankAccounts();
                    }
                }).catch((error) => {
                    alert("Failed to change the status");
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
                            "Id: " + res.data[i]["id"] + ", " +
                            "Fullname: " + res.data[i]["fullname"] + ", " +
                            "Email: " + res.data[i]["email"] + ", " +
                            "Phone: " + res.data[i]["phone"] + ", " +
                            "Role: " + res.data[i]["role"];

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
                        "Id: " + res.data[i]["id"] + ", " +
                        "Account type: " + res.data[i]["accountType"] + ", " +
                        "iban: " + res.data[i]["iban"] + ", " +
                        "balance: " + res.data[i]["balance"] + ", " +
                        "Creation date: " + res.data[i]["creationDate"] + ", " +
                        "Status: " + res.data[i]["status"] + ", " +
                        "Owner id: " + res.data[i]["userId"] + ", " +
                        "absolute limit: " + res.data[i]["absolute limit"];
                        
                        
                        container.append(bankAccInfoElm);
                    }
                }
            });
    }
    
    return (
        <div>
            <div style={{ marginTop: "0.8rem", width: "100%", borderBottom: "0.4rem solid black"}}>
                <h1>BANKING APPLICATION</h1>
                <div style={{ display: "block" }}>
                    <p id="currentUserFullName" style={{ width: "100%", textAlign: "center"}}></p>
                </div>

                <div id="currentUsersBankingInfo">
                    <div style={{ display: "block" }}>
                        <p style={{ display: "inline-block"}}>Total Balance: </p>
                        <p id="displayTotalBalance" style={{ display: "inline-block", paddingLeft: "0.3rem"}}></p>
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

                <button id="createBankAccountsButt" onClick={ CreateBankAccountsForUser } style={{ display: "block", visibility: "hidden", width: "15%", marginLeft: "42.5%", marginBottom: "1.8rem"}}>create your banking accounts</button>
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

                    <p style={{ fontSize: "1.15rem", marginBottom: "0rem", display: "inline-block"}}>Change status of iban: </p>
                    <input id="changeStatusIban" style={{ display: "inline-block", fontSize: "1.15rem", marginLeft: "0.2rem", marginRight: "0.2rem"}} />
                    <select id="changeStatus" style={{ display: "inline-block"}}>
                        <option>Active</option>
                        <option>Inactive</option>
                        <option>Closed</option>
                    </select>
                    <button onClick={ ChangeBankAccountStatus } style={{ display: "inline-block", fontSize: "1.15rem", marginLeft: "0.2rem"}}>Confirm</button>

                </div>

                <div>
                    <p style={{ fontSize: "1.4rem", fontWeight: "bold", marginBottom: "0rem"}}>All users without bankaccount:</p>
                    <div id="allUsersWithoutBankAccountsList" style={{height: "auto", width: "50%", marginLeft: "25%", overflow: "auto", display: "block"}}>
                        
                    </div>
                    <p style={{ fontSize: "1.15rem", marginBottom: "0rem", display: "inline-block"}}>Create bank accounts for user id: </p>
                    <input id="inputCreateBankAccUserId" defaultValue="0" style={{ fontSize: "1.15rem", marginBottom: "0rem", display: "inline-block", marginLeft: "0.2rem", marginRight: "0.2rem", width: "5rem", textAlign: "center" }} placeholder="user id" />
                    <button onClick={ EmployeeCreateBankAccountForUser } style={{ display: "inline-block", fontSize: "1.15rem", marginLeft: "0.2rem"}}>Confirm</button>

                </div>
            </div>

            { SetBankingInfoCurrentUser() }
        </div>

        

    )
}

export default bankingPageComponent;