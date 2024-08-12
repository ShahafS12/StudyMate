import React from 'react';
import { useAuth } from './security/AuthContext';
import { getSessionsForUser } from './api/StudyMateApiService';
import SessionCalendar from './SessionCalendar';

export default function SessionsPerUser() {
    const { username, token } = useAuth();

    return (
        <div className="container sessions-container">
            <div className="row justify-content-center mt-5">
                <div className="col-md-6">
                    <div className="card">
                        <div className="card-header">
                            <span className="session-title"><h1>My Sessions</h1></span>
                        </div>
                        <div className="card-body calendar-card-body">
                            <SessionCalendar
                                fetchSessions={() => getSessionsForUser(token, username)}
                                isMember={false} // No membership checks needed here
                                onSessionAction={null} // No session action buttons in this context
                            />
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}
