import React from 'react';
import axios from 'axios';

const RegisterComp = () => {
    
    
    const RegisterAttempt = (e) => {
        var instance = axios.create();

        var uname = document.getElementById("uname").value;
        var fullName = document.getElementById("firstName").value + " " + document.getElementById("lastName").value;
        var email = document.getElementById("email").value;
        var telephone = document.getElementById("telephone").value;
        var dayOfBirth = document.getElementById("dayOfBirth").value;
        var password = document.getElementById("passW").value;
        var confirmPassword = document.getElementById("confirmPassW").value;

        if(password != confirmPassword) alert("Your password is not the same as the confirm password.");
        else {
            const params = JSON.stringify({
                "username": uname,
                "fullname": fullName,
                "email": email,
                "phone": telephone,
                "dateOfBirth": dayOfBirth,
                "password": password,
                "dayLimit": 10000,
                "transactionLimit": 10000,
                "role": "customer"
            });

            instance.post('http://localhost:8080/api/user/registeruser', params, {
                    "headers": {
                        "content-type": "application/json",
                    },
                })
                .then(res => {
                    if(res.status >= 200 && res.status < 300) {
                        alert("Succesfully registered, login now.");
                        window.location.href = "/login";
                    }
                    
                })
                .catch((error) => {
                    alert("Unsuccesfully registered. Try again.");
                    console.log(error)
                });
        }

        
    }
    
    
    
    return (
        <div style={{ width: '20%', marginLeft: '40%', marginTop: '5rem'}}>
            <h1>Register</h1>
            <p style={{ textAlign: "center", fontSize: "1.15rem", fontWeight: "bold", marginBottom: "0rem" }} tabIndex="0">First Name</p>
            <input id="firstName"  />
            <p style={{ textAlign: "center", fontSize: "1.15rem", fontWeight: "bold", marginBottom: "0rem" }} tabIndex="0">Last Name</p>
            <input id="lastName"  />
            <p style={{ textAlign: "center", fontSize: "1.15rem", fontWeight: "bold", marginBottom: "0rem" }} tabIndex="0">New username</p>
            <input id="uname"  />
            <p style={{ textAlign: "center", fontSize: "1.15rem", fontWeight: "bold", marginBottom: "0rem" }} tabIndex="1">Your email</p>
            <input id="email"  />
            <p style={{ textAlign: "center", fontSize: "1.15rem", fontWeight: "bold", marginBottom: "0rem" }} tabIndex="2">Your telephone number</p>
            <input id="telephone"  />
            <p style={{ textAlign: "center", fontSize: "1.15rem", fontWeight: "bold", marginBottom: "0rem" }} tabIndex="3">Your day of birth</p>
            <input id="dayOfBirth" placeholder="dd/mm/yy"  />
            <p style={{ textAlign: "center", fontSize: "1.15rem", fontWeight: "bold", marginBottom: "0rem" }} tabIndex="4">New password</p>
            <input id="passW" type="password" />
            <p style={{ textAlign: "center", fontSize: "1.15rem", fontWeight: "bold", marginBottom: "0rem" }} tabIndex="5">Confirm new password</p>
            <input id="confirmPassW" type="password" />

            <button onClick={ RegisterAttempt } style={{ display: 'block', width: "40%", marginLeft: "30%", marginTop: "2rem", cursor: "pointer" }} tabIndex="6">Register</button>
            <a href="/" style={{ display: 'block', width: "40%", marginLeft: "30%", marginTop: "0.5rem", cursor: "pointer" }} tabIndex="7">Back</a>
        </div>
    )
}

export default RegisterComp