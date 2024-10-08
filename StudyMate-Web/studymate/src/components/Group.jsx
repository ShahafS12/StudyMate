import React, { useEffect, useState, useCallback } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
    getGroupByName,
    isUserMemberInGroup,
    getSessionsForGroup,
    joinSession,
    exitSession,
    deleteSession,
    joinGroup,
    exitGroup,
    deleteGroup
} from './api/StudyMateApiService';
import { useAuth } from './security/AuthContext';
import SessionCalendar from './SessionCalendar';
import '../styles/Group.css';

export default function Group({ onGroupDeleted }) {
    const { groupName } = useParams();
    const [group, setGroup] = useState(null);
    const [isMember, setIsMember] = useState(false);
    const [joinedSessions, setJoinedSessions] = useState(new Set());
    const navigate = useNavigate();
    const { username, token } = useAuth();

    const fetchGroupDetails = useCallback(() => {
        if (!groupName) return;

        const decodedGroupName = decodeURIComponent(groupName);

        getGroupByName(decodedGroupName)
            .then(response => {
                setGroup(response);

                if (username && token) {
                    isUserMemberInGroup(username, decodedGroupName, token)
                        .then(isMember => {
                            setIsMember(isMember);
                        })
                        .catch(error => console.error('Failed to check membership:', error));
                }
            })
            .catch(error => console.error('Failed to fetch group details:', error));
    }, [groupName, username, token]);

    useEffect(() => {
        fetchGroupDetails();
    }, [fetchGroupDetails]);

    const handleCreateSessionClick = () => {
        if (group?.groupName) {
            navigate(`/create-session/${group.groupName}`);
        }
    };

    const handleJoinExitSession = async (sessionId, action) => {
        try {
            if (action === 'join') {
                await joinSession(token, sessionId);
                setJoinedSessions(prev => new Set(prev).add(sessionId));
            } else if (action === 'exit') {
                await exitSession(token, sessionId);
                setJoinedSessions(prev => {
                    const newSet = new Set(prev);
                    newSet.delete(sessionId);
                    return newSet;
                });
            } else if (action === 'delete') {
                await deleteSession(token, sessionId);
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
            fetchGroupDetails(); // Refresh group details after join/exit
        } catch (error) {
            console.error(`Failed to ${action} group:`, error);
        }
    };

    const handleDeleteGroup = async () => {
        try {
            if (group?.groupName) {
                await deleteGroup(token, group.groupName);
                if (onGroupDeleted) {
                    onGroupDeleted(); // Notify parent component
                }
                navigate('/groups'); // Redirect after deletion
            }
        } catch (error) {
            console.error('Failed to delete group:', error);
        }
    };

    if (!group) {
        return <p>Loading group details...</p>;
    }

    const isOnlyAdmin = group.groupAdmins.length === 1 && group.groupAdmins.includes(username);

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
                                    {username && isMember && (
                                        <button className="btn btn-primary mt-3" onClick={handleCreateSessionClick}>
                                            Create New Session
                                        </button>
                                    )}
                                    {username && !isOnlyAdmin && (
                                        <button
                                            className={`btn ${isMember ? 'btn-danger' : 'btn-success'} mt-3`}
                                            onClick={() => handleJoinExitGroup(isMember ? 'exit' : 'join')}
                                        >
                                            {isMember ? 'Exit Group' : 'Join Group'}
                                        </button>
                                    )}
                                    {username && group.groupAdmins.includes(username) && (
                                        <button
                                            className="btn btn-danger mt-3"
                                            onClick={handleDeleteGroup}
                                        >
                                            Delete Group
                                        </button>
                                    )}
                                </div>
                                <div className="calendar-container col-md-6">
                                    <SessionCalendar
                                        fetchSessions={() => getSessionsForGroup(group.groupName, token)}
                                        isMember={isMember}
                                        onSessionAction={handleJoinExitSession}
                                        redirectPath={`/group/${group.groupName}`}  // Redirect after deletion
                                    />
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}
