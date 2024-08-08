import { useEffect, useState } from 'react';
import { useAuth } from './security/AuthContext';
import { getGroups } from './api/StudyMateApiService';
import { Link } from "react-router-dom";
import '../styles/AllGroups.css'; // Ensure this CSS file is created

export default function AllGroups() {
    const { token, isAuthenticated } = useAuth(); // Get the JWT token from the AuthContext
    const [groupNames, setGroupNames] = useState([]);

    // Function to parse the group names
    const parseGroupNames = (data) => {
        return data.map(item => JSON.parse(item).groupName);
    };

    useEffect(() => {
        getGroups(token)
            .then(response => {
                const names = parseGroupNames(response);
                setGroupNames(names);
            })
            .catch(error => {
                console.error('Failed to fetch groups:', error);
            });
    }, [token]);

    return (
        <div className="container groups-container">
            <div className="row justify-content-center mt-5">
                <div className="col-md-8">
                    <div className="card">
                        <div className="card-header">
                            <span className="group-title"><h1>Groups</h1></span>
                            {isAuthenticated &&
                                <Link to="/create-group" className="btn btn-primary">Create New Group</Link>}
                            <p></p>
                            <p>Groups are a way to connect with other students and study together.
                                Join a group to collaborate with other students and share study resources.</p>
                        </div>
                        <div className="card-body">
                            {/* Display each group name in a separate card */}
                            {groupNames.length > 0 ? (
                                <div className="row">
                                    {groupNames.map((groupName, index) => (
                                        <div key={index} className="col-md-4 mb-3">
                                            <div className="card">
                                                <div className="card-body">
                                                    <h5 className="card-title">
                                                        <Link to={`/group/${encodeURIComponent(groupName)}`} className="group-link">{groupName}</Link>
                                                    </h5>
                                                </div>
                                            </div>
                                        </div>
                                    ))}
                                </div>
                            ) : (
                                <p>No groups found.</p>
                            )}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}
