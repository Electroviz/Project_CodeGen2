import React from 'react';
import axios from 'axios';

const LoginComp = () => {
    const LoginAttempt = (e) => {
                console.log("inside");
                var instance = axios.create();

                const params = JSON.stringify({
                    "username": "test",
                    "password": "geheim"
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

                // instance.get('http://localhost:8080/api/user/testLogin/' + document.getElementById("uname").value + '/' + document.getElementById("passW").value, {
                //     headers: {
                //         'Content-Type': null,
                //     }
                // })
                // .then(res => {
                //     if(res.status == 200) {
                //         console.log(res.data);
                //         window.location.href = "/continue";
                //     }
                    
                // })
                // .catch((error) => console.log(error));
            }

    return (
        <div style={{ width: '20%', marginLeft: '40%', marginTop: '5rem'}}>
            <h1>Login</h1>
            <h2>Username</h2>
            <input id="uname" defaultValue="test" />
            <h3>Password</h3>
            <input id="passW" defaultValue="geheim" type="password" />

            <button onClick={ LoginAttempt } style={{ display: 'block' }}>Login</button>
        </div>
    )
}

export default LoginComp