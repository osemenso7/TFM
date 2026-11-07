import { Routes, Route, Navigate } from 'react-router-dom';

import { AuthProvider} from './Context/AuthContext';
import useAuth from './Hooks/UseAuth';

import Header from './Components/Header.jsx';
import Login from './Views/Login.jsx';
import Register from './Views/Register.jsx';
import Dashboard from './Views/Dashboard.jsx';
import Sketch from './Views/Sketch.jsx';

function AppContent() {
    const { isAuthenticated } = useAuth();

     return (
        <div className="App">
            <Header/>
            <Routes>
                <Route 
                    path="/" 
                    element={
                        isAuthenticated 
                            ? <Dashboard/>
                            : <Login/>
                    } 
                />
                <Route 
                    path="/register" 
                    element={
                        isAuthenticated 
                            ? <Dashboard/>
                            : <Register/> 
                    }
                />
                <Route path="/dashboard" element={<Dashboard/>}/>
                <Route path="/sketch" element={<Sketch/>}/>
            </Routes>
        </div>
    );
}

function App() {
    return (
        <AuthProvider>
            <AppContent/>
        </AuthProvider>
    );
}

export default App