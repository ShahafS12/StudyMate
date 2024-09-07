import React, { useState, useEffect, useRef } from 'react';
import { Link } from 'react-router-dom';
import logo from '../images/studymatelogo.JPG';
import '../styles/Header.css';
import { useAuth } from "./security/AuthContext";
import { getUserNotifications, search } from './api/StudyMateApiService';
import NotificationDropdown from './NotificationDropdown';
import SearchResultsDropdown from './SearchResultsDropdown';

export default function Header() {
    const authContext = useAuth();
    const isAuthenticated = authContext.isAuthenticated;
    const username = authContext.username;
    const [searchQuery, setSearchQuery] = useState('');
    const { token } = useAuth();
    const [notifications, setNotifications] = useState([]);
    const [searchResults, setSearchResults] = useState([]);
    const [showDropdown, setShowDropdown] = useState(false);
    const dropdownRef = useRef(null);

    useEffect(() => {
        if (username && token) {
            getUserNotifications(username, token)
                .then(response => {
                    setNotifications(response);
                })
                .catch(error => {
                    console.error('Failed to fetch notifications:', error);
                });
        }
    }, [username, token]);

    const newNotifications = notifications.filter(notification => !notification.read).length;

    useEffect(() => {
        function handleClickOutside(event) {
            if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
                setShowDropdown(false);
                setSearchResults([]); // Clear search results
            }
        }
        document.addEventListener('mousedown', handleClickOutside);
        return () => {
            document.removeEventListener('mousedown', handleClickOutside);
        };
    }, [dropdownRef]);

    function logout() {
        authContext.logout();
    }

    function handleSearchSubmit(event) {
        event.preventDefault();
        search(searchQuery).then(response => {
            setSearchResults(response);
            setShowDropdown(true);
        });
    }

    function handleResultClick() {
        setShowDropdown(false);
        setSearchResults([]); // Clear search results after clicking a result
    }

    return (
        <header className="border-bottom border-light border-5 mb-5 p-2">
            <div className="container">
                <div className="row">
                    <nav className="navbar navbar-expand-lg">
                        <Link className="navbar-brand ms-2" to="/home">
                            <div className="fs-2 fw-bold text-black title">
                                <img className="logo" src={logo} alt="StudyMate Logo" />
                                StudyMate
                            </div>
                        </Link>
                        <div className="collapse navbar-collapse">
                            <ul className="navbar-nav">
                                {isAuthenticated && (
                                    <>
                                        <li className="nav-item fs-5">
                                            <Link className="nav-link" to={`/profile/${username}`}>Profile</Link>
                                        </li>
                                        <li className="nav-item fs-5">
                                            <Link className="nav-link" to="/mygroups">My Groups</Link>
                                        </li>
                                        <li className="nav-item fs-5">
                                            <Link className="nav-link" to="/sessions">My Sessions</Link>
                                        </li>
                                    </>
                                )}
                            </ul>
                        </div>
                        <ul className="navbar-nav">
                            {isAuthenticated ? (
                                <>
                                    <li className="nav-item fs-5">
                                        <NotificationDropdown
                                            newNotifications={newNotifications}
                                            notifications={notifications}
                                        />
                                    </li>
                                    <li className="nav-item fs-5">
                                        <div ref={dropdownRef} className="search-bar-container">
                                            <form className="d-flex" onSubmit={handleSearchSubmit}>
                                                <input
                                                    className="form-control me-2"
                                                    type="search"
                                                    placeholder="Search"
                                                    aria-label="Search"
                                                    value={searchQuery}
                                                    onChange={(e) => setSearchQuery(e.target.value)}
                                                    onFocus={() => setShowDropdown(searchResults.length > 0)}
                                                />
                                                <button className="btn btn-outline-primary" type="submit">Search</button>
                                            </form>
                                            {showDropdown && (
                                                <SearchResultsDropdown
                                                    searchResults={searchResults}
                                                    onResultClick={handleResultClick}
                                                />
                                            )}
                                        </div>
                                    </li>
                                    <li className="nav-item fs-5">
                                        <Link className="nav-link" to="/logout" onClick={logout}>Logout</Link>
                                    </li>
                                </>
                            ) : (
                                <>
                                    <li className="nav-item fs-5">
                                        <Link className="nav-link" to="/login">Login</Link>
                                    </li>
                                    <li className="nav-item fs-5">
                                        <Link className="nav-link" to="/register">Register</Link>
                                    </li>
                                </>
                            )}
                        </ul>
                    </nav>
                </div>
            </div>
        </header>
    );
}
