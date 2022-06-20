import React from 'react';
import { useState } from 'react';
import axios from 'axios';

const bankingPageComponent = () => {
    var instance = axios.create();
    var jwtToken = "";
    var userId = -1;
    
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
                                console.log(res.data[i]);
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
                    else document.getElementById("displayTotalBalance").innerHTML = "€ 0";
                });
            }
            else {
                if(window.location.pathname == "/")window.location.href = "/login";
            }
            
        })
        .catch((error) => {
            if(window.location.pathname == "/homePage") window.location.href = "/login";
        });

    }
    
    return (
        <div>
            
            <div id="currentUsersBankingInfo" style={{ marginTop: "4rem", width: "100%", paddingLeft: "10rem;", borderBottom: "0.4rem solid black"}}>
                <h1>BANKING APPLICATION</h1>
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

                <button id="createBankAccountsButt" style={{ display: "block", visibility: "hidden", width: "15%", marginLeft: "42.5%", marginBottom: "1.8rem"}}>create your banking accounts</button>
            </div>


            <div style={{ marginTop: "4rem", width: "100%", paddingLeft: "0" }}>
                
                <p style={{ marginTop: "6rem"}}>Search iban by Full name</p>
                <input id="inputFullNameToIban" placeholder="full name" />
                <button onClick={ GetIbanByFullName } style={{ display: "block", cursor: "pointer", width:"15%", marginLeft: "42.5%", marginTop: "0.3rem"}}>Search</button>
                <p style={{ display: "inline-block", color: "red", fontWeight: "bold", paddingRight: "0.6rem"}}>result: </p>
                <p id="fullNameToIbanResult" style={{ display: "inline-block", color: "red", fontWeight: "bold"}}>test</p>
            </div>
            { SetBankingInfoCurrentUser() }
        </div>

    )
}

export default bankingPageComponent;