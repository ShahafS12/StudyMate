import {useParams} from "react-router-dom";
import { useEffect } from "react";


export default function ProfileComponent() {
    const {username} = useParams()

    useEffect(() => {
        // This will cause the component to re-render when the username changes
    }, [username]);

    return (
        <div className="Welcome">
            <h1>Welcome {username}</h1>
        </div>
    );
}

