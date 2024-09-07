import React, { useState, useEffect } from 'react';
import Calendar from 'react-calendar';
import 'react-calendar/dist/Calendar.css';
import '../styles/SessionCalendar.css';
import { useAuth } from "./security/AuthContext";

export default function SessionCalendar({ fetchSessions, isMember, onSessionAction }) {
    const { username } = useAuth();
    const [sessions, setSessions] = useState([]);
    const [selectedDate, setSelectedDate] = useState(null);
    const [selectedSessions, setSelectedSessions] = useState([]);

    useEffect(() => {
        const loadSessions = async () => {
            try {
                const response = await fetchSessions();
                setSessions(response);
            } catch (error) {
                console.error('Failed to fetch sessions:', error);
            }
        };

        loadSessions();
    }, [fetchSessions]);

    const getSessionsForDate = (date) => {
        const dateStr = date.toISOString().split('T')[0];
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

    const isUserAdmin = (adminsName) => {
        return adminsName.includes(username);
    };

    const handleSessionAction = async (sessionId, action) => {
        try {
            await onSessionAction(sessionId, action);
            // Re-fetch sessions to get updated list
            const updatedSessions = await fetchSessions();
            setSessions(updatedSessions);
            // Optionally clear selected sessions and date
            setSelectedDate(null);
            setSelectedSessions([]);
        } catch (error) {
            console.error(`Failed to ${action} session:`, error);
        }
    };

    return (
        <div className="calendar-container">
            <Calendar
                onClickDay={handleDateClick}
                tileClassName={({ date, view }) => {
                    if (view === 'month' && isDateWithSession(date)) {
                        return 'highlight';
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
                                <>
                                    {isUserAdmin(session.adminsName) ? (
                                        <button
                                            className="btn btn-danger"
                                            onClick={() => handleSessionAction(session.id, 'delete')}
                                        >
                                            Delete Session
                                        </button>
                                    ) : (
                                        <button
                                            className={`btn ${session.membersName.includes(username) ? 'btn-danger' : 'btn-success'}`}
                                            onClick={() => handleSessionAction(session.id, session.membersName.includes(username) ? 'exit' : 'join')}
                                        >
                                            {session.membersName.includes(username) ? 'Exit Session' : 'Join Session'}
                                        </button>
                                    )}
                                </>
                            )}
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
}
