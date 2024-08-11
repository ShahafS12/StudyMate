import React from 'react';
import { Link } from 'react-router-dom';
import '../styles/SearchResultsDropdown.css';

export default function SearchResultsDropdown({ searchResults, onResultClick }) {
    const users = searchResults.filter(result => result.type === 'user');
    const groups = searchResults.filter(result => result.type === 'group');

    return (
        <div className="search-results-dropdown">
            {users.length > 0 && (
                <>
                    <h5>Users</h5>
                    <ul>
                        {users.map((user, index) => (
                            <li key={index}>
                                <Link to={`/profile/${user.name}`} onClick={onResultClick}>
                                    {user.name}
                                </Link>
                            </li>
                        ))}
                    </ul>
                </>
            )}
            {groups.length > 0 && (
                <>
                    <h5>Groups</h5>
                    <ul>
                        {groups.map((group, index) => (
                            <li key={index}>
                                <Link to={`/group/${group.name}`} onClick={onResultClick}>
                                    {group.name}
                                </Link>
                            </li>
                        ))}
                    </ul>
                </>
            )}
        </div>
    );
}
