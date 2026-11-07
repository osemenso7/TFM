import { Link } from 'react-router-dom';
import { useState } from 'react';

import useAuth from '../Hooks/UseAuth';

import '../Styles/Login.css';

const Login = () => {

    const { login } = useAuth();
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');

    const handleLogin = async (event) => {
        event.preventDefault();
        setLoading(true);
        setError('');

        const formData = new FormData(event.currentTarget);

        const credentials = {
            login: formData.get('login'),
            password: formData.get('password')
        };

        try {
            await login(credentials);
        } catch (error) {
            setError(error.message || 'Error al iniciar sesión');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="login">
            <form className="login__pannel" onSubmit={handleLogin} method="post">
                <h2 className="pannel__title">Iniciar sesión</h2>

                <label htmlFor="user" className="pannel__label">Usuario</label>
                <input type="text" id="user" name="login" placeholder="Correo Electrónico o Username" className="pannel__input" required/>

                <label htmlFor="password" className="pannel__label">Contraseña</label>
                <input type="password" id="password" name="password" placeholder="Contraseña" className="pannel__input" required/>

                {error && <span className='pannel__error'>{error}</span>}

                <button type="submit" className="pannel__button" disabled={loading}>
                    <span className="button__text">Iniciar sesión</span>
                </button>

                <Link to="/register" className="pannel__button">
                    <span className="button__text">Registrarse</span>
                </Link>
            </form>
        </div>
    );
};

export default Login;