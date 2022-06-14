import React from 'react';
import axios from 'axios';

const LoginComp = () => {
    const LoginAttempt = (e) => {
                console.log("inside");
                var instance = axios.create();

                delete instance.defaults.headers.common.Authorization;
                instance.get('http://localhost:3001/api/testLogin/Jantje/jantje123', {
                    headers: {
                        'Content-Type': null,
                    }
                })
                .then(res => {
                    console.log(res.data);
                })
                .catch((error) => console.log(error));
            }

    return (
        <div style={{ width: '20%', marginLeft: '40%', marginTop: '5rem'}}>
            <h1>Login</h1>
            <h2>Username</h2>
            <input defaultValue="username" />
            <h3>Password</h3>
            <input defaultValue="password" type="password" />

            <button onClick={ LoginAttempt } style={{ display: 'block' }}>Login</button>
        </div>
    )
}

export default LoginComp