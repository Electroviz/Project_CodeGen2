import React from 'react';
import axios from 'axios';

const RegisterComp = () => {
    return (
        <div style={{ width: '20%', marginLeft: '40%', marginTop: '5rem'}}>
            <h1>Register</h1>
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

            <button style={{ display: 'block', width: "40%", marginLeft: "30%", marginTop: "2rem", cursor: "pointer" }} tabIndex="6">Register</button>
            <a href="/" style={{ display: 'block', width: "40%", marginLeft: "30%", marginTop: "0.5rem", cursor: "pointer" }} tabIndex="7">Back</a>
        </div>
    )
}

export default RegisterComp