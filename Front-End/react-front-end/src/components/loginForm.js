const LoginContainer = () => {
    const onClickLogin = (e) => {

        const response = fetch("http://localhost:8080/api/testLogin/" + document.getElementById("userName").value + "/" + document.getElementById("passWord").value);
        const data = response.json();
        console.log(response);

//        var url = "http://localhost:8080/api/testLogin/" + document.getElementById("userName").value + "/" + document.getElementById("passWord").value;
//        console.log(url);
//        var xmlHttp = new XMLHttpRequest();
//        xmlHttp.open( "GET", url, false );
//        xmlHttp.send( null );
//        console.log(xmlHttp.responseText);
    }

    return (
        <div>
            <h1>Username</h1>
            <input id="userName" placeholder="username" defaultValue="Jantje"/>
            <h2>Username</h2>
            <input id="passWord" placeholder="password" type="password" defaultValue="jantje123"/>
            <button onClick={onClickLogin} style={{ display: 'block', width: '20%', marginLeft: '40%', marginTop: '2rem'}}>login</button>
        </div>
    )
}

    export default LoginContainer