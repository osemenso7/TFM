import { createContext, useState } from 'react';

export const CategoriesContext = createContext();

export const CategoriesProvider = ({ children }) => {
    const [categoriesMarked, setCategoriesMarked] = useState(null);

    const categoriesContextValue = {
        categoriesMarked,
        setCategoriesMarked
    };

    return (
        <CategoriesContext.Provider value={categoriesContextValue}>
            {children}
        </CategoriesContext.Provider>
    );
};