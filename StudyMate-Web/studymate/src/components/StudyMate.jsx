import '../styles/StudyMate.css';
import {BrowserRouter, Navigate, Route, Routes} from "react-router-dom";
import Login from "./Login";
import ProfileComponent from "./ProfileComponent";
import ErrorComponent from "./ErrorComponent";
import Header from "./Header";
import Footer from "./Footer";
import Logout from "./Logout";
import AuthProvider, {useAuth} from "./security/AuthContext";
import Register from "./Register";
import HomePage from "./HomePage";
import GroupsPerUser from "./GroupsPerUser";
import SessionsPerUser from "./SessionsPerUser";
import Group from "./Group";
import CreateGroup from "./CreateGroup";

function AuthenticatedRoute({children}) {
    const authContext = useAuth()

    if (authContext.isAuthenticated) {
        return children
    } else {
        return <Navigate to="/"/>
    }
}


export default function StudyMate() {
    return (
        <div className="StudyMate">
            <AuthProvider>
                <BrowserRouter>
                    <Header />
                    <Routes>
                        <Route path='/' element={ <HomePage /> }></Route>
                        <Route path='/login' element={ <Login /> }></Route>
                        <Route path='/register' element={ <Register /> }></Route>
                        <Route path='/home' element={ <HomePage /> }></Route>
                        <Route path='/groups' element={ <Group /> }></Route>
                        <Route path='/groups:groupname' element={ <Group /> }></Route>




                        <Route path='/profile/:username' element={
                            <AuthenticatedRoute>
                                <ProfileComponent />
                            </AuthenticatedRoute>}>
                        </Route>
                        <Route path='/create-group' element={
                            <AuthenticatedRoute>
                                <CreateGroup  />
                            </AuthenticatedRoute>}>
                        </Route>
                        <Route path='/logout' element={
                            <AuthenticatedRoute>
                                <Logout />
                            </AuthenticatedRoute>}>
                        </Route>
                        <Route path='/mygroups' element={
                            <AuthenticatedRoute>
                                <GroupsPerUser />
                            </AuthenticatedRoute>}>
                        </Route>
                        <Route path='/sessions' element={
                            <AuthenticatedRoute>
                                <SessionsPerUser />
                            </AuthenticatedRoute>}>
                        </Route>

                        <Route path='*' element={ <ErrorComponent /> }></Route>
                    </Routes>
                    <Footer />
                </BrowserRouter>
            </AuthProvider>
        </div>
    );
}

