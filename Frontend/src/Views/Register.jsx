import { useNavigate } from 'react-router-dom';
import { useState } from 'react';

import useAuth from '../Hooks/UseAuth';

import '../Styles/Register.css';

const Register = () => {

    const navigate = useNavigate();

    const { register } = useAuth();
    const [registering, setregistering] = useState(false);
    const [error, setError] = useState('');

    const formatDate = (date) => {
        return date.split("-").reverse().join("-");
    };

    const handleRegister = async (event) => {
        event.preventDefault();
        setregistering(true);
        setError('');

        const formData = new FormData(event.currentTarget);

        const credentials = {
            username: formData.get('username'),
            password: formData.get('password'),
            mail: formData.get('mail'),
            name: formData.get('name'),
            surname: formData.get('surname'),
            birthday: formatDate(formData.get('birthday'))
        };

        try {
            await register(credentials);
            navigate('/');
        } catch (error) {
            setError(error.message || 'Error al registrar usuario');
        } finally {
            setregistering(false);
        }
    };

    return (
        <div className="register">
            <form className="register__pannel" onSubmit={handleRegister} method="post">
                <h2 className="pannel__title">Registrarse</h2>

                <label htmlFor="username" className="pannel__label">Nombre de usuario</label>
                <input type="text" id="username" name="username" placeholder="Nombre de usuario" className="pannel__input" required/>

                <label htmlFor="name" className="pannel__label">Nombre</label>
                <input type="text" id="name" name="name" placeholder="Nombre" className="pannel__input" required/>

                <label htmlFor="surname" className="pannel__label">Apellidos</label>
                <input type="text" id="surname" name="surname" placeholder="Apellidos" className="pannel__input" required/>

                <label htmlFor="email" className="pannel__label">Correo Electrónico</label>
                <input type="email" id="email" name="mail" placeholder="Correo Electrónico" className="pannel__input" required/>

                <label htmlFor="password" className="pannel__label">Contraseña</label>
                <input type="password" id="password" name="password" placeholder="Contraseña" className="pannel__input" required/>

                <label htmlFor="birthday" className="pannel__label">Fecha de nacimientp</label>
                <input type="date" id="birthday" name="birthday" className="pannel__input" required/>

                {error && <span className='pannel__error'>{error}</span>}

                <button type="submit" className="pannel__button" disabled={registering}>
                    <span className="button__text"> Registrarse </span>
                </button>
            </form>
        </div>
    );
};

export default Register;