import React, { useState, useEffect } from 'react';
import Calendar from 'react-calendar';
import 'react-calendar/dist/Calendar.css';
import '../styles/SessionCalendar.css';
import {useAuth} from "./security/AuthContext"; // Create a new CSS file or reuse existing styles

export default function SessionCalendar({ fetchSessions, isMember, onSessionAction }) {
    const {username} = useAuth()
    const [sessions, setSessions] = useState([]);
    const [selectedDate, setSelectedDate] = useState(null);
    const [selectedSessions, setSelectedSessions] = useState([]);

    useEffect(() => {
        fetchSessions()
            .then(response => {
                setSessions(response);
            })
            .catch(error => {
                console.error('Failed to fetch sessions:', error);
            });
    }, [fetchSessions]);

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

    const onDeleteSession = (sessionId) => {

    }

    return (
        <div className="calendar-container">
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
                            {isMember && onSessionAction && (
                                <button
                                    className={`btn ${session.membersName.includes(username) ? 'btn-danger' : 'btn-success'}`}
                                    onClick={() => onSessionAction(session.id, session.membersName.includes(username) ? 'exit' : 'join')}
                                >
                                    {session.membersName.includes(username) ? 'Exit Session' : 'Join Session'}
                                </button>

                            )}

                        </div>
                    ))}
                </div>
            )}
        </div>
    );
}
