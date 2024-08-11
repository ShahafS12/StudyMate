import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import Calendar from 'react-calendar';
import 'react-calendar/dist/Calendar.css';
import {
    getGroupByName,
    isUserMemberInGroup,
    getSessionsForGroup,
    joinSession,
    exitSession,
    joinGroup,
    exitGroup
} from './api/StudyMateApiService';
import { useAuth } from './security/AuthContext';
import '../styles/Group.css';

export default function Group() {
    const { groupName } = useParams(); // Get the group name from the route parameters
    const [group, setGroup] = useState(null);
    const [isMember, setIsMember] = useState(false);
    const [sessions, setSessions] = useState([]);
    const [selectedDate, setSelectedDate] = useState(null);
    const [selectedSessions, setSelectedSessions] = useState([]);
    const [joinedSessions, setJoinedSessions] = useState(new Set());
    const navigate = useNavigate();
    const { username, token } = useAuth(); // Get current user info

    useEffect(() => {
        if (!groupName) return;

        const decodedGroupName = decodeURIComponent(groupName);

        // Fetch group details by name
        getGroupByName(decodedGroupName)
            .then(response => {
                setGroup(response);

                // Fetch group sessions
                getSessionsForGroup(decodedGroupName, token)
                    .then(sessionResponse => setSessions(sessionResponse))
                    .catch(error => console.error('Failed to fetch group sessions:', error));

                // Check if the logged-in user is a member of this group
                if (username && token) {
                    isUserMemberInGroup(username, decodedGroupName, token)
                        .then(isMember => setIsMember(isMember))
                        .catch(error => console.error('Failed to check membership:', error));
                }
            })
            .catch(error => {
                console.error('Failed to fetch group details:', error);
                // Optionally, navigate away or display an error message
            });
    }, [groupName, username, token]);

    useEffect(() => {
        // Optionally, you can implement logic to fetch joined sessions for the user
        // and update the joinedSessions state accordingly
    }, [sessions]);

    const getSessionsForDate = (date) => {
        const dateStr = date.toISOString().split('T')[0]; // Get the date in YYYY-MM-DD format
        return sessions.filter(session => session.sessionDate.startsWith(dateStr));
    };

    const handleDateClick = (date) => {
        const sessionsForDate = getSessionsForDate(date);
        setSelectedDate(date);
        setSelectedSessions(sessionsForDate);
    };

    const formatSessionTime = (dateTime) => {
        const date = new Date(dateTime);
        return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
    };

    const isDateWithSession = (date) => {
        return getSessionsForDate(date).length > 0;
    };

    const handleCreateSessionClick = () => {
        if (group?.groupName) {
            navigate(`/create-session/${group.groupName}`); // Navigate to CreateSession page with the groupName
        }
    };

    const handleJoinExitSession = async (sessionId, action) => {
        try {
            if (action === 'join') {
                await joinSession(sessionId, token);
                setJoinedSessions(prev => new Set(prev).add(sessionId));
            } else if (action === 'exit') {
                await exitSession(sessionId, token);
                setJoinedSessions(prev => {
                    const newSet = new Set(prev);
                    newSet.delete(sessionId);
                    return newSet;
                });
            }
        } catch (error) {
            console.error(`Failed to ${action} session:`, error);
        }
    };

    const handleJoinExitGroup = async (action) => {
        try {
            if (action === 'join') {
                await joinGroup(token, group.groupName, username);
                setIsMember(true);
            } else if (action === 'exit') {
                await exitGroup(token, group.groupName, username);
                setIsMember(false);
            }
        } catch (error) {
            console.error(`Failed to ${action} group:`, error);
        }
    };

    if (!group) {
        return <p>Loading group details...</p>;
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

                                    {/* Conditionally render the "Create New Session" button */}
                                    {username && isMember && (
                                        <button className="btn btn-primary mt-3" onClick={handleCreateSessionClick}>
                                            Create New Session
                                        </button>
                                    )}

                                    {/* Conditionally render the "Join/Exit Group" button */}
                                    {username && (
                                        <button
                                            className={`btn ${isMember ? 'btn-danger' : 'btn-success'} mt-3`}
                                            onClick={() => handleJoinExitGroup(isMember ? 'exit' : 'join')}
                                        >
                                            {isMember ? 'Exit Group' : 'Join Group'}
                                        </button>
                                    )}
                                </div>
                                <div className="calendar-container col-md-6">
                                    <Calendar
                                        onClickDay={handleDateClick}
                                        tileClassName={({ date, view }) => {
                                            if (view === 'month' && isDateWithSession(date)) {
                                                return 'highlight'; // Add a special class if the date has sessions
                                            }
                                            return null;
                                        }}
                                    />
                                    {selectedDate && selectedSessions.length > 0 && (
                                        <div className="session-details-container mt-3">
                                            <h4>Sessions on {selectedDate.toLocaleDateString()}</h4>
                                            {selectedSessions.map((session) => (
                                                <div key={session.id} className="session-details">
                                                    <p><strong>Time:</strong> {formatSessionTime(session.sessionDate)}</p>
                                                    <p><strong>Location:</strong> {session.location}</p>
                                                    <p><strong>Description:</strong> {session.description}</p>
                                                    {username && (
                                                        <button
                                                            className={`btn ${joinedSessions.has(session.id) ? 'btn-danger' : 'btn-success'}`}
                                                            onClick={() =>
                                                                handleJoinExitSession(session.id, joinedSessions.has(session.id) ? 'exit' : 'join')
                                                            }
                                                        >
                                                            {joinedSessions.has(session.id) ? 'Exit Session' : 'Join Session'}
                                                        </button>
                                                    )}
                                                </div>
                                            ))}
                                        </div>
                                    )}
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}
