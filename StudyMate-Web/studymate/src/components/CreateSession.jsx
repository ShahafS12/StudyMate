import { useState } from 'react';
import { useAuth } from './security/AuthContext';
import { createSession } from './api/StudyMateApiService';
import { useNavigate, useParams } from 'react-router-dom';

export default function CreateSession() {
    const { token, username } = useAuth();
    const { groupName } = useParams(); // Get the groupName from the URL parameters
    const [membersName] = useState([]);
    const [adminsName] = useState([username]);
    const [sessionDate, setSessionDate] = useState('');
    const [location, setLocation] = useState('');
    const [description, setDescription] = useState('');
    const [maxParticipants, setMaxParticipants] = useState(10);
    const [isLimited, setIsLimited] = useState(false);
    const [error, setError] = useState(null);


    const navigate = useNavigate();

    function checkValidity(session) {
        if (!session.sessionDate) {
            throw new Error('Session date is required');
        }
        if (!session.location) {
            throw new Error('Location is required');
        }
        if (!session.maxParticipants) {
            throw new Error('Max participants is required');
        }
    }

    const handleSubmit = async (event) => {
        event.preventDefault();
        const session = {
            membersName: membersName,
            adminsName: adminsName,
            createdDate: new Date(),  // Current date and time
            sessionDate: new Date(sessionDate),
            location: location,
            description: description,
            isLimited: isLimited,
            groupName: groupName, // Use the groupName from URL parameters
            maxParticipants: maxParticipants,
        };

        try {
            checkValidity(session);
            await createSession(session, token);
            navigate(`/group/${groupName}`);
        } catch (error) {
            setError(error.message);
            // Handle error
        }
    };

    return (
        <div className="container">
            <div className="row justify-content-center mt-5">
                <div className="col-md-6">
                    <div className="card">
                        <div className="card-header">Create Session for {groupName}</div>
                        <div className="card-body">
                            <form onSubmit={handleSubmit}>
                                <div className="mb-3">
                                    <label htmlFor="sessionDate" className="form-label">Session Date</label>
                                    <input type="datetime-local" className="form-control" id="sessionDate" value={sessionDate}
                                           onChange={(e) => setSessionDate(e.target.value)}/>
                                </div>
                                <div className="mb-3">
                                    <label htmlFor="location" className="form-label">Location</label>
                                    <input type="text" className="form-control" id="location" value={location}
                                           onChange={(e) => setLocation(e.target.value)}/>
                                </div>
                                <div className="mb-3">
                                    <label htmlFor="description" className="form-label">Description</label>
                                    <textarea className="form-control" id="description" value={description}
                                              onChange={(e) => setDescription(e.target.value)}/>
                                </div>
                                <div className="mb-3">
                                    <label htmlFor="maxParticipants" className="form-label">Max Participants</label>
                                    <input type="number" className="form-control" id="maxParticipants" value={maxParticipants}
                                           onChange={(e) => setMaxParticipants(e.target.value)}/>
                                </div>
                                <div className="mb-3 form-check">
                                    <input type="checkbox" className="form-check-input" id="isLimited" checked={isLimited}
                                           onChange={(e) => setIsLimited(e.target.checked)}/>
                                    <label className="form-check-label" htmlFor="isLimited">Is Limited?</label>
                                </div>
                                {error && <div className="alert alert-danger">{error}</div>}
                                <button type="submit" className="btn btn-primary">Create Session</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}
