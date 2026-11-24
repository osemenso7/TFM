import { useEffect, useState } from 'react';
import { requestCategories, requestSelectedCategories } from '../Services/CategoriesService';

const useCategories = (token) => {
    const [categories, setCategories] = useState([]);
    const [selectedCategories, setSelectedCategories] = useState(null);
    const [categoriesError, setCategoriesError] = useState('');

    useEffect(() => {
        if (!token) return;

        const loadCategories = async () => {
            try {
                const categories = await requestCategories(token);
                const selectedCategoriesIds = await requestSelectedCategories(token);

                setCategories(categories);
                setSelectedCategories(selectedCategoriesIds);
            } catch (error) {
                setCategoriesError(error.message);
            }
        };

        loadCategories();
    }, [token]);

    return { categories, selectedCategories, categoriesError };
};

export default useCategories;