import {useState} from "react";
import {useNavigate} from "react-router-dom";

export default function Login() {
    const [username, setUsername] = useState('')
    const [password, setPassword] = useState('')
    const navigate = useNavigate()

    function handleUserNameChange(event) {
        setUsername(event.target.value)
    }

    function handlePasswordChange(event) {
        setPassword(event.target.value)
    }

    function handleSubmit() {
        if(username === 'Shahar' && password === 'admin') {
            alert('Login successful')
            navigate(`/welcome/${username}`)
        } else {
            alert('Login failed')
        }
    }

    return (
        <div className="Login">
            <div className="Login-Form">
                <h1>Login</h1>
                <div>
                    <label>User name:</label>
                    <input type="text" name="username" value={username} onChange={handleUserNameChange}/>
                </div>
                <div>
                    <label>Password:</label>
                    <input type="password" name="password" value={password} onChange={handlePasswordChange}/>
                </div>
                <div>
                    <button type="button" name="login" onClick={handleSubmit}>Login</button>
                </div>
            </div>
        </div>
    );
}

