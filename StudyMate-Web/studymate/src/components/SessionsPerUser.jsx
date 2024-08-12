import React from 'react';
import { useAuth } from './security/AuthContext';
import { getSessionsForUser, exitSession, deleteSession } from './api/StudyMateApiService';
import SessionCalendar from './SessionCalendar';

export default function SessionsPerUser() {
    const { username, token } = useAuth();

    const handleSessionAction = async (sessionId, action) => {
        try {
            if (action === 'exit') {
                await exitSession(token, sessionId);
            } else if (action === 'delete') {
                await deleteSession(token, sessionId);
            }
        } catch (error) {
            console.error(`Failed to ${action} session:`, error);
        }
    };

    return (
        <div className="container sessions-container">
            <div className="row justify-content-center mt-5">
                <div className="col-md-6">
                    <div className="card">
                        <div className="card-header">
                            <h1>My Sessions</h1>
                        </div>
                        <div className="card-body calendar-card-body">
                            <SessionCalendar
                                fetchSessions={() => getSessionsForUser(token, username)}
                                isMember={true}  // Show action buttons
                                onSessionAction={handleSessionAction}
                                redirectPath="/sessions"  // Redirect after deletion
                            />
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}
