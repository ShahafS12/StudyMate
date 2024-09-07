import React from 'react';

const NotificationBadge = ({ newNotifications }) => {
    return newNotifications > 0 ? (
        <span className="notification-badge">{newNotifications}</span>
    ) : null;
};

const NotificationItem = ({ message }) => (
    <li>
        <button className="dropdown-item" type="button">{message}</button>
    </li>
);

const NotificationDropdown = ({ newNotifications, notifications }) => (
    <div className="dropdown">
        <button
            className={`notification-button dropdown-toggle ${newNotifications > 0 ? 'new-notifications' : ''}`}
            type="button"
            id="dropdownMenuButton"
            data-bs-toggle="dropdown"
            aria-expanded="false"
            aria-haspopup="true"
        >
            <NotificationBadge newNotifications={newNotifications} />
        </button>
        <ul className="dropdown-menu" aria-labelledby="dropdownMenuButton">
            {notifications.length > 0 ? (
                notifications.map((notification, index) => (
                    <NotificationItem key={index} message={notification.message} />
                ))
            ) : (
                <NotificationItem message="You don't have any notifications" />
            )}
        </ul>
    </div>
);

export default NotificationDropdown;
