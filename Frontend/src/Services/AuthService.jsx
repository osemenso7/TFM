const API_URL = 'http://localhost:8081/finantialweb';

export const requestRegister = async (credentials) => {
    const response = await fetch(`${API_URL}/users`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            targetMethod: 'POST',
            body: credentials
        })
    });

    const data = await response.json().catch(() => null);

    if (!response.ok) {
        throw new Error(data?.message || 'Error al registrar usuario');
    }
};

export const requestLogin = async (credentials) => {
    const response = await fetch(`${API_URL}/users/login`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            targetMethod: 'POST',
            body: credentials
        })
    });

    const data = await response.json().catch(() => null);

    if (!response.ok) {
        throw new Error(data?.message || 'No se ha podido iniciar sesión');
    }

    return data;
};