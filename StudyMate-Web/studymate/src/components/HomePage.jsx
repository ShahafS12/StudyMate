import { Link } from 'react-router-dom';

export default function HomePage() {
return (
        <div className="container">
            <div className="row justify-content-center mt-5">
                <div className="col-md-6">
                    <div className="card">
                        <div className="card-header"><h1>Welcome to StudyMate</h1></div>
                        <div className="card-body">
                        <p>StudyMate is a social platform for students to connect, collaborate and study together.</p>
                            <p>Sign up to create a profile and join groups to study together.</p>
                            <p>Join us today and start your journey to success!</p>
                            <Link to="/groups" className="btn btn-primary">Explore Our Groups</Link>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}