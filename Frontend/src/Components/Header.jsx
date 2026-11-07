import { useNavigate } from "react-router-dom";

import NavButton from "./NavButton.jsx";
import useAuth from '../Hooks/UseAuth';

import '../Styles/Header.css';

const Header = () => {

    const navigate = useNavigate();
    const { isAuthenticated } = useAuth();

    const navigateToDashboard = () => {
        if (isAuthenticated) {
            navigate('/dashboard');
        }
    }

    const navigateToSketch = () => {
        if (isAuthenticated) {
            navigate('/sketch');
        }
    }

    return (
        <header id="header">
            <nav className="header__nav">
                <div className="nav__title col-2">
                    <span className="title__text">FinantialWeb</span>
                </div>
                <div className="nav_empty col-2"></div>
                <div className="nav__buttons col-4">
                    <NavButton className="buttons__dashboard" text="Dashboard" onClick={navigateToDashboard}/>
                    <NavButton className="buttons__sketch" text="Sketch" onClick={navigateToSketch}/>
                </div>
                <div className="nav_empty col-2"></div>
                <div className="nav__account col-2">
                    <NavButton className="account__button col" text="Mi Cuenta"/>
                </div>
            </nav>
        </header>
    );
};

export default Header;