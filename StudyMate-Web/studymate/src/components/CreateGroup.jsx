import { useState } from 'react';
import { useAuth } from './security/AuthContext';
import { createGroup } from './api/StudyMateApiService';
import { useNavigate } from 'react-router-dom';


export default function CreateGroup() {
    const { token, username } = useAuth();
    const [groupName, setGroupName] = useState('');
    const [institute, setInstitute] = useState('');
    const [curriculum, setCurriculum] = useState('');
    const [error, setError] = useState(null);

    const navigate = useNavigate();

    function checkValidity(group) {
        if (!group.groupName) {
            throw new Error('Group name is required');
        }
        if (!group.institute) {
            throw new Error('Institute is required');
        }
        if (!group.curriculum) {
            throw new Error('Curriculum is required');
        }
    }

    const handleSubmit = async (event) => {
        event.preventDefault();
        const group = {
            groupName: groupName,
            institute: institute,
            curriculum: curriculum,
            groupAdmin: username,
            members: [username]
        };

        try {
            checkValidity(group);
            await createGroup(group, token);
            navigate('/groups');
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
                        <div className="card-header">Create Group</div>
                        <div className="card-body">
                            <form onSubmit={handleSubmit}>
                                <div className="mb-3">
                                    <label htmlFor="groupName" className="form-label">Group Name</label>
                                    <input type="text" className="form-control" id="groupName" value={groupName}
                                           onChange={(e) => setGroupName(e.target.value)}/>
                                </div>
                                <div className="mb-3">
                                    <label htmlFor="institute" className="form-label">Institute</label>
                                    <input type="text" className="form-control" id="institute" value={institute}
                                           onChange={(e) => setInstitute(e.target.value)}/>
                                </div>
                                <div className="mb-3">
                                    <label htmlFor="curriculum" className="form-label">Curriculum</label>
                                    <input type="text" className="form-control" id="curriculum" value={curriculum}
                                           onChange={(e) => setCurriculum(e.target.value)}/>
                                </div>
                                {error && <div className="alert alert-danger">{error}</div>}
                                <button type="submit" className="btn btn-primary">Create Group</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}