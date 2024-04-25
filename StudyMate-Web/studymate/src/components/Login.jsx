import {useState} from "react";
import {useNavigate} from "react-router-dom";
import {useAuth} from "./security/AuthContext";

export default function Login() {
    const [username, setUsername] = useState('')
    const [password, setPassword] = useState('')
    const [errorMessage, setErrorMessage] = useState(false)

    const navigate = useNavigate()
    const authContext = useAuth();

    function handleUserNameChange(event) {
        setUsername(event.target.value)
    }

    function handlePasswordChange(event) {
        setPassword(event.target.value)
    }

    function handleSubmit() {
        if(authContext.login(username, password)) {
            navigate(`/welcome/${username}`)
        } else {
            setErrorMessage(true)
        }
    }

    return (
        <div className="Login">
            <div className="Login-Form">
                <h1>Login</h1>
                {errorMessage && <div className="alert alert-danger">Login failed</div>}
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

