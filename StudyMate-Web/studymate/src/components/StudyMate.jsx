import './StudyMate.css';
import {BrowserRouter, Route, Routes} from "react-router-dom";
import Login from "./Login";
import WelcomeComponent from "./WelcomeComponent";
import ErrorComponent from "./ErrorComponent";


export default function StudyMate() {
    return (
        <div className="StudyMate">
            <BrowserRouter>
                <Routes>
                    <Route path='/' element={ <Login /> }></Route>
                    <Route path='/login' element={ <Login /> }></Route>
                    <Route path='/welcome/:username' element={ <WelcomeComponent /> }></Route>
                    <Route path='*' element={ <ErrorComponent /> }></Route>
                </Routes>
            </BrowserRouter>
        </div>
    );
}

