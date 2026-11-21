const API_URL = 'http://localhost:8081/finantialweb';

export const requestImportCsv = async (file, token) => {
    const formData = new FormData();
    formData.append('file', file);

    const response = await fetch(`${API_URL}/imports`, {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Target-Method': 'POST'
        },
        body: formData
    });

    if (!response.ok) {
        const data = await response.json().catch(() => null);
        throw new Error(data?.message || 'Error al importar el archivo CSV');
    }
};