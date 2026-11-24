import { useContext, useEffect } from 'react';
import { CategoriesContext } from '../Context/CategoriesContext';

const useCategoriesMarked = (selectedCategories) => {
    const context = useContext(CategoriesContext);

    if (!context) {
        throw new Error('useCategoriesMarked debe utilizarse dentro de CategoriesProvider');
    }

    const { categoriesMarked, setCategoriesMarked } = context;

    useEffect(() => {
        if (selectedCategories === null) return;

        setCategoriesMarked((currentCategoriesMarked) => {
            if (currentCategoriesMarked === null) {
                return selectedCategories;
            }

            return currentCategoriesMarked;
        });
    }, [selectedCategories, setCategoriesMarked]);

    const handleCategoriesSelected = (categoryId, isChecked) => {
        setCategoriesMarked((currentCategoriesMarked) =>
            isChecked
                ? [...(currentCategoriesMarked ?? []), categoryId]
                : (currentCategoriesMarked ?? []).filter((id) => id !== categoryId)
        );
    };

    return { categoriesMarked: categoriesMarked ?? [], handleCategoriesSelected };
};

export default useCategoriesMarked;