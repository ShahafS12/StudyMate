import {Link} from "react-router-dom";
import logo from '../images/studymatelogo.JPG';
import '../styles/Header.css';
import {useAuth} from "./security/AuthContext";
import {useState} from "react";


export default function Header() {
    const authContext = useAuth();
    const isAuthenticated = authContext.isAuthenticated;
    const username = authContext.username; // Get the username from the authContext
    const [searchQuery, setSearchQuery] = useState('');

    function logout() {
        authContext.logout()
    }

    function handleSearchSubmit(event) {
        event.preventDefault(); // Prevent default form submission behavior
        console.log('Search query:', searchQuery);
    }

    return (
        <header className="border-bottom border-light border-5 mb-5 p-2">
            <div className="container">
                <div className="row">
                    <nav className="navbar navbar-expand-lg">
                        <Link className="navbar-brand ms-2" to="/home">
                            <div className="fs-2 fw-bold text-black title">
                                <img className="logo" src={logo} alt="StudyMate Logo"  />
                                StudyMate
                            </div>
                        </Link>

                        <div className="collapse navbar-collapse">
                            <ul className="navbar-nav">
                                <li className="nav-item fs-5">
                                    {isAuthenticated &&
                                        <Link className="nav-link" to={`/profile/${username}`}>Profile</Link>}
                                </li>
                                <li className="nav-item fs-5">
                                    {isAuthenticated &&
                                        <Link className="nav-link" to="/groups">My Groups</Link>}
                                </li>
                                <li className="nav-item fs-5">
                                    {isAuthenticated &&
                                        <Link className="nav-link" to="/sessions">My Sessions</Link>}
                                </li>
                            </ul>
                        </div>
                        <ul className="navbar-nav">
                            <li className="nav-item fs-5">
                                {!isAuthenticated &&
                                    <Link className="nav-link" to="/login">Login</Link>}
                            </li>
                            <li className="nav-item fs-5">
                                {!isAuthenticated &&
                                    <Link className="nav-link" to="/register">Register</Link>}
                            </li>
                            <li className="nav-item fs-5">
                                {isAuthenticated &&
                                    <div>
                                        <form className="d-flex" onSubmit={handleSearchSubmit}>
                                            <input className="form-control me-2" type="search" placeholder="Search"
                                                   aria-label="Search" value={searchQuery} onChange={(e) => setSearchQuery(e.target.value)}/>
                                            <button className="btn btn-outline-primary" type="submit">Search</button>
                                        </form>
                                    </div>}
                            </li>
                            <li className="nav-item fs-5">
                                {isAuthenticated &&
                                    <Link className="nav-link" to="/logout" onClick={logout}>Logout</Link>}
                            </li>
                        </ul>
                    </nav>
                </div>
            </div>
        </header>
    );
}


