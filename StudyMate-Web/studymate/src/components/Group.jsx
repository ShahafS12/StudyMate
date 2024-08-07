import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import Calendar from 'react-calendar';
import 'react-calendar/dist/Calendar.css';
import { getGroupByName, getSessionsForGroup } from './api/StudyMateApiService';
import '../styles/Group.css'

export default function Group() {
    const { groupName } = useParams(); // Get the group name from the route parameters
    const [group, setGroup] = useState(null);
    const [sessions, setSessions] = useState([]);

    useEffect(() => {
        // Decode the group name from the URL
        const decodedGroupName = decodeURIComponent(groupName);

        // Fetch group details by name
        getGroupByName(decodedGroupName)
            .then(response => {
                setGroup(response);
                // Optionally fetch sessions for the group here if needed
            })
            .catch(error => {
                console.error('Failed to fetch group details or sessions:', error);
            });
    }, [groupName]);

    if (!group) {
        return <p>Loading...</p>;
    }

    return (
        <div className="container mt-5">
            <div className="row justify-content-center">
                <div className="col-md-12">
                    <div className="card">
                        <div className="card-header text-center">
                            <h1>{group.groupName}</h1>
                        </div>
                        <div className="card-body">
                            <div className="d-flex flex-column flex-md-row">
                                <div className="group-details col-md-6">
                                    <p><strong>Institute:</strong> {group.institute}</p>
                                    <p><strong>Curriculum:</strong> {group.curriculum}</p>
                                    <p><strong>Admin(s):</strong> {group.groupAdmins.join(', ')}</p>
                                    <p><strong>Members:</strong> {group.members.join(', ')}</p>
                                    <p><strong>Members Count:</strong> {group.membersCount}</p>
                                    <p><strong>Created Date:</strong> {group.createdDate ? new Date(group.createdDate).toLocaleDateString() : 'N/A'}</p>
                                </div>
                                <div className="calendar-container col-md-6">
                                    <Calendar />
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}
