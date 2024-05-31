import { useEffect, useState } from "react";
import { useAuth } from './security/AuthContext';
import { getGroupsForUser } from './api/StudyMateApiService';

export default function GroupsPerUser() {
    const { username, token } = useAuth(); // Get the username and token from the AuthContext
    const [groups, setGroups] = useState([]); // Create a state variable for the groups

    useEffect(() => {
        // Make an API call to fetch the groups that the user is a member of
        getGroupsForUser(username, token)
            .then(response => {
                // Update the state variable with the fetched groups
                console.log('Groups:', response);
                setGroups(response);
            })
            .catch(error => {
                console.error('Failed to fetch groups:', error);
            });
    }, [username, token]);

    return (
        <div className="container">
            <div className="row justify-content-center mt-5">
                <div className="col-md-6">
                    <div className="card">
                        <div className="card-header"><h1>Groups</h1></div>
                        <div className="card-body">
                            <p>Groups are a way to connect with other students and study together.</p>
                            <p>Join a group to collaborate with other students and share study resources.</p>
                            {/* Map over the groups and display each one */}
                            {groups.map(group => (
                                <div key={group.id}>
                                    <h2>{group.name}</h2>
                                    <p>{group.description}</p>
                                </div>
                            ))}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}