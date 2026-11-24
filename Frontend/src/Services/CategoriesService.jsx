const API_URL = 'http://localhost:8081/finantialweb/categories';

export const requestCategories = async (token) => {
    const response = await fetch(`${API_URL}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({
            targetMethod: 'GET'
        })
    });

    const data = await response.json().catch(() => null);

    if (!response.ok) {
        throw new Error(data?.message || 'Error al obtener las categorías');
    }

    return data;
};

export const requestSelectedCategories = async (token) => {
    const response = await fetch(`${API_URL}/selected`,
        {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({
                targetMethod: 'GET'
            })
        }
    );

    const data = await response.json().catch(() => null);

    if (!response.ok) {
        throw new Error(data?.message || 'Error al obtener las categorías');
    }

    return data;
};

export const saveSelectedCategories = async (token, categoryIds) => {
    const response = await fetch(`${API_URL}`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify({
            targetMethod: "PUT",
            body: {
                categoryIds: categoryIds
            }
        })
    });

    if (!response.ok) {
        const data = await response.json().catch(() => null);
        throw new Error(data?.message || "Error al guardar las categorías");
    }
};