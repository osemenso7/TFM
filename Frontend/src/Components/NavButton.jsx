import '../Styles/NavButton.css';

const NavButton = ({ text, onClick }) => {

    return (
        <button type="button" className="navButton" onClick={onClick}>
            <span className="navButton__text">{text}</span>
        </button>
    );
};

export default NavButton;