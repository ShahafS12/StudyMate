import {Link} from "react-router-dom";
import logo from '../images/studymatelogo.JPG';
import '../styles/Header.css';
import {useAuth} from "./security/AuthContext";


export default function Header() {
    const authContext = useAuth();
    const isAuthenticated = authContext.isAuthenticated;

    function logout() {
        authContext.logout()
    }

    return (
        <header className="border-bottom border-light border-5 mb-5 p-2">
            <div className="container">
                <div className="row">
                    <nav className="navbar navbar-expand-lg">
                        <Link className="navbar-brand ms-2" to="/homepage">
                            <div className="fs-2 fw-bold text-black title">
                                <img className="logo" src={logo} alt="StudyMate Logo"  />
                                StudyMate
                            </div>
                        </Link>

                        <div className="collapse navbar-collapse">
                            <ul className="navbar-nav">
                                <li className="nav-item fs-5">
                                    {isAuthenticated &&
                                        <Link className="nav-link" to="/welcome/Shahar">Home</Link>}
                                </li>
                                <li className="nav-item fs-5">
                                    {isAuthenticated &&
                                        <Link className="nav-link" to="/groups">My Groups</Link>}
                                </li>
                            </ul>
                        </div>
                        <ul className="navbar-nav">
                            <li className="nav-item fs-5">
                                {!isAuthenticated &&
                                    <Link className="nav-link" to="/login">Login</Link>}
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


