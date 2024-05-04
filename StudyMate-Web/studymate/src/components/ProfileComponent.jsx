import {useParams} from "react-router-dom";


export default function ProfileComponent() {
    const {username} = useParams()


    return (
        <div className="Welcome">
            <h1>Welcome {username}</h1>
            <h2></h2>
        </div>
    );
}

