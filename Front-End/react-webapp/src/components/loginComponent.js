import React from 'react';
import axios from 'axios';

const LoginComp = () => {
    const LoginAttempt = (e) => {
                console.log("inside");
                var instance = axios.create();

                const params = JSON.stringify({
                    "username": document.getElementById("uname").value,
                    "password": document.getElementById("passW").value
                })
                //delete instance.defaults.headers.common.Authorization;
                instance.post('http://localhost:8080/api/user/login', params, {
                    "headers": {
                        "content-type": "application/json",
                    },
                })
                .then(res => {
                    if(res.status == 200) {
                        console.log(res.data);
                        document.cookie = "jwt=" + res.data;
                        window.location.href = "/homePage";
                    }
                    
                })
                .catch((error) => console.log(error));
            }

    return (
        <div style={{ width: '20%', marginLeft: '40%', marginTop: '5rem'}}>
            <h1>Login</h1>
            <h2>Username</h2>
            <input id="uname" defaultValue="test" />
            <h3>Password</h3>
            <input id="passW" defaultValue="geheim" type="password" />

            <button onClick={ LoginAttempt } style={{ display: 'block', width: "40%", marginLeft: "30%", marginTop: "2rem", cursor: "pointer" }}>Login</button>
            <a href="/register" style={{ display: 'block', width: "40%", marginLeft: "30%", marginTop: "0.5rem", cursor: "pointer" }}>Registreer</a>
        </div>
    )
}

export default LoginComp