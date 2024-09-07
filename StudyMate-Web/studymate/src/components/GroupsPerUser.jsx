import { useEffect, useState } from "react";
import { useAuth } from './security/AuthContext';
import { getGroupsForUser } from './api/StudyMateApiService';
import { Link } from "react-router-dom";
import '../styles/AllGroups.css'; // Correct path to CSS file if needed

export default function GroupsPerUser() {
    const { username, token } = useAuth(); // Get the username and token from the AuthContext
    const [groups, setGroups] = useState([]); // Create a state variable for the groups

    useEffect(() => {
        // Make an API call to fetch the groups that the user is a member of
        getGroupsForUser(username, token)
            .then(response => {
                // Update the state variable with the fetched groups
                setGroups(response);
            })
            .catch(error => {
                console.error('Failed to fetch groups:', error);
            });
    }, [username, token]);

    return (
        <div className="container">
            <div className="row justify-content-center mt-5">
                <div className="col-md-8">
                    <div className="card">
                        <div className="card-header">
                            <h1>My Groups</h1>
                        </div>
                        <div className="card-body">
                            {groups.length > 0 ? (
                                groups.map(group => (
                                    <div key={group.groupName} className="card mb-3">
                                        <div className="card-body">
                                            <h2>
                                                <Link to={`/group/${encodeURIComponent(group.groupName)}`} className="group-link">
                                                    {group.groupName}
                                                </Link>
                                            </h2>
                                            <p><strong>Institute:</strong> {group.institute}</p>
                                            <p><strong>Curriculum:</strong> {group.curriculum}</p>
                                        </div>
                                    </div>
                                ))
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
