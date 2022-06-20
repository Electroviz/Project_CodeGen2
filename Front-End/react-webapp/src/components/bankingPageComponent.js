import React from 'react';
import { useState } from 'react';
import axios from 'axios';

const bankingPageComponent = () => {
    var instance = axios.create();
    
    const GetIbanByFullName = (e) => {
        const params = JSON.stringify({
            "username": "test",
            "password": "geheim"
        })
        //delete instance.defaults.headers.common.Authorization;
        instance.get('http://localhost:8080/api/getBankAccount/name/'+ document.getElementById("inputFullNameToIban").value, {
            headers: {
                'Content-Type': null,
            }
        })
        .then(res => {
            if(res.status >= 200 && res.status <= 300) {
                document.getElementById("fullNameToIbanResult").innerHTML = res.data;
            }
            else {
                document.getElementById("fullNameToIbanResult").innerHTML = "Not Found"
            }
            
        })
        .catch((error) => console.log(error));
        
    }
    
    return (
        

        <div style={{ marginTop: "4rem", width: "100%", paddingLeft: "10rem;" }}>
            <h1>BANKING APPLICATION</h1>
            
            <p style={{ marginTop: "6rem"}}>Search iban by Full name</p>
            <input id="inputFullNameToIban" placeholder="full name" />
            <buttom onClick={ GetIbanByFullName } style={{ display: "block", cursor: "pointer"}}>Search</buttom>
            <p style={{ display: "inline-block", color: "red", fontWeight: "bold", paddingRight: "0.6rem"}}>result: </p>
            <p id="fullNameToIbanResult" style={{ display: "inline-block", color: "red", fontWeight: "bold"}}>test</p>
        </div>
    )
}

export default bankingPageComponent;