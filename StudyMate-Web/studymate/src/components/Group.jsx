import { useEffect, useState } from 'react';
import { useAuth } from './security/AuthContext';
import { getGroups } from './api/StudyMateApiService';
import { Link } from "react-router-dom";

export default function Group() {
    const { token, isAuthenticated } = useAuth(); // Get the JWT token from the AuthContext
    const [groups, setGroups] = useState([]);

    useEffect(() => {
        getGroups(token)
            .then(response => {
                setGroups(response);
            })
            .catch(error => {
                console.error('Failed to fetch groups:', error);
            });
    }, [token]);

    return (
        <div className="container groups-container">
            <div className="row justify-content-center mt-5">
                <div className="col-md-6">
                    <div className="card">
                        <div className="card-header">
                            <span className="group-title"><h1>Groups</h1></span>
                            {isAuthenticated && <Link to="/create-group" className="btn btn-primary">Create New Group</Link>}
                        </div>
                        <div className="card-body groups-card-body"> {/* Add unique class here */}
                            {groups.map((group, index) => (
                                <div key={index}>
                                    <p>{group.name}</p>
                                </div>
                            ))}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}
