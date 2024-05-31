import React, { useState, useEffect } from 'react';
import Calendar from 'react-calendar';
import 'react-calendar/dist/Calendar.css';
import '../styles/SessionsPerUser.css'; // Import the CSS file
import { useAuth } from './security/AuthContext';
import {getSessionsForUser} from './api/StudyMateApiService';

export default function SessionsPerUser() {
    const { token } = useAuth(); // Get the JWT token from the AuthContext
    const [sessions, setSessions] = useState([]);

    useEffect(() => {
        getSessionsForUser(token)
            .then(response => {
                setSessions(response);
            })
            .catch(error => {
                console.error('Failed to fetch sessions:', error);
            });
    }, [token]);

    const getSessionsForDate = (date) => {
        const dateStr = date.toISOString().split('T')[0]; // Get the date in YYYY-MM-DD format
        return sessions.filter(session => session.date.startsWith(dateStr));
    };

    return (
        <div className="container sessions-container">
            <div className="row justify-content-center mt-5">
                <div className="col-md-6">
                    <div className="card">
                        <div className="card-header">
                            <span className="session-title"><h1>My Sessions</h1></span>
                        </div>
                        <div className="card-body calendar-card-body"> {/* Add unique class here */}
                            <Calendar
                                tileContent={({ date, view }) => view === 'month' && (
                                    <div>
                                        {getSessionsForDate(date).map((session, index) => (
                                            <div key={index}>
                                                <p>{session.time}</p>
                                                <p>{session.details}</p>
                                            </div>
                                        ))}
                                    </div>
                                )}
                            />
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}