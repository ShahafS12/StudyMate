import React, { useState } from 'react';
import {useNavigate} from "react-router-dom";
import {registerUser} from "./api/StudyMateApiService";

export default function Register() {
    // State variables for form fields
    const [error, setErrorMessage] = useState(null)
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [email, setEmail] = useState('');
    const [college, setCollege] = useState('');
    const [degree, setDegree] = useState('');
    const [curriculum, setCurriculum] = useState('');
    const [gender, setGender] = useState('');

    const navigate = useNavigate();

    // Event handler for form submission
    function checkValidity(user) {
        if (!user.username) {
            throw new Error('Username is required');
        }
        if (!user.password) {
            throw new Error('Password is required');
        }
        if (!user.email) {
            throw new Error('Email is required');
        }
        if (!user.college) {
            throw new Error('College is required');
        }
        if (!user.degree) {
            throw new Error('Degree is required');
        }
        if (!user.curriculum) {
            throw new Error('Curriculum is required');
        }
        if (!user.gender) {
            throw new Error('Gender is required');
        }
    }

    // Event handler for form submission
    const handleSubmit = async (e) => {
        e.preventDefault();
        // Perform form validation
        const user = {
            username,
            password,
            email,
            college,
            degree,
            curriculum,
            gender
        };

        try {
            checkValidity(user);
             const response = await registerUser(username, password, email, college, degree, curriculum, gender);

            // Handle successful registration
            console.log('Registration successful:', response.data);
            alert('Registration successful')
            navigate(`/login`);
        } catch (error) {
            // Handle registration error
            console.error('Registration error:', error);
            setErrorMessage(error.message)
        }
    };

    return (
        <div className="container">
            <div className="row justify-content-center mt-5">
                <div className="col-md-6">
                    <div className="card">
                        <div className="card-header">Sign Up</div>
                        <div className="card-body">
                            <form onSubmit={handleSubmit}>
                                <div className="mb-3">
                                    <label htmlFor="username" className="form-label">Username</label>
                                    <input type="text" className="form-control" id="username" value={username}
                                           onChange={(e) => setUsername(e.target.value)}/>
                                </div>
                                <div className="mb-3">
                                    <label htmlFor="password" className="form-label">Password</label>
                                    <input type="password" className="form-control" id="password" value={password}
                                           onChange={(e) => setPassword(e.target.value)}/>
                                </div>
                                <div className="mb-3">
                                    <label htmlFor="email" className="form-label">Email</label>
                                    <input type="email" className="form-control" id="email" value={email}
                                           onChange={(e) => setEmail(e.target.value)}/>
                                </div>
                                <div className="mb-3">
                                    <label htmlFor="college" className="form-label">University / College</label>
                                    <input type="text" className="form-control" id="college" value={college}
                                           onChange={(e) => setCollege(e.target.value)}/>
                                </div>
                                <div className="mb-3">
                                    <label htmlFor="degree" className="form-label">Degree</label>
                                    <select className="form-select" id="degree" value={degree}
                                            onChange={(e) => setDegree(e.target.value)}>
                                        <option value="">Select Degree</option>
                                        <option value="bachelor">Bachelor's</option>
                                        <option value="master">Master's</option>
                                        <option value="phd">PhD</option>
                                    </select>
                                </div>
                                <div className="mb-3">
                                    <label htmlFor="type" className="form-label">Curriculum</label>
                                    <input type="text" className="form-control" id="type" value={curriculum}
                                           onChange={(e) => setCurriculum(e.target.value)}/>
                                </div>
                                <div className="mb-3">
                                    <label htmlFor="gender" className="form-label">Gender</label>
                                    <select className="form-select" id="gender" value={gender}
                                            onChange={(e) => setGender(e.target.value)}>
                                        <option value="">Select Gender</option>
                                        <option value="male">Male</option>
                                        <option value="female">Female</option>
                                        <option value="other">Other</option>
                                    </select>
                                </div>
                                <button type="submit" className="btn btn-primary" onClick={handleSubmit}>Sign Up</button>
                            </form>
                        </div>
                        {error && <div className="alert alert-danger">{error}</div>}

                    </div>
                </div>
            </div>
        </div>
    );
}
