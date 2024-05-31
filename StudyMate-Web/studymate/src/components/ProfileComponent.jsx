import {useParams} from "react-router-dom";
import { useEffect, useState } from "react";
import { useAuth } from './security/AuthContext';
import { getUser } from './api/StudyMateApiService';
import '../styles/Profile.css'; // Import the CSS file

export default function ProfileComponent() {
    const {username} = useParams();
    const [userDetails, setUserDetails] = useState(null);
    const { token } = useAuth(); // Get the JWT token from the AuthContext

    useEffect(() => {
        // Make an API call to the server to get the user details
        getUser(username, token)
            .then(response => {
                // Store the user details in the state variable
                setUserDetails(response);
            })
            .catch(error => {
                console.error('Failed to fetch user details:', error);
            });
    }, [username, token]);

    return (
        <div className="container profile-container">
            <div className="row justify-content-center mt-5">
                <div className="col-md-6">
                    <div className="card">
                        <div className="card-header username">
                            <h1>Welcome {username} !</h1>
                        </div>
                        <div className="card-body">
                            {/* Display the user details */}
                            {userDetails && (
                                <div>
                                    <p>Username: {userDetails.username}</p>
                                    <p>University: {userDetails.university}</p>
                                    <p>Degree: {userDetails.degree}</p>
                                    <p>Curriculum: {userDetails.curriculum}</p>
                                    <p>Email: {userDetails.email}</p>
                                    <p>Gender: {userDetails.gender}</p>
                                    {/* Add more fields as necessary */}
                                </div>
                            )}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}