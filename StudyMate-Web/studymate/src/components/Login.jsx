import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from './security/AuthContext';

export default function Login() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState(false);

    const navigate = useNavigate();
    const authContext = useAuth();

    function handleUserNameChange(event) {
        setUsername(event.target.value);
    }

    function handlePasswordChange(event) {
        setPassword(event.target.value);
    }

    async function handleSubmit() {
        const loginSuccessful = await authContext.login(username, password);
        console.log('Login successful:', loginSuccessful);
        if (loginSuccessful) {
            navigate(`/profile/${username}`);
        } else {
            setErrorMessage(true);
        }
    }

    return (
        <div className="container">
            <div className="row justify-content-center mt-5">
                <div className="col-md-6">
                    <div className="card">
                        <div className="card-header">Login</div>
                        <div className="card-body">
                            {errorMessage && <div className="alert alert-danger">Login failed</div>}
                            <div className="mb-3">
                                <label htmlFor="username" className="form-label">User groupName:</label>
                                <input type="text" className="form-control" id="username" groupName="username" value={username} onChange={handleUserNameChange} />
                            </div>
                            <div className="mb-3">
                                <label htmlFor="password" className="form-label">Password:</label>
                                <input type="password" className="form-control" id="password" groupName="password" value={password} onChange={handlePasswordChange} />
                            </div>
                            <div>
                                <button type="button" className="btn btn-primary" groupName="login" onClick={handleSubmit}>Login</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}
