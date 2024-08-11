import React, { useState, useEffect } from 'react';
import Calendar from 'react-calendar';
import 'react-calendar/dist/Calendar.css';
import '../styles/SessionsPerUser.css';
import { useAuth } from './security/AuthContext';
import { getSessionsForUser } from './api/StudyMateApiService';

export default function SessionsPerUser() {
    const { username, token } = useAuth(); // Get current user info
    const [sessions, setSessions] = useState([]);
    const [selectedDate, setSelectedDate] = useState(null);
    const [selectedSessions, setSelectedSessions] = useState([]);

    useEffect(() => {
        getSessionsForUser(token, username)
            .then(response => {
                setSessions(response);
            })
            .catch(error => {
                console.error('Failed to fetch sessions:', error);
            });
    }, [token, username]);

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

    return (
        <div className="container sessions-container">
            <div className="row justify-content-center mt-5">
                <div className="col-md-6">
                    <div className="card">
                        <div className="card-header">
                            <span className="session-title"><h1>My Sessions</h1></span>
                        </div>
                        <div className="card-body calendar-card-body">
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
                                        </div>
                                    ))}
                                </div>
                            )}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}
